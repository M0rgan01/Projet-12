package org.paniergarni.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class P12ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(P12ApiGatewayApplication.class, args);
    }

}
