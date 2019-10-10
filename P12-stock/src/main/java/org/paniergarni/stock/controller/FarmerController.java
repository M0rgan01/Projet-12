package org.paniergarni.stock.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.paniergarni.stock.business.FarmerBusiness;
import org.paniergarni.stock.entities.Farmer;
import org.paniergarni.stock.entities.FarmerDTO;
import org.paniergarni.stock.exception.FarmerException;
import org.paniergarni.stock.exception.StockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@Api( description="API de gestion des agriculteurs")
@RestController
public class FarmerController {


    @Autowired
    private FarmerBusiness farmerBusiness;

    @ApiOperation(value = "Récupère un agriculteur selon son id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 409, message = "Aucune correspondance"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/public/farmer/{id}")
    public ResponseEntity<?> getFarmerById(@PathVariable(name = "id") Long id) throws FarmerException {
        Farmer farmer = farmerBusiness.getFarmer(id);
        return ResponseEntity.ok().body(farmer);
    }

    @ApiOperation(value = "Récupère la liste de tous les agriculteurs")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 409, message = "Aucune correspondance"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/public/farmers")
    public ResponseEntity<?> getFarmers() {
        List<Farmer> farmers = farmerBusiness.getFarmers();
        return ResponseEntity.ok().body(farmers);
    }

    @ApiOperation(value = "Création d'un agriculteur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la création"),
            @ApiResponse(code = 409, message = "Object non conforme, nom déjà utilisé"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PostMapping(value = "/adminRole/farmer")
    public ResponseEntity<?> createFarmer(@Valid @RequestBody FarmerDTO farmerDTO) throws FarmerException {
        Farmer farmer = farmerBusiness.createFarmer(farmerDTO);
        return ResponseEntity.ok().body(farmer);
    }

    @ApiOperation(value = "Modification d'un agriculteur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la modification"),
            @ApiResponse(code = 409, message = "Aucune correspondance, object non conforme, nom déjà utilisé"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PutMapping(value = "/adminRole/farmer/{id}")
    public ResponseEntity<?> updateFarmer(@PathVariable(name = "id") Long id, @Valid @RequestBody FarmerDTO farmerDTO) throws FarmerException {
        Farmer farmer = farmerBusiness.updateFarmer(id, farmerDTO);
        return ResponseEntity.ok().body(farmer);
    }

    @ApiOperation(value = "Supression d'un agriculteur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la supression"),
            @ApiResponse(code = 409, message = "Aucune correspondance"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @DeleteMapping(value = "/adminRole/farmer/{id}")
    public ResponseEntity<?> deleteFarmer(@PathVariable(name = "id") Long id) throws FarmerException {
        farmerBusiness.deleteFarmer(id);
        return ResponseEntity.ok().body(null);
    }
}
