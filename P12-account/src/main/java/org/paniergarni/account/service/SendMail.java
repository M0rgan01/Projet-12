package org.paniergarni.account.service;

import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Objet servant à l'envoie d'email
 *
 * @author PICHAT morgan
 */
@Service
public class SendMail {
    //pour l'envoie de mail java via gmail --> Accès moins sécurisé des applications --> https://myaccount.google.com/lesssecureapps

    /**
     * Envoie un email
     *
     * @param from    adresse qui envoyer le mail
     * @param pass    mot de passe de l'adresse qui envoyer le mail
     * @param to      tableau contenant les email de destination
     * @param subject Object du mail
     * @param body    contenu du mail
     */
    public void sendFromGMail(String from, String pass, String[] to, String subject, String body) throws MessagingException, AddressException {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(from));
        InternetAddress[] toAddress = new InternetAddress[to.length];

        // To get the array of addresses
        for (int i = 0; i < to.length; i++) {
            toAddress[i] = new InternetAddress(to[i]);
        }

        for (int i = 0; i < toAddress.length; i++) {
            message.addRecipient(Message.RecipientType.TO, toAddress[i]);
        }

        //attribution du sujet
        message.setSubject(subject);
        //attribution du corp de l'email
        message.setText(body);
        Transport transport = session.getTransport("smtp");
        //connexion
        transport.connect(host, from, pass);
        //envoi du mail
        transport.sendMessage(message, message.getAllRecipients());

        transport.close();

    }
}
