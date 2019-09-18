package org.paniergarni.order.business;

import feign.FeignException;
import org.paniergarni.order.dao.OrderRepository;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.paniergarni.order.exception.OrderException;
import org.paniergarni.order.object.Product;
import org.paniergarni.order.object.User;
import org.paniergarni.order.proxy.ProductProxy;
import org.paniergarni.order.proxy.UserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class OrderBusinessImpl implements OrderBusiness{

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserProxy userProxy;
    @Autowired
    private ProductProxy productProxy;

    @Override
    public synchronized Order createOrder(List<OrderProduct> orderProducts, String userName) {
        User user = userProxy.findByUserName(userName);
        Order order = new Order();
        double totalPrice = 0;

        for (OrderProduct orderProduct : orderProducts) {
            try {
                Product product = productProxy.updateProductQuantity(orderProduct.getOrderQuantity(), orderProduct.getProductId());
                orderProduct.setRealQuantity(product.getOrderProductRealQuantity());
                orderProduct.setTotalPriceRow(product.getPrice() * orderProduct.getRealQuantity());
                totalPrice = totalPrice + orderProduct.getTotalPriceRow();
                orderProduct.setOrder(order);
            } catch (FeignException e) {
                orderProduct.setRealQuantity(0);
            }
        }

        if (totalPrice == 0){
            throw new OrderException("order.products.quantity.null");
        }

        order.setOrderProducts(orderProducts);
        order.setTotalPrice(totalPrice);
        order.setUserId(user.getId());
        order.setDate(new Date());
        order.setPaid(false);
        order.setReception(new Date());
        order.setTotalPrice(25);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderException("order.id.incorrect"));
    }

}
