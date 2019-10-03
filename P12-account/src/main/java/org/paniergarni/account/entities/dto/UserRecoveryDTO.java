package org.paniergarni.account.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Represente les données permettant la modifcation du passWord par récupération")
public class UserRecoveryDTO {

    @ApiModelProperty(notes = "Mot de passe, minimum 8 caractères, une majuscule et un chiffre", example = "TestPassWord1", required = true)
    @NotBlank(message ="user.password.blank")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", message = "user.password.not.true")
    private String passWord;
    @ApiModelProperty(notes = "Confirmation du mot de passe", example = "TestPassWord1", required = true)
    @NotBlank(message ="user.passwordConfirm.blank")
    private String passWordConfirm;
}
