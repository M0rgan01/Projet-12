package org.paniergarni.stock.business;

import org.paniergarni.stock.dao.FarmerRepository;
import org.paniergarni.stock.entities.Farmer;
import org.paniergarni.stock.exception.StockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FarmerBusinessImpl implements FarmerBusiness{

    @Autowired
    private FarmerRepository farmerRepository;

    @Override
    public Farmer createFarmer(Farmer farmer) throws StockException {

        if (farmerRepository.findByName(farmer.getName()).isPresent())
            throw new StockException("farmer.name.already.exist");

        return farmerRepository.save(farmer);
    }

    @Override
    public Farmer updateFarmer(Farmer farmer) {
        return farmerRepository.save(farmer);
    }

    @Override
    public Farmer getFarmer(Long id) throws StockException {
        return farmerRepository.findById(id).orElseThrow(() -> new StockException("farmer.id.incorrect"));
    }

    @Override
    public List<Farmer> getFarmers() {
        return farmerRepository.findAll();
    }
}
