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

import at.ac.tuwien.dsg.cloud.elise.model.extra.contract.Constraint;
import at.ac.tuwien.dsg.cloud.elise.model.extra.contract.ContractTemplate;
import at.ac.tuwien.dsg.cloud.elise.model.extra.contract.ContractTerm;
import at.ac.tuwien.dsg.cloud.elise.model.extra.contract.Parameter;
import at.ac.tuwien.dsg.cloud.elise.model.extra.contract.ParameterTemplate;
import at.ac.tuwien.dsg.cloud.elise.model.extra.contract.Script;
import at.ac.tuwien.dsg.cloud.elise.model.provider.ServiceTemplate;
import at.ac.tuwien.dsg.thinggovernor.model.UnitData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Peter Klein
 */
@Controller
public class ContractService {
    
    static final Logger log = Logger.getLogger(ContractService.class.getName());
    
    @Autowired
    ConfigHandler configHandler;
    
    @Autowired
    LogAndPaymentHandler logHandler;
    
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Map<String, List<String>>> scripts = new HashMap<>();
    
    @RequestMapping(value = "/governor/assign", 
	            method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UnitData> receiveContractAssignment(
	    @RequestBody UnitData unit, 
	    UriComponentsBuilder builder) {
        try {
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(unit));
            buildContract(unit);
            if (!logHandler.deployLogContract(unit.getServiceTemplate())) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/governor/deassign", 
	            method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UnitData> receiveContractDeassignment(
	    @RequestBody UnitData unit, 
	    UriComponentsBuilder builder) {
        try {
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(unit));
            if (scripts.get(unit.getUnit()) == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (scripts.get(unit.getUnit()).get(unit.getServiceTemplate()) == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            scripts.get(unit.getUnit()).put(unit.getServiceTemplate(), null);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/governor/deassignall", 
	            method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UnitData> receiveContractDeassignmentAll(
	    @RequestBody UnitData unit, 
	    UriComponentsBuilder builder) {
        try {
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(unit));
            if (scripts.get(unit.getUnit()) == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            scripts.put(unit.getUnit(), new HashMap());
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    
    @RequestMapping(value = "/governor/scripts/{unit}", 
	            method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getScripts(
            @PathVariable("unit") String unitName,
            UriComponentsBuilder builder)  {
        log.info("retrieving scripts for " + unitName);
        Map<String, List<String>> byServiceTemplate = scripts.get(unitName);
        if (byServiceTemplate == null) {
           return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); 
        } else {
           List<String> allScripts = new LinkedList();
           Set<String> keys = byServiceTemplate.keySet();
           Iterator it = keys.iterator();
           while (it.hasNext()) {
               List<String> scriptsByServiceTemplate = byServiceTemplate.get(it.next());
               for (String scr : scriptsByServiceTemplate) {
                   allScripts.add(scr);
               }
           } 
           return new ResponseEntity<>(allScripts, HttpStatus.OK); 
        }
    }
    
    private void buildContract(UnitData unit) {
        ServiceTemplate serviceTemplate = fetchServiceTemplate(unit.getServiceTemplate());
        ContractTemplate contractTemplate = fetchContractTemplate(serviceTemplate.getContract().getTemplate());
        List<String> newScripts = new LinkedList<>();
        for (String termName : contractTemplate.getTerms()) {
            ContractTerm contractTerm = fetchContractTerm(termName);
            for (Constraint constraint : contractTerm.getConstraints()) {
                String scriptName = constraint.getEnforcementScript();
                Script script = fetchScript(scriptName);
                String scriptCode = script.getCode();
                scriptCode = "var resObj = new Object(); var _result = ''; var _reason = 'OK'; var _log = '';" +
                             "var _serviceTemplate = '" + unit.getServiceTemplate() + "';" + 
                             "var _unit = '" + unit.getUnit() + "';" + 
                             "var _amount = 0; var _sender = ''; var _receiver = '';" +
                             scriptCode +
                             "resObj.reason = _reason;" +
                             "resObj.log = _log;" +
                             "resObj.unit = _unit;" +
                             "resObj.serviceTemplate = _serviceTemplate;" +
                             "resObj.amount = _amount;" +
                             "resObj.sender = _sender;" +
                             "resObj.receiver = _receiver;" +
                             "result = JSON.stringify(resObj);";
                             
                for (ParameterTemplate paramTempl : constraint.getParameters()) {
                    Parameter param = findParam(serviceTemplate, paramTempl.getName());
                    if (param == null) {
                        log.warning("Parameter " + paramTempl.getName() + " not found in Contract Template" );
                    } else {
                        if (scriptCode.indexOf("@" + param.getName()) != -1) {
                            scriptCode = scriptCode.replaceAll("@" + param.getName(), "'"+param.getValue()+"'");
                        } else {
                            log.warning("Parameter " + paramTempl.getName() + " not found in Script" );
                        }
                    }
                }
                newScripts.add(scriptCode);
            } 
        }
        Map<String, List<String>> byServiceTemplate = scripts.get(unit.getUnit());
        if (byServiceTemplate == null) {
            byServiceTemplate = new HashMap();
        }
        List<String> existingScripts = byServiceTemplate.get(unit.getServiceTemplate());
        if (existingScripts == null) {
            existingScripts = new LinkedList();
        }
        for (String script : newScripts) {
            existingScripts.add(script);
        }
        byServiceTemplate.put(unit.getServiceTemplate(), existingScripts);
        scripts.put(unit.getUnit(), byServiceTemplate);
    }
    
    private Parameter findParam(ServiceTemplate serviceTemplate, String paramName) {
        for (Parameter param : serviceTemplate.getContract().getParameters()) {
            if (param.getName().equals(paramName)) {
                return param;
            }
        }
        return null;
    }
    
    private ServiceTemplate fetchServiceTemplate(String template) {
        try {
            return objectMapper.readValue(readWs(configHandler.getConfigData().getSalsaUrl() + 
                                                 "/salsa-engine/rest/elise/servicetemplate/" + 
                                                 template), ServiceTemplate.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private ContractTemplate fetchContractTemplate(String template) {
        try {
            return objectMapper.readValue(readWs(configHandler.getConfigData().getSalsaUrl() + 
                                                 "/salsa-engine/rest/elise/extracdg/contracttemplate/" + 
                                                 template), ContractTemplate.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private ContractTerm fetchContractTerm(String term) {
        try {
            return objectMapper.readValue(readWs(configHandler.getConfigData().getSalsaUrl() + 
                                                 "/salsa-engine/rest/elise/extracdg/contractterm/" + 
                                                 term), ContractTerm.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private Script fetchScript(String script) {
        try {
            return objectMapper.readValue(readWs(configHandler.getConfigData().getSalsaUrl() + 
                                                 "/salsa-engine/rest/elise/extracdg/script/" + 
                                                 script), Script.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private synchronized InputStream readWs(String urlString) {
        try {
            URL url = new URL(urlString);
            log.info(urlString);
            HttpURLConnection conn  = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "application/json");
            conn.setDoOutput(false);
            String responseCode = conn.getResponseMessage();
            log.log(Level.INFO, "response message={0}", responseCode);
            if (responseCode.equals("OK")) {
                return conn.getInputStream();
            } else {
                return null;
            }
            
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return null;
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
