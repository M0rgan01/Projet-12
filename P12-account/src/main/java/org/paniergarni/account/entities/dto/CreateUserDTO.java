package org.paniergarni.account.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserDTO {

    @NotBlank(message ="user.password.blank")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", message = "user.password.not.true")
    private String passWord;
    @NotBlank(message ="user.passwordConfirm.blank")
    private String passWordConfirm;
    @NotBlank
    @Size(min = 4, max = 20)
    private String userName;
    @NotNull(message = "mail.email.null")
    @Pattern(regexp = "^[^\\W][a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\@[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\.[a-zA-Z]{2,4}$", message = "mail.email.not.true")
    private String email;
}
