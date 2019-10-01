package org.paniergarni.account.controller;

import org.paniergarni.account.business.UserBusiness;
import org.paniergarni.account.entities.User;
import org.paniergarni.account.entities.dto.CreateUserDTO;
import org.paniergarni.account.entities.dto.UserRecoveryDTO;
import org.paniergarni.account.entities.dto.UserUpdatePassWordDTO;
import org.paniergarni.account.exception.AccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserBusiness userBusiness;


    @PostMapping(value = "/adminRole/user")
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserDTO createUserDTO) throws AccountException {

        User user = userBusiness.createUser(createUserDTO);

        return ResponseEntity.ok().body(user);
    }

    @PutMapping(value = "/adminRole/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) throws AccountException {

        user = userBusiness.updateUser(id, user);

        return ResponseEntity.ok().body(user);
    }

    @PutMapping(value = "/userRole/updateUserName/{id}/{userName}")
    public ResponseEntity<?> updateUser(@PathVariable String userName, @PathVariable Long id) throws AccountException {

        User user = userBusiness.updateUserName(id, userName);

        return ResponseEntity.ok().body(user);
    }

    @PutMapping(value = "/userRole/updatePassWord/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdatePassWordDTO user) throws AccountException {

        userBusiness.updatePassWord(id, user);

        return ResponseEntity.ok().body(null);
    }

    @GetMapping(value = "/userRole/userByUserName/{userName}")
    public ResponseEntity<?> getUserByUserName(@PathVariable String userName) throws AccountException {

        User user = userBusiness.getUserByUserName(userName);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/userRole/userByEmail/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) throws AccountException {

        User user = userBusiness.getUserByEmail(email);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/userRole/userById/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) throws AccountException {

        User user = userBusiness.getUserById(id);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping(value = "/adminRole/userConnection/{username}/{password}")
    public ResponseEntity<?> userConnection(@PathVariable String username, @PathVariable String password) throws AccountException {

        User user = userBusiness.doConnection(username, password);

        return ResponseEntity.ok().body(user);
    }


    @PutMapping(value = "/public/editPassWordByRecovery/{email}")
    public ResponseEntity<?> editPassWordByRecovery(@PathVariable String email,@Valid @RequestBody UserRecoveryDTO userRecoveryDTO) throws AccountException {

        userBusiness.editPassWordByRecovery(email, userRecoveryDTO);

        return ResponseEntity.ok().body(null);
    }
}
