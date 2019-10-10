package org.paniergarni.order.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
    private boolean available;
    private int quantity;
    private double price;
    private boolean promotion;
    private double oldPrice;
    private int orderProductRealQuantity;
}
