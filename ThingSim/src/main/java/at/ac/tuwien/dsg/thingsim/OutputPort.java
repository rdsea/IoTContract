/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.thingsim;

import at.ac.tuwien.dsg.thingsim.model.Command;
import at.ac.tuwien.dsg.thingsim.model.DataPoint;
import at.ac.tuwien.dsg.thingsim.model.PhysicalAddress;
import at.ac.tuwien.dsg.thingsim.model.Thing;

/**
 *
 * @author Peter Klein
 */
abstract public class OutputPort {
    
    abstract public void sendDataPoint(PhysicalAddress remote, String port, DataPoint dp, Thing thing);
    
    abstract public void sendCommand(PhysicalAddress remote, String port, Command cmd, Thing thing);
    
}
