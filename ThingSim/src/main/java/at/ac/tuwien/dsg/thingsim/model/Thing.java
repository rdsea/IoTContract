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
