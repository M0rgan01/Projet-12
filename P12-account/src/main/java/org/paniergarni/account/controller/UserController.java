package org.paniergarni.account.controller;

import org.paniergarni.account.business.UserBusiness;
import org.paniergarni.account.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

   @Autowired
    private UserBusiness userBusiness;


   @PostMapping(value = "/user")
    public ResponseEntity<?> createUser(@RequestBody @Valid User user){

       try {
           user = userBusiness.createUser(user);
       }catch (IllegalArgumentException e){
           return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
       }
       return ResponseEntity.ok().body(user);
   }

    @PatchMapping(value = "/user")
    public ResponseEntity<?> updateUser(@RequestBody @Valid User user){
        try {
            user = userBusiness.updateUser(user);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok().body(user);
    }

   @GetMapping(value = "/userByUserName/{userName}")
   public ResponseEntity<?> getUserByUserName(@PathVariable String userName){

       User user;

       try {
           user = userBusiness.getUserByUserName(userName);
       }catch (IllegalArgumentException e){
           return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
       }
       return ResponseEntity.ok().body(user);
   }

    @GetMapping(value = "/userByIs/{id}")
    public ResponseEntity<?> getUserByUserName(@PathVariable Long id){

        User user;

        try {
            user = userBusiness.getUserById(id);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok().body(user);
    }


}
