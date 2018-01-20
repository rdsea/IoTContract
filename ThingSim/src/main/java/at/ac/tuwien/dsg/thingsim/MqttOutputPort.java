/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.thingsim;

import at.ac.tuwien.dsg.thingsim.model.DataPoint;
import at.ac.tuwien.dsg.thingsim.model.Command;
import at.ac.tuwien.dsg.thingsim.model.ConfigData;
import at.ac.tuwien.dsg.thingsim.model.PhysicalAddress;
import at.ac.tuwien.dsg.thingsim.model.Port;
import at.ac.tuwien.dsg.thingsim.model.Port.DataType;
import at.ac.tuwien.dsg.thingsim.model.Thing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;

/**
 *
 * @author Peter Klein
 */

@IntegrationComponentScan
public class MqttOutputPort extends OutputPort {
    
    static final Logger log = Logger.getLogger(MqttOutputPort.class.getName());
    
    Port port;
    ConfigData config;
    
    public MqttOutputPort(Port port, ConfigData config) {
        this.port = port;
        this.config = config;
    }
        
    @MessagingGateway(
        defaultRequestChannel = "mqttOutputChannel"
        )
    public interface MyGateway {

        void sendToMqtt(String data);

    }
    
    //@Autowired
    //private MyGateway gateway;
    
    private synchronized void send(Port p, String remoteThing, String data, Thing thing) {
        try {
            thing.getMqttOutputAdapter().setDefaultTopic(remoteThing + "/" + p.getType().toString() + "/" + p.getName());
            MyGateway gateway = thing.getCtx().getBean(MyGateway.class);
            gateway.sendToMqtt(data);
            log.info(data);
	    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
   
    @Override
    public synchronized void sendDataPoint(PhysicalAddress thingAddress, String port, DataPoint dp, Thing thing) {
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            Port p = new Port();
            p.setName(port);
            p.setType(DataType.DATA);
            String data = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dp);
            send(p, thingAddress.getThing(), data, thing);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    @Override
    public synchronized void sendCommand (PhysicalAddress thingAddress, String port, Command cmd, Thing thing) {
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            Port p = new Port();
            p.setName(port);
            p.setType(DataType.COMMAND);
            String data = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd);
            send(p, thingAddress.getThing(), data, thing);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
}
