package org.paniergarni.stock.business;

import org.paniergarni.stock.dao.ProductRepository;
import org.paniergarni.stock.entities.Product;
import org.paniergarni.stock.exception.StockException;
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

        if (productRepository.findByName(product.getName()).isPresent())
            throw new StockException("product.name.already.exist");
        if (product.getQuantity() == 0)
            product.setAvailable(false);

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new StockException("product.id.incorrect"));
    }

    @Override
    public Page<Product> getPageProductsByName(int page, int size, String name) {
        return productRepository.findByNameContains(name, PageRequest.of(page, size));
    }

    @Override
    public Page<Product> getPageProductsByPromotion(int page, int size) {
        return productRepository.findByPromotionIsTrue(PageRequest.of(page, size));
    }

    @Override
    public Page<Product> getPageProductsByCategory(int page, int size, Long categoryId) {
        return productRepository.findByCategoryId(categoryId, PageRequest.of(page, size));
    }

    @Override
    public Product updateProductQuantity(int quantity, Long id) {
        Product product = getProduct(id);
        if (product.isAvailable()) {

            product.setQuantity(product.getQuantity() - quantity);
            if (product.getQuantity() < 0) {
                product.setOrderProductRealQuantity(Math.abs(product.getQuantity()));
                product.setQuantity(0);
                product.setAvailable(false);
            }else if(product.getQuantity() == 0){
                product.setOrderProductRealQuantity(quantity);
                product.setAvailable(false);
            }else {
                product.setOrderProductRealQuantity(quantity);
            }
            productRepository.save(product);
            return product;
        }else {
            throw new StockException("product.not.available");
        }
    }
}
