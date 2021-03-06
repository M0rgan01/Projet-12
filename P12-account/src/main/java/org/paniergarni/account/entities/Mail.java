package org.paniergarni.account.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Mail {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String token;
    private int tryToken;
    private Date expiryToken;
    private boolean availablePasswordRecovery;
    private Date expiryPasswordRecovery;


    @JsonIgnore
    public String getToken() {
        return token;
    }
    @JsonProperty
    public void setToken(String token) {
        this.token = token;
    }
}
