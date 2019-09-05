package org.paniergarni.order.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
public class OrderProduct implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn
    private Order order;

    @Id
    @JoinColumn
    private Long productId;

    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderProduct)) return false;
        OrderProduct that = (OrderProduct) o;
        return Objects.equals(order.getReference(), that.order.getReference()) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order.getReference(), productId, quantity);
    }

}
