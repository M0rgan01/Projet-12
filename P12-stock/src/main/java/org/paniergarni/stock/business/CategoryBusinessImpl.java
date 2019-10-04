package org.paniergarni.stock.business;

import org.modelmapper.ModelMapper;
import org.paniergarni.stock.dao.CategoryRepository;
import org.paniergarni.stock.entities.Category;
import org.paniergarni.stock.entities.CategoryDTO;
import org.paniergarni.stock.exception.StockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryBusinessImpl implements CategoryBusiness {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Category createCategory(CategoryDTO categoryDTO) throws StockException {
        Category category = modelMapper.map(categoryDTO, Category.class);
        checkNameExist(category.getName());
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDTO) throws StockException {
        Category categoryCompare = getCategory(id);
        Category category = modelMapper.map(categoryDTO, Category.class);
        if (categoryCompare.getName().equals(category.getName()))
            checkNameExist(category.getName());
        category.setId(id);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) throws StockException {
        Category category = getCategory(id);
        categoryRepository.delete(category);
    }

    @Override
    public Category getCategory(Long id) throws StockException {
        return categoryRepository.findById(id).orElseThrow(() -> new StockException("category.id.incorrect"));
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    private void checkNameExist(String name) throws StockException {
        if (categoryRepository.findByName(name).isPresent()) {
            throw new StockException("category.name.already.exist");
        }
    }
}
