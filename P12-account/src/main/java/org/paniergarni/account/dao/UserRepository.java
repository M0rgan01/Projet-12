package org.paniergarni.account.dao;

import org.paniergarni.account.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
