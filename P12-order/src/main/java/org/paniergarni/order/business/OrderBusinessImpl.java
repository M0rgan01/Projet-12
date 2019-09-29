package org.paniergarni.order.business;

import feign.FeignException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public synchronized Order createOrder(List<OrderProduct> orderProducts, String userName, Long reception) throws OrderException {
        User user = userProxy.findByUserName(userName);
        Order order = new Order();
        double totalPrice = 0;

        for (OrderProduct orderProduct : orderProducts) {

            try {
                Product product = productProxy.updateProductQuantity(orderProduct.getOrderQuantity(), orderProduct.getProductId(), false);
                orderProduct.setRealQuantity(product.getOrderProductRealQuantity());
                if (!product.isPromotion()) {
                    orderProduct.setTotalPriceRow(product.getPrice() * orderProduct.getRealQuantity());
                } else {
                    orderProduct.setTotalPriceRow(product.getPromotionPrice() * orderProduct.getRealQuantity());
                }
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

        Date receptionDate = new Date(reception);
        receptionDate = truncateTime(receptionDate);

        if (receptionDate.before(truncateTime(calendar.getTime())))
            throw new ReceptionException("order.reception.before.min.value");

        calendar.setTime(getListDateReception().get(getListDateReception().size() - 1));

        if (receptionDate.after(calendar.getTime()))
            throw new ReceptionException("order.reception.after.max.value");

        order.setOrderProducts(orderProducts);
        order.setTotalPrice(totalPrice);
        order.setUserId(user.getId());
        order.setDate(new Date());
        order.setReference(addReference(order));
        order.setPaid(false);
        order.setCancel(false);
        order.setReception(receptionDate);
        order.setTotalPrice(totalPrice);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrder(Long id) throws OrderException {
        return orderRepository.findById(id).orElseThrow(() -> new OrderException("order.id.incorrect"));
    }

    @Override
    public Order getOrder(Long id, String userName) throws OrderException, FeignException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderException("order.id.incorrect"));
        User user = userProxy.findByUserName(userName);

        if (!order.getUserId().equals(user.getId())) {
            throw new OrderException("order.id.incorrect");
        }

        return order;
    }

    @Override
    public Page<Order> getOrders(String userName, int page, int size) throws FeignException {
        User user = userProxy.findByUserName(userName);
        return orderRepository.getAllByUserIdOrderByDate(user.getId(), PageRequest.of(page, size));
    }

    @Override
    public Order cancelOrder(Long id) throws OrderException {
        Order order = getOrder(id);
        return validateCancelOrder(order);
    }

    @Override
    public Order cancelOrder(Long id, String userName) throws OrderException, FeignException {
        Order order = getOrder(id, userName);
        return validateCancelOrder(order);
    }

    private Order validateCancelOrder(Order order) throws CancelException {

        if (order.getCancel())
            throw new CancelException("order.already.cancel");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(order.getDate());
        calendar.add(Calendar.HOUR, maxHoursCancelOrder);

        if (new Date().after(calendar.getTime()))
            throw new CancelException("order.cancel.after.max.value");

        order.setCancel(true);

        for (OrderProduct orderProduct: order.getOrderProducts()) {
            productProxy.updateProductQuantity(orderProduct.getRealQuantity(), orderProduct.getProductId(), true);
        }

        return orderRepository.save(order);
    }

    @Override
    public Order paidOrder(Long id) throws OrderException {
        Order order = getOrder(id);
        order.setPaid(true);
        return orderRepository.save(order);
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

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(order.getDate());

        String sequence = String.format("%05d", orderRepository.getCountOrderByMount(calendar.get(Calendar.MONTH) + 1))
                + '-' + (calendar.get(Calendar.MONTH) + 1) + '/'
                + calendar.get(Calendar.DATE);

        return sequence;
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

    @Override
    public int getMaxDaysReception() {
        return maxDaysReception;
    }

    @Override
    public int getMaxHoursCancelOrder() {
        return maxHoursCancelOrder;
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
