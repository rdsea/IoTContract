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
package at.ac.tuwien.dsg.thingsim.monitoring;

import at.ac.tuwien.dsg.thingsim.model.DataPoint;
import at.ac.tuwien.dsg.thingsim.model.Thing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author  Peter Klein
 */
@Aspect
public class Monitor {
    
    List<Object> scripts = null;
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String,Object> scratchpad = new HashMap<>();
    
    static final Logger log = Logger.getLogger(Monitor.class.getName());
    
    private void initMonitor() {
        System.out.println("Monitor created");
        log.log(Level.INFO,"starting to load scripts");
        String thingName = System.getProperty("thingName");
        System.out.println("Thing=" + thingName);
        List<String> s = fetchScripts(thingName);
        scripts = new LinkedList<Object>();
        for (String script : s) {
            scripts.add((compileScript(script)));
        }
        log.log(Level.INFO,"finished loading scripts");
    }
    
    private List<String> fetchScripts(String unit) {
        try {
            InputStream is = receive(System.getProperty("thingGovernor") + "/governor/scripts/" + unit);
            if (is == null){
                return new LinkedList<String>();
            } else {
                return objectMapper.readValue(is, List.class);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private synchronized InputStream receive(String urlString) {
        try {
            URL url = new URL(urlString);
            log.info(urlString);
            HttpURLConnection conn  = (HttpURLConnection)url.openConnection();
            if (conn == null) {
                log.severe("could not open connection");
            }
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "application/json");
            conn.setDoOutput(false);
            int responseCode = conn.getResponseCode();
            log.log(Level.INFO, "response code={0}", responseCode);
            if (responseCode == 200) {
                return conn.getInputStream();
            } else {
                log.info("no data found for " + urlString);
                return null;
            }
            
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return null;
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
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
    
    private Object compileScript (String script) {
        log.info("script: " + script);
        try {
            Context cx = Context.enter();
            return cx.compileString(script,"script",1,null); 
        } catch (org.mozilla.javascript.EvaluatorException ex) {
            log.info("error parsing script: " + ex.getMessage());
            return null;
        } finally {
            Context.exit();
        }
    }

    
    @Around("execution(* processDataPoint(..)) && args(fileName,dp,thing,port)")
    public void aroundProcessDataPoint(ProceedingJoinPoint joinPoint,
                                       String fileName,
                                       DataPoint dp,
                                       Thing thing,
                                       String port) throws Throwable {
        System.out.println("Around before");
        if (scripts == null) {
            initMonitor();
        }
        for (Object script : scripts) {
            try {
                Context cx = Context.enter();
                Scriptable scope = cx.initStandardObjects();
                Object[] signatureArgs = joinPoint.getArgs();
                Object wrappedDp = Context.javaToJS(dp, scope);
                ScriptableObject.putProperty(scope, "dataPoint", wrappedDp);
                Object wrappedPort = Context.javaToJS(port, scope);
                ScriptableObject.putProperty(scope, "port", wrappedPort);
                System.out.println("Port=" + port);
                Object o = ((org.mozilla.javascript.Script)script).exec(cx, scope);
                String r = Context.toString(o);
                ScriptIF scriptIf = objectMapper.readValue(r, ScriptIF.class);
                System.out.println("reason="+scriptIf.getReason());
                if (!(scriptIf.getReason().equals("OK"))) {
                    LogEntry l = new LogEntry();
                    l.setTs(new Date());
                    l.setReason(scriptIf.getReason());
                    l.setLog(scriptIf.getLog());
                    l.setUnit(scriptIf.getUnit());
                    l.setServiceTemplate(scriptIf.getServiceTemplate());
                    l.setId(UUID.randomUUID().toString());
                    String data = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(l);
                    send(System.getProperty("thingGovernor") + "/governor/log", data);
                }
                if (Integer.parseInt(scriptIf.getAmount()) > 0) {
                    PaymentEntry p = new PaymentEntry();
                    p.setAmount(scriptIf.getAmount());
                    p.setSender(scriptIf.getSender());
                    p.setReceiver(scriptIf.getReceiver());
                    String data = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(p);
                    send(System.getProperty("thingGovernor") + "/governor/payment", data);
                }
                if (scriptIf.getReason().equals("ABORT")) {
                    System.out.println("Access denied, aborting");
                    return;
                }
            } finally {
                Context.exit();
            }
        }
        joinPoint.proceed();
        System.out.println("Around after");
    }

}
