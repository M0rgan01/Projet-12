package org.paniergarni.stock.business;

import org.paniergarni.stock.dao.specification.SearchCriteria;
import org.paniergarni.stock.entities.Product;
import org.paniergarni.stock.exception.CriteriaException;
import org.paniergarni.stock.exception.StockException;
import org.springframework.data.domain.Page;

import javax.security.auth.login.CredentialException;
import java.util.List;

public interface ProductBusiness {

    Product createProduct(Product product);
    Product updateProduct(Long id, Product product) throws StockException;
    Product getProduct(Long id) throws StockException;
    Page<Product> searchProduct(int page, int size, List<SearchCriteria> searchCriteria) throws CriteriaException;
    Product updateProductQuantity(int quantity, Long id, boolean cancel) throws StockException;
}
