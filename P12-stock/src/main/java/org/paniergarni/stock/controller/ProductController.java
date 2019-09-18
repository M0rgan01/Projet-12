package org.paniergarni.stock.controller;

import org.paniergarni.stock.business.ProductBusiness;
import org.paniergarni.stock.entities.Measure;
import org.paniergarni.stock.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ProductController {

    @Autowired
    private ProductBusiness productBusiness;

    @GetMapping(value = "/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable(name = "id") Long id) {

        Product product = productBusiness.getProduct(id);

        return ResponseEntity.ok().body(product);
    }

    @GetMapping(value = "/product/measures")
    public ResponseEntity<?> getMeasure() {
        return ResponseEntity.ok().body(Measure.getListMeasure());
    }

    @GetMapping(value = "/productsByCategoryId/{id}/{page}/{size}")
    public ResponseEntity<?> getPageProductByCategoryId(@PathVariable(name = "id") Long id, @PathVariable(name = "page") int page, @PathVariable(name = "size") int size) {

        Page<Product> products = productBusiness.getPageProductsByCategory(page, size, id);

        return ResponseEntity.ok().body(products);
    }

    @GetMapping(value = "/productsByPromotion/{page}/{size}")
    public ResponseEntity<?> getPageProductByCategoryId(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size) {

        Page<Product> products = productBusiness.getPageProductsByPromotion(page, size);

        return ResponseEntity.ok().body(products);
    }

    @GetMapping(value = "/productsByNamesContains/{name}/{page}/{size}")
    public ResponseEntity<?> getPageProductByCategoryId(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size, @PathVariable(name = "name") String name) {

        Page<Product> products = productBusiness.getPageProductsByName(page, size, name);

        return ResponseEntity.ok().body(products);
    }

    @PostMapping(value = "/product")
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product) {

        product = productBusiness.createProduct(product);

        return ResponseEntity.ok().body(product);
    }

    @PutMapping(value = "/productQuantity/{quantity}/{id}")
    public ResponseEntity<?> updateProductQuantity(@PathVariable int quantity, @PathVariable Long id) {

       Product product = productBusiness.updateProductQuantity(quantity, id);

        return ResponseEntity.ok().body(product);
    }

}
