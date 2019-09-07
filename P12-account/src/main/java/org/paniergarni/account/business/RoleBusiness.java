package org.paniergarni.account.business;

import org.paniergarni.account.entities.Role;

public interface RoleBusiness {


    Role createRole(Role role);
    Role getRoleById(Long id);

}
