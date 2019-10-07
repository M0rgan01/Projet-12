package org.paniergarni.stock.business;

import org.paniergarni.stock.dao.specification.SearchCriteria;
import org.paniergarni.stock.entities.Product;
import org.paniergarni.stock.entities.ProductDTO;
import org.paniergarni.stock.exception.CriteriaException;
import org.paniergarni.stock.exception.ProductException;
import org.paniergarni.stock.exception.StockException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductBusiness {

    Product createProduct(ProductDTO productDTO) throws ProductException;
    Product updateProduct(Long id, ProductDTO productDTO) throws ProductException;
    void deleteProduct(Long id) throws ProductException;
    Product getProduct(Long id) throws ProductException;
    Page<Product> searchProduct(int page, int size, List<SearchCriteria> searchCriteria) throws CriteriaException;
    Product updateProductQuantity(int quantity, Long id, boolean cancel) throws ProductException;
    void setProductPhoto(Long id ,MultipartFile multipartFile) throws IOException, ProductException;
}
