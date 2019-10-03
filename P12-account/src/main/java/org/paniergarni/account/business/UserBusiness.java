package org.paniergarni.account.business;

import org.paniergarni.account.entities.User;
import org.paniergarni.account.entities.dto.CreateUserDTO;
import org.paniergarni.account.entities.dto.UserRecoveryDTO;
import org.paniergarni.account.entities.dto.UserUpdatePassWordDTO;
import org.paniergarni.account.exception.AccountException;
import org.paniergarni.account.exception.PassWordException;

import java.util.List;

public interface UserBusiness {

    User createUser(CreateUserDTO user) throws AccountException;

    User updateUserActive(Long id, boolean active) throws AccountException;
    User updateUserName(Long id, String userName)throws AccountException;

    void updatePassWord(Long id, UserUpdatePassWordDTO user)throws AccountException;

    User getUserById(Long id) throws AccountException;
    User getUserByUserName(String userName) throws AccountException;
    User getUserByEmail(String email) throws AccountException;

    User doConnection(String userName, String passWord) throws AccountException;

    void editPassWordByRecovery(String email, UserRecoveryDTO user) throws AccountException;

    User setUserRole(Long id) throws AccountException;
    User setAdminRole(Long id) throws AccountException;
}
