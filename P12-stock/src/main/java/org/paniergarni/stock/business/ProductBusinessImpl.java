package org.paniergarni.stock.business;

import org.modelmapper.ModelMapper;
import org.paniergarni.stock.dao.ProductRepository;
import org.paniergarni.stock.dao.specification.ProductSpecificationBuilder;
import org.paniergarni.stock.dao.specification.SearchCriteria;
import org.paniergarni.stock.entities.Product;
import org.paniergarni.stock.entities.ProductDTO;
import org.paniergarni.stock.exception.CriteriaException;
import org.paniergarni.stock.exception.ProductException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class ProductBusinessImpl implements ProductBusiness {

    @Value("${product.photo.location}")
    private String photoLocation;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(ProductBusinessImpl.class);

    @Override
    public Product createProduct(ProductDTO productDTO) throws ProductException {
        Product product = modelMapper.map(productDTO, Product.class);

        if (product.getQuantity() == 0)
            product.setAvailable(false);

        if (product.isPromotion())
            checkPromotionPrice(product.getPrice(), product.getOldPrice());


         product = productRepository.save(product);
        logger.info("create product " + product.getId());
        return product;
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws ProductException {
        Product product = modelMapper.map(productDTO, Product.class);
        Product productCompare = getProduct(id);
        if (product.isPromotion()){
            checkPromotionPrice(product.getPrice(), product.getOldPrice());
        }

        product.setId(productCompare.getId());
        logger.info("Update product " + product.getId());
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) throws ProductException {
        Product product = getProduct(id);
        productRepository.delete(product);
        logger.info("Delete product " + product.getId());
    }

    @Override
    public Product getProduct(Long id) throws ProductException {
        return productRepository.findById(id).orElseThrow(() -> new ProductException("product.id.incorrect"));
    }

    @Override
    public Page<Product> searchProduct(int page, int size, List<SearchCriteria> searchCriteriaList) throws CriteriaException {
        ProductSpecificationBuilder builder = new ProductSpecificationBuilder(searchCriteriaList);
        logger.debug("Searching products with list of criteria of " + searchCriteriaList.size() + "elements");
        Specification<Product> spec = builder.build();
        return productRepository.findAll(spec, PageRequest.of(page, size));
    }

    @Override
    public Product updateProductQuantity(int quantity, Long id, boolean cancel) throws ProductException {
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
                throw new ProductException("product.not.available");
            }
        } else {
            product.setQuantity(product.getQuantity() + quantity);

            if (product.getQuantity() != 0 && !product.isAvailable())
                product.setAvailable(true);
            logger.debug("Cancel order --> update product quantity for product ID : " + id);
            return productRepository.save(product);
        }
    }

    private void checkPromotionPrice(double currentPrice, double oldPrice) throws ProductException {
        if (oldPrice == 0){
            throw new ProductException("product.old.price.null");
        } else if (currentPrice > oldPrice){
            throw new ProductException("product.price.greater.than.old.price");
        }
    }

    public void setProductPhoto(Long id, MultipartFile file) throws IOException, ProductException {
        Product product = getProduct(id);
        product.setPhoto( product.getId() + ".png");
        Files.write(Paths.get( photoLocation + product.getPhoto()), file.getBytes());
        productRepository.save(product);
    }

}
