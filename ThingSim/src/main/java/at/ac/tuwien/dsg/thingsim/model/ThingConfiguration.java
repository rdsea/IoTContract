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
package at.ac.tuwien.dsg.thingsim.model;

import at.ac.tuwien.dsg.thingsim.ContextInitFinishListener;
import at.ac.tuwien.dsg.thingsim.InputPort;
import at.ac.tuwien.dsg.thingsim.model.Port.DataType;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 *
 * @author Peter Klein
 */
@Configuration
public class ThingConfiguration {
    
    static Logger log = Logger.getLogger(ThingConfiguration.class.getName());
    
    @Autowired
    Thing thing;

    @Bean
    public ApplicationListener<ContextRefreshedEvent> contextInitFinishListener() {
        try {
            return new ContextInitFinishListener(thing);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        String brokerUrl = System.getProperty("mqttBrokerUrl");
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        //factory.setServerURIs("tcp://iot.eclipse.org:1883");
        factory.setServerURIs(brokerUrl);
        //factory.setUserName("username");
        //factory.setPassword("password");
        log.info("MQTT started");
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }
    
    @Bean
    public MessageChannel mqttOutputChannel() {
        return new DirectChannel();
    }

    @Bean (name="MqttInputAdapter")
    public MessageProducer inbound() {
        String clientName = thing.configHandler.getConfigData().getName();   
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter("ThingSim_inbound_" + clientName, mqttClientFactory(), thing.configHandler.getConfigData().getName() + "Input");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean (name="MqttInputChannel")
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler mqttInputHandler() {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println(Thread.currentThread().getName() + ":" + 
                                   Thread.currentThread().getId() + ":" + 
                                   message.getHeaders().get("mqtt_topic") + ":" + message.getPayload());
                System.out.println(message.getHeaders().get("mqtt_topic", String.class));
                String[] header = message.getHeaders().get("mqtt_topic", String.class).split("/");
                DataType type = DataType.valueOf(header[1]);
                String portName = header[2];
                InputPort p = thing.portHandler.getInputPorts().get(portName);
                p.process(thing, ""+message.getPayload());
            }

        };
    }
       
    @Bean (name="MqttOutputAdapter")
    @ServiceActivator(inputChannel = "mqttOutputChannel")
    public MessageHandler mqttOutputHandler() {
            String clientName = thing.configHandler.getConfigData().getName();
            MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
                            "ThingSim_outbound" + clientName, mqttClientFactory());
            messageHandler.setAsync(true);
            messageHandler.setDefaultTopic(clientName + "Output");
            return messageHandler;
    }

}
