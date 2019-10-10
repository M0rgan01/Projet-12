package org.paniergarni.account.business;

import org.paniergarni.account.entities.Role;
import org.paniergarni.account.exception.AccountException;

import java.util.List;
/**
 * Manipulation de Role
 *
 * @author Pichat morgan
 *
 * 05 octobre 2019
 */
public interface RoleBusiness {

    /** Création d'un role
     *
     * @param role
     * @return Role
     * @throws AccountException
     */
    Role createRole(Role role) throws AccountException;

    /** Récupération d'un role par son ID
     *
     * @param id
     * @return Role
     * @throws AccountException
     */
    Role getRoleById(Long id) throws AccountException;

    /** Récupération du role d'un utilisateur
     *
     * @return Role
     * @throws AccountException
     */
    Role getUserRole() throws AccountException;

    /** Récupération des roles d'un administrateur
     *
     * @return List Role
     * @throws AccountException
     */
    List<Role> getAdminRole() throws AccountException;
}
