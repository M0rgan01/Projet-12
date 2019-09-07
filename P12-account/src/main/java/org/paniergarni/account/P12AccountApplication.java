package org.paniergarni.account;

import org.paniergarni.account.dao.MailRepository;
import org.paniergarni.account.dao.RoleRepository;
import org.paniergarni.account.dao.UserRepository;
import org.paniergarni.account.entities.Mail;
import org.paniergarni.account.entities.Role;
import org.paniergarni.account.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Arrays;

@SpringBootApplication
@EnableDiscoveryClient
public class P12AccountApplication implements CommandLineRunner {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailRepository mailRepository;
    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(P12AccountApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Mail mail = new Mail();
        mail.setEmail("test@test.fr");

        Mail mail2 = new Mail();
        mail2.setEmail("test2@test.fr");

        Role role = new Role();
        role.setName("ROLE_ADMIN");
        Role role2 = new Role();
        role2.setName("ROLE_USER");

        roleRepository.save(role);
        roleRepository.save(role2);

        User user = new User();
        user.setUserName("Admin");
        user.setMail(mail);
        user.setRoles(Arrays.asList(role, role2));

        User user2 = new User();
        user2.setUserName("User");
        user2.setMail(mail2);
        user2.setRoles(Arrays.asList(role2));

        userRepository.saveAll(Arrays.asList(user, user2));
    }
}
