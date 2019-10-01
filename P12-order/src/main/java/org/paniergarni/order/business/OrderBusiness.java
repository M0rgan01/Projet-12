package org.paniergarni.order.business;

import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.paniergarni.order.exception.OrderException;
import org.paniergarni.order.exception.SequenceException;
import org.paniergarni.order.dao.specification.SearchCriteria;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface OrderBusiness {
    Order createOrder(List<OrderProduct> orderProducts, String userName, Long reception) throws OrderException;

    Order getOrder(Long id) throws OrderException;
    Order getOrder(Long id, String userName) throws OrderException;

    Order cancelOrder(Long id) throws OrderException;
    Order cancelOrder(Long id, String userName) throws OrderException;
    Order paidOrder(Long id) throws OrderException, SequenceException;

    List<Order> getListOrderLate();
    List<Date> getListDateReception();
    List<Order> getListOrderReception();

    Page<Order> searchProduct(String userName, int page, int size, List<SearchCriteria> searchCriteriaList) throws OrderException;
    Page<Order> searchProduct(int page, int size, List<SearchCriteria> searchCriteriaList) throws OrderException;

    String addReference(Order order);

    int getMaxDaysReception();
    int getMaxHoursCancelOrder();
}
