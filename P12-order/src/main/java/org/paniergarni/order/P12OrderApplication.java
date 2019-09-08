package org.paniergarni.order;

import org.paniergarni.order.dao.OrderRepository;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.paniergarni.order.object.Product;
import org.paniergarni.order.object.User;
import org.paniergarni.order.proxy.UserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Arrays;

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


        User user = userProxy.findByUserName("Admin");

        System.out.println(user.toString());

        /*Product p = new Product();
        p.setId(1l);
        p.setName("Product1");

        Product p2 = new Product();
        p2.setId(2l);
        p2.setName("Product2");


        Order order = new Order();

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProductId(p.getId());
        orderProduct.setOrder(order);
        orderProduct.setQuantity(5);
        order.setOrderProducts(Arrays.asList(orderProduct));

        order = orderRepository.save(order);

        Order order1 = orderRepository.findById(order.getId()).get();

        for ( OrderProduct orderProduct1: order1.getOrderProducts()) {
            System.out.println(orderProduct1.getQuantity());
        }*/

    }
}
