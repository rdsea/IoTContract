/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.thinggovernor;

import at.ac.tuwien.dsg.thinggovernor.model.LogEntry;
import at.ac.tuwien.dsg.thinggovernor.model.PaymentEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigInteger;
import java.util.Set;
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
public class LogAndPaymentService {
    
    static final Logger log = Logger.getLogger(ContractService.class.getName());
    
    ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    ConfigHandler configHandler;
    
    @Autowired
    LogAndPaymentHandler handler;
    
    @RequestMapping(value = "/governor/log", 
	            method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LogEntry> addLog(
	    @RequestBody LogEntry logEntry, 
	    UriComponentsBuilder builder) {
        try {
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(logEntry));
            handler.addLog(logEntry);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/governor/log/{serviceTemplate}", 
	            method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<LogEntry>> getLogs(
            @PathVariable("serviceTemplate") String serviceTemplate,
            UriComponentsBuilder builder)  {
        log.info("retrieving logs for " + serviceTemplate);
        Set<LogEntry> result = handler.readLogs(serviceTemplate);
        if (result == null) {
           return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
        } else {
           return new ResponseEntity<>(result, HttpStatus.OK); 
        }
    }
    
    @RequestMapping(value = "/governor/sclog/{serviceTemplate}/{id}", 
	            method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getScLogs(
            @PathVariable("serviceTemplate") String serviceTemplate,
            @PathVariable("id") String id,
            UriComponentsBuilder builder)  {
        log.info("retrieving logs for " + serviceTemplate);
        String result = "{\"digest\":\"" + handler.readScLogs(serviceTemplate, id) + "\"}";
        if (result == null) {
           return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); 
        } else {
           return new ResponseEntity<>(result, HttpStatus.OK); 
        }
    }
    
    @RequestMapping(value = "/governor/payment", 
	            method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentEntry> makePayment(
	    @RequestBody PaymentEntry paymentEntry, 
	    UriComponentsBuilder builder) {
        try {
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(paymentEntry));
            handler.makePayment(paymentEntry.getSender(),
                                paymentEntry.getReceiver(), 
                                new BigInteger(paymentEntry.getAmount()));
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
