package org.paniergarni.stock.controller;

import org.paniergarni.stock.business.FarmerBusiness;
import org.paniergarni.stock.entities.Farmer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FarmerController {


    @Autowired
    private FarmerBusiness farmerBusiness;


    @GetMapping(value = "/farmer/{id}")
    public ResponseEntity<?> getFarmerById(@PathVariable(name = "id") Long id) {

        Farmer farmer = farmerBusiness.getFarmer(id);

        return ResponseEntity.ok().body(farmer);
    }

    @GetMapping(value = "/farmers")
    public ResponseEntity<?> getFarmers() {

        List<Farmer> farmers = farmerBusiness.getFarmers();

        return ResponseEntity.ok().body(farmers);
    }

    @PostMapping(value = "/farmer")
    public ResponseEntity<?> createFarmer(@Valid @RequestBody Farmer farmer) {

        farmer = farmerBusiness.createFarmer(farmer);

        return ResponseEntity.ok().body(farmer);
    }
}
