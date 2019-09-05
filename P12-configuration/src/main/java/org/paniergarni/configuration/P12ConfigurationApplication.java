package org.paniergarni.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class P12ConfigurationApplication {

    public static void main(String[] args) {
        SpringApplication.run(P12ConfigurationApplication.class, args);
    }

}
