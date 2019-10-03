package org.paniergarni.stock.business;

import org.paniergarni.stock.dao.ProductRepository;
import org.paniergarni.stock.dao.specification.ProductSpecificationBuilder;
import org.paniergarni.stock.dao.specification.SearchCriteria;
import org.paniergarni.stock.entities.Product;
import org.paniergarni.stock.exception.CriteriaException;
import org.paniergarni.stock.exception.StockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductBusinessImpl implements ProductBusiness {

    @Autowired
    private ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductBusinessImpl.class);

    @Override
    public Product createProduct(Product product) {

        if (product.getQuantity() == 0)
            product.setAvailable(false);

         product = productRepository.save(product);
        logger.info("create product with id " + product.getId());
        return product;
    }

    @Override
    public Product updateProduct(Long id, Product product) throws StockException {
        Product product1 = getProduct(id);
        product1 = product;
        logger.info("Update product with id " + product1.getId());
        return productRepository.save(product1);
    }

    @Override
    public Product getProduct(Long id) throws StockException {
        return productRepository.findById(id).orElseThrow(() -> new StockException("product.id.incorrect"));
    }

    @Override
    public Page<Product> searchProduct(int page, int size, List<SearchCriteria> searchCriteriaList) throws CriteriaException {
        ProductSpecificationBuilder builder = new ProductSpecificationBuilder(searchCriteriaList);
        logger.debug("Searching products with list of criteria of " + searchCriteriaList.size() + "elements");
        Specification<Product> spec = builder.build();
        return productRepository.findAll(spec, PageRequest.of(page, size));
    }


    @Override
    public Product updateProductQuantity(int quantity, Long id, boolean cancel) throws StockException {
        Product product = getProduct(id);
        if (!cancel) {
            if (product.isAvailable()) {

                if (product.getQuantity() < quantity)
                    quantity = product.getQuantity();

                product.setQuantity(product.getQuantity() - quantity);

                if (product.getQuantity() == 0) {
                    product.setOrderProductRealQuantity(quantity);
                    product.setAvailable(false);
                } else {
                    product.setOrderProductRealQuantity(quantity);
                }
                logger.debug("Create order --> update product quantity for product ID : " + id);
                return productRepository.save(product);
            } else {
                throw new StockException("product.not.available");
            }
        } else {
            product.setQuantity(product.getQuantity() + quantity);

            if (product.getQuantity() != 0 && !product.isAvailable())
                product.setAvailable(true);
            logger.debug("Cancel order --> update product quantity for product ID : " + id);
            return productRepository.save(product);
        }
    }
}
