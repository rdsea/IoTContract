/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.thingsim.model;

import at.ac.tuwien.dsg.thingsim.ConfigHandler;
import at.ac.tuwien.dsg.thingsim.OutputPort;
import at.ac.tuwien.dsg.thingsim.PortHandler;
import at.ac.tuwien.dsg.thingsim.Processor;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.stereotype.Component;

/**
 *
 * @author Peter Klein
 */
@Component
public class Thing {
    
    static Logger log = Logger.getLogger(Thing.class.getName());
       
    private Processor processor = new Processor();
    private MqttPahoMessageDrivenChannelAdapter mqttInputAdapter;
    private MqttPahoMessageHandler mqttOutputAdapter;
    private ApplicationContext ctx;
    
    @Autowired
    ConfigHandler configHandler;
    
    @Autowired
    PortHandler portHandler;
    
    public Thing () {
        log.info("constructor for Thing called");
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public MqttPahoMessageDrivenChannelAdapter getMqttInputAdapter() {
        return mqttInputAdapter;
    }

    public void setMqttInputAdapter(MqttPahoMessageDrivenChannelAdapter mqttInputAdapter) {
        this.mqttInputAdapter = mqttInputAdapter;
    }

    public MqttPahoMessageHandler getMqttOutputAdapter() {
        return mqttOutputAdapter;
    }

    public void setMqttOutputAdapter(MqttPahoMessageHandler mqttOutputAdapter) {
        this.mqttOutputAdapter = mqttOutputAdapter;
    }

    public ApplicationContext getCtx() {
        return ctx;
    }

    public void setCtx(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    public PortHandler getPortHandler() {
        return portHandler;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }
 
    public void sendDataPoint(String port, DataPoint dp) {
        Address remote = configHandler.findRemoteAddress(port);
        if (remote == null) {
            log.warning("Could not find remote port for " + port);
        }
        OutputPort p = portHandler.getOutputPorts().get(port);
        PhysicalAddress pa = configHandler.findPhysicalAddress(remote.getThing());
        if (pa.getAddress() == null) {
            log.warning("Could not find address for " + remote.getThing());
        }
        p.sendDataPoint(pa, remote.getPort(), dp, this);
    }
    
    public void sendCommand(String port, Command cmd) {
        Address remote = configHandler.findRemoteAddress(port);
        if (remote == null) {
            log.warning("Could not find remote port for " + port);
        }
        OutputPort p = portHandler.getOutputPorts().get(port);
        PhysicalAddress pa = configHandler.findPhysicalAddress(remote.getThing());
        if (pa.getAddress() == null) {
            log.warning("Could not find address for " + remote.getThing());
        }
        p.sendCommand(pa, remote.getPort(), cmd, this);
    }
    

       
    
    
    
}
