package org.paniergarni.stock.business;

import org.paniergarni.stock.entities.Category;

import java.util.List;

public interface CategoryBusiness {

    Category createCategory(Category category);
    Category getCategory(Long id);
    List<Category> getCategories();
}
