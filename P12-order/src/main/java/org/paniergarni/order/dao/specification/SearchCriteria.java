package org.paniergarni.order.dao.specification;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel(description = "Represente les données permettant la recherche précise d'une commande")
public class SearchCriteria {
    @ApiModelProperty(notes = "clé de l'attribut", example = "quantity", required = true)
    private String key;
    @ApiModelProperty(notes = "Opération à éffectué, égalité -> :, supérieur ou égal -> >=, supérieur -> >, inférieur ou égal -> <=," +
            " inférieur -> <, ordonné croissant -> ORDER_BY_ASC, ordonné décroissant -> ORDER_BY_DESC", example = ":", required = true)
    private String operation;
    @ApiModelProperty(notes = "Valeur de l'attribut", example = "12", required = true)
    private Object value;
}
