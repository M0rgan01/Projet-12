package org.paniergarni.account.business;

import org.paniergarni.account.dao.UserRepository;
import org.paniergarni.account.entities.Mail;
import org.paniergarni.account.entities.Role;
import org.paniergarni.account.entities.User;
import org.paniergarni.account.entities.dto.CreateUserDTO;
import org.paniergarni.account.entities.dto.UserRecoveryDTO;
import org.paniergarni.account.entities.dto.UserUpdatePassWordDTO;
import org.paniergarni.account.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserBusinessImpl implements UserBusiness {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleBusiness roleBusiness;
    @Autowired
    private MailBusiness mailBusiness;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Value("${max.try.incorrect.login}")
    private int tryIncorrectLogin;
    @Value("${waiting.for.max.try.login.inMinutes}")
    private int waitingMinutes;

    private final Logger logger = LoggerFactory.getLogger(UserBusinessImpl.class);

    @Override
    public User createUser(CreateUserDTO user) throws AccountException {
        User user1 = new User();
        Mail mail = new Mail();
        checkUserNameExist(user.getUserName());
        mailBusiness.checkEmailExist(user.getEmail());
        checkPassWordConfirm(user.getPassWord(), user.getPassWordConfirm());

        user1.setPassWord(bCryptPasswordEncoder.encode(user.getPassWord()));
        user1.setActive(true);
        user1.setUserName(user.getUserName());
        user1.setMail(mail);
        user1.getMail().setEmail(user.getEmail());

        List<Role> roles = new ArrayList<>();
        roles.add(roleBusiness.getUserRole());
        user1.setRoles(roles);

        return userRepository.save(user1);
    }

    @Override
    public User updateUser(Long id, User user) throws AccountException {

        User user1 = getUserById(user.getId());

        return userRepository.save(user);
    }

    @Override
    public User updateUserName(Long id, String userName) throws AccountException {

        User user = getUserById(id);
        checkUserActive(user.isActive());

        if (!user.isActive()) {
            throw new UserNotActiveException("user.not.active");
        } else if (userName == null || userName.isEmpty()) {
            throw new AccountException("user.userName.empty");
        } else if (user.getUserName().equals(userName)) {
            throw new AccountException("user.userName.same.value");
        }

        checkUserNameExist(userName);
        user.setUserName(userName);
        return userRepository.save(user);
    }

    @Override
    public void updatePassWord(Long id, UserUpdatePassWordDTO userDTO) throws AccountException {
        User user = getUserById(id);
        checkUserActive(user.isActive());
        checkPassWordConfirm(userDTO.getPassWord(), userDTO.getPassWordConfirm());
        checkOldPassWord(userDTO.getOldPassWord(), user.getPassWord());
        user.setPassWord(bCryptPasswordEncoder.encode(userDTO.getPassWord()));
        userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) throws AccountException {
        return userRepository.findById(id).orElseThrow(() -> new AccountException("user.id.incorrect"));
    }

    @Override
    public User getUserByUserName(String userName) throws AccountException {
        return userRepository.findByUserName(userName).orElseThrow(() -> new AccountException("user.username.incorrect"));
    }

    @Override
    public User getUserByEmail(String email) throws AccountException {
        return userRepository.findByEmail(email).orElseThrow(() -> new AccountException("user.email.incorrect"));
    }

    @Override
    public User doConnection(String userName, String passWord) throws AccountException {

        // récupération du contact pour la comparaison
        User contact = userRepository.findByUserName(userName)
                .orElseGet(() -> userRepository.findByEmail(userName)
                        .orElseThrow(() -> new AccountException("user.not.found")));

        checkUserActive(contact.isActive());

        if (contact.getExpiryConnection() != null) {
            if (contact.getExpiryConnection().after(new Date())) {
                throw new ExpirationException("user.expiryConnection.after.date");
            } else {
                contact.setTryConnection(0);
                contact.setExpiryConnection(null);
                userRepository.save(contact);
            }
        }

        // vérification du password
        if (!bCryptPasswordEncoder.matches(passWord, contact.getPassWord())) {
            contact.setTryConnection(contact.getTryConnection() + 1);

            if (contact.getTryConnection() >= tryIncorrectLogin) {

                Calendar date = Calendar.getInstance();
                date.add(Calendar.MINUTE, waitingMinutes);
                contact.setExpiryConnection(date.getTime());

                userRepository.save(contact);
                throw new AccountException("user.tryConnection.out");
            }
            userRepository.save(contact);
            throw new BadCredencialException("user.passWord.not.valid");
        }

        return contact;
    }


    private void checkPassWordConfirm(String passWord, String passWordConfirm) throws PassWordException {
        if (!passWord.equals(passWordConfirm))
            throw new PassWordException("user.incorrect.passWord.confirm");
    }

    private void checkOldPassWord(String oldPassWord, String actualPassWord) throws PassWordException {
        if (!bCryptPasswordEncoder.matches(oldPassWord, actualPassWord))
            throw new PassWordException("user.incorrect.old.passWord");
    }

    private void checkUserNameExist(String userName) throws AccountException {
        if (userRepository.findByUserName(userName).isPresent()) {
            throw new AccountException("user.userName.already.exist");
        }
    }

    private void checkUserActive(boolean b) throws UserNotActiveException {
        if (!b)
            throw new UserNotActiveException("user.not.active");
    }

    @Override
    public void editPassWordByRecovery(String email, UserRecoveryDTO user) throws AccountException {
        // récupération du mail
        User user2 = getUserByEmail(email);
        if (!user2.getMail().isAvailablePasswordRecovery()) {
            throw new AccountException("user.mail.not.available.recovery");
        } else if (user2.getMail().getExpiryPasswordRecovery().before(new Date())) {
            user2.getMail().setAvailablePasswordRecovery(false);
            user2.getMail().setExpiryPasswordRecovery(null);
            userRepository.save(user2);
            throw new ExpirationException("user.passWord.recovery.expiry");
        }

        // on vérifie
        checkPassWordConfirm(user.getPassWord(), user.getPassWordConfirm());
        user2.setPassWord(bCryptPasswordEncoder.encode(user.getPassWord()));

        userRepository.save(user2);
        logger.info("Update passWord by recovery for user " + user2.getId());
    }
}
