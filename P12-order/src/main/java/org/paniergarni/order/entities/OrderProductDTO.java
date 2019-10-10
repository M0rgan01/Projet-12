package org.paniergarni.order.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Représente les données permettant la création d'une commande")
public class OrderProductDTO {

    @NotNull(message = "order.product.quantity.null")
    @Min(value = 1, message = "order.product.quantity.min.error")
    @Max(value = 99, message = "order.product.quantity.max.error")
    @ApiModelProperty(notes = "Quantité demandée pour la commande", example = "1", required = true)
    private int orderQuantity;
    @ApiModelProperty(notes = "ID du produit concerné", example = "1", required = true)
    @NotNull(message = "order.product.product.id.null")
    private Long productId;
}
