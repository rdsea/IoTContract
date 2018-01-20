/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.thingsim.model;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Peter Klein
 */
public class CommandDef {
    
    private String port;
    private List<Command> commands = new LinkedList<>();

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }
    
    
    
}
