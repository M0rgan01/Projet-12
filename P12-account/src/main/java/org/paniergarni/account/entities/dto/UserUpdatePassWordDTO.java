package org.paniergarni.account.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUpdatePassWordDTO {
    @NotBlank(message ="user.password.blank")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", message = "user.password.not.true")
    private String passWord;
    @NotBlank(message ="user.passwordConfirm.blank")
    private String passWordConfirm;
    @NotBlank(message ="user.oldPassWord.blank")
    private String oldPassWord;
}
