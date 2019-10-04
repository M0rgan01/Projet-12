package org.paniergarni.stock.business;

import org.paniergarni.stock.entities.Category;
import org.paniergarni.stock.entities.CategoryDTO;
import org.paniergarni.stock.exception.StockException;

import java.util.List;

public interface CategoryBusiness {

    Category createCategory(CategoryDTO category) throws StockException;
    Category updateCategory(Long id, CategoryDTO category) throws StockException;
    void deleteCategory(Long id) throws StockException;
    Category getCategory(Long id) throws StockException;
    List<Category> getCategories();
}
