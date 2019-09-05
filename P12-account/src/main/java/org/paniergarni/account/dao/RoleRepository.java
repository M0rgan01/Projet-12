package org.paniergarni.account.dao;

import org.paniergarni.account.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
