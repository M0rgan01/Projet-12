package org.paniergarni.stock.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.paniergarni.stock.business.CategoryBusiness;
import org.paniergarni.stock.entities.Category;
import org.paniergarni.stock.entities.CategoryDTO;
import org.paniergarni.stock.exception.CategoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@Api( description="API de gestion des categories")
@RestController
public class CategoryController {

    @Autowired
    private CategoryBusiness categoryBusiness;

    @ApiOperation(value = "Récupère une catégorie selon son id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 409, message = "Aucune correspondance"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/public/category/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable(name = "id") Long id) throws CategoryException {
        Category category = categoryBusiness.getCategory(id);
        return ResponseEntity.ok().body(category);
    }

    @ApiOperation(value = "Récupère la liste de toutes les catégories")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la récupération"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @GetMapping(value = "/public/categories")
    public ResponseEntity<?> getCategories() {
        List<Category> categories = categoryBusiness.getCategories();
        return ResponseEntity.ok().body(categories);
    }

    @ApiOperation(value = "Création d'une catégorie")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la création"),
            @ApiResponse(code = 409, message = "Object non conforme, nom déjà utilisé"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PostMapping(value = "/adminRole/category")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) throws CategoryException {
        Category category = categoryBusiness.createCategory(categoryDTO);
        return ResponseEntity.ok().body(category);
    }

    @ApiOperation(value = "Modification d'une catégorie")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la modification"),
            @ApiResponse(code = 409, message = "Aucune correspondance, object non conforme, nom déjà utilisé"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @PutMapping(value = "/adminRole/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable(name = "id") Long id, @Valid @RequestBody CategoryDTO categoryDTO) throws CategoryException {
        Category category = categoryBusiness.updateCategory(id, categoryDTO);
        return ResponseEntity.ok().body(category);
    }

    @ApiOperation(value = "Supression d'une catégorie")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la création"),
            @ApiResponse(code = 409, message = "Aucune correspondance"),
            @ApiResponse(code = 500, message = "Erreur interne")
    })
    @DeleteMapping(value = "/adminRole/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable(name = "id") Long id) throws CategoryException {
        categoryBusiness.deleteCategory(id);
        return ResponseEntity.ok().body(null);
    }
}
