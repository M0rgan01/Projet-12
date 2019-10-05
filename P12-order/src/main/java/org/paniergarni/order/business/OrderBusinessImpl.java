package org.paniergarni.order.business;

import feign.FeignException;
import org.paniergarni.order.dao.OrderRepository;
import org.paniergarni.order.dao.specification.OrderSpecificationBuilder;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.paniergarni.order.entities.OrderProductDTO;
import org.paniergarni.order.entities.WrapperOrderProductDTO;
import org.paniergarni.order.exception.CancelException;
import org.paniergarni.order.exception.CriteriaException;
import org.paniergarni.order.exception.OrderException;
import org.paniergarni.order.exception.ReceptionException;
import org.paniergarni.order.object.Product;
import org.paniergarni.order.dao.specification.SearchCriteria;
import org.paniergarni.order.object.User;
import org.paniergarni.order.proxy.ProductProxy;
import org.paniergarni.order.proxy.UserProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class OrderBusinessImpl implements OrderBusiness {

    private static final Logger logger = LoggerFactory.getLogger(OrderBusinessImpl.class);

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
    public synchronized Order createOrder(WrapperOrderProductDTO wrapperOrderProductDTO, String userName, Long reception) throws OrderException {
        User user = userProxy.findByUserName(userName);
        Order order = new Order();
        List<OrderProduct> orderProducts = new ArrayList<>();
        double totalPrice = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, minHoursReception);

        Date receptionDate = new Date(reception);

        if (receptionDate.before(calendar.getTime())) {
            logger.warn("Order reception before min value");
            throw new ReceptionException("order.reception.before.min.value");
        }

        calendar.setTime(getListDateReception().get(getListDateReception().size() - 1));

        if (receptionDate.after(calendar.getTime())) {
            logger.warn("Order reception after max value");
            throw new ReceptionException("order.reception.after.max.value");
        }

        receptionDate = truncateTime(receptionDate);

        for (OrderProductDTO orderProductDTO : wrapperOrderProductDTO.getOrderProducts()) {

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setOrderQuantity(orderProductDTO.getOrderQuantity());
            orderProduct.setProductId(orderProductDTO.getProductId());
            try {
                Product product = productProxy.updateProductQuantity(orderProductDTO.getOrderQuantity(), orderProductDTO.getProductId(), false);
                orderProduct.setRealQuantity(product.getOrderProductRealQuantity());
                orderProduct.setTotalPriceRow(product.getPrice() * orderProduct.getRealQuantity());
                orderProduct.setProductPrice(product.getPrice());
                if (product.isPromotion())
                    orderProduct.setProductOldPrice(product.getOldPrice());
                totalPrice = totalPrice + orderProduct.getTotalPriceRow();
                orderProducts.add(orderProduct);
            } catch (FeignException e) {
                System.out.println("Catch");
                orderProduct.setRealQuantity(0);
            }
        }

        if (totalPrice == 0) {
            logger.warn("Order product quantity null");
            throw new OrderException("order.products.quantity.null");
        }

        order.setOrderProducts(orderProducts);
        order.setTotalPrice(totalPrice);
        order.setUserId(user.getId());
        order.setDate(new Date());
        order.setReference(addReference(order));
        order.setPaid(false);
        order.setCancel(false);
        order.setReception(receptionDate);
        order.setTotalPrice(totalPrice);
        logger.info("Create order with reference " + order.getReference());
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
    public Page<Order> searchOrder(String userName, int page, int size, List<SearchCriteria> searchCriteriaList) throws FeignException, CriteriaException {
        User user = userProxy.findByUserName(userName);
        if (searchCriteriaList == null)
            searchCriteriaList = new ArrayList<>();

        logger.debug("Searching orders for userName " + userName + " and list of criteria of " + searchCriteriaList.size() + "elements");
        searchCriteriaList.add(new SearchCriteria("userId", ":", user.getId()));
        OrderSpecificationBuilder builder = new OrderSpecificationBuilder(searchCriteriaList);
        Specification<Order> spec = builder.build();
        return orderRepository.findAll(spec, PageRequest.of(page, size));
    }

    @Override
    public Page<Order> searchOrder(int page, int size, List<SearchCriteria> searchCriteriaList) throws CriteriaException {
        if (searchCriteriaList != null) {
            logger.debug("Admin searching orders with list of criteria of " + searchCriteriaList.size() + "elements");
        } else {
            logger.debug("Admin searching orders");
        }
        OrderSpecificationBuilder builder = new OrderSpecificationBuilder(searchCriteriaList);
        Specification<Order> spec = builder.build();
        return orderRepository.findAll(spec, PageRequest.of(page, size));
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
    public Order paidOrder(Long id) throws OrderException {
        logger.info("Paid order ID : " + id);
        Order order = getOrder(id);
        if (order.getPaid())
            throw new OrderException("order.already.paid");
        order.setPaid(true);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long id) throws OrderException {
        logger.info("Admin cancel order ID : " + id);
        Order order = getOrder(id);
        return validateCancelOrder(order, true);
    }

    @Override
    public Order cancelOrder(Long id, String userName) throws OrderException, FeignException {
        logger.info("User cancel order ID : " + id);
        Order order = getOrder(id, userName);
        return validateCancelOrder(order, false);
    }

    private Order validateCancelOrder(Order order, boolean admin) throws CancelException {

        if (order.getCancel()) {
            logger.info("Order already cancel for order ID : " + order.getId());
            throw new CancelException("order.already.cancel");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, maxHoursCancelOrder);

        if (order.getDate().after(calendar.getTime()) && !admin) {
            logger.info("Order cancel after max hours for order ID : " + order.getId());
            throw new CancelException("order.cancel.after.max.value");
        }

        order.setCancel(true);

        for (OrderProduct orderProduct : order.getOrderProducts()) {
            productProxy.updateProductQuantity(orderProduct.getRealQuantity(), orderProduct.getProductId(), true);
        }
        logger.info("Success cancel order ID : " + order.getId());
        return orderRepository.save(order);
    }


    @Override
    public String addReference(Order order) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(order.getDate());

        return String.format("%05d", orderRepository.getCountOrderByMount(calendar.get(Calendar.MONTH) + 1))
                + '-' + (calendar.get(Calendar.MONTH) + 1) + '/'
                + calendar.get(Calendar.DATE);
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
