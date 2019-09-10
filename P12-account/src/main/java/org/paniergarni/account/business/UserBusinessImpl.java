package org.paniergarni.account.business;

import org.paniergarni.account.dao.UserRepository;
import org.paniergarni.account.entities.Role;
import org.paniergarni.account.entities.User;
import org.paniergarni.account.exception.AccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class UserBusinessImpl implements UserBusiness{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleBusiness roleBusiness;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Value("${max.try.incorrect.login}")
    private int tryIncorrectLogin;
    @Value("${waiting.minutes.for.max.try.login}")
    private int waitingMinutes;

    @Override
    public User createUser(User user) {

        userRepository.findByUserName(user.getUserName()).ifPresent(user1 -> {
            throw new IllegalArgumentException("User name " + user1.getUserName() + " already exist");
        });

        user.setActive(true);
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

       /* if (!user1.getUserName().equals(user.getUserName()) && user.getId() != user1.getId())
            userRepository.findByUserName(user.getUserName()).ifPresent(user2 -> {
                throw new IllegalArgumentException("User name " + user2.getUserName() + " already exist");
            });
*/
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID " + id + " Incorrect"));
    }

    @Override
    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() -> new IllegalArgumentException("User name " + userName + " Incorrect"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Email " + email + " Incorrect"));
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


}
