/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.thingsim;

import at.ac.tuwien.dsg.thingsim.model.ConfigData;
import at.ac.tuwien.dsg.thingsim.model.Port;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Peter Klein
 */
@Component
public class PortHandler {
    
    static final Logger log = Logger.getLogger(PortHandler.class.getName());
    
    private Map<String, OutputPort> outputPorts = new HashMap<>();
    private Map<String, InputPort> inputPorts = new HashMap<>();
    
    @Autowired
    ConfigHandler configHandler;
    
    public PortHandler() {
        //initOutputPorts();
        //initInputPorts();
    }
    
    public void init() {
        initOutputPorts();
        initInputPorts();
    }

    public Map<String, OutputPort> getOutputPorts() {
        return outputPorts;
    }

    public void setOutputPorts(Map<String, OutputPort> outputPorts) {
        this.outputPorts = outputPorts;
    }

    public Map<String, InputPort> getInputPorts() {
        return inputPorts;
    }

    public void setInputPorts(Map<String, InputPort> inputPorts) {
        this.inputPorts = inputPorts;
    }
    
    
    
    private void initOutputPorts() {
        outputPorts.clear();
        ConfigData configData = configHandler.getConfigData();
        for (Port p : configData.getPorts()) {
            if (p.getDirection() == Port.DirectionType.OUT) {
                if (p.getAdapter() == Port.AdapterType.HTTP) {
                    outputPorts.put(p.getName(), new HttpOutputPort(p, configData));
                }
                if (p.getAdapter() == Port.AdapterType.MQTT) {
                    outputPorts.put(p.getName(), new MqttOutputPort(p, configData));
                }
            }
        }
    }
    
    private void initInputPorts() {
        inputPorts.clear();
        ConfigData configData = configHandler.getConfigData();
        for (Port p : configData.getPorts()) {
            if (p.getDirection() == Port.DirectionType.IN) {
                if (p.getAdapter() == Port.AdapterType.FILE) {
                    InputPort ip = new FileInputPort(p, configData);
                    inputPorts.put(p.getName(), ip);
                }
                if (p.getAdapter() == Port.AdapterType.RANDOM) {
                    InputPort ip = new RandomInputPort(p, configData);
                    inputPorts.put(p.getName(), ip);
                }
                if (p.getAdapter() == Port.AdapterType.MQTT) {
                    InputPort ip = new MqttInputPort(p, configData);
                    inputPorts.put(p.getName(), ip);
                }
            }
        }
    }

}
