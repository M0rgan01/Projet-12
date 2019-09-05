package org.paniergarni.account.controller;

import org.paniergarni.account.dao.UserRepository;
import org.paniergarni.account.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

   @Autowired
    private UserRepository userRepository;



   @PostMapping(value = "/User")
    public User createUser(@RequestBody @Valid User user){
      return userRepository.save(user);
   }

}
