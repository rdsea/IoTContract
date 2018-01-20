/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.thingsim;

import at.ac.tuwien.dsg.thingsim.model.Command;
import at.ac.tuwien.dsg.thingsim.model.ConfigData;
import at.ac.tuwien.dsg.thingsim.model.DataPoint;
import at.ac.tuwien.dsg.thingsim.model.Port;
import at.ac.tuwien.dsg.thingsim.model.Thing;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter Klein
 */
public class MqttInputPort extends InputPort {
    
    Port port; 
    ConfigData config;
    static final Logger log = Logger.getLogger(MqttInputPort.class.getName());
    ObjectMapper objectMapper = new ObjectMapper();
    
    public MqttInputPort(Port port, ConfigData config) {
        this.port = port;
        this.config = config;
    }
    
    @Override
    public void execute(Thing thing) {
        thing.getMqttInputAdapter().addTopic(config.getName() + "/" + port.getType().toString() + "/" + port.getName(), 1);
    }
    
    @Override
    public void process(Thing thing, String payload) {
        try {
            if (null != port.getType()) switch (port.getType()) {
            case DATA:
                DataPoint dp = objectMapper.readValue(payload, DataPoint.class);
                thing.getProcessor().processDataPoint(config.getScriptfile(), dp, thing, port.getName());
                break;
            case COMMAND:
                Command cmd = objectMapper.readValue(payload, Command.class);
                thing.getProcessor().processCommand(config.getScriptfile(), cmd, thing, port.getName());  
                break;
            default:
                log.log(Level.WARNING, "Unknown datatype {0} found", port.getType().toString());
                break;
        }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    
}
