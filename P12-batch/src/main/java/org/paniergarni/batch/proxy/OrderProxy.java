package org.paniergarni.batch.proxy;

import org.paniergarni.batch.object.Order;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "P12-order")
@RibbonClient(name = "P12-order")
public interface OrderProxy {

    @GetMapping(value = "/receptionOrders")
    List<Order> getListReceptionOrder();

    @GetMapping(value = "/lateOrders")
    List<Order> getListLateOrder();
}
