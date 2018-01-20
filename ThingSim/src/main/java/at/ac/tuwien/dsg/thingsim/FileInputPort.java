/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.thingsim;

import at.ac.tuwien.dsg.thingsim.model.Command;
import at.ac.tuwien.dsg.thingsim.model.CommandList;
import at.ac.tuwien.dsg.thingsim.model.ConfigData;
import at.ac.tuwien.dsg.thingsim.model.DataPoint;
import at.ac.tuwien.dsg.thingsim.model.DataPointList;
import at.ac.tuwien.dsg.thingsim.model.Port;
import at.ac.tuwien.dsg.thingsim.model.Thing;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter Klein
 */
public class FileInputPort extends InputPort {
    
    Thing thing;
    ConfigData config;
    Port port;
    
    static Logger log = Logger.getLogger(FileInputPort.class.getName());
    ObjectMapper objectMapper = new ObjectMapper();
    
    
    String filename; 
    Long repeatDelay;
    Long nrOfRepeats;
    String fileformat;
    DataPointList dataPoints = null;
    CommandList commands = null;
    
    private DataPointList parseCsv() {
        String line = "";
        String cvsSplitBy = ";";
        String nameColumn = config.getPortParameter(port.getName(), "nameColumn");
        String timestampColumn = config.getPortParameter(port.getName(), "timestampColumn");
        String valueColumns = config.getPortParameter(port.getName(), "valueColumns");
        String[] valueColumnsAsArray = valueColumns.split(",");
        int nameColumnIndex = 0;
        int timestampColumnIndex = 0;
        List<Integer> valueColumnIndices = new LinkedList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            if ((line = br.readLine()) != null) {
                DataPointList dps = new DataPointList();
                String headerline = line;
                String headers[] = headerline.split(cvsSplitBy);
                for (int i=0; i<headers.length; i++) {
                    if (headers[i].equals(nameColumn)) {
                        nameColumnIndex = i;
                    }
                    if (headers[i].equals(timestampColumn)) {
                        timestampColumnIndex = i;
                    }
                    for (int j=0; j<valueColumnsAsArray.length; j++) {
                        if (headers[i].equals(valueColumnsAsArray[j])) {
                            valueColumnIndices.add(i);
                        }
                    }
                }
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(cvsSplitBy);
                    for (int valueColumnIndex : valueColumnIndices) {
                        DataPoint dp = new DataPoint();
                        dp.setName(data[nameColumnIndex]);
                        dp.setTimestamp(data[timestampColumnIndex]);
                        dp.setValue(data[valueColumnIndex]);
                        dps.getDataPoints().add(dp);
                    }
                }
                return dps;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
   
    public FileInputPort(Port port, ConfigData config) {
        this.port = port;
        this.config = config;
        filename = config.getPortParameter(port.getName(), "filename");
        repeatDelay = Long.parseLong(config.getPortParameter(port.getName(), "repeatDelay"));
        nrOfRepeats = Long.parseLong(config.getPortParameter(port.getName(), "nrOfRepeats"));
        fileformat = config.getPortParameter(port.getName(), "fileformat");
        
        try {
            if (null != port.getType()) switch (port.getType()) {
                case DATA:
                    if (fileformat.equals("json")) {
                       dataPoints = objectMapper.readValue(new File(filename), DataPointList.class); 
                    } else if (fileformat.equals("csv")) {
                       dataPoints = parseCsv();
                    } else {
                       log.log(Level.WARNING, "Unknown fileformat {0} found", fileformat); 
                    }
                    
                    break;
                case COMMAND:
                    commands = objectMapper.readValue(new File(filename), CommandList.class);
                    break;
                default:
                    log.log(Level.WARNING, "Unknown datatype {0} found", port.getType().toString());
                    break;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void processFile() {
        if (null != port.getType()) switch (port.getType()) {
            case DATA:
                for (DataPoint dataPoint : dataPoints.getDataPoints()) {
                    if (!(config.isAllowedDataPoint(port.getName(), dataPoint.getName()))) {
                        log.log(Level.INFO, "DataPoint {0} not found", dataPoint.getName());
                        continue;
                    }
                   thing.getProcessor().processDataPoint(config.getScriptfile(), dataPoint, thing, port.getName());
                }   
                break;
            case COMMAND:
                for (Command command : commands.getCommands()) {
                    if (!(config.isAllowedCommand(port.getName(), command.getName(), command.getArguments()))) {
                        log.log(Level.INFO, "Command {0} not found", command.getName());
                        continue;
                    }
                    thing.getProcessor().processCommand(config.getScriptfile(), command, thing, port.getName());
                }   
                break;
            default:
                log.log(Level.WARNING, "Unknown datatype {0} found", port.getType().toString());
                break;
        }
    }
    
    Runnable runnable = () -> {

    try {
       if (nrOfRepeats == -1) {
            while(true) {
               processFile();
               TimeUnit.SECONDS.sleep(repeatDelay);
            }
        } else {
           for (int i=0; i<nrOfRepeats; i++) {
               processFile();
                TimeUnit.SECONDS.sleep(repeatDelay);
           }
           if (!(config.getStopAfterFileProcessing() != null && config.getStopAfterFileProcessing().equals("no"))) {
               log.log(Level.INFO, "File processing completed, shutting down");
               System.exit(0);
           }
        } 
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
        

    };

    @Override
    public void execute(Thing thing) {
        this.thing = thing;
        Thread thread = new Thread(runnable);
        thread.start();
    }
    
    @Override
    public void process(Thing thing, String payload){};
    
}
