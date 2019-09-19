package org.paniergarni.order.business;

import feign.FeignException;
import org.hibernate.cache.CacheException;
import org.paniergarni.order.dao.OrderRepository;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.paniergarni.order.exception.CancelException;
import org.paniergarni.order.exception.OrderException;
import org.paniergarni.order.exception.ReceptionException;
import org.paniergarni.order.object.Product;
import org.paniergarni.order.object.User;
import org.paniergarni.order.proxy.ProductProxy;
import org.paniergarni.order.proxy.UserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
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
    @Value("${min.hours.reception}")
    private int minHoursReception;
    @Value("${max.days.reception}")
    private int maxDaysReception;
    @Value("${max.hours.cancel.order}")
    private int maxHoursCancelOrder;

    @Override
    public synchronized Order createOrder(List<OrderProduct> orderProducts, String userName, Date reception) throws OrderException {
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

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, minHoursReception);

        if (reception.before(calendar.getTime()))
            throw new ReceptionException("order.reception.before.min.value");

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, maxDaysReception);

        if (reception.after(calendar.getTime()))
            throw new ReceptionException("order.reception.after.max.value");

        order.setOrderProducts(orderProducts);
        order.setTotalPrice(totalPrice);
        order.setUserId(user.getId());
        order.setDate(new Date());
        order.setPaid(false);
        order.setCancel(false);
        order.setReception(new Date());
        order.setTotalPrice(totalPrice);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrder(Long id) throws OrderException {
        return orderRepository.findById(id).orElseThrow(() -> new OrderException("order.id.incorrect"));
    }

    @Override
    public void cancelOrder(Long id) throws CancelException {
        Order order = getOrder(id);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(order.getDate());
        calendar.add(Calendar.HOUR, maxHoursCancelOrder);

        if (new Date().after(calendar.getTime()))
            throw new CancelException("order.cancel.after.max.value");

        order.setCancel(true);
        orderRepository.save(order);
    }

    @Override
    public void paidOrder(Long id) {
        Order order = getOrder(id);
        order.setPaid(true);
        orderRepository.save(order);
    }

}
