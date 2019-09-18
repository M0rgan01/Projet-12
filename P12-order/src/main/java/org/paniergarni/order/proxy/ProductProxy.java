package org.paniergarni.order.proxy;

import org.paniergarni.order.object.Product;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "P12-stock")
@RibbonClient(name = "P12-stock")
public interface ProductProxy {

    @GetMapping(value = "/product/{id}")
    Product findById(@PathVariable Long id);

    @PutMapping(value = "/productQuantity/{quantity}/{id}")
    Product updateProductQuantity(@PathVariable int quantity, @PathVariable Long id);
}
