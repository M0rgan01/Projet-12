package org.paniergarni.order.business;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class OrderBusinessTest {

    @Mock
    private UserProxy userProxy;
    @Mock
    private ProductProxy productProxy;
    @InjectMocks
    private OrderBusinessImpl orderBusiness;
    @Mock
    private OrderRepository orderRepository;

    private List<OrderProduct> orderProducts;
    private User user;
    private Product product;
    private Product product2;
    private Product product3;
    private OrderProduct orderProduct;
    private OrderProduct orderProduct2;
    private OrderProduct orderProduct3;
    private Order order;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(orderBusiness, "minHoursReception", 24);
        ReflectionTestUtils.setField(orderBusiness, "maxDaysReception", 15);
        ReflectionTestUtils.setField(orderBusiness, "maxHoursCancelOrder", 6);

        product = new Product();
        product.setId(1L);
        product.setOrderProductRealQuantity(5);
        product.setPrice(200);

        product2 = new Product();
        product2.setId(2L);
        product2.setOrderProductRealQuantity(15);
        product2.setPromotion(true);
        product2.setPromotionPrice(300);

        product3 = new Product();
        product3.setId(3L);
        product3.setOrderProductRealQuantity(25);
        product3.setPrice(400);

        user = new User();
        user.setUserName("Test");
        user.setId(1L);

        orderProduct = new OrderProduct();
        orderProduct.setProductId(1L);
        orderProduct.setOrderQuantity(5);

        orderProduct2 = new OrderProduct();
        orderProduct2.setProductId(2L);
        orderProduct2.setOrderQuantity(55);

        orderProduct3 = new OrderProduct();
        orderProduct3.setProductId(3L);
        orderProduct3.setOrderQuantity(25);

        orderProducts = new ArrayList<>();
        orderProducts.add(orderProduct);
        orderProducts.add(orderProduct2);
        orderProducts.add(orderProduct3);

    }


    @Test
    public void testCreateOrder() throws OrderException {

        double prod1 = product.getPrice() * product.getOrderProductRealQuantity();
        double prod2 = product2.getPromotionPrice() * product2.getOrderProductRealQuantity();
        double prod3 = product3.getPrice() * product3.getOrderProductRealQuantity();

        double totalPrice = prod1 + prod2 + prod3;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 2);

        Mockito.when(userProxy.findByUserName(user.getUserName())).thenReturn(user);
        Mockito.when(productProxy.updateProductQuantity(orderProduct.getOrderQuantity(), product.getId(), false)).thenReturn(product);
        Mockito.when(productProxy.updateProductQuantity(orderProduct2.getOrderQuantity(), product2.getId(), false)).thenReturn(product2);
        Mockito.when(productProxy.updateProductQuantity(orderProduct3.getOrderQuantity(), product3.getId(), false)).thenReturn(product3);

        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        order = orderBusiness.createOrder(orderProducts, user.getUserName(), calendar.getTimeInMillis());

        for (OrderProduct orderProduct: order.getOrderProducts()) {
            if (orderProduct.getRealQuantity() == 0) {
                assertTrue(orderProduct.getTotalPriceRow() == 0);
            } else {
                assertTrue(orderProduct.getTotalPriceRow() != 0);
            }
        }
        assertEquals(order.getTotalPrice(), totalPrice, 0.001);
        assertEquals(order.getUserId(), user.getId());
        assertNotNull(order.getReception());
        assertNotNull(order.getReference());
        assertNotNull(order.getDate());
        assertFalse(order.getPaid());
        assertFalse(order.getCancel());
    }

    @Test(expected = OrderException.class)
    public void testCreateOrderWithNoQuantity() throws OrderException {
        product.setOrderProductRealQuantity(0);
        product2.setOrderProductRealQuantity(0);
        product3.setOrderProductRealQuantity(0);
        Mockito.when(userProxy.findByUserName(user.getUserName())).thenReturn(user);
        Mockito.when(productProxy.updateProductQuantity(orderProduct.getOrderQuantity(), product.getId(), false)).thenReturn(product);
        Mockito.when(productProxy.updateProductQuantity(orderProduct2.getOrderQuantity(), product2.getId(), false)).thenReturn(product2);
        Mockito.when(productProxy.updateProductQuantity(orderProduct3.getOrderQuantity(), product3.getId(), false)).thenReturn(product3);

         orderBusiness.createOrder(orderProducts, user.getUserName(), new Date().getTime());

    }


    @Test(expected = ReceptionException.class)
    public void testCreateOrderWithBadMinReception() throws OrderException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 2);
        Mockito.when(userProxy.findByUserName(user.getUserName())).thenReturn(user);
        Mockito.when(productProxy.updateProductQuantity(orderProduct.getOrderQuantity(), product.getId(), false)).thenReturn(product);
        Mockito.when(productProxy.updateProductQuantity(orderProduct2.getOrderQuantity(), product2.getId(), false)).thenReturn(product2);
        Mockito.when(productProxy.updateProductQuantity(orderProduct3.getOrderQuantity(), product3.getId(), false)).thenReturn(product3);

        orderBusiness.createOrder(orderProducts, user.getUserName(), calendar.getTimeInMillis());
    }

    @Test(expected = ReceptionException.class)
    public void testCreateOrderWithBadMaxReception() throws OrderException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 18);
        Mockito.when(userProxy.findByUserName(user.getUserName())).thenReturn(user);
        Mockito.when(productProxy.updateProductQuantity(orderProduct.getOrderQuantity(), product.getId(), false)).thenReturn(product);
        Mockito.when(productProxy.updateProductQuantity(orderProduct2.getOrderQuantity(), product2.getId(), false)).thenReturn(product2);
        Mockito.when(productProxy.updateProductQuantity(orderProduct3.getOrderQuantity(), product3.getId(), false)).thenReturn(product3);

        orderBusiness.createOrder(orderProducts, user.getUserName(), calendar.getTimeInMillis());
    }

    @Test
    public void testCancelOrder() throws OrderException {
        order = new Order();
        order.setId(1L);
        order.setDate(new Date());
        order.setCancel(false);
        order.setOrderProducts(orderProducts);

        Mockito.when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        orderBusiness.cancelOrder(order.getId());

        ArgumentCaptor<Order> argument = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(argument.capture());

        assertTrue(argument.getValue().getCancel());
    }

    @Test(expected = CancelException.class)
    public void testCancelOrderWithBadDate() throws OrderException {
        order = new Order();
        order.setId(1L);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 7);
        order.setDate(calendar.getTime());
        order.setCancel(false);
        order.setOrderProducts(orderProducts);

        Mockito.when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        orderBusiness.cancelOrder(order.getId());
    }

    @Test(expected = CancelException.class)
    public void testCancelOrderAlreadyCancel() throws OrderException {
        order = new Order();
        order.setId(1L);
        order.setCancel(true);
        order.setOrderProducts(orderProducts);

        Mockito.when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        orderBusiness.cancelOrder(order.getId());
    }

}
