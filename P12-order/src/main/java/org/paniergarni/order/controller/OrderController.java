package org.paniergarni.order.controller;

import org.paniergarni.order.business.OrderBusiness;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.paniergarni.order.exception.OrderException;
import org.paniergarni.order.exception.SequenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderBusiness orderBusiness;

    @GetMapping(value = "/order/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable(name = "id") Long id) throws OrderException {

        Order order = orderBusiness.getOrder(id);

        return ResponseEntity.ok().body(order);
    }

    @GetMapping(value = "/receptionOrders")
    public ResponseEntity<?> getListReceptionOrder() {

        List<Order> orders = orderBusiness.getListOrderReception();

        return ResponseEntity.ok().body(orders);
    }

    @GetMapping(value = "/lateOrders")
    public ResponseEntity<?> getListLateOrder() {

        List<Order> orders = orderBusiness.getListOrderLate();

        return ResponseEntity.ok().body(orders);
    }


    @PostMapping(value = "/order/{userName}")
    public ResponseEntity<?> createOrder(@PathVariable(name = "userName") String userName, @PathVariable(name = "reception") Date reception, @RequestBody List<OrderProduct> orderProducts) throws OrderException {

        Order order = orderBusiness.createOrder(orderProducts, userName, reception);

        return ResponseEntity.ok().body(order);
    }

    @PutMapping(value = "/cancelOrder/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable(name = "id") Long id) throws OrderException {

        orderBusiness.cancelOrder(id);

        return ResponseEntity.ok().body(null);
    }

    @PutMapping(value = "/paidOrder/{id}")
    public ResponseEntity<?> paidOrder(@PathVariable(name = "id") Long id) throws SequenceException, OrderException {

        orderBusiness.paidOrder(id);

        return ResponseEntity.ok().body(null);
    }
}
