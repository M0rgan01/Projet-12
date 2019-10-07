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

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Represente les données permettant la modifcation du passWord")
public class UserUpdatePassWordDTO {
    @ApiModelProperty(notes = "Mot de passe, minimum 8 caractères, une majuscule et un chiffre", example = "TestPassWord1", required = true)
    @NotNull(message ="user.passWord.null")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", message = "user.passWord.not.true")
    private String passWord;
    @ApiModelProperty(notes = "Confirmation du mot de passe", example = "TestPassWord1", required = true)
    @NotNull(message ="user.passWordConfirm.null")
    private String passWordConfirm;
    @ApiModelProperty(notes = "Ancien mot de passe", example = "TestPassWord1", required = true)
    @NotNull(message ="user.oldPassWord.null")
    private String oldPassWord;
}
