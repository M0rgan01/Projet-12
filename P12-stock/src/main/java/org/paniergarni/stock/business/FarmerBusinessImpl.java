package org.paniergarni.stock.business;

import org.paniergarni.stock.dao.FarmerRepository;
import org.paniergarni.stock.entities.Farmer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FarmerBusinessImpl implements FarmerBusiness{

    @Autowired
    private FarmerRepository farmerRepository;

    @Override
    public Farmer createFarmer(Farmer farmer) {
        farmerRepository.findByName(farmer.getName()).ifPresent(farmer1 -> {
            throw new IllegalArgumentException("Farmer name " + farmer1.getName() + " already exist");
        });
        return farmerRepository.save(farmer);
    }

    @Override
    public Farmer updateFarmer(Farmer farmer) {
        return farmerRepository.save(farmer);
    }

    @Override
    public Farmer getFarmer(Long id) {
        return farmerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID " + id + " Incorrect"));
    }

    @Override
    public List<Farmer> getFarmers() {
        return farmerRepository.findAll();
    }
}
