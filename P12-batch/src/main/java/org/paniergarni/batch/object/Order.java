package org.paniergarni.batch.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private String reference;
    private double totalPrice;
    private Date date;
    private Date reception;
    private Boolean paid;
    private Boolean cancel;
    private Long userId;
}
