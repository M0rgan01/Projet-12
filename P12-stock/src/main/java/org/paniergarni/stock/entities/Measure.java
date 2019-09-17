package org.paniergarni.stock.entities;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum Measure {

    KILOS ("Kilos"),
    KILOSGRAMME ("Kilos-gramme"),
    LITRE ("Litre"),
    CENTILITRE ("Centilitre"),
    UNITE ("Unité");

    private String name;

}
