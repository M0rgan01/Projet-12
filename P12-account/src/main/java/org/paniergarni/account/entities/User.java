package org.paniergarni.account.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Min(4)
    @Max(15)
    private String userName;
    private String passWord;
    private boolean active;
    private int tryConnection;
    private Date expiryConnection;
    @OneToOne
    private Mail mail;
    @ManyToMany
    private Collection<Role> roles = new HashSet<>();
}
