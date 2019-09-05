package org.paniergarni.stock.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

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
    private boolean available;
    private int quantity;
    private double price;
    private boolean promotion;
    private double promotionPrice;
    @ManyToOne
    private Category category;
    @ManyToOne
    private Farmer farmer;
}
