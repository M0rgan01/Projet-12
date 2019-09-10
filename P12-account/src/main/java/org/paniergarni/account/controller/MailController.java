package org.paniergarni.account.controller;

import org.paniergarni.account.business.MailBusiness;
import org.paniergarni.account.entities.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class MailController {

    @Autowired
    private MailBusiness mailBusiness;

    @PatchMapping(value = "/mail")
    public ResponseEntity<?> updateUser(@RequestBody @Valid Mail mail){
        try {
            mail = mailBusiness.updateMail(mail);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok().body(mail);
    }

    @GetMapping(value = "/mailByEmail/{email}")
    public ResponseEntity<?> getMailByEmail(@PathVariable String email){

        Mail mail;

        try {
            mail = mailBusiness.getMailByEmail(email);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok().body(mail);
    }

    @GetMapping(value = "/mailById/{id}")
    public ResponseEntity<?> getMailById(@PathVariable Long id){

        Mail mail;

        try {
            mail = mailBusiness.getMailById(id);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok().body(mail);
    }

}
