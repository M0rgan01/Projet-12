package org.paniergarni.order.entities;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class WrapperOrderProductDTO {

    @Valid
    private List<OrderProductDTO> orderProducts;
}
