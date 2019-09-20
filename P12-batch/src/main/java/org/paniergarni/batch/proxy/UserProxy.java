package org.paniergarni.batch.proxy;

import org.paniergarni.batch.object.User;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "P12-account")
@RibbonClient(name = "P12-account")
public interface UserProxy {

    @GetMapping(value = "/userById/{id}")
    User findByUserName(@PathVariable Long id);
}
