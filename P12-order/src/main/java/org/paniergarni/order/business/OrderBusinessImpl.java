package org.paniergarni.order.business;

import org.paniergarni.order.dao.OrderRepository;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.paniergarni.order.object.User;
import org.paniergarni.order.proxy.UserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderBusinessImpl implements OrderBusiness{

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserProxy userProxy;

    @Override
    public Order createOrder(List<OrderProduct> orderProducts, String userName) {

        Order order = new Order();
        order.setOrderProducts(orderProducts);
        User user = userProxy.findByUserName(userName);
        order.setUserId(user.getId());

        return orderRepository.save(order);
    }

    @Override
    public Order getOrder(Long id) {
        return null;
    }
}
