package org.paniergarni.account.business;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.paniergarni.account.dao.UserRepository;
import org.paniergarni.account.entities.Mail;
import org.paniergarni.account.entities.Role;
import org.paniergarni.account.entities.User;
import org.paniergarni.account.entities.dto.CreateUserDTO;
import org.paniergarni.account.entities.dto.UserRecoveryDTO;
import org.paniergarni.account.entities.dto.UserUpdatePassWordDTO;
import org.paniergarni.account.exception.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;


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

    private UserRecoveryDTO userRecoveryDTO;
    private UserUpdatePassWordDTO userUpdatePassWordDTO;
    private CreateUserDTO createUserDTO;
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

        createUserDTO = new CreateUserDTO();
        createUserDTO.setUserName("Test");
        createUserDTO.setPassWord("Test1234");
        createUserDTO.setPassWordConfirm("Test1234");
        createUserDTO.setEmail("Test@account.fr");

        userUpdatePassWordDTO = new UserUpdatePassWordDTO();
        userUpdatePassWordDTO.setPassWord("test");
        userUpdatePassWordDTO.setPassWordConfirm("test");
        userUpdatePassWordDTO.setOldPassWord("Test1234");

        userRecoveryDTO = new UserRecoveryDTO();
        userRecoveryDTO.setPassWord("test");
        userRecoveryDTO.setPassWordConfirm("test");
    }


    @Test
    public void testCreateUser() throws AccountException {

        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.ofNullable(null));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(roleBusiness.getUserRole()).thenReturn(role);
        Mockito.when(bCryptPasswordEncoder.encode(user.getPassWord())).thenReturn("passwordEncoded1");
        user = userBusiness.createUser(createUserDTO);

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
        user = userBusiness.createUser(createUserDTO);
    }



   @Test
    public void testUpdateUserName() throws AccountException {
        user.setId(1l);
        user.setUserName("Pseudo2");
        user.setActive(true);

        Mockito.when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.ofNullable(null));
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        user = userBusiness.updateUserName(1l, "Pseudo");

        assertEquals(user.getUserName(), "Pseudo");

    }

    @Test(expected = UserNotActiveException.class)
    public void testUpdateUserNameNoActive() throws AccountException {
        user.setId(1l);
        user.setActive(false);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userBusiness.updateUserName(user.getId(), "Pseudo");
    }

    @Test(expected = AccountException.class)
    public void testUpdateUserNameWithAlreadyUserNameExist() throws AccountException {
        user.setId(1l);
        user.setUserName("Pseudo2");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));

        userBusiness.updateUserName(user.getId(), "Pseudo");
    }

    @Test(expected = AccountException.class)
    public void testUpdateUserNameWithSameValue() throws AccountException {
        user.setId(1l);
        user.setUserName("Pseudo2");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userBusiness.updateUserName(user.getId(), user.getUserName());
    }

    @Test(expected = AccountException.class)
    public void testUpdateUserNameWithEmptyValue() throws AccountException {
        user.setId(1l);
        user.setUserName("Pseudo2");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userBusiness.updateUserName(user.getId(), "");
    }

    @Test(expected = AccountException.class)
    public void testUpdateUserNameWithNullValue() throws AccountException {
        user.setId(1l);
        user.setUserName("Pseudo2");

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userBusiness.updateUserName(user.getId(), null);
    }

    @Test
    public void testUpdatePassWord() throws AccountException {
        user.setId(1l);
        user.setActive(true);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(bCryptPasswordEncoder.encode(userUpdatePassWordDTO.getPassWord())).thenReturn("passwordEncoded1");
        Mockito.when(bCryptPasswordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);

        userBusiness.updatePassWord(user.getId(), userUpdatePassWordDTO);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argument.capture());

        assertEquals(argument.getValue().getPassWord(), "passwordEncoded1");
    }

    @Test(expected = PassWordException.class)
    public void testUpdatePassWordWithNoMatchPassWordConfirm() throws AccountException {
        user.setId(1l);
        user.setActive(true);
        userUpdatePassWordDTO.setPassWordConfirm("test1");
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userBusiness.updatePassWord(user.getId(), userUpdatePassWordDTO);
    }

    @Test(expected = PassWordException.class)
    public void testUpdatePassWordWithNoMatchOldPassWord() throws AccountException {
        user.setId(1l);
        user.setActive(true);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(bCryptPasswordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(false);

        userBusiness.updatePassWord(user.getId(), userUpdatePassWordDTO);
    }


    @Test
    public void testEditPassWordByRecovery() throws AccountException {
        user.setId(1l);
        user.getMail().setAvailablePasswordRecovery(true);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        user.getMail().setExpiryPasswordRecovery(calendar.getTime());

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(bCryptPasswordEncoder.encode(Mockito.any())).thenReturn("passwordEncoded1");

        userBusiness.editPassWordByRecovery(user.getMail().getEmail(), userRecoveryDTO);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argument.capture());

        assertEquals(argument.getValue().getPassWord(), "passwordEncoded1");
    }

    @Test(expected = AccountException.class)
    public void testEditPassWordByRecoveryWithNotAvailablePasswordRecovery() throws AccountException {
        user.setId(1l);
        user.getMail().setAvailablePasswordRecovery(false);

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));

        userBusiness.editPassWordByRecovery(user.getMail().getEmail(), userRecoveryDTO);

    }

    @Test
    public void testEditPassWordByRecoveryWithRecoveryExpiry() throws AccountException {
        user.setId(1l);
        user.getMail().setAvailablePasswordRecovery(true);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -30);
        user.getMail().setExpiryPasswordRecovery(calendar.getTime());

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        try {
            userBusiness.editPassWordByRecovery(user.getMail().getEmail(), userRecoveryDTO);
        }catch (ExpirationException e) {
            ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(argument.capture());

            assertFalse(argument.getValue().getMail().isAvailablePasswordRecovery());
            assertNull(argument.getValue().getMail().getExpiryPasswordRecovery());
        }
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

    @Test(expected = BadCredencialException.class)
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

    @Test(expected = ExpirationException.class)
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
