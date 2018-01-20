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
 * @author atw10ke0
 */
public class Topology {
    
    private List<Route> routes = new LinkedList<>();
    
    private List<PhysicalAddress> physicalAddresses = new LinkedList<>();

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<PhysicalAddress> getPhysicalAddresses() {
        return physicalAddresses;
    }

    public void setPhysicalAddresses(List<PhysicalAddress> physicalAddresses) {
        this.physicalAddresses = physicalAddresses;
    }
    
    
}
