package org.paniergarni.account.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
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
    @Size(min = 4, max = 20)
    private String userName;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", message = "user.password.not.true")
    private String passWord;
    private String passWordConfirm;
    @Transient
    private String oldPassWord;
    private boolean active;
    private int tryConnection;
    private Date expiryConnection;
    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    private Mail mail;
    @ManyToMany
    private Collection<Role> roles = new HashSet<>();

    @JsonIgnore
    public String getPassWordConfirm() {
        return passWordConfirm;
    }
    @JsonIgnore
    public String getOldPassWord() {
        return oldPassWord;
    }

    @JsonIgnore
    public String getPassWord() {
        return passWord;
    }
}
