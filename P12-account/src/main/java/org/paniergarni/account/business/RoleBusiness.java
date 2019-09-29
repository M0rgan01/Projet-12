package org.paniergarni.account.business;

import org.paniergarni.account.entities.Role;
import org.paniergarni.account.exception.AccountException;

import java.util.List;

public interface RoleBusiness {


    Role createRole(Role role) throws AccountException;
    Role getRoleById(Long id) throws AccountException;
    Role getUserRole() throws AccountException;
    List<Role> getAdminRole() throws AccountException;
}
