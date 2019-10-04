package org.paniergarni.stock.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Represente les données permettant la création / modification d'une catégorie")
public class CategoryDTO {
    @ApiModelProperty(notes = "Nom de la catégorie, de 4 à 50 caractères", example = "Légumes")
    @NotBlank
    @Size(min = 4, max = 50, message = "category.name.incorrect.size")
    private String name;

}
