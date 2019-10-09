package org.paniergarni.stock.business;

import org.paniergarni.stock.entities.Farmer;
import org.paniergarni.stock.entities.FarmerDTO;
import org.paniergarni.stock.exception.FarmerException;
import org.paniergarni.stock.exception.StockException;

import java.util.List;
/**
 * Manipulation d'agriculteur
 *
 * @author Pichat morgan
 *
 * 05 octobre 2019
 */
public interface FarmerBusiness {

    /** Création d'un agriculteur
     *
     * @param farmerDTO
     * @return Farmer
     * @throws FarmerException
     */
    Farmer createFarmer(FarmerDTO farmerDTO) throws FarmerException;

    /** Mise à jour d'un agriculteur
     *
     * @param id
     * @param farmerDTO
     * @return Farmer
     * @throws FarmerException
     */
    Farmer updateFarmer(Long id, FarmerDTO farmerDTO) throws FarmerException;

    /** Supression d"un agriculteur
     *
     * @param id
     * @throws FarmerException
     */
    void deleteFarmer(Long id)throws FarmerException;

    /** Récupération d'un agriculteur par son ID
     *
     * @param id
     * @return Farmer
     * @throws FarmerException
     */
    Farmer getFarmer(Long id) throws FarmerException;

    /** Récupération de la liste des agriculteurs
     *
     * @return list Farmer
     */
    List<Farmer> getFarmers();
}
