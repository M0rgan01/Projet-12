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

/**
 * Manipulation de produit
 *
 * @author Pichat morgan
 *
 * 05 octobre 2019
 */
public interface ProductBusiness {

    /** Création d'un produit
     *
     * @param productDTO
     * @return Product
     * @throws ProductException
     */
    Product createProduct(ProductDTO productDTO) throws ProductException;

    /** Mise à jour d'un produit
     *
     * @param id
     * @param productDTO
     * @return Product
     * @throws ProductException
     */
    Product updateProduct(Long id, ProductDTO productDTO) throws ProductException;

    /** Supression d'un produit
     *
     * @param id
     * @throws ProductException
     */
    void deleteProduct(Long id) throws ProductException;

    /** Récupération d'un produit par son ID
     *
     * @param id
     * @return Product
     * @throws ProductException
     */
    Product getProduct(Long id) throws ProductException;

    /** Recherche de produit
     *
     * @param page
     * @param size
     * @param searchCriteria
     * @return page Product
     * @throws CriteriaException
     */
    Page<Product> searchProduct(int page, int size, List<SearchCriteria> searchCriteria) throws CriteriaException;

    /** Mise à jour de la quantitée d'un produit
     *
     * @param quantity
     * @param id
     * @param cancel
     * @return Product
     * @throws ProductException
     */
    Product updateProductQuantity(int quantity, Long id, boolean cancel) throws ProductException;

    /** Mise à jour de la photo d'un produit
     *
     * @param id
     * @param multipartFile
     * @throws IOException
     * @throws ProductException
     */
    void setProductPhoto(Long id ,MultipartFile multipartFile) throws IOException, ProductException;
}
