package org.paniergarni.account.business;

import org.paniergarni.account.entities.User;

public interface UserBusiness {

    User createUser(User user);
    User updateUser(User user);
    User getUserById(Long id);
    User getUserByUserName(String userName);
}
