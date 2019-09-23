package org.paniergarni.stock.controller;

import org.paniergarni.stock.business.FarmerBusiness;
import org.paniergarni.stock.entities.Farmer;
import org.paniergarni.stock.exception.StockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FarmerController {


    @Autowired
    private FarmerBusiness farmerBusiness;


    @GetMapping(value = "/public/farmer/{id}")
    public ResponseEntity<?> getFarmerById(@PathVariable(name = "id") Long id) throws StockException {

        Farmer farmer = farmerBusiness.getFarmer(id);

        return ResponseEntity.ok().body(farmer);
    }

    @GetMapping(value = "/public/farmers")
    public ResponseEntity<?> getFarmers() {

        List<Farmer> farmers = farmerBusiness.getFarmers();

        return ResponseEntity.ok().body(farmers);
    }

    @PostMapping(value = "/adminRole/farmer")
    public ResponseEntity<?> createFarmer(@Valid @RequestBody Farmer farmer) throws StockException {

        farmer = farmerBusiness.createFarmer(farmer);

        return ResponseEntity.ok().body(farmer);
    }
}
