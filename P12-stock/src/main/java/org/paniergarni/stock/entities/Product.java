package org.paniergarni.stock.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
    @Size(min = 4, max = 50)
    private String name;
    @Size(max = 500)
    private String description;
    private boolean available;
    private int quantity;
    private double capacity;
    private double price;
    private boolean promotion;
    private double promotionPrice;
    private String photo;

    @ManyToOne
    private Category category;
    @ManyToOne
    private Farmer farmer;
    @Enumerated(EnumType.ORDINAL)
    private Measure measure;
}
