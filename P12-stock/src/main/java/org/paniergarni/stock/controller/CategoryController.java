package org.paniergarni.stock.controller;

import org.paniergarni.stock.business.CategoryBusiness;
import org.paniergarni.stock.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryBusiness categoryBusiness;

    @GetMapping(value = "/category/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable(name = "id") Long id) {

        Category category;

        try {
            category = categoryBusiness.getCategory(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok().body(category);
    }

    @GetMapping(value = "/categories")
    public ResponseEntity<?> getCategories() {

        List<Category> categories;

        try {
            categories = categoryBusiness.getCategories();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok().body(categories);
    }

    @PostMapping(value = "/category")
    public  ResponseEntity<?> createCategory( @Valid @RequestBody Category category){

        try {
            category = categoryBusiness.createCategory(category);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok().body(category);
    }



}
