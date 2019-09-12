package org.paniergarni.account.business;

import org.paniergarni.account.entities.Mail;

public interface MailBusiness {

    Mail createMail(Mail mail);
    Mail updateMail(Mail mail);
    Mail getMailById(Long id);
    Mail getMailByEmail(String email);
    void checkEmailExist(String email);
    void sendTokenForRecovery(String email);
    void validateToken(String token, String email);
}
