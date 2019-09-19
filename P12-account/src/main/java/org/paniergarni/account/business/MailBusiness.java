package org.paniergarni.account.business;

import org.paniergarni.account.entities.Mail;
import org.paniergarni.account.exception.AccountException;

public interface MailBusiness {

    Mail createMail(Mail mail) throws AccountException;
    Mail updateMail(Mail mail);
    Mail getMailById(Long id) throws AccountException;
    Mail getMailByEmail(String email) throws AccountException;
    void checkEmailExist(String email) throws AccountException;
    void sendTokenForRecovery(String email) throws AccountException ;
    void validateToken(String token, String email)  throws AccountException;
}
