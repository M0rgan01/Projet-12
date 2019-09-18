package org.paniergarni.order.controller;

import org.paniergarni.order.business.OrderBusiness;
import org.paniergarni.order.dao.OrderProductRepository;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderBusiness orderBusiness;
    @Autowired
    private OrderProductRepository orderProductRepository;


    @GetMapping(value = "/order/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable(name = "id") Long id) {

        Order order = orderBusiness.getOrder(id);

        return ResponseEntity.ok().body(order);
    }

    @GetMapping(value = "/orderProduct/{id}")
    public ResponseEntity<?> getOrderProductById(@PathVariable(name = "id") Long id) {

        OrderProduct orderProduct = orderProductRepository.findById(id).get();

        return ResponseEntity.ok().body(orderProduct);
    }

    @PostMapping(value = "/order/{userName}")
    public ResponseEntity<?> createOrder(@PathVariable(name = "userName") String userName, @RequestBody List<OrderProduct> orderProducts) {

        Order order = orderBusiness.createOrder(orderProducts, userName);

        return ResponseEntity.ok().body(order);
    }
}
