package org.paniergarni.account.controller;

import org.paniergarni.account.business.UserBusiness;
import org.paniergarni.account.entities.User;
import org.paniergarni.account.exception.AccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserBusiness userBusiness;


    @PostMapping(value = "/user")
    public ResponseEntity<?> createUser(@RequestBody @Valid User user) throws AccountException {

        user = userBusiness.createUser(user);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping(value = "/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user) throws AccountException {

        user = userBusiness.updateUser(user);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/userByUserName/{userName}")
    public ResponseEntity<?> getUserByUserName(@PathVariable String userName) throws AccountException {

        User user = userBusiness.getUserByUserName(userName);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/userByEmail/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) throws AccountException {

        User user = userBusiness.getUserByEmail(email);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/userById/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) throws AccountException {

        User user = userBusiness.getUserById(id);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/userConnection/{username}/{password}")
    public ResponseEntity<?> getUserById(@PathVariable String username, @PathVariable String password) throws AccountException {

        User user = userBusiness.doConnection(username, password);

        return ResponseEntity.ok().body(user);
    }
}
