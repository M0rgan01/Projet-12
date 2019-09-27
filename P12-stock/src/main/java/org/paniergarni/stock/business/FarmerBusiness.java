package org.paniergarni.stock.business;

import org.paniergarni.stock.entities.Farmer;
import org.paniergarni.stock.exception.StockException;

import java.util.List;

public interface FarmerBusiness {

    Farmer createFarmer(Farmer farmer) throws StockException;
    Farmer updateFarmer(Farmer farmer);
    Farmer getFarmer(Long id) throws StockException;
    List<Farmer> getFarmers();
}
