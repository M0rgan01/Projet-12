package org.paniergarni.account.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.paniergarni.account.business.MailBusiness;
import org.paniergarni.account.entities.Mail;
import org.paniergarni.account.exception.AccountException;
import org.paniergarni.account.exception.SendMailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class MailController {

    @Autowired
    private MailBusiness mailBusiness;

    @ApiOperation(value = "Mise à jour de l'adresse email d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la mise à jour"),
            @ApiResponse(code = 400, message = "ID mail incorrect, email inchangé, email similaire déjà présent"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PutMapping(value = "/userRole/mail/{id}/{email}")
    public ResponseEntity<?> updateMail(@PathVariable Long id, @PathVariable String email) throws AccountException {

        Mail mail = mailBusiness.updateMail(id, email);

        return ResponseEntity.ok().body(mail);
    }

    @ApiOperation(value = "Récupère un Mail selon son email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 400, message = "Aucune correspondance"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/userRole/mailByEmail/{email}")
    public ResponseEntity<?> getMailByEmail(@PathVariable String email) throws AccountException {

        Mail mail = mailBusiness.getMailByEmail(email);

        return ResponseEntity.ok().body(mail);
    }

    @ApiOperation(value = "Récupère un Mail selon son ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 400, message = "Aucune correspondance"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/userRole/mailById/{id}")
    public ResponseEntity<?> getMailById(@PathVariable Long id) throws AccountException {

        Mail mail = mailBusiness.getMailById(id);

        return ResponseEntity.ok().body(mail);
    }

    @ApiOperation(value = "Envoie un token sur l'email renseigné pour récupération du compte")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de l'envoi"),
            @ApiResponse(code = 400, message = "Aucune correspondance, utilisateur désactivé"),
            @ApiResponse(code = 500, message = "Erreur interne"),
            @ApiResponse(code = 503, message = "Service d'envoi d'email indisponnible")
    })
    @PutMapping(value = "/public/sendTokenForRecovery/{email}")
    public ResponseEntity<?> sendTokenForRecovery(@PathVariable String email) throws AccountException, SendMailException {

        Mail mail = mailBusiness.sendTokenForRecovery(email);

        return ResponseEntity.ok().body(mail);
    }

    @ApiOperation(value = "Vérification du token renseigné pour récupération du compte")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la vérification"),
            @ApiResponse(code = 400, message = "Aucune correspondance, token expiré, token incorrect, essais > 3"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PutMapping(value = "/public/validateTokenForRecovery/{email}/{token}")
    public ResponseEntity<?> validateToken(@PathVariable String email, @PathVariable String token) throws AccountException {

        Mail mail = mailBusiness.validateToken(token, email);

        return ResponseEntity.ok().body(mail);
    }

}
