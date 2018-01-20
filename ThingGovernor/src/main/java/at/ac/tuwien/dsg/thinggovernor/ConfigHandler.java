/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
