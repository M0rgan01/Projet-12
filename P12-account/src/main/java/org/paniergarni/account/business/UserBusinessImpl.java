package org.paniergarni.account.business;

import org.paniergarni.account.dao.UserRepository;
import org.paniergarni.account.entities.Role;
import org.paniergarni.account.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class UserBusinessImpl implements UserBusiness{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleBusiness roleBusiness;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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

        if (!user1.getUserName().equals(user.getUserName()))
            userRepository.findByUserName(user.getUserName()).ifPresent(user2 -> {
                throw new IllegalArgumentException("User name " + user2.getUserName() + " already exist");
            });

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
}
