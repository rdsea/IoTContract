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
public class DataPointList {
        
        List<DataPoint> dataPoints = new LinkedList<>();

        public List<DataPoint> getDataPoints() {
            return dataPoints;
        }

        public void setDataPoints(List<DataPoint> dataPoints) {
            this.dataPoints = dataPoints;
        }
    }
