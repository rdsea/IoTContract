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


import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Peter Klein
 */
public class ConfigData {
    
    private List<Port> ports = new LinkedList<>();
    private List<DataPointDef> dataPoints = new LinkedList<>();
    private List<CommandDef> commandDefs = new LinkedList<>();
    private List<PortParam> portParameters = new LinkedList<>();
    private String scriptfile;
    private String name;
    private String mqttBrokerUrl;
    private String stopAfterFileProcessing;

    public List<Port> getPorts() {
        return ports;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public List<DataPointDef> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPointDef> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public List<CommandDef> getCommandDefs() {
        return commandDefs;
    }

    public void setCommands(List<CommandDef> commandDefs) {
        this.commandDefs = commandDefs;
    }

    public List<PortParam> getPortParameters() {
        return portParameters;
    }

    public void setPortParameters(List<PortParam> portParameters) {
        this.portParameters = portParameters;
    }

    public String getScriptfile() {
        return scriptfile;
    }

    public void setScriptfile(String scriptfile) {
        this.scriptfile = scriptfile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMqttBrokerUrl() {
        return mqttBrokerUrl;
    }

    public void setMqttBrokerUrl(String mqttBrokerUrl) {
        this.mqttBrokerUrl = mqttBrokerUrl;
    }
    
    public String getStopAfterFileProcessing() {
        return stopAfterFileProcessing;
    }

    public void setStopAfterFileProcessing(String stopAfterFileProcessing) {
        this.stopAfterFileProcessing = stopAfterFileProcessing;
    }

    public Port getPort(String portName) {
        for (Port p : ports) {
            if (p.getName().equals(portName)) {
                return p;
            }
        }
        return null;
    }
    
    public boolean isAllowedDataPoint(String port, String inDpName) {
        for (DataPointDef d : dataPoints) {
            if (d.getPort().equals(port)) {
                for (String dpName : d.getPoints()) {
                    if (dpName.equals(inDpName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isAllowedCommand(String port, String commandName, List<String> arguments) {
        for (CommandDef cd : commandDefs) {
            if (cd.getPort().equals(port)) {
                for (Command c : cd.getCommands()) {
                    if (c.getName().equals(commandName)) {
                        for (String a : c.getArguments()) {
                            if (!(arguments.contains(a))) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public String getPortParameter(String port, String attributeName){
        for (PortParam p : portParameters) {
            if (p.getPort().equals(port)) {
                for (Attribute a : p.getAttributes()){
                    if (a.getName().equals(attributeName)) {
                        return a.getValue();
                    }
                }
            }
        }
        return null;
    }
    
    
    
}
