package org.paniergarni.order.business;

import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;

import java.util.List;

public interface OrderBusiness {
    Order createOrder(List<OrderProduct> orderProducts);
    Order getOrder(Long id);
}
