package org.paniergarni.stock.business;

import org.paniergarni.stock.dao.ProductRepository;
import org.paniergarni.stock.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class ProductBusinessImpl implements ProductBusiness {

    @Autowired
    private ProductRepository productRepository;


    @Override
    public Product createProduct(Product product) {

        productRepository.findByName(product.getName()).ifPresent(product1 -> {
            throw new IllegalArgumentException("Category name " + product1.getName() + " already exist");
        });

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID " + id + " Incorrect"));
    }

    @Override
    public Page<Product> getPageProductsByName(int page, int size, String name) {
        return productRepository.findByNameContains(name, PageRequest.of(page, size));
    }

    @Override
    public Page<Product> getPageProductsByPromotion(int page, int size) {
        return productRepository.findByPromotion(PageRequest.of(page, size));
    }

    @Override
    public Page<Product> getPageProductsByCategory(int page, int size, Long categoryId) {
        return productRepository.findByCategoryId(categoryId, PageRequest.of(page, size));
    }

}
