package org.paniergarni.stock.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 4, max = 50, message = "product.name.incorrect.size")
    private String name;
    @Size(max = 500, message = "product.description.incorrect.size")
    private String description;
    private boolean available;
    @Min(value = 0, message = "product.quantity.incorrect")
    private int quantity;
    @Transient
    private int orderProductRealQuantity;
    private double capacity;
    private double price;
    private boolean promotion;
    private double promotionPrice;
    private String photo;
    @Valid
    @ManyToOne
    private Category category;
    @Valid
    @ManyToOne
    private Farmer farmer;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Measure measure;

}
