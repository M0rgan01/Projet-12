package org.paniergarni.stock.business;

import org.paniergarni.stock.entities.Farmer;
import org.paniergarni.stock.entities.FarmerDTO;
import org.paniergarni.stock.exception.FarmerException;
import org.paniergarni.stock.exception.StockException;

import java.util.List;

public interface FarmerBusiness {

    Farmer createFarmer(FarmerDTO farmerDTO) throws FarmerException;
    Farmer updateFarmer(Long id, FarmerDTO farmerDTO) throws FarmerException;
    void deleteFarmer(Long id)throws FarmerException;
    Farmer getFarmer(Long id) throws FarmerException;
    List<Farmer> getFarmers();
}
