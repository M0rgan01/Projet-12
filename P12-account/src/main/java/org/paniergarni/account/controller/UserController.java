package org.paniergarni.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api( description="API de gestion des utilisateurs")
@RestController
public class UserController {

    @Autowired
    private UserBusiness userBusiness;

    @ApiOperation(value = "Création d'un utilisateur, ne retourne aucune authentification")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la création"),
            @ApiResponse(code = 400, message = "Entitée non conforme"),
            @ApiResponse(code = 409, message = "Username ou email similaire déjà présent, passWordConfirm non correspondant"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PostMapping(value = "/adminRole/user")
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserDTO createUserDTO) throws AccountException {

        User user = userBusiness.createUser(createUserDTO);

        return ResponseEntity.ok().body(user);
    }

    @ApiOperation(value = "Modification de la variable 'active' d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la modification"),
            @ApiResponse(code = 409, message = "Aucune corresponce pour l'ID, variable de même valeur"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PutMapping(value = "/adminRole/updateUser/{id}/{active}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @PathVariable boolean active) throws AccountException {

        User user = userBusiness.updateUserActive(id, active);

        return ResponseEntity.ok().body(user);
    }

    @ApiOperation(value = "Modification du username d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la modification"),
            @ApiResponse(code = 409, message = "Aucune corresponce pour l'ID, utilisateur désactivé, Username null, vide, inchangé, ou déjà présent"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PutMapping(value = "/userRole/updateUserName/{id}/{userName}")
    public ResponseEntity<?> updateUserName(@PathVariable String userName, @PathVariable Long id) throws AccountException {

        User user = userBusiness.updateUserName(id, userName);

        return ResponseEntity.ok().body(user);
    }

    @ApiOperation(value = "Modification du mot de passe d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la modification"),
            @ApiResponse(code = 400, message = "Entitée non conforme"),
            @ApiResponse(code = 409, message = "Aucune corresponce pour l'ID, utilisateur désactivé, confirmation ou ancien mot de passe non correspondant"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PutMapping(value = "/userRole/updatePassWord/{id}")
    public ResponseEntity<?> updateUserPassWord(@PathVariable Long id, @RequestBody @Valid UserUpdatePassWordDTO user) throws AccountException {

        userBusiness.updatePassWord(id, user);

        return ResponseEntity.ok().body(null);
    }

    @ApiOperation(value = "Modification du mot de passe d'un utilisateur par récupération")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la modification"),
            @ApiResponse(code = 400, message = "Entitée non conforme"),
            @ApiResponse(code = 409, message = "Aucune corresponce pour l'email, compte non valable pour modification par récupération, " +
                    "expiration de la modification, confirmation ou ancien mot de passe non correspondant"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PutMapping(value = "/public/editPassWordByRecovery/{email}")
    public ResponseEntity<?> editPassWordByRecovery(@PathVariable String email,@Valid @RequestBody UserRecoveryDTO userRecoveryDTO) throws AccountException {

        userBusiness.editPassWordByRecovery(email, userRecoveryDTO);

        return ResponseEntity.ok().body(null);
    }

    @ApiOperation(value = "Modification des roles d'un utilisateur pour avoir un utilisateur avec le role USER")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la modification"),
            @ApiResponse(code = 409, message = "Aucune corresponce pour l'ID, role non trouvé"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PutMapping(value = "/adminRole/userRole/{id}")
    public ResponseEntity<?> editUserRole(@PathVariable Long id) throws AccountException {

        User user = userBusiness.setUserRole(id);

        return ResponseEntity.ok().body(user);
    }

    @ApiOperation(value = "Modification des roles d'un utilisateur pour avoir un utilisateur avec le role ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la modification"),
            @ApiResponse(code = 409, message = "Aucune corresponce pour l'ID, roles non trouvé"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PutMapping(value = "/adminRole/adminRole/{id}")
    public ResponseEntity<?> editAdminRole(@PathVariable Long id) throws AccountException {

        User user = userBusiness.setAdminRole(id);

        return ResponseEntity.ok().body(user);
    }

    @ApiOperation(value = "Récupération d'un utilisateur par son userName")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 409, message = "Aucune corresponce pour le userName"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/userRole/userByUserName/{userName}")
    public ResponseEntity<?> getUserByUserName(@PathVariable String userName) throws AccountException {

        User user = userBusiness.getUserByUserName(userName);

        return ResponseEntity.ok().body(user);
    }

    @ApiOperation(value = "Récupération d'un utilisateur par son email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 409, message = "Aucune corresponce pour l'email"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/userRole/userByEmail/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) throws AccountException {

        User user = userBusiness.getUserByEmail(email);

        return ResponseEntity.ok().body(user);
    }

    @ApiOperation(value = "Récupération d'un utilisateur par son ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 409, message = "Aucune corresponce pour l'ID"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/userRole/userById/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) throws AccountException {

        User user = userBusiness.getUserById(id);

        return ResponseEntity.ok().body(user);
    }

    @ApiOperation(value = "Connection d'un utilisateur, ne retourne aucune authentification")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la connection"),
            @ApiResponse(code = 409, message = "Aucune correspondance pour le userName ou email, utilisateur désactivé,  " +
                    "passWord incorrect, nombre d'éssais > 3, attente d'expiration pour nombre d'essais > 3"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/adminRole/userConnection/{username}/{password}")
    public ResponseEntity<?> userConnection(@PathVariable String username, @PathVariable String password) throws AccountException {

        User user = userBusiness.doConnection(username, password);

        return ResponseEntity.ok().body(user);
    }


}
