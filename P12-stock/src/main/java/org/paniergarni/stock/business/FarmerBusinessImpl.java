package org.paniergarni.stock.business;

import org.modelmapper.ModelMapper;
import org.paniergarni.stock.dao.FarmerRepository;
import org.paniergarni.stock.entities.Farmer;
import org.paniergarni.stock.entities.FarmerDTO;
import org.paniergarni.stock.exception.FarmerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FarmerBusinessImpl implements FarmerBusiness{

    @Autowired
    private FarmerRepository farmerRepository;
    @Autowired
    private ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(FarmerBusinessImpl.class);

    @Override
    public Farmer createFarmer(FarmerDTO farmerDTO) throws FarmerException {
        Farmer farmer = modelMapper.map(farmerDTO, Farmer.class);
        checkNameExist(farmer.getName());
        farmer = farmerRepository.save(farmer);
        logger.info("Create farmer " + farmer.getId());
        return farmer;
    }

    @Override
    public Farmer updateFarmer(Long id, FarmerDTO farmerDTO) throws FarmerException {
        Farmer farmerCompare = getFarmer(id);
        Farmer farmer = modelMapper.map(farmerDTO, Farmer.class);
        if (!farmerCompare.getName().equals(farmer.getName()))
            checkNameExist(farmer.getName());
        farmer.setId(id);
        logger.info("Update farmer " + farmer.getId());
        return farmerRepository.save(farmer);
    }

    @Override
    public void deleteFarmer(Long id) throws FarmerException {
        Farmer farmer = getFarmer(id);
        farmerRepository.delete(farmer);
        logger.info("Delete category " + farmer.getId());
    }

    @Override
    public Farmer getFarmer(Long id) throws FarmerException {
        return farmerRepository.findById(id).orElseThrow(() -> new FarmerException("farmer.id.incorrect"));
    }

    @Override
    public List<Farmer> getFarmers() {
        return farmerRepository.findAll();
    }

    private void checkNameExist(String name) throws FarmerException {
        if (farmerRepository.findByName(name).isPresent()) {
            throw new FarmerException("farmer.name.already.exist");
        }
    }
}
