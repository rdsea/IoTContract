/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.thingsim.model;

/**
 *
 * @author Peter Klein
 */
public class Port {
    
    public enum AdapterType {
        HTTP, MQTT, FILE, RANDOM 
    }
    
    public enum DirectionType {
        IN, OUT 
    }
    
    public enum DataType {
        DATA, COMMAND
    }
    
    private String name;
    private AdapterType adapter;
    private DirectionType direction;
    private DataType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AdapterType getAdapter() {
        return adapter;
    }

    public void setAdapter(AdapterType adapter) {
        this.adapter = adapter;
    }

    public DirectionType getDirection() {
        return direction;
    }

    public void setDirection(DirectionType direction) {
        this.direction = direction;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }
    
    public boolean isHttpInputDataPort() {
        return ((adapter == Port.AdapterType.HTTP) &&
            (direction == Port.DirectionType.IN) &&
            (type == Port.DataType.DATA));
    }
    
    public boolean isHttpInputCommandPort() {
        return ((adapter == Port.AdapterType.HTTP) &&
            (direction == Port.DirectionType.IN) &&
            (type == Port.DataType.COMMAND));
    }
    
    public boolean isHttpOutputDataPort() {
        return ((adapter == Port.AdapterType.HTTP) &&
            (direction == Port.DirectionType.OUT) &&
            (type == Port.DataType.DATA));
    }
    
    public boolean isHttpOutputCommandPort() {
        return ((adapter == Port.AdapterType.HTTP) &&
            (direction == Port.DirectionType.OUT) &&
            (type == Port.DataType.COMMAND));
    }
    
    public boolean isMqttOutputDataPort() {
        return ((adapter == Port.AdapterType.MQTT) &&
            (direction == Port.DirectionType.OUT) &&
            (type == Port.DataType.DATA));
    }
    
    public boolean isMqttOutputCommandPort() {
        return ((adapter == Port.AdapterType.MQTT) &&
            (direction == Port.DirectionType.OUT) &&
            (type == Port.DataType.COMMAND));
    }
    
    public boolean isMqttInputDataPort() {
        return ((adapter == Port.AdapterType.MQTT) &&
            (direction == Port.DirectionType.IN) &&
            (type == Port.DataType.DATA));
    }
    
    public boolean isMqttInputCommandPort() {
        return ((adapter == Port.AdapterType.MQTT) &&
            (direction == Port.DirectionType.IN) &&
            (type == Port.DataType.COMMAND));
    }
    
    
    
}
