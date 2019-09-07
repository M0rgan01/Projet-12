package org.paniergarni.stock.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
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
    @NotNull
    @Size(min = 4)
    private String name;
    private boolean available;
    private int quantity;
    private double price;
    private boolean promotion;
    private double promotionPrice;
    private String photo;

    @ManyToOne
    private Category category;
    @ManyToOne
    private Farmer farmer;
}
