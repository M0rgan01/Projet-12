package org.paniergarni.stock.controller;

import org.paniergarni.stock.business.ProductBusiness;
import org.paniergarni.stock.dao.specification.SearchCriteria;
import org.paniergarni.stock.entities.Measure;
import org.paniergarni.stock.entities.Product;
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

@RestController
public class ProductController {

    @Autowired
    private ProductBusiness productBusiness;

    @GetMapping(value = "/public/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable(name = "id") Long id) throws StockException {

        Product product = productBusiness.getProduct(id);

        return ResponseEntity.ok().body(product);
    }

    @GetMapping(value = "/public/product/measures")
    public ResponseEntity<?> getMeasure() {
        return ResponseEntity.ok().body(Measure.getListMeasure());
    }


    @GetMapping(value = "/public/searchProduct/{page}/{size}")
    public ResponseEntity<?> searchProduct(@PathVariable(name = "page") int page,
                                                        @PathVariable(name = "size") int size,
                                                        @RequestBody(required=false) List<SearchCriteria> searchCriteriaList) {

        Page<Product> products = productBusiness.searchProduct(page, size, searchCriteriaList);

        return ResponseEntity.ok().body(products);
    }

    @PostMapping(value = "/adminRole/product")
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product) throws StockException {

        product = productBusiness.createProduct(product);

        return ResponseEntity.ok().body(product);
    }

    @PutMapping(value = "/adminRole/product/{id}")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody Product product, @PathVariable(name = "id") Long id) throws StockException {

        product = productBusiness.updateProduct(id, product);

        return ResponseEntity.ok().body(product);
    }

    @PutMapping(value = "adminRole/productQuantity/{id}/{quantity}/{cancel}")
    public ResponseEntity<?> updateProductQuantity( @PathVariable Long id, @PathVariable int quantity, @PathVariable boolean cancel) throws StockException {

       Product product = productBusiness.updateProductQuantity(quantity, id, cancel);

        return ResponseEntity.ok().body(product);
    }

    @GetMapping(value = "/public/product/{id}/photo", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getPhoto(@PathVariable(name = "id") Long id) throws IOException, StockException {
        Product p = productBusiness.getProduct(id);
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Test/" + p.getPhoto()));
    }

    @PostMapping(value = "/adminRole/product/{id}/photo")
    public void uploadPhotoProduct(MultipartFile file, @PathVariable(name = "id") Long id) throws IOException, StockException {
        Product p = productBusiness.getProduct(id);
        p.setPhoto(id + ".png");
        Files.write(Paths.get(System.getProperty("user.home") + "/Test/" + p.getPhoto()), file.getBytes());
        productBusiness.updateProduct(id, p);
    }

}
