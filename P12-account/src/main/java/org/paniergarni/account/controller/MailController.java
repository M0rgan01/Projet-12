package org.paniergarni.account.controller;

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


    @PutMapping(value = "/userRole/mail/{id}/{email}")
    public ResponseEntity<?> updateMail(@PathVariable Long id, @PathVariable String email) throws AccountException {

        Mail mail = mailBusiness.updateMail(id, email);

        return ResponseEntity.ok().body(mail);
    }

    @GetMapping(value = "/userRole/mailByEmail/{email}")
    public ResponseEntity<?> getMailByEmail(@PathVariable String email) throws AccountException {

        Mail mail = mailBusiness.getMailByEmail(email);

        return ResponseEntity.ok().body(mail);
    }

    @GetMapping(value = "/userRole/mailById/{id}")
    public ResponseEntity<?> getMailById(@PathVariable Long id) throws AccountException {

        Mail mail = mailBusiness.getMailById(id);

        return ResponseEntity.ok().body(mail);
    }

    @PutMapping(value = "/public/sendTokenForRecovery/{email}")
    public ResponseEntity<?> sendTokenForRecovery(@PathVariable String email) throws AccountException, SendMailException {

        Mail mail = mailBusiness.sendTokenForRecovery(email);

        return ResponseEntity.ok().body(mail);
    }

    @PutMapping(value = "/public/validateTokenForRecovery/{email}/{token}")
    public ResponseEntity<?> validateToken(@PathVariable String email, @PathVariable String token) throws AccountException {

        Mail mail = mailBusiness.validateToken(token, email);

        return ResponseEntity.ok().body(mail);
    }

}
