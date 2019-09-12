package org.paniergarni.account.controller;

import org.paniergarni.account.business.UserBusiness;
import org.paniergarni.account.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserBusiness userBusiness;


    @PostMapping(value = "/user")
    public ResponseEntity<?> createUser(@RequestBody @Valid User user) {

        user = userBusiness.createUser(user);

        return ResponseEntity.ok().body(user);
    }

    @PutMapping(value = "/user")
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user) {

        user = userBusiness.updateUser(user);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/userByUserName/{userName}")
    public ResponseEntity<?> getUserByUserName(@PathVariable String userName) {

        User user = userBusiness.getUserByUserName(userName);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/userByEmail/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {

        User user = userBusiness.getUserByEmail(email);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/userById/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        User user = userBusiness.getUserById(id);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/userConnection/{username}/{password}")
    public ResponseEntity<?> getUserById(@PathVariable String username, @PathVariable String password) {

        User user = userBusiness.doConnection(username, password);

        return ResponseEntity.ok().body(user);
    }
}
