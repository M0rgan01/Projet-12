package org.paniergarni.account.business;

import org.paniergarni.account.dao.MailRepository;
import org.paniergarni.account.entities.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailBusinessImpl implements  MailBusiness {

    @Autowired
    private MailRepository mailRepository;

    @Override
    public Mail createMail(Mail mail) {

        mailRepository.findByEmail(mail.getEmail()).ifPresent(mail1 -> {
            throw new IllegalArgumentException("Mail name " + mail1.getEmail() + " already exist");
        });

        return mailRepository.save(mail);
    }

    @Override
    public Mail updateMail(Mail mail) {
        return mailRepository.save(mail);
    }

    @Override
    public Mail getMailById(Long id) {
        return mailRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID " + id + " Incorrect"));
    }

    @Override
    public Mail getMailByEmail(String email) {

        return mailRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Email " + email + " Incorrect"));
    }
}
