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

/**
 *
 * @author Peter Klein
 */
import at.ac.tuwien.dsg.thingsim.model.Thing;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;

public class ContextInitFinishListener implements ApplicationListener<ContextRefreshedEvent> {

    private final Thing thing;
    static final Logger log = Logger.getLogger(ContextInitFinishListener.class.getName());

    public ContextInitFinishListener(Thing thing) {
        this.thing = thing;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ctx = event.getApplicationContext();
        log.info("Application context inistialized");
        thing.setMqttInputAdapter((MqttPahoMessageDrivenChannelAdapter)ctx.getBean("MqttInputAdapter"));
        thing.setMqttOutputAdapter((MqttPahoMessageHandler)ctx.getBean("MqttOutputAdapter"));
        thing.setCtx(event.getApplicationContext());
        thing.getPortHandler().init();
        Map<String, InputPort> ports = thing.getPortHandler().getInputPorts();
        Set<String> keys = ports.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String key = it.next();
            InputPort p = ports.get(key);
            if ((p instanceof FileInputPort)      || 
                (p instanceof RandomInputPort) ||
                (p instanceof MqttInputPort)) {
                p.execute(thing);
            }
        }
    }
}
