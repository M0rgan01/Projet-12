package org.paniergarni.account.controller;

import org.paniergarni.account.business.MailBusiness;
import org.paniergarni.account.entities.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class MailController {

    @Autowired
    private MailBusiness mailBusiness;

    @PatchMapping(value = "/mail")
    public ResponseEntity<?> updateUser(@RequestBody @Valid Mail mail){

        mail = mailBusiness.updateMail(mail);

        return ResponseEntity.ok().body(mail);
    }

    @GetMapping(value = "/mailByEmail/{email}")
    public ResponseEntity<?> getMailByEmail(@PathVariable String email){

        Mail mail = mailBusiness.getMailByEmail(email);

        return ResponseEntity.ok().body(mail);
    }

    @GetMapping(value = "/mailById/{id}")
    public ResponseEntity<?> getMailById(@PathVariable Long id){

        Mail mail = mailBusiness.getMailById(id);

        return ResponseEntity.ok().body(mail);
    }

}
