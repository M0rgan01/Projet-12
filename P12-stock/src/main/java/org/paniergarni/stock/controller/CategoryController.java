package org.paniergarni.stock.controller;

import org.paniergarni.stock.business.CategoryBusiness;
import org.paniergarni.stock.entities.Category;
import org.paniergarni.stock.exception.StockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryBusiness categoryBusiness;

    @GetMapping(value = "/public/category/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable(name = "id") Long id) throws StockException {

        Category category = categoryBusiness.getCategory(id);

        return ResponseEntity.ok().body(category);
    }

    @GetMapping(value = "/public/categories")
    public ResponseEntity<?> getCategories() {

        List<Category> categories = categoryBusiness.getCategories();

        return ResponseEntity.ok().body(categories);
    }

    @PostMapping(value = "/adminRole/category")
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category) throws StockException {


        category = categoryBusiness.createCategory(category);

        return ResponseEntity.ok().body(category);
    }


}
