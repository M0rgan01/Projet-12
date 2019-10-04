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
    private String name;
    private String description;
    private boolean available;
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
