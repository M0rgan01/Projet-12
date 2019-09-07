package org.paniergarni.stock.business;

import org.paniergarni.stock.entities.Product;
import org.springframework.data.domain.Page;

public interface ProductBusiness {

    Product createProduct(Product product);
    Product updateProduct(Product product);
    Product getProduct(Long id);
    Page<Product> getPageProductsByName(int page, int size, String name);
    Page<Product> getPageProductsByPromotion(int page, int size);
    Page<Product> getPageProductsByCategory(int page, int size, Long categoryId);
}
