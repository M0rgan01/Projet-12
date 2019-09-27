package org.paniergarni.order.business;

import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.paniergarni.order.exception.OrderException;
import org.paniergarni.order.exception.SequenceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface OrderBusiness {
    Order createOrder(List<OrderProduct> orderProducts, String userName, Long reception) throws OrderException;
    Order getOrder(Long id) throws OrderException;
    Order getOrder(Long id, String userName) throws OrderException;
    Page<Order> getOrders(String userName, int page, int size) throws OrderException;
    Order cancelOrder(Long id) throws OrderException;
    Order cancelOrder(Long id, String userName) throws OrderException;
    Order paidOrder(Long id) throws OrderException, SequenceException;
    List<Order> getListOrderLate();
    List<Order> getListOrderReception();
    String addReference(Order order);
    List<Date> getListDateReception();
    int getMaxDaysReception();
    int getMaxHoursCancelOrder();
}
