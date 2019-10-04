package org.paniergarni.stock.business;

import org.paniergarni.stock.entities.Category;
import org.paniergarni.stock.entities.CategoryDTO;
import org.paniergarni.stock.exception.CategoryException;
import org.paniergarni.stock.exception.StockException;

import java.util.List;

public interface CategoryBusiness {

    Category createCategory(CategoryDTO category) throws CategoryException;
    Category updateCategory(Long id, CategoryDTO category) throws CategoryException;
    void deleteCategory(Long id) throws CategoryException;
    Category getCategory(Long id) throws CategoryException;
    List<Category> getCategories();
}
