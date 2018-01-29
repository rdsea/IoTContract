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
package at.ac.tuwien.dsg.thinggovernor;

import at.ac.tuwien.dsg.thinggovernor.model.ConfigData;
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
    
    public ConfigHandler() {
        try {
            String conf = System.getProperty("governorConfFile");
            log.info(conf);
            System.out.println(conf);
            ObjectMapper objectMapper = new ObjectMapper();
            configData = objectMapper.readValue(new File(conf), ConfigData.class);           
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
    
}
