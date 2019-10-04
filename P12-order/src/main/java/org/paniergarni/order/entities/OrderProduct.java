package org.paniergarni.order.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.paniergarni.order.object.Product;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "ID de l'entité")
    private Long id;
    private Long productId;
    @JsonIgnore
    @ManyToOne
    private Order order;
    private int orderQuantity;
    private int realQuantity;
    private double totalPriceRow;
    @Transient
    private Product product;
}
