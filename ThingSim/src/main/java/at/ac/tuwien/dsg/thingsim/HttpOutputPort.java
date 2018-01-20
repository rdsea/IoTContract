/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.thingsim;

import at.ac.tuwien.dsg.thingsim.model.DataPoint;
import at.ac.tuwien.dsg.thingsim.model.Command;
import at.ac.tuwien.dsg.thingsim.model.ConfigData;
import at.ac.tuwien.dsg.thingsim.model.PhysicalAddress;
import at.ac.tuwien.dsg.thingsim.model.Port;
import at.ac.tuwien.dsg.thingsim.model.Thing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter Klein
 */
public class HttpOutputPort extends OutputPort {
    
    static final Logger log = Logger.getLogger(HttpOutputPort.class.getName());
    
    private final Port port;
    private final ConfigData config;
    
    public HttpOutputPort(Port port, ConfigData config) {
        this.port = port;
        this.config = config;
    }
    
    private synchronized void send(String urlString, String data) {
        try {
            URL url = new URL(urlString);
            log.info(urlString);
            HttpURLConnection conn  = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "application/json");
            conn.setDoOutput(true);
            conn.setRequestProperty("content-type", "application/json");
	    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            log.info(data);
	    out.write(data);
	    out.close();
            int responseCode = conn.getResponseCode();
            log.log(Level.INFO, "response code={0}", responseCode);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public synchronized void sendDataPoint(PhysicalAddress thingAddress, String port, DataPoint dp, Thing thing) {
        String urlString = "http://" + thingAddress.getAddress() + "/data/" + port;
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            String data = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dp);
            send(urlString, data);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    @Override
    public synchronized void sendCommand (PhysicalAddress thingAddress, String port, Command cmd, Thing thing) {
        String urlString = "http://" + thingAddress.getAddress() + "/command/" + port;
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            String data = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cmd);
            send(urlString, data);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
}
