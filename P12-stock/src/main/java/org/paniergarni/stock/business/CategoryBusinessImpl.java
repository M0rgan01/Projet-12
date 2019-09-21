package org.paniergarni.stock.business;

import org.paniergarni.stock.dao.CategoryRepository;
import org.paniergarni.stock.entities.Category;
import org.paniergarni.stock.exception.StockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryBusinessImpl implements CategoryBusiness {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category) throws StockException {

        if (categoryRepository.findByName(category.getName()).isPresent())
            throw new StockException("category.name.already.exist");

        return categoryRepository.save(category);
    }

    @Override
    public Category getCategory(Long id) throws StockException {
        return categoryRepository.findById(id).orElseThrow(() -> new StockException("category.id.incorrect"));
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
