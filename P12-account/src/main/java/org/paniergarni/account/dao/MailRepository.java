package org.paniergarni.account.dao;

import org.paniergarni.account.entities.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailRepository extends JpaRepository<Mail, Long> {

   Optional<Mail> findByEmail(String email);
}
