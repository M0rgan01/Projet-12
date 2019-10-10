package org.paniergarni.stock.business;

import org.modelmapper.ModelMapper;
import org.paniergarni.stock.dao.CategoryRepository;
import org.paniergarni.stock.entities.Category;
import org.paniergarni.stock.entities.CategoryDTO;
import org.paniergarni.stock.exception.CategoryException;
import org.paniergarni.stock.exception.StockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryBusinessImpl implements CategoryBusiness {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(CategoryBusinessImpl.class);

    @Override
    public Category createCategory(CategoryDTO categoryDTO) throws CategoryException {
        Category category = modelMapper.map(categoryDTO, Category.class);
        checkNameExist(category.getName());
        category = categoryRepository.save(category);
        logger.info("Create category " + category.getId());
        return category;
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDTO) throws CategoryException {
        Category categoryCompare = getCategory(id);
        Category category = modelMapper.map(categoryDTO, Category.class);
        if (!categoryCompare.getName().equals(category.getName()))
            checkNameExist(category.getName());
        category.setId(id);
        logger.info("Update category " + category.getId());
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) throws CategoryException {
        Category category = getCategory(id);
        categoryRepository.delete(category);
        logger.info("Delete category " + category.getId());
    }

    @Override
    public Category getCategory(Long id) throws CategoryException {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryException("category.id.incorrect"));
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    private void checkNameExist(String name) throws CategoryException {
        if (categoryRepository.findByName(name).isPresent()) {
            throw new CategoryException("category.name.already.exist");
        }
    }
}
