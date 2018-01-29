/*
 * Copyright 2018 Copyright Peter Klein, Hong-Linh Truong.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
