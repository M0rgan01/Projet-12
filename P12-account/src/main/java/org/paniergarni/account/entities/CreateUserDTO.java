package org.paniergarni.account.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Represente les données permettant la création d'un utilisateur")
public class CreateUserDTO {

    @ApiModelProperty(notes = "Mot de passe, minimum 8 caractères, une majuscule et un chiffre", example = "TestPassWord1", required = true)
    @NotNull(message ="user.passWord.null")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", message = "user.passWord.not.true")
    private String passWord;
    @ApiModelProperty(notes = "Confirmation du mot de passe", example = "TestPassWord1", required = true)
    @NotNull(message ="user.passwordConfirm.null")
    private String passWordConfirm;
    @ApiModelProperty(notes = "Pseudo de l'utilisateur, entre 4 et 20 caractères", example = "Pseudo", required = true)
    @NotNull(message ="user.userName.null")
    @Size(min = 4, max = 20, message ="user.userName.size.not.correct")
    private String userName;
    @ApiModelProperty(notes = "Adresse e-mail de l'utilisateur, doit être une email valide", example = "Test@test.com", required = true)
    @NotNull(message = "mail.email.null")
    @Pattern(regexp = "^[^\\W][a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\@[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\.[a-zA-Z]{2,4}$", message = "mail.email.not.true")
    private String email;
}
