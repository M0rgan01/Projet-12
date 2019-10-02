package org.paniergarni.account.business;

import org.paniergarni.account.dao.MailRepository;
import org.paniergarni.account.entities.Mail;
import org.paniergarni.account.entities.User;
import org.paniergarni.account.exception.*;
import org.paniergarni.account.service.SendMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.validation.constraints.NotNull;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class MailBusinessImpl implements MailBusiness {

    private static final Logger logger = LoggerFactory.getLogger(MailBusinessImpl.class);

    @Autowired
    private MailRepository mailRepository;
    @Autowired
    private SendMail sendMail;
    @Autowired
    private UserBusiness userBusiness;

    @Value("${mail.username}")
    private String emailUsers;
    @Value("${mail.password}")
    private String emailPassword;
    @Value("${mail.object.recovery}")
    private String objectRecovery;
    @Value("${mail.body.recovery}")
    private String bodyRecovery;

    @Value("${mail.expirationToken.inMinutes}")
    private int expirationToken;
    @Value("${mail.expirationRecovery.inMinutes}")
    private int expirationRecovery;


    @Override
    public Mail updateMail(Long id, String email) throws AccountException {
        Mail mail = getMailById(id);
        if (mail.getEmail().equals(email)) {
            throw new AccountException("mail.email.same.value");
        }
        checkEmailExist(email);
        mail.setEmail(email);

        return mailRepository.save(mail);
    }

    @Override
    public Mail getMailById(Long id) throws AccountException {
        return mailRepository.findById(id).orElseThrow(() -> new AccountException("mail.id.incorrect"));
    }

    @Override
    public Mail getMailByEmail(String email) throws AccountException {
        return mailRepository.findByEmail(email).orElseThrow(() -> new AccountException("mail.email.incorrect"));
    }

    @Override
    public void checkEmailExist(String email) throws AccountException {
        if (mailRepository.findByEmail(email).isPresent())
            throw new AccountException("mail.email.already.exist");
    }

    @Override
    public Mail sendTokenForRecovery(String email) throws AccountException, SendMailException {

        User user = userBusiness.getUserByEmail(email);

        if (!user.isActive())
            throw new UserNotActiveException("user.not.active");

        // generation du token
        String token = generateToken();
        // assignation au mail
        user.getMail().setToken(token);
        user.getMail().setTryToken(0);
        user.getMail().setAvailablePasswordRecovery(false);
        user.getMail().setExpiryPasswordRecovery(null);
        // creation d'une date d'expiration pour le token
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expirationToken);
        user.getMail().setExpiryToken(cal.getTime());

        String body = MessageFormat.format(bodyRecovery, user.getMail().getToken());

        String[] tableau_email = {user.getMail().getEmail()};
        try {
            sendMail.sendFromGMail(emailUsers, emailPassword, tableau_email, objectRecovery, body);
        } catch (Exception e) {
            throw new SendMailException("internal.error");
        }

        logger.info("Send token to the email " + user.getMail().getEmail());
        logger.info("Update mail " + user.getMail().getId());
        return mailRepository.save(user.getMail());
    }

    @Override
    public Mail validateToken(String token, String email) throws AccountException {

        Mail mail = getMailByEmail(email);

        // si les jetons correspondes et si le nombre
        // d'essais
        // et plus petit que 3
        if (token.equals(mail.getToken()) && mail.getTryToken() < 3) {
            // on vérifie la date
            if (!new Date().before(mail.getExpiryToken())) {
                logger.error("Token for email " + mail.getId() + " expiry");
                throw new ExpirationException("mail.token.expiry");
            }
            mail.setAvailablePasswordRecovery(true);
            // creation d'une date d'expiration pour le token
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, expirationRecovery);
            mail.setExpiryPasswordRecovery(cal.getTime());
            return mailRepository.save(mail);
            // sinon on incrémente le nombre d'essais
        } else {
            // si le nombre d'essais est supérieur ou égal à 2
            if (mail.getTryToken() >= 3) {
                logger.error("Number of tests for token exceeded for mail " + mail.getId());
                throw new RecoveryException("mail.token.try.out");
            }
            mail.setTryToken(mail.getTryToken() + 1);

            mailRepository.save(mail);
            if (mail.getTryToken() == 3) {
                logger.error("Number of tests for token exceeded for mail " + mail.getId());
                throw new RecoveryException("mail.token.try.out");
            }
            logger.info("Increment tryToken for Mail " + mail.getId() + " and update");
            throw new RecoveryException("mail.token.not.correct");
        }
    }


    /**
     * génération d'un token
     *
     * @return token
     */
    public String generateToken() {
        SecureRandom random = new SecureRandom();
        int longToken = Math.abs(random.nextInt());
        String randomString = Integer.toString(longToken, 16);
        logger.info("Generate token for password recovery");
        return randomString;
    }
}
