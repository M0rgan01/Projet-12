package org.paniergarni.stock.business;

import org.paniergarni.stock.entities.Category;
import org.paniergarni.stock.entities.CategoryDTO;
import org.paniergarni.stock.exception.CategoryException;
import org.paniergarni.stock.exception.StockException;

import java.util.List;

/**
 * Manipulation de Categorie
 *
 * @author Pichat morgan
 *
 * 05 octobre 2019
 */
public interface CategoryBusiness {

    /** Création d'une catégorie
     *
     * @param category
     * @return Category
     * @throws CategoryException
     */
    Category createCategory(CategoryDTO category) throws CategoryException;

    /** Mise à jour d'une catégorie
     *
     * @param id
     * @param category
     * @return Category
     * @throws CategoryException
     */
    Category updateCategory(Long id, CategoryDTO category) throws CategoryException;

    /** Supression d'une catégorie
     *
     * @param id
     * @throws CategoryException
     */
    void deleteCategory(Long id) throws CategoryException;

    /** Récupère une catégorie par son ID
     *
     * @param id
     * @return Category
     * @throws CategoryException
     */
    Category getCategory(Long id) throws CategoryException;

    /** Récupère la liste des catégories
     *
     * @return list Category
     */
    List<Category> getCategories();
}
