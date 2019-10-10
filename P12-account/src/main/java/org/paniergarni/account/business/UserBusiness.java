package org.paniergarni.account.business;

import org.paniergarni.account.entities.User;
import org.paniergarni.account.entities.CreateUserDTO;
import org.paniergarni.account.entities.UserRecoveryDTO;
import org.paniergarni.account.entities.UserUpdatePassWordDTO;
import org.paniergarni.account.exception.AccountException;

/**
 * Manipulation de User
 *
 * @author Pichat morgan
 *
 * 05 octobre 2019
 */
public interface UserBusiness {

    /** Création d'un utilisateur
     *
     * @param user
     * @return User
     * @throws AccountException
     */
    User createUser(CreateUserDTO user) throws AccountException;

    /** Mise à jour du statut active d'un utilisateur
     *
     * @param user
     * @return User
     * @throws AccountException
     */
    User updateUserActive(Long id, boolean active) throws AccountException;

    /** Mise à jour de l'attribut userName d'un utilisateur
     *
     * @param id
     * @param active
     * @return User
     * @throws AccountException
     */
    User updateUserName(Long id, String userName)throws AccountException;

    /** Mise à jour de l'attribut passWord d'un utilisateur
     *
     * @param id
     * @param user
     * @throws AccountException
     */
    void updatePassWord(Long id, UserUpdatePassWordDTO user)throws AccountException;

    /** Récupération d'un utilisateur par son ID
     *
     * @param id
     * @return User
     * @throws AccountException
     */
    User getUserById(Long id) throws AccountException;

    /** Récupération d'un utilisateur par son userName
     *
     * @param userName
     * @return User
     * @throws AccountException
     */
    User getUserByUserName(String userName) throws AccountException;

    /** Récupération d'un utilisateur par son email
     *
     * @param email
     * @return User
     * @throws AccountException
     */
    User getUserByEmail(String email) throws AccountException;

    /** Connection d'un utilisateur
     *
     * @param userName
     * @param passWord
     * @return User
     * @throws AccountException
     */
    User doConnection(String userName, String passWord) throws AccountException;

    /** Mise à jour de l'attribut passWord d'un utilisateur par récupération
     *
     * @param email
     * @param user
     * @throws AccountException
     */
    void editPassWordByRecovery(String email, UserRecoveryDTO user) throws AccountException;

    /** Assigne le role ROLE_USER à un utilisateur
     *
     * @param id
     * @return User
     * @throws AccountException
     */
    User setUserRole(Long id) throws AccountException;

    /** Assigne le role ROLE_ADMIN et ROLE_USER à un utilisateur
     *
     * @param id
     * @return User
     * @throws AccountException
     */
    User setAdminRole(Long id) throws AccountException;
}
