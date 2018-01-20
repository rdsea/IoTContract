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
public class DataPointDef {
    
    private String port;
    private List<String> points = new LinkedList<>();

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public List<String> getPoints() {
        return points;
    }

    public void setPoints(List<String> points) {
        this.points = points;
    }
    
    
    
}
