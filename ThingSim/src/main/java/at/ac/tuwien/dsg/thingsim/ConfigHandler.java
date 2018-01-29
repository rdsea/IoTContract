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

import at.ac.tuwien.dsg.thingsim.model.Address;
import at.ac.tuwien.dsg.thingsim.model.ConfigData;
import at.ac.tuwien.dsg.thingsim.model.PhysicalAddress;
import at.ac.tuwien.dsg.thingsim.model.Route;
import at.ac.tuwien.dsg.thingsim.model.Topology;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

/**
 *
 * @author Peter Klein
 */
@Component
public class ConfigHandler {
    
    static final Logger log = Logger.getLogger(ConfigHandler.class.getName());
    
    private ConfigData configData;
    private Topology topology;
    
    
    public ConfigHandler() {
        try {
            String thingConfFile = System.getProperty("thingConfFile");
            log.info(thingConfFile);
            System.out.println(thingConfFile);
            ObjectMapper objectMapper = new ObjectMapper();
            configData = objectMapper.readValue(new File(thingConfFile), ConfigData.class);
            String topologyFile = System.getProperty("topologyFile");
            log.info(topologyFile);
            topology = objectMapper.readValue(new File(topologyFile), Topology.class);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }

    public ConfigData getConfigData() {
        return configData;
    }

    public void setConfigData(ConfigData configData) {
        this.configData = configData;
    }

    public Topology getTopology() {
        return topology;
    }

    public void setTopology(Topology topology) {
        this.topology = topology;
    }
    
    public Address findRemoteAddress(String port) {
        for (Route r : topology.getRoutes()){
            if ((r.getFrom().getThing().equals(configData.getName())) &&
                (r.getFrom().getPort().equals(port))) {
                return r.getTo();
            }
        }
        return null;
    }
    
    public PhysicalAddress findPhysicalAddress(String thing) {
        for (PhysicalAddress p : topology.getPhysicalAddresses()){
            if (p.getThing().equals(thing)) {
                return p;
            }
        }
        return null;
    }
    
    
}
