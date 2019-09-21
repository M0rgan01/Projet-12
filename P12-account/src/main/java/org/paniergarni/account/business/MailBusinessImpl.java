package org.paniergarni.account.business;

import org.paniergarni.account.dao.MailRepository;
import org.paniergarni.account.entities.Mail;
import org.paniergarni.account.entities.User;
import org.paniergarni.account.exception.AccountException;
import org.paniergarni.account.exception.BadCredencialException;
import org.paniergarni.account.exception.ExpirationException;
import org.paniergarni.account.service.SendMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    @Value("${mail.expirationToken}")
    private int expirationToken;

    @Override
    public Mail createMail(Mail mail) throws AccountException {
        checkEmailExist(mail.getEmail());
        return mailRepository.save(mail);
    }

    @Override
    public Mail updateMail(Mail mail) {
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
    public void sendTokenForRecovery(String email) throws AccountException {

        User user = userBusiness.getUserByEmail(email);

        if (!user.isActive()) {
            throw new AccountException("user.not.active");
        }

        // generation du token
        String token = generateToken();
        // assignation au mail
        user.getMail().setToken(token);
        user.getMail().setTryToken(0);
        // creation d'une date d'expiration
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expirationToken);
        user.getMail().setExpiryToken(cal.getTime());

        String body = MessageFormat.format(bodyRecovery,  user.getMail().getToken());

        String[] tableau_email = {  user.getMail().getEmail() };

        sendMail.sendFromGMail(emailUsers, emailPassword, tableau_email, objectRecovery, body);
        logger.info("Send token to the email " +  user.getMail().getEmail());
        mailRepository.save( user.getMail());
        logger.info("Update mail " +  user.getMail().getId());
    }

    @Override
    public void validateToken(String token, String email) throws AccountException {

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
            // sinon on incrémente le nombre d'essais
        } else {

            // si le nombre d'essais est supérieur ou égal à 2
            if (mail.getTryToken() >= 2) {
                logger.error("Number of tests for token exceeded for mail " + mail.getId());
                throw new AccountException("mail.token.try.out");
            }

            mail.setTryToken(mail.getTryToken() + 1);
            mailRepository.save(mail);
            logger.info("Increment tryToken for Mail " + mail.getId() + " and update");
            throw new BadCredencialException("mail.token.not.correct");
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
