package org.paniergarni.account.business;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.paniergarni.account.dao.MailRepository;
import org.paniergarni.account.entities.Mail;
import org.paniergarni.account.entities.User;
import org.paniergarni.account.exception.AccountException;
import org.paniergarni.account.service.SendMail;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class MailBusinessTest {

    @Mock
    private UserBusinessImpl userBusiness;
    @InjectMocks
    private MailBusinessImpl mailBusiness;
    @Mock
    private MailRepository mailRepository;
    @Mock
    private SendMail sendMail;
    private User user;
    private Mail mail;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(mailBusiness, "emailUsers", "");
        ReflectionTestUtils.setField(mailBusiness, "emailPassword", "");
        ReflectionTestUtils.setField(mailBusiness, "objectRecovery", "");
        ReflectionTestUtils.setField(mailBusiness, "bodyRecovery", "");
        ReflectionTestUtils.setField(mailBusiness, "expirationToken", 30);

        user = new User();
        user.setActive(true);
        mail = new Mail();
        mail.setEmail("Test@account.fr");
        user.setMail(mail);
    }

    @Test
    public void testSendTokenForRecovery() throws AccountException {
        mail.setTryToken(3);

        Mockito.when(userBusiness.getUserByEmail(mail.getEmail())).thenReturn(user);
        Mockito.when(mailRepository.save(mail)).thenReturn(mail);

        mailBusiness.sendTokenForRecovery(mail.getEmail());

        ArgumentCaptor<Mail> argument = ArgumentCaptor.forClass(Mail.class);
        verify(mailRepository).save(argument.capture());

        assertEquals(argument.getValue().getTryToken(), 0);
        assertFalse(argument.getValue().getToken().isEmpty());
        assertNotNull(argument.getValue().getExpiryToken());
    }

    @Test(expected = AccountException.class)
    public void testSendTokenForRecoveryWithBadEmail() throws AccountException {
        Mockito.when(userBusiness.getUserByEmail(mail.getEmail())).thenThrow(new AccountException(""));

        mailBusiness.sendTokenForRecovery(mail.getEmail());
    }

    @Test(expected = AccountException.class)
    public void testSendTokenForRecoveryWithUserNotActive() throws AccountException {
        user.setActive(false);
        Mockito.when(userBusiness.getUserByEmail(mail.getEmail())).thenReturn(user);

        mailBusiness.sendTokenForRecovery(mail.getEmail());
    }

    @Test
    public void testValidateToken() throws AccountException {
        mail.setToken("token");
        mail.setTryToken(0);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 1);
        mail.setExpiryToken(c.getTime());

        Mockito.when(mailRepository.findByEmail(mail.getEmail())).thenReturn(Optional.of(mail));
        //si la méthode ne lève aucune erreur c'est bon
        mailBusiness.validateToken(mail.getToken(), mail.getEmail());
    }

    @Test(expected = AccountException.class)
    public void testValidateTokenWithTokenExpiry() throws AccountException {
        mail.setToken("token");
        mail.setTryToken(0);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, -1);
        mail.setExpiryToken(c.getTime());

        Mockito.when(mailRepository.findByEmail(mail.getEmail())).thenReturn(Optional.of(mail));
        //si la méthode ne lève aucune erreur c'est bon
        mailBusiness.validateToken(mail.getToken(), mail.getEmail());
    }

    @Test(expected = AccountException.class)
    public void testValidateTokenWithBadTryToken() throws AccountException {
        mail.setToken("token");
        mail.setTryToken(3);

        Mockito.when(mailRepository.findByEmail(mail.getEmail())).thenReturn(Optional.of(mail));
        //si la méthode ne lève aucune erreur c'est bon
        mailBusiness.validateToken(mail.getToken(), mail.getEmail());
    }

    @Test
    public void testValidateTokenWithBadToken() throws AccountException {
        mail.setToken("token");
        mail.setTryToken(0);

        Mockito.when(mailRepository.findByEmail(mail.getEmail())).thenReturn(Optional.of(mail));
        Mockito.when(mailRepository.save(mail)).thenReturn(mail);
        //si la méthode ne lève aucune erreur c'est bon
        try {
            mailBusiness.validateToken("Bla", mail.getEmail());
        } catch (AccountException e) {
            ArgumentCaptor<Mail> argument = ArgumentCaptor.forClass(Mail.class);
            verify(mailRepository).save(argument.capture());
            assertEquals(argument.getValue().getTryToken(), 1);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateTokenWithNullToken() throws AccountException {
        Mockito.when(mailRepository.findByEmail(mail.getEmail())).thenReturn(Optional.of(mail));
            mailBusiness.validateToken(mail.getToken(), mail.getEmail());
    }
}
