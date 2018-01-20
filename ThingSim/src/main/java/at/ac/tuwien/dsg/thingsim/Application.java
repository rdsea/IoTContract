/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.thingsim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

/** Main Application class utilizing spring boot
 * @author Peter Klein
 *
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {
    
    /**
     * @param args standard program args
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebEnvironment(true);
        ConfigurableApplicationContext ctx = app.run(args);
    }
	
}
