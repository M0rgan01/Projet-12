package org.paniergarni.order.business;

import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.paniergarni.order.exception.OrderException;
import org.paniergarni.order.exception.SequenceException;

import java.util.Date;
import java.util.List;

public interface OrderBusiness {
    Order createOrder(List<OrderProduct> orderProducts, String userName, Date reception) throws OrderException;
    Order getOrder(Long id) throws OrderException;
    void cancelOrder(Long id) throws OrderException;
    void paidOrder(Long id) throws OrderException, SequenceException;
    List<Order> getListOrderLate();
    List<Order> getListOrderReception();
    String addReference(Order order);
    List<Date> getListDateReception();
}
