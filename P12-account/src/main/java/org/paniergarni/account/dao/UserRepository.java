package org.paniergarni.account.dao;

import org.paniergarni.account.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);
    @Query("select c from User c where c.mail.email like :x")
    Optional<User> findByEmail(@Param("x")String email);
}
