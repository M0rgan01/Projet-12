package org.paniergarni.apigateway.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String userName;
    private String passWord;
    private String passWordConfirm;
    private String oldPassWord;
    private List<Role> roles;
    private Mail mail;
    private boolean active;
    private int tryConnection;
    private Date expiryConnection;

}
