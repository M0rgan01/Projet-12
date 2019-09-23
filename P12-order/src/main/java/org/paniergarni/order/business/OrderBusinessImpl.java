package org.paniergarni.order.business;

import feign.FeignException;
import org.paniergarni.order.dao.OrderRepository;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.paniergarni.order.entities.Sequence;
import org.paniergarni.order.exception.CancelException;
import org.paniergarni.order.exception.OrderException;
import org.paniergarni.order.exception.ReceptionException;
import org.paniergarni.order.exception.SequenceException;
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
public class OrderBusinessImpl implements OrderBusiness {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SequenceBusiness sequenceBusiness;
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

        if (totalPrice == 0) {
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
        order.setReference(addReference(order));
        order.setPaid(false);
        order.setCancel(false);
        order.setReception(truncateTime(reception));
        order.setTotalPrice(totalPrice);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrder(Long id) throws OrderException {
        return orderRepository.findById(id).orElseThrow(() -> new OrderException("order.id.incorrect"));
    }

    @Override
    public void cancelOrder(Long id) throws OrderException {
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
    public void paidOrder(Long id) throws OrderException, SequenceException {
        Order order = getOrder(id);
        sequenceBusiness.addSaleNumber(order.getDate());
        order.setPaid(true);
        orderRepository.save(order);
    }

    @Override
    public List<Order> getListOrderLate() {
        return orderRepository.getListOrderLate(truncateTime(new Date()));
    }

    @Override
    public List<Order> getListOrderReception() {
        return orderRepository.getListOrderReception(truncateTime(new Date()));
    }

    @Override
    public String addReference(Order order) {

        Sequence sequence;
        try {
            sequence = sequenceBusiness.getByDate(order.getDate());
        } catch (SequenceException e) {
            sequence = sequenceBusiness.createSequence();
        }

        return sequence.getDate() + "-" + sequence.getSequence();
    }

    @Override
    public List<Date> getListDateReception() {

        List<Date> dateList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < maxDaysReception; i++) {

            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                calendar.add(Calendar.DATE, 1);

            calendar.add(Calendar.DATE, 1);
            dateList.add(calendar.getTime());
        }

        return dateList;
    }


    public static Date truncateTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}
