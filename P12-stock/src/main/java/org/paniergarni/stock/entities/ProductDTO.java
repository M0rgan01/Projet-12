package org.paniergarni.stock.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long id;
    @NotBlank(message = "product.name.null")
    @Size(min = 4, max = 50, message = "product.name.incorrect.size")
    private String name;

    @Size(max = 500, message = "product.description.incorrect.size")
    private String description;
    private boolean available;

    @NotNull(message = "product.quantity.null")
    @Min(value = 0, message = "product.quantity.incorrect")
    private int quantity;

    @NotNull(message = "product.capacity.null")
    @Min(value = 0, message = "product.capacity.incorrect.min.value")
    private double capacity;

    @NotNull(message = "product.price.null")
    @Min(value = 0, message = "product.price.incorrect.min.value")
    private double price;

    private boolean promotion;
    private double promotionPrice;
    private String photo;

    @NotNull(message = "product.category.null")
    private Category category;
    @NotNull(message = "product.farmer.null")
    private Farmer farmer;
    @NotNull(message = "product.measure.null")
    private Measure measure;
}
