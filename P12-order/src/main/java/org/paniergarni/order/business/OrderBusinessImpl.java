package org.paniergarni.order.business;

import org.paniergarni.order.dao.OrderRepository;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderBusinessImpl implements OrderBusiness{

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order createOrder(List<OrderProduct> orderProducts) {
        return null;
    }

    @Override
    public Order getOrder(Long id) {
        return null;
    }
}
