package org.paniergarni.account.business;

import org.jetbrains.annotations.NotNull;
import org.paniergarni.account.dao.UserRepository;
import org.paniergarni.account.entities.Role;
import org.paniergarni.account.entities.User;
import org.paniergarni.account.exception.AccountException;
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
    @Value("${waiting.minutes.for.max.try.login}")
    private int waitingMinutes;

    @Override
    public User createUser(User user) {
        checkUserNameExist(user.getUserName());
        mailBusiness.checkEmailExist(user.getMail().getEmail());
        user.setActive(true);
        checkPassWordConfirm(user);
        String hashPW = bCryptPasswordEncoder.encode(user.getPassWord());
        user.setPassWord(hashPW);

        List<Role> roles = new ArrayList<>();
        roles.add(roleBusiness.getUserRole());
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {

        User user1 = getUserById(user.getId());

        if (!user1.isActive()) {
            throw new AccountException("user.not.active");
        }else if (!user1.getUserName().equals(user.getUserName())) {
            checkUserNameExist(user.getUserName());
        }else if (!user1.getMail().getEmail().equals(user.getMail().getEmail())) {
            mailBusiness.checkEmailExist(user.getMail().getEmail());
        }else if (!bCryptPasswordEncoder.matches(user.getPassWord(), user1.getPassWord())){
            checkOldPassWord(user, user1.getPassWord());
            checkPassWordConfirm(user);
        }

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new AccountException("user.id.incorrect"));
    }

    @Override
    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() -> new AccountException("user.username.incorrect"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new AccountException("user.email.incorrect"));
    }

    @Override
    public User doConnection(String userName, String passWord) {

        // récupération du contact pour la comparaison
        User contact = userRepository.findByUserName(userName)
                .orElseGet(() -> userRepository.findByEmail(userName)
                        .orElseThrow(() -> new AccountException("user.not.found")));

        if (!contact.isActive())
            throw new AccountException("user.not.active");

        if (contact.getExpiryConnection() != null) {
            if (contact.getExpiryConnection().after(new Date())) {
                throw new AccountException("user.expiryConnection.after.date");
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
            throw new AccountException("user.password.not.valid");
        }

        return contact;
    }


    public void checkPassWordConfirm(User user){
        if (user.getPassWordConfirm() == null || user.getPassWordConfirm().isEmpty())
            throw new AccountException("user.password.confirm.empty");
        if (!user.getPassWord().equals(user.getPassWordConfirm()))
            throw new AccountException("user.incorrect.password.confirm");
    }

    public void checkOldPassWord(User user, String oldPassWord){
        if (user.getOldPassWord() == null || user.getOldPassWord().isEmpty())
            throw new AccountException("user.old.password.empty");
        if (!bCryptPasswordEncoder.matches(user.getOldPassWord(), oldPassWord))
            throw new AccountException("user.incorrect.old.password");
    }

    public void checkUserNameExist(String userName){
        if (userRepository.findByUserName(userName).isPresent()){
            throw new AccountException("user.username.already.exist");
        }
    }
}
