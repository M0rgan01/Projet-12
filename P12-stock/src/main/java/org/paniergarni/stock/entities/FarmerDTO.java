package org.paniergarni.stock.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Represente les données permettant la création / modification d'un agriculteur")
public class FarmerDTO {

    @ApiModelProperty(notes = "Nom de l'agriculteur, de 4 à 50 caractères", example = "La ferme")
    @NotNull(message = "farmer.name.null")
    @Size(min = 4, max = 50, message = "farmer.name.incorrect.size")
    private String name;
    @ApiModelProperty(notes = "Location de l'agriculteur, de 4 à 50 caractères", example = "69006 Lyon")
    @NotNull(message = "farmer.location.null")
    @Size(min = 10, max = 70, message = "farmer.location.incorrect.size")
    private String location;
    @ApiModelProperty(notes = "Téléphone de l'agriculteur, 10 caractères", example = "0101010101")
    @NotNull(message = "farmer.phone.null")
    @Size(min = 10, max = 10, message = "farmer.phone.incorrect.size")
    private String phone;

}
