package org.paniergarni.account.business;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.paniergarni.account.dao.UserRepository;
import org.paniergarni.account.entities.Mail;
import org.paniergarni.account.entities.Role;
import org.paniergarni.account.entities.User;
import org.paniergarni.account.exception.AccountException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserBusinessTest {

    @InjectMocks
    private UserBusinessImpl userBusiness;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MailBusiness mailBusiness;
    @Mock
    private RoleBusiness roleBusiness;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private User user;
    private Mail mail;
    private Role role;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(userBusiness, "tryIncorrectLogin", 3);
        ReflectionTestUtils.setField(userBusiness, "waitingMinutes", 30);

        user = new User();
        user.setUserName("Test");
        user.setPassWord("Test1234");
        user.setPassWordConfirm("Test1234");
        mail = new Mail();
        mail.setEmail("Test@account.fr");
        user.setMail(mail);
        role = new Role();
        role.setName("ROLE");
    }


    @Test
    public void testCreateUser() throws AccountException {

        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.ofNullable(null));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(roleBusiness.getUserRole()).thenReturn(role);
        Mockito.when(bCryptPasswordEncoder.encode(user.getPassWord())).thenReturn("passwordEncoded1");
        user = userBusiness.createUser(user);

        assertTrue(user.isActive());

        assertEquals(user.getPassWord(), "passwordEncoded1");

        for (Role role: user.getRoles()) {
            assertEquals(role.getName(), "ROLE");
        }
    }

    @Test(expected = AccountException.class)
    public void testCreateUserWithAlreadyUserNameExist() throws AccountException {
        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(roleBusiness.getUserRole()).thenReturn(role);
        Mockito.when(bCryptPasswordEncoder.encode(user.getPassWord())).thenReturn("passwordEncoded1");
        user = userBusiness.createUser(user);
    }

    @Test
    public void testUpdateUser() throws AccountException {
        user.setId(1l);

        User user1 = new User();
        user1.setUserName(user.getUserName());
        user1.setPassWord(user.getPassWord());
        user1.setPassWordConfirm(user.getPassWordConfirm());
        user1.setActive(true);

        user.setUserName("Pseudo2");
        user.setPassWord("PassWord1");
        user.setPassWordConfirm("PassWord1");

        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.ofNullable(null));
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user1));
        Mockito.when(bCryptPasswordEncoder.matches(user.getPassWord(), "Test1")).thenReturn(false);
        Mockito.when(userRepository.save(user)).thenReturn(user);

        user = userBusiness.updateUser(user);

        assertEquals(user.getPassWord(), "PassWord1");
        assertEquals(user.getUserName(), "Pseudo2");

    }

    @Test(expected = AccountException.class)
    public void testUpdateUserNoActive() throws AccountException {
        user.setId(1l);

        User user1 = new User();
        user1.setActive(false);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user1));

        userBusiness.updateUser(user);
    }

    @Test(expected = AccountException.class)
    public void testUpdateUserWithAlreadyUserNameExist() throws AccountException {
        user.setId(1l);

        User user1 = new User();
        user1.setUserName(user.getUserName());

        user1.setActive(true);

        user.setUserName("Pseudo2");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user1));

        userBusiness.updateUser(user);
    }

    @Test(expected = AccountException.class)
    public void testUpdateUserWithNotSamePassWordConfirm() throws AccountException {
        user.setId(1l);

        User user1 = new User();
        user1.setUserName(user.getUserName());
        user1.setPassWord(user.getPassWord());
        user1.setPassWordConfirm(user.getPassWordConfirm());
        user1.setMail(mail);
        user1.setActive(true);

        user.setPassWord("PassWord1");
        user.setPassWordConfirm("PassWord");
        user.setOldPassWord(user1.getPassWord());

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user1));
        Mockito.when(bCryptPasswordEncoder.matches(user.getOldPassWord(), user1.getPassWord())).thenReturn(true);

        userBusiness.updateUser(user);
    }

    @Test
    public void testDoConnection() throws AccountException {
        user.setActive(true);
        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        Mockito.when(bCryptPasswordEncoder.matches(user.getPassWord(), user.getPassWord())).thenReturn(true);

        userBusiness.doConnection(user.getUserName(), user.getPassWord());
    }

    @Test(expected = AccountException.class)
    public void testDoConnectionWithUserNoActive() throws AccountException {
        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        userBusiness.doConnection(user.getUserName(), user.getPassWord());
    }

    @Test(expected = AccountException.class)
    public void testDoConnectionWithBadPassWorld() throws AccountException {
        user.setActive(true);
        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        Mockito.when(bCryptPasswordEncoder.matches(user.getPassWord(), user.getPassWord())).thenReturn(false);
        user = userBusiness.doConnection(user.getUserName(), user.getPassWord());

        assertEquals(user.getTryConnection(), 1);
    }

    @Test(expected = AccountException.class)
    public void testDoConnectionWithBadPassWorldAndMaxTryConnection() throws AccountException {
        user.setActive(true);
        user.setTryConnection(2);
        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        Mockito.when(bCryptPasswordEncoder.matches(user.getPassWord(), user.getPassWord())).thenReturn(false);
        user = userBusiness.doConnection(user.getUserName(), user.getPassWord());

        assertEquals(user.getTryConnection(), 3);
        assertNotNull(user.getExpiryConnection());
    }

    @Test(expected = AccountException.class)
    public void testDoConnectionWithExpiryConnection() throws AccountException {
        user.setActive(true);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 10);
        user.setExpiryConnection(c.getTime());
        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        Mockito.when(bCryptPasswordEncoder.matches(user.getPassWord(), user.getPassWord())).thenReturn(false);
        user = userBusiness.doConnection(user.getUserName(), user.getPassWord());

    }

    @Test
    public void testDoConnectionWithExpiryConnectionExpired() throws AccountException {
        user.setActive(true);
        user.setExpiryConnection(new Date());
        user.setTryConnection(3);
        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        Mockito.when(bCryptPasswordEncoder.matches(user.getPassWord(), user.getPassWord())).thenReturn(true);
        user = userBusiness.doConnection(user.getUserName(), user.getPassWord());

        assertEquals(user.getTryConnection(), 0);
        assertNull(user.getExpiryConnection());
    }

    @Test(expected = AccountException.class)
    public void testDoConnectionWithExpiryConnectionExpiredAndBadPassWord() throws AccountException {
        user.setActive(true);
        user.setExpiryConnection(new Date());
        user.setTryConnection(3);
        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        Mockito.when(bCryptPasswordEncoder.matches(user.getPassWord(), user.getPassWord())).thenReturn(false);
        user = userBusiness.doConnection(user.getUserName(), user.getPassWord());

        assertEquals(user.getTryConnection(), 1);
        assertNull(user.getExpiryConnection());
    }
}
