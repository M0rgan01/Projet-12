package org.paniergarni.stock.business;

import org.paniergarni.stock.entities.Farmer;

import java.util.List;

public interface FarmerBusiness {

    Farmer createFarmer(Farmer farmer);
    Farmer updateFarmer(Farmer farmer);
    Farmer getFarmer(Long id);
    List<Farmer> getFarmers();
}
