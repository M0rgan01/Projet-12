package org.paniergarni.stock.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Measure {

    Kilos,
    Gramme,
    Litre,
    Centilitre,
    Unite;

    public static List<Measure> getListMeasure(){
        return new ArrayList<>(Arrays.asList(Kilos, Gramme, Litre, Centilitre, Unite));
    }

}
