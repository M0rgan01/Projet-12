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
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public BCryptPasswordEncoder getBCPE() {
        return new BCryptPasswordEncoder();
    }


    public static void main(String[] args) {
        SpringApplication.run(P12AccountApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Mail mail = new Mail();
        mail.setEmail("account@account.fr");

        Mail mail2 = new Mail();
        mail2.setEmail("test2@account.fr");

        Role role = new Role();
        role.setName("ROLE_ADMIN");
        Role role2 = new Role();
        role2.setName("ROLE_USER");

        roleRepository.save(role);
        roleRepository.save(role2);

        User user = new User();
        user.setUserName("Admin");
        user.setPassWord(bCryptPasswordEncoder.encode("Admin1234"));
        user.setMail(mail);
        user.setRoles(Arrays.asList(role, role2));
        user.setActive(true);

        User user2 = new User();
        user2.setUserName("User");
        user2.setPassWord(bCryptPasswordEncoder.encode("User1234"));
        user2.setMail(mail2);
        user2.setRoles(Arrays.asList(role2));
        user2.setActive(true);

        userRepository.saveAll(Arrays.asList(user, user2));
    }
}
