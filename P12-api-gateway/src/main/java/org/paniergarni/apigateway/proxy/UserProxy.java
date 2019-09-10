package org.paniergarni.apigateway.proxy;

import org.paniergarni.apigateway.object.User;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "P12-account")
@RibbonClient(name = "P12-account")
public interface UserProxy {

    @GetMapping(value = "/userByUserName/{userName}")
    User findByUserName(@PathVariable String userName);

    @GetMapping(value = "/userByEmail/{email}")
    User findByEmail(@PathVariable String email);

    @PostMapping(value = "/user")
    User createUser(@RequestBody User user);
}
