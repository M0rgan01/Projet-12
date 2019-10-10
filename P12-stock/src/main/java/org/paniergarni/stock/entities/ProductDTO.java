package org.paniergarni.stock.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Represente les données permettant la création / modification d'un produit")
public class ProductDTO {

    @ApiModelProperty(notes = "ID du produit, requis pour la modification", example = "1")
    private Long id;

    @ApiModelProperty(notes = "Nom du produit, entre 4 et 50 caractères", example = "Patate", required = true)
    @NotNull(message = "product.name.null")
    @Size(min = 4, max = 50, message = "product.name.incorrect.size")
    private String name;

    @ApiModelProperty(notes = "Description du produit, 500 caractères maximum", example = "Une description")
    @Size(max = 500, message = "product.description.incorrect.size")
    private String description;

    @ApiModelProperty(notes = "Disponibilité du produit", example = "true")
    private boolean available;

    @ApiModelProperty(notes = "Stock du produit", example = "30", required = true)
    @NotNull(message = "product.quantity.null")
    @Min(value = 0, message = "product.quantity.incorrect")
    private int quantity;

    @ApiModelProperty(notes = "Capacité du produit", example = "300.02", required = true)
    @NotNull(message = "product.capacity.null")
    @Min(value = 0, message = "product.capacity.incorrect.min.value")
    private double capacity;

    @ApiModelProperty(notes = "Prix du produit", example = "300.99", required = true)
    @NotNull(message = "product.price.null")
    @Min(value = 0, message = "product.price.incorrect.min.value")
    private double price;

    @ApiModelProperty(notes = "Promotion du produit")
    private boolean promotion;

    @ApiModelProperty(notes = "Prix du produit avant promotion", example = "200.50")
    @Min(value = 0, message = "product.oldPrice.incorrect.min.value")
    private double oldPrice;

    @ApiModelProperty(notes = "Categorie du produit")
    @NotNull(message = "product.category.null")
    private Category category;
    @ApiModelProperty(notes = "Agriculteur lié au produit")
    @NotNull(message = "product.farmer.null")
    private Farmer farmer;
    @ApiModelProperty(notes = "Contenance du produit")
    @NotNull(message = "product.measure.null")
    private Measure measure;
}
