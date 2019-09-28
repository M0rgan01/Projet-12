package org.paniergarni.account.business;

import org.paniergarni.account.dao.UserRepository;
import org.paniergarni.account.entities.Mail;
import org.paniergarni.account.entities.Role;
import org.paniergarni.account.entities.User;
import org.paniergarni.account.entities.dto.UserRecoveryDTO;
import org.paniergarni.account.exception.AccountException;
import org.paniergarni.account.exception.BadCredencialException;
import org.paniergarni.account.exception.ExpirationException;
import org.paniergarni.account.exception.PassWordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserBusinessImpl implements UserBusiness{

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
    public User createUser(User user) throws AccountException {
        checkUserNameExist(user.getUserName());
        mailBusiness.checkEmailExist(user.getMail().getEmail());
        user.setActive(true);
        checkPassWordConfirm(user.getPassWord(), user.getPassWordConfirm());
        String hashPW = bCryptPasswordEncoder.encode(user.getPassWord());
        user.setPassWord(hashPW);

        List<Role> roles = new ArrayList<>();
        roles.add(roleBusiness.getUserRole());
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) throws AccountException {

        User user1 = getUserById(user.getId());

        if (!user1.isActive()) {
            throw new AccountException("user.not.active");
        }else if (!user1.getUserName().equals(user.getUserName())) {
            checkUserNameExist(user.getUserName());
        }else if (!user1.getMail().getEmail().equals(user.getMail().getEmail())) {
            mailBusiness.checkEmailExist(user.getMail().getEmail());
        }else if (!bCryptPasswordEncoder.matches(user.getPassWord(), user1.getPassWord())){
            checkOldPassWord(user, user1.getPassWord());
            checkPassWordConfirm(user.getPassWord(), user.getPassWordConfirm());
        }

        return userRepository.save(user);
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

        if (!contact.isActive())
            throw new AccountException("user.not.active");

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
            throw new BadCredencialException("user.password.not.valid");
        }

        return contact;
    }


    public void checkPassWordConfirm(String passWord, String passWordConfirm) throws PassWordException {
        if (passWordConfirm == null || passWordConfirm.isEmpty())
            throw new PassWordException("user.password.confirm.empty");
        if (!passWord.equals(passWordConfirm))
            throw new PassWordException("user.incorrect.password.confirm");
    }

    public void checkOldPassWord(User user, String oldPassWord) throws PassWordException {
        if (user.getOldPassWord() == null || user.getOldPassWord().isEmpty())
            throw new PassWordException("user.old.password.empty");
        if (!bCryptPasswordEncoder.matches(user.getOldPassWord(), oldPassWord))
            throw new PassWordException("user.incorrect.old.password");
    }

    public void checkUserNameExist(String userName) throws AccountException {
        if (userRepository.findByUserName(userName).isPresent()){
            throw new AccountException("user.username.already.exist");
        }
    }

    @Override
    public void editPassWordByRecovery(String email, UserRecoveryDTO user) throws AccountException {
        // récupération du mail
        User user2 = getUserByEmail(email);
        if (!user2.getMail().isAvailablePasswordRecovery()){
            throw new AccountException("user.mail.not.available.recovery");
        } else if (user2.getMail().getExpiryPasswordRecovery().before(new Date())){
            user2.getMail().setAvailablePasswordRecovery(false);
            user2.getMail().setExpiryPasswordRecovery(null);
            userRepository.save(user2);
            throw new ExpirationException("user.passWord.recovery.expiry");
        }

        // on vérifie
        checkPassWordConfirm(user.getPassWord(), user.getPassWordConfirm());
        user2.setPassWord(bCryptPasswordEncoder.encode(user.getPassWord()));

        userRepository.save(user2);
        logger.info("Update password by recovery for user " + user2.getId());
    }
}
