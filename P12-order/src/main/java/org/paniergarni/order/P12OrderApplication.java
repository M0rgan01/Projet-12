package org.paniergarni.order;

import org.paniergarni.order.dao.OrderRepository;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.paniergarni.order.object.User;
import org.paniergarni.order.proxy.UserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    private UserProxy userProxy;

    @Override
    public void run(String... args) throws Exception {

/*
        User user = userProxy.findByUserName("Admin");

        Order order = new Order();


        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProductId(1L);
        orderProduct.setOrderQuantity(1);
        orderProduct.setOrder(order);

        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setProductId(2L);
        orderProduct2.setOrderQuantity(1);
        orderProduct2.setOrder(order);

        OrderProduct orderProduct3 = new OrderProduct();
        orderProduct3.setProductId(3L);
        orderProduct3.setOrderQuantity(1);
        orderProduct3.setOrder(order);

        List<OrderProduct> orderProducts = new ArrayList<>();
        orderProducts.add(orderProduct);
        orderProducts.add(orderProduct2);
        orderProducts.add(orderProduct3);

        order.setOrderProducts(orderProducts);
        order.setUserId(user.getId());
        order.setDate(new Date());
        order.setPaid(false);
        order.setReception(new Date());

        order = orderRepository.save(order);

        Order order1 = orderRepository.findById(order.getId()).get();

        System.out.println(order1.getId());
        System.out.println(order1.getOrderProducts().size());*/
    }
}
