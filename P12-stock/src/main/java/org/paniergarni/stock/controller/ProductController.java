package org.paniergarni.stock.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.paniergarni.stock.business.ProductBusiness;
import org.paniergarni.stock.dao.specification.SearchCriteria;
import org.paniergarni.stock.entities.Measure;
import org.paniergarni.stock.entities.Product;
import org.paniergarni.stock.entities.ProductDTO;
import org.paniergarni.stock.exception.ProductException;
import org.paniergarni.stock.exception.StockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
@Api( description="API de gestion des produits")
@RestController
public class ProductController {

    @Autowired
    private ProductBusiness productBusiness;

    @ApiOperation(value = "Récupère un produit selon son id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 409, message = "Aucune correspondance"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/public/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable(name = "id") Long id) throws ProductException {
        Product product = productBusiness.getProduct(id);
        return ResponseEntity.ok().body(product);
    }

    @ApiOperation(value = "Récupère la liste des unitées de mesure")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/public/product/measures")
    public ResponseEntity<?> getMeasure() {
        return ResponseEntity.ok().body(Measure.getListMeasure());
    }

    @ApiOperation(value = "Recherche global des produits via le numéro de la page, la taille et une liste de critère")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 406, message = "Critère incorrect"),
            @ApiResponse(code = 412, message = "Erreur du JSON"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PostMapping(value = "/public/searchProduct/{page}/{size}")
    public ResponseEntity<?> searchProduct(@PathVariable(name = "page") int page,
                                           @PathVariable(name = "size") int size,
                                           @RequestBody(required=false) List<SearchCriteria> searchCriteriaList) {

        Page<Product> products = productBusiness.searchProduct(page, size, searchCriteriaList);

        return ResponseEntity.ok().body(products);
    }

    @ApiOperation(value = "Création d'un produit")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la création"),
            @ApiResponse(code = 409, message = "Object non conforme"),
            @ApiResponse(code = 412, message = "Erreur du JSON"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PostMapping(value = "/adminRole/product")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO) throws ProductException, IOException {
        Product product = productBusiness.createProduct(productDTO);
        return ResponseEntity.ok().body(product);
    }

    @ApiOperation(value = "Modification d'un produit")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la modification"),
            @ApiResponse(code = 409, message = "Object non conforme"),
            @ApiResponse(code = 412, message = "Erreur du JSON"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PutMapping(value = "/adminRole/product/{id}")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable(name = "id") Long id) throws ProductException, IOException {
        Product product = productBusiness.updateProduct(id, productDTO);
        return ResponseEntity.ok().body(product);
    }

    @ApiOperation(value = "Modification du stock d'un produit, pour création et annulation d'une commande")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la modification"),
            @ApiResponse(code = 409, message = "Aucune correspondance, produit non disponible"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PutMapping(value = "/adminRole/productQuantity/{id}/{quantity}/{cancel}")
    public ResponseEntity<?> updateProductQuantity( @PathVariable Long id, @PathVariable int quantity, @PathVariable boolean cancel) throws ProductException {
       Product product = productBusiness.updateProductQuantity(quantity, id, cancel);
        return ResponseEntity.ok().body(product);
    }

    @ApiOperation(value = "Récupération de la photo d'un produit")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 409, message = "Aucune correspondance"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/public/product/{id}/photo", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getPhoto(@PathVariable(name = "id") Long id) throws IOException, ProductException {
        Product p = productBusiness.getProduct(id);
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Test/" + p.getPhoto()));
    }

    @ApiOperation(value = "Changement de la photo d'un produit")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la modification"),
            @ApiResponse(code = 409, message = "Aucune correspondance"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PostMapping(value = "/adminRole/product/{id}/photo")
    public  ResponseEntity<?> setPhoto(MultipartFile file, @PathVariable(name = "id") Long id) throws IOException, ProductException {
        productBusiness.setProductPhoto(id, file);
        return ResponseEntity.ok().body(null);
    }

}
