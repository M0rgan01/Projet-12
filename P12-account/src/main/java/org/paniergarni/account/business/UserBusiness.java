package org.paniergarni.account.business;

import org.paniergarni.account.entities.User;
import org.paniergarni.account.entities.dto.UserRecoveryDTO;
import org.paniergarni.account.exception.AccountException;
import org.paniergarni.account.exception.PassWordException;

public interface UserBusiness {

    User createUser(User user) throws AccountException;
    User updateUser(User user) throws AccountException;
    User getUserById(Long id) throws AccountException;
    User getUserByUserName(String userName) throws AccountException;
    User getUserByEmail(String email) throws AccountException;
    User doConnection(String userName, String passWord) throws AccountException;
    void checkPassWordConfirm(String passWord, String passWordConfirm) throws PassWordException;
    void checkOldPassWord(User user, String oldPassword) throws PassWordException;
    void checkUserNameExist(String userName) throws AccountException;
    void editPassWordByRecovery(String email, UserRecoveryDTO user);
}
