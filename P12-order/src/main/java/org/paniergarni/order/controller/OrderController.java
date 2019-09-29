package org.paniergarni.order.controller;

import feign.FeignException;
import org.paniergarni.order.business.OrderBusiness;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.paniergarni.order.exception.OrderException;
import org.paniergarni.order.exception.SequenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderBusiness orderBusiness;

    @GetMapping(value = "/adminRole/order/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable(name = "id") Long id) throws OrderException {

        Order order = orderBusiness.getOrder(id);

        return ResponseEntity.ok().body(order);
    }

    @GetMapping(value = "/userRole/order/{id}/{userName}")
    public ResponseEntity<?> getOrderById(@PathVariable(name = "id") Long id, @PathVariable(name = "userName") String userName) throws OrderException {

        Order order = orderBusiness.getOrder(id, userName);

        return ResponseEntity.ok().body(order);
    }

    @GetMapping(value = "/userRole/orders/{userName}/{page}/{size}")
    public ResponseEntity<?> getOrdersByUserName(@PathVariable(name = "userName") String userName,
                                                 @PathVariable(name = "page") int page,
                                                 @PathVariable(name = "size") int size) throws OrderException {

        Page<Order> orders = orderBusiness.getOrders(userName, page, size);

        return ResponseEntity.ok().body(orders);
    }

    @GetMapping(value = "/adminRole/receptionOrders")
    public ResponseEntity<?> getListReceptionOrder() {

        List<Order> orders = orderBusiness.getListOrderReception();

        return ResponseEntity.ok().body(orders);
    }

    @GetMapping(value = "/adminRole/lateOrders")
    public ResponseEntity<?> getListLateOrder() {

        List<Order> orders = orderBusiness.getListOrderLate();

        return ResponseEntity.ok().body(orders);
    }

    @GetMapping(value = "/userRole/listDateReception")
    public ResponseEntity<?> getListDateReception() {

        List<Date> dateList = orderBusiness.getListDateReception();

        return ResponseEntity.ok().body(dateList);
    }

    @GetMapping(value = "/public/maxDaysReception")
    public ResponseEntity<?> getMaxDaysReception() {

        int a = orderBusiness.getMaxDaysReception();

        return ResponseEntity.ok().body(a);
    }

    @GetMapping(value = "/public/maxHoursCancelOrder")
    public ResponseEntity<?> getMaxHoursCancelOrder() {

        int a = orderBusiness.getMaxHoursCancelOrder();

        return ResponseEntity.ok().body(a);
    }

    @PostMapping(value = "/userRole/order/{userName}/{reception}")
    public ResponseEntity<?> createOrder(@PathVariable(name = "userName") String userName,
                                         @PathVariable(name = "reception") Long reception,
                                         @RequestBody List<OrderProduct> orderProducts) throws OrderException {

        Order order = orderBusiness.createOrder(orderProducts, userName, reception);

        return ResponseEntity.ok().body(order);
    }

    @PutMapping(value = "/userRole/cancelOrder/{id}/{userName}")
    public ResponseEntity<?> cancelOrder(@PathVariable(name = "id") Long id,
                                         @PathVariable(name = "userName") String userName) throws OrderException, FeignException {

        Order order = orderBusiness.cancelOrder(id, userName);

        return ResponseEntity.ok().body(order);
    }

    @PutMapping(value = "/adminRole/cancelOrder/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable(name = "id") Long id) throws OrderException, FeignException {

        Order order = orderBusiness.cancelOrder(id);

        return ResponseEntity.ok().body(order);
    }

    @PutMapping(value = "/adminRole/paidOrder/{id}")
    public ResponseEntity<?> paidOrder(@PathVariable(name = "id") Long id) throws SequenceException, OrderException {

        orderBusiness.paidOrder(id);

        return ResponseEntity.ok().body(null);
    }
}
