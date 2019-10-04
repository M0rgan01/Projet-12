package org.paniergarni.account.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String userName;
    private String passWord;
    private String passWordConfirm;
    private boolean active;
    private int tryConnection;
    private Date expiryConnection;
    @OneToOne(cascade = CascadeType.ALL)
    private Mail mail;
    @ManyToMany
    private Collection<Role> roles = new HashSet<>();

    @JsonIgnore
    public String getPassWordConfirm() {
        return passWordConfirm;
    }
    @JsonIgnore
    public String getPassWord() {
        return passWord;
    }
    @JsonProperty
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
    @JsonProperty
    public void setPassWordConfirm(String passWordConfirm) {
        this.passWordConfirm = passWordConfirm;
    }
    
}
