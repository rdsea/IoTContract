/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.thingsim;

import at.ac.tuwien.dsg.thingsim.model.ConfigData;
import at.ac.tuwien.dsg.thingsim.model.DataPoint;
import at.ac.tuwien.dsg.thingsim.model.DataPointDef;
import at.ac.tuwien.dsg.thingsim.model.Port;
import static at.ac.tuwien.dsg.thingsim.model.Port.DataType.COMMAND;
import static at.ac.tuwien.dsg.thingsim.model.Port.DataType.DATA;
import at.ac.tuwien.dsg.thingsim.model.Thing;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter Klein
 */
public class RandomInputPort extends InputPort {
    
    Thing thing;
    Port port;
    ConfigData config;
    static final Logger log = Logger.getLogger(RandomInputPort.class.getName());
    Long repeatDelay;
    Long nrOfRepeats;
    Double mean;
    Double variance;
    
    Random random = new Random();
    
    public RandomInputPort(Port port, ConfigData config) {
        repeatDelay = Long.parseLong(config.getPortParameter(port.getName(), "repeatDelay"));
        nrOfRepeats = Long.parseLong(config.getPortParameter(port.getName(), "nrOfRepeats"));
        mean = Double.parseDouble(config.getPortParameter(port.getName(), "mean"));
        variance = Double.parseDouble(config.getPortParameter(port.getName(), "variance"));
        this.port = port;   
        this.config = config;
    }
    
    private DataPoint genDataPoint(String pointName) {
        DataPoint dp = new DataPoint();
        dp.setName(pointName);
        dp.setValue("" + mean + random.nextGaussian() * variance);
        return dp;
    }
    
    private void send() {
        try {
            if (null != port.getType()) switch (port.getType()) {
                case DATA:
                    for (DataPointDef dpDef : config.getDataPoints()) {
                        if (dpDef.getPort().equals(port.getName())) {
                            for (String point : dpDef.getPoints()) {
                                thing.getProcessor().processDataPoint(config.getScriptfile(), 
                                                                      genDataPoint(point), 
                                                                      thing, 
                                                                      port.getName());
                            }
                        }
                    }
                    TimeUnit.SECONDS.sleep(repeatDelay);
                    break;
                case COMMAND:
                    log.log(Level.WARNING, "Datatype COMMAND not supported for FileInputWriter");
                    break;
                default:
                    log.log(Level.WARNING, "Unknown datatype {0} found", port.getType().toString());
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    Runnable runnable = () -> {
    if (nrOfRepeats == -1) {
        while(true) {
            send();
        }
    } else {
        for (long i=0; i<nrOfRepeats; i++) {
            send();
        }
    }
    
    };

    public void execute(Thing thing) {
        this.thing = thing;
        Thread thread = new Thread(runnable);
        thread.start();
    }
    
    @Override
    public void process(Thing thing, String payload){};
    
}
