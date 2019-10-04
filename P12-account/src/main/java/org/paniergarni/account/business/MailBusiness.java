package org.paniergarni.account.business;

import org.paniergarni.account.entities.Mail;
import org.paniergarni.account.exception.AccountException;
import org.paniergarni.account.exception.SendMailException;

public interface MailBusiness {

    Mail updateMail(Long id, String email) throws AccountException;
    Mail getMailById(Long id) throws AccountException;
    Mail getMailByEmail(String email) throws AccountException;
    void checkEmailExist(String email) throws AccountException;
    Mail sendTokenForRecovery(String email) throws AccountException, SendMailException;
    Mail validateToken(String token, String email)  throws AccountException;
}
