package org.paniergarni.order;

import org.paniergarni.order.business.OrderBusiness;
import org.paniergarni.order.business.OrderBusinessImpl;
import org.paniergarni.order.dao.OrderRepository;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.paniergarni.order.proxy.UserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.*;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("org.paniergarni.order.proxy")
public class P12OrderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(P12OrderApplication.class, args);
    }

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderBusiness orderBusiness;
    @Autowired
    private UserProxy userProxy;

    @Override
    public void run(String... args) throws Exception { }
}
