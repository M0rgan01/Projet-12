package org.paniergarni.account.business;

import org.paniergarni.account.dao.UserRepository;
import org.paniergarni.account.entities.Mail;
import org.paniergarni.account.entities.Role;
import org.paniergarni.account.entities.User;
import org.paniergarni.account.entities.CreateUserDTO;
import org.paniergarni.account.entities.UserRecoveryDTO;
import org.paniergarni.account.entities.UserUpdatePassWordDTO;
import org.paniergarni.account.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Manipulation de User
 *
 * @author Pichat morgan
 *
 * 05 octobre 2019
 */
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
        // vérification de la présence d'un userName identique
        checkUserNameExist(user.getUserName());
        // vérification de la présence d'un email identique
        mailBusiness.checkEmailExist(user.getEmail());
        // vérification de la confirmation du mot de passe
        checkPassWordConfirm(user.getPassWord(), user.getPassWordConfirm());

        user1.setPassWord(bCryptPasswordEncoder.encode(user.getPassWord()));
        user1.setActive(true);
        user1.setUserName(user.getUserName());
        user1.setMail(mail);
        user1.getMail().setEmail(user.getEmail());

        List<Role> roles = new ArrayList<>();
        // récupération du role utilisateur
        roles.add(roleBusiness.getUserRole());
        user1.setRoles(roles);
        logger.info("Create user : " + user.getUserName());
        return userRepository.save(user1);
    }

    @Override
    public User updateUserActive(Long id, boolean active) throws AccountException {

        User user = getUserById(id);

        // vérification que l'utilisateur n'est pas déjà dans l'état souhaité
        if (user.isActive() == active)
            throw new AccountException("user.active.same.value");

        user.setActive(active);

        logger.info("Update user : " + id+ ", set active :" + active);
        return userRepository.save(user);
    }

    @Override
    public User updateUserName(Long id, String userName) throws AccountException {
        // récupération de l'utilisateur pour comparaison
        User user = getUserById(id);
        // on vérifie qu'il est bien actif
        checkUserActive(user.isActive());

         if (userName == null || userName.isEmpty()) {
            throw new AccountException("user.userName.empty");
        } else if (user.getUserName().equals(userName)) {
            throw new AccountException("user.userName.same.value");
        }

        // vérification de la présence d'un userName identique
        checkUserNameExist(userName);
        user.setUserName(userName);
        logger.info("Update userName for user ID : " + user.getId());
        return userRepository.save(user);
    }

    @Override
    public void updatePassWord(Long id, UserUpdatePassWordDTO userDTO) throws AccountException {
        // récupération de l'utilisateur pour comparaison
        User user = getUserById(id);
        // on vérifie qu'il est bien actif
        checkUserActive(user.isActive());
        // vérification de la confirmation
        checkPassWordConfirm(userDTO.getPassWord(), userDTO.getPassWordConfirm());
        // vérification de l'ancien mot de passe
        checkOldPassWord(userDTO.getOldPassWord(), user.getPassWord());
        user.setPassWord(bCryptPasswordEncoder.encode(userDTO.getPassWord()));
        logger.info("Update passWord for user ID : " + user.getId());
        userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) throws AccountException {
        return userRepository.findById(id).orElseThrow(() -> new AccountException("user.id.incorrect"));
    }

    @Override
    public User getUserByUserName(String userName) throws AccountException {
        return userRepository.findByUserName(userName).orElseThrow(() -> new AccountException("user.userName.incorrect"));
    }

    @Override
    public User getUserByEmail(String email) throws AccountException {
        return userRepository.findByEmail(email).orElseThrow(() -> new AccountException("user.email.incorrect"));
    }

    @Override
    public User doConnection(String userName, String passWord) throws AccountException {

        // récupération de l'utilisateur pour la comparaison
        User contact = userRepository.findByUserName(userName)
                .orElseGet(() -> userRepository.findByEmail(userName)
                        .orElseThrow(() -> new AccountException("user.not.found")));

        // on vérifie qu'il est bien actif
        checkUserActive(contact.isActive());

        // vérification de la présence d'une expiration de connection
        if (contact.getExpiryConnection() != null) {
            if (contact.getExpiryConnection().after(new Date())) {
                logger.debug("Expiry connection error for user ID : " + contact.getId());
                throw new ExpirationException("user.expiryConnection.after.date");
            } else {
                logger.debug("Expiry connection finish for user ID : " + contact.getId());
                contact.setTryConnection(0);
                contact.setExpiryConnection(null);
                userRepository.save(contact);
            }
        }

        // vérification du password
        if (!bCryptPasswordEncoder.matches(passWord, contact.getPassWord())) {
            contact.setTryConnection(contact.getTryConnection() + 1);

            // si l'utilisateur à dépassé le nombre maximum d'éssais on lui assigne une date d'expiration
            if (contact.getTryConnection() >= tryIncorrectLogin) {

                Calendar date = Calendar.getInstance();
                date.add(Calendar.MINUTE, waitingMinutes);
                contact.setExpiryConnection(date.getTime());

                userRepository.save(contact);
                logger.debug("Set expiry connection for user ID : " + contact.getId());
                throw new AccountException("user.tryConnection.out");
            }
            userRepository.save(contact);
            logger.debug("Bad passWord for user ID : " + contact.getId());
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
        // vérification de l'éligibilité au changement de mot de passe par récupération
        if (!user2.getMail().isAvailablePasswordRecovery()) {
            logger.debug("User passWord recovery not available for user ID : " + user2.getId());
            throw new AccountException("user.mail.not.available.recovery");
        // si la date d'expiration pour changement à expirée
        } else if (user2.getMail().getExpiryPasswordRecovery().before(new Date())) {
            user2.getMail().setAvailablePasswordRecovery(false);
            user2.getMail().setExpiryPasswordRecovery(null);
            userRepository.save(user2);
            logger.debug("User passWord recovery expiry for user ID : " + user2.getId());
            throw new ExpirationException("user.passWord.recovery.expiry");
        }

        // on vérifie
        checkPassWordConfirm(user.getPassWord(), user.getPassWordConfirm());
        user2.setPassWord(bCryptPasswordEncoder.encode(user.getPassWord()));

        userRepository.save(user2);
        logger.info("Update passWord by recovery for user " + user2.getId());
    }

    @Override
    public User setUserRole(Long id) throws AccountException {
        User user = getUserById(id);
        List<Role> roles = new ArrayList<>();
        roles.add(roleBusiness.getUserRole());
        user.setRoles(roles);
        logger.info("Update user : " + id + " , set User role");
        return userRepository.save(user);
    }

    @Override
    public User setAdminRole(Long id) throws AccountException {
        User user = getUserById(id);
        List<Role> roles = roleBusiness.getAdminRole();
        user.setRoles(roles);
        logger.info("Update user : " + id + " , set Admin role");
        return userRepository.save(user);
    }

}
