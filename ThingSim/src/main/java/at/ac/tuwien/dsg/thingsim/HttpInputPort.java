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

import at.ac.tuwien.dsg.thingsim.model.DataPoint;
import at.ac.tuwien.dsg.thingsim.model.Command;
import at.ac.tuwien.dsg.thingsim.model.Port;
import at.ac.tuwien.dsg.thingsim.model.Thing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Peter Klein
 */
@Controller
public class HttpInputPort {
    
    static final Logger log = Logger.getLogger(HttpInputPort.class.getName());
    
    ObjectMapper objectMapper = new ObjectMapper();
    Port port; 
    
    @Autowired
    ConfigHandler configHandler;
    
    @Autowired
    Thing thing;
    
    @RequestMapping(value = "/data/{port}", 
	            method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataPoint> receiveDataPoint(
	    @PathVariable("port") String port,
	    @RequestBody DataPoint dataPoint, 
	    UriComponentsBuilder builder) {
        try {
            log.log (Level.INFO, "message received on port {0}", port);
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataPoint));
            Port p = configHandler.getConfigData().getPort(port);
            if (p == null){
               log.log(Level.INFO, "Port {0} not found", port);
               return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
            }
            if (!(p.isHttpInputDataPort())) {
                log.log(Level.INFO, "Port {0} is no HTTP Data Input port", port);
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            if (!(configHandler.getConfigData().isAllowedDataPoint(port, dataPoint.getName()))) {
                log.log(Level.INFO, "DataPoint {0} not found", dataPoint.getName());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            thing.getProcessor().processDataPoint(configHandler.getConfigData().getScriptfile(), dataPoint, thing, port);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/command/{port}", 
	            method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Command> receiveDataPoint(
	    @PathVariable("port") String port,
	    @RequestBody Command command, 
	    UriComponentsBuilder builder) {
        try {
            log.info (configHandler.getConfigData().getPorts().get(0).getName());
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(command));
            Port p = configHandler.getConfigData().getPort(port);
            if (p == null){
               log.log(Level.INFO, "Port {0} not found", port);
               return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
            }
            if (!(p.isHttpInputCommandPort())) {
                log.log(Level.INFO, "Port {0} is no HTTP Command Input port", port);
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            if (!(configHandler.getConfigData().isAllowedCommand(port, command.getName(), command.getArguments()))) {
                log.log(Level.INFO, "Command {0} not found", command.getName());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            thing.getProcessor().processCommand(configHandler.getConfigData().getScriptfile(), command, thing, port);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
