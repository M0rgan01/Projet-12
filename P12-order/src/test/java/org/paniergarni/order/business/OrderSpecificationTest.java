package org.paniergarni.order.business;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.paniergarni.order.P12OrderApplication;
import org.paniergarni.order.dao.OrderRepository;
import org.paniergarni.order.dao.specification.OrderSpecification;
import org.paniergarni.order.dao.specification.OrderSpecificationBuilder;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.dao.specification.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = P12OrderApplication.class)
public class OrderSpecificationTest {


    @Autowired
    private OrderRepository orderRepository;

    private Order order1;
    private Order order2;

    @Before
    public void init() {
        order1 = new Order();
        order1.setPaid(true);
        order1.setReference("1234");
        order1.setUserId(1L);
        order1.setDate(new Date());
        order1.setReception(new Date());
        order1.setTotalPrice(1000);

        order2 = new Order();
        order2.setPaid(false);
        order2.setReference("abcd");
        order2.setUserId(2L);
        order2.setDate(new Date());
        order2.setReception(new Date());

        orderRepository.save(order1);
        orderRepository.save(order2);
    }

    @Test
    public void givenLast_whenGettingListOfUsers_thenCorrect() {

        OrderSpecification spec = new OrderSpecification(new SearchCriteria("reference", ":", "12"));
        OrderSpecification spec2 = new OrderSpecification(new SearchCriteria("userId", ":", 1));

        List<Order> results = orderRepository.findAll(Specification.where(spec).and(spec2));

        for (Order order: results ) {
            System.out.println(order.getReference());
        }
    }

    @Test
    public void givenLast_whenGettingListOfUsers_thenCorrect2() {

        OrderSpecification spec = new OrderSpecification(new SearchCriteria("paid", ":", true));

        List<Order> results = orderRepository.findAll(spec);

        for (Order order: results ) {
            System.out.println(order.getReference());
        }
    }

    @Test
    public void givenLast_whenGettingListOfUsers_thenCorrect3() {

        SearchCriteria searchCriteria = new SearchCriteria("paid", ":", true);
        SearchCriteria searchCriteria2 = new SearchCriteria("userId", ":", 1);
        SearchCriteria searchCriteria3 = new SearchCriteria("totalPrice", ">", 900);

        OrderSpecificationBuilder orderSpecificationBuilder = new OrderSpecificationBuilder(Arrays.asList(searchCriteria, searchCriteria2, searchCriteria3));
        Specification<Order> spec = orderSpecificationBuilder.build();
        List<Order> results = orderRepository.findAll(spec);

        for (Order order: results ) {
            System.out.println(order.getReference());
        }
    }
}
