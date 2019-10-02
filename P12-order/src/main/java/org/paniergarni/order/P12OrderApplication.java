package org.paniergarni.order;

import org.paniergarni.order.business.OrderBusiness;
import org.paniergarni.order.business.OrderBusinessImpl;
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
    public void run(String... args) throws Exception {

/*
        User user = userProxy.findByUserName("Admin");

        Order order = new Order();




        order.setOrderProducts(orderProducts);
        order.setUserId(user.getId());
        order.setDate(new Date());
        order.setPaid(false);
        order.setReception(new Date());

        order = orderRepository.save(order);

        Order order1 = orderRepository.findById(order.getId()).get();

        System.out.println(order1.getId());
        System.out.println(order1.getOrderProducts().size());*/



        Order order = new Order();
        order.setReception(OrderBusinessImpl.truncateTime(new Date()));
        order.setUserId(1L);
        order.setDate(OrderBusinessImpl.truncateTime(new Date()));
        order.setPaid(false);


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


        Order order2 = new Order();
        order2.setReception(OrderBusinessImpl.truncateTime(new Date()));
        order2.setUserId(2L);
        order2.setDate(new Date());
        order2.setPaid(false);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -2);

        Order order3 = new Order();
        order3.setReception(OrderBusinessImpl.truncateTime(calendar.getTime()));
        order3.setUserId(2L);
        order3.setDate(new Date());
        order3.setPaid(false);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);

        Order order4 = new Order();
        order4.setReception(OrderBusinessImpl.truncateTime(calendar.getTime()));
        order4.setUserId(1L);
        order4.setDate(new Date());
        order4.setPaid(false);

        order = orderRepository.save(order);
        order2 = orderRepository.save(order2);
        order3 = orderRepository.save(order3);
        order4 = orderRepository.save(order4);

        System.out.println(order.getOrderProducts().size());
        Order order1 = orderRepository.findById(order.getId()).get();
        System.out.println(order1.getOrderProducts().size());

        List<Order> orderLate = orderBusiness.getListOrderLate();
        System.out.println(orderLate.size() + ": orderLate");
        List<Order> orderReception = orderBusiness.getListOrderReception();
        System.out.println(orderReception.size() + ": reception");
    }
}
