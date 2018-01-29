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
package at.ac.tuwien.dsg.thingsim;

import at.ac.tuwien.dsg.thingsim.model.Command;
import at.ac.tuwien.dsg.thingsim.model.DataPoint;
import at.ac.tuwien.dsg.thingsim.model.Thing;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mozilla.javascript.*;
import org.springframework.stereotype.Component;

/**
 *
 * @author Peter Klein
 */
@Component
public class Processor {
    
    static final Logger log = Logger.getLogger(Processor.class.getName());
    
    int eventCount = 0;
    
    public void processDataPoint(String fileName, 
                                 DataPoint dp, 
                                 Thing thing,
                                 String port) {
         Context cx = Context.enter();
         try {
             log.log(Level.INFO,"starting to process data point" + ++eventCount);
             BufferedReader br = new BufferedReader(new FileReader(fileName));
             Scriptable scope = cx.initStandardObjects();
             Object wrappedDp = Context.javaToJS(dp, scope);
             ScriptableObject.putProperty(scope, "dataPoint", wrappedDp);
             Object wrappedThing = Context.javaToJS(thing, scope);
             ScriptableObject.putProperty(scope, "thing", wrappedThing);
             Object wrappedPort = Context.javaToJS(port, scope);
             ScriptableObject.putProperty(scope, "port", wrappedPort);
             Object result = cx.evaluateReader(scope, br, "<1>", 1, null);
             log.log(Level.INFO,"finished to process data point" + eventCount);
         } catch (FileNotFoundException ex) {
             ex.printStackTrace();
         } catch (IOException ex) {
             ex.printStackTrace();
         } finally {
            // Exit from the context.
            Context.exit();
        }
    }
    
    public void processCommand(String fileName, 
                               Command cmd, 
                               Thing thing,
                               String port) {
         Context cx = Context.enter();
         try {
             BufferedReader br = new BufferedReader(new FileReader(fileName));
             Scriptable scope = cx.initStandardObjects();
             Object wrappedCmd = Context.javaToJS(cmd, scope);
             ScriptableObject.putProperty(scope, "command", wrappedCmd);
             Object wrappedThing = Context.javaToJS(thing, scope);
             ScriptableObject.putProperty(scope, "thing", wrappedThing);
             Object wrappedPort = Context.javaToJS(port, scope);
             ScriptableObject.putProperty(scope, "port", wrappedPort);
             Object result = cx.evaluateReader(scope, br, "<1>", 1, null);
         } catch (FileNotFoundException ex) {
             ex.printStackTrace();
         } catch (IOException ex) {
             ex.printStackTrace();
         } finally {
            // Exit from the context.
            Context.exit();
        }
    }
    
}
