package org.paniergarni.stock.business;

import org.paniergarni.stock.dao.CategoryRepository;
import org.paniergarni.stock.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryBusinessImpl implements CategoryBusiness {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category) {

        categoryRepository.findByName(category.getName()).ifPresent(category1 -> {
         throw new IllegalArgumentException("Category name " + category1.getName() + " already exist");
        });
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID " + id + " Incorrect"));
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
