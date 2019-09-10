package org.paniergarni.account.business;

import org.paniergarni.account.dao.RoleRepository;
import org.paniergarni.account.entities.Role;
import org.paniergarni.account.exception.AccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleBusinessImpl implements RoleBusiness {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role createRole(Role role) {

        roleRepository.findByName(role.getName()).ifPresent(role1 -> {
            throw new IllegalArgumentException("Role name " + role1.getName() + " already exist");
        });

        return roleRepository.save(role);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID " + id + " Incorrect"));
    }

    @Override
    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").orElseThrow(() -> new AccountException("role.user.not.found"));
    }

    @Override
    public List<Role> getAdminRole() {

        Role user = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new AccountException("role.user.not.found"));
        Role admin = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new AccountException("role.admin.not.found"));

        List<Role> roles = new ArrayList<>();

        roles.add(user);
        roles.add(admin);

        return roles;
    }
}
