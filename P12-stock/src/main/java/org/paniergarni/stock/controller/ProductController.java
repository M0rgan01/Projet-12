package org.paniergarni.stock.controller;

import org.paniergarni.stock.business.ProductBusiness;
import org.paniergarni.stock.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ProductController {

    @Autowired
    private ProductBusiness productBusiness;

    @GetMapping(value = "/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable(name = "id") Long id){

        Product product;

        try {
            product = productBusiness.getProduct(id);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok().body(product);
    }

    @GetMapping(value = "/productsByCategoryId/{id}/{page}/{size}")
    public ResponseEntity<?> getPageProductByCategoryId(@PathVariable(name = "id") Long id,@PathVariable(name = "page") int page,@PathVariable(name = "size") int size){

        Page<Product> products;

        try {
            products = productBusiness.getPageProductsByCategory(page, size, id);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok().body(products);
    }

    @GetMapping(value = "/productsByPromotion/{page}/{size}")
    public ResponseEntity<?> getPageProductByCategoryId(@PathVariable(name = "page") int page,@PathVariable(name = "size") int size){

        Page<Product> products;

        try {
            products = productBusiness.getPageProductsByPromotion(page, size);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok().body(products);
    }

    @GetMapping(value = "/productsByNamesContains/{name}/{page}/{size}")
    public ResponseEntity<?> getPageProductByCategoryId(@PathVariable(name = "page") int page,@PathVariable(name = "size") int size, @PathVariable(name = "name") String name){

        Page<Product> products;

        try {
            products = productBusiness.getPageProductsByName(page, size, name);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok().body(products);
    }

    @PostMapping(value = "/product")
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product){

        try {
            product = productBusiness.createProduct(product);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok().body(product);
    }
}
