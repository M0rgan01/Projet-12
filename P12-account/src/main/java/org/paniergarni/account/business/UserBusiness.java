package org.paniergarni.account.business;

import org.paniergarni.account.entities.User;

public interface UserBusiness {

    User createUser(User user);
    User updateUser(User user);
    User getUserById(Long id);
    User getUserByUserName(String userName);
    User getUserByEmail(String email);
    User doConnection(String userName, String passWord);
    void checkPassWordConfirm(User user);
    void checkOldPassWord(User user, String oldPassword);
    void checkUserNameExist(String userName);
}
