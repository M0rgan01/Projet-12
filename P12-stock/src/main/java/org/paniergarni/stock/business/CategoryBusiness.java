package org.paniergarni.stock.business;

import org.paniergarni.stock.entities.Category;
import org.paniergarni.stock.exception.StockException;

import java.util.List;

public interface CategoryBusiness {

    Category createCategory(Category category) throws StockException;
    Category getCategory(Long id) throws StockException;
    List<Category> getCategories();
}
