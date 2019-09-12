package org.paniergarni.account.business;

import org.paniergarni.account.entities.Role;

import java.util.List;

public interface RoleBusiness {


    Role createRole(Role role);
    Role getRoleById(Long id);
    Role getUserRole();
    List<Role> getAdminRole();
}
