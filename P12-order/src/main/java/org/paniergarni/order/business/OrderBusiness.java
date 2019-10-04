package org.paniergarni.order.business;

import feign.FeignException;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.paniergarni.order.entities.WrapperOrderProductDTO;
import org.paniergarni.order.exception.CriteriaException;
import org.paniergarni.order.exception.OrderException;
import org.paniergarni.order.dao.specification.SearchCriteria;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface OrderBusiness {
    Order createOrder(WrapperOrderProductDTO wrapperOrderProductDTO, String userName, Long reception) throws OrderException;

    Order getOrder(Long id) throws OrderException ;
    Order getOrder(Long id, String userName) throws OrderException, FeignException;

    Order cancelOrder(Long id) throws OrderException;
    Order cancelOrder(Long id, String userName) throws OrderException, FeignException;
    Order paidOrder(Long id) throws OrderException;

    List<Order> getListOrderLate();
    List<Date> getListDateReception();
    List<Order> getListOrderReception();

    Page<Order> searchOrder(String userName, int page, int size, List<SearchCriteria> searchCriteriaList) throws CriteriaException, FeignException;
    Page<Order> searchOrder(int page, int size, List<SearchCriteria> searchCriteriaList) throws CriteriaException;

    String addReference(Order order);

    int getMaxDaysReception();
    int getMaxHoursCancelOrder();
}
