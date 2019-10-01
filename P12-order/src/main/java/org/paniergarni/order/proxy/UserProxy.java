package org.paniergarni.order.proxy;


import org.paniergarni.order.object.User;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "P12-account")
@RibbonClient(name = "P12-account")
public interface UserProxy {

    @GetMapping(value = "/userRole/userByUserName/{userName}")
    User findByUserName(@PathVariable String userName);

    @GetMapping(value = "/adminRole/usersId/{username}")
    List<Long> getUsersByUserNameContains(@PathVariable String username);

}
