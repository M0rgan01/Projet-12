package org.paniergarni.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableFeignClients("org.paniergarni.batch.proxy")
public class P12BatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(P12BatchApplication.class, args);
    }

}
