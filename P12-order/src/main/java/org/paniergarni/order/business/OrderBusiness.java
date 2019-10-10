package org.paniergarni.order.business;

import feign.FeignException;
import org.paniergarni.order.entities.Order;
import org.paniergarni.order.entities.OrderProduct;
import org.paniergarni.order.entities.WrapperOrderProductDTO;
import org.paniergarni.order.exception.CriteriaException;
import org.paniergarni.order.exception.OrderException;
import org.paniergarni.order.dao.specification.SearchCriteria;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
/**
 * Manipulation de commande
 *
 * @author Pichat morgan
 *
 * 05 octobre 2019
 */
public interface OrderBusiness {
    /** Création d'une commande
     *
     * @param wrapperOrderProductDTO
     * @param userName
     * @param reception
     * @return Order
     * @throws OrderException
     */
    Order createOrder(WrapperOrderProductDTO wrapperOrderProductDTO, String userName, Long reception) throws OrderException;

    /** récupération d'une commande par son ID
     *
     * @param id
     * @return Order
     * @throws OrderException
     */
    Order getOrder(Long id) throws OrderException ;

    /** récupération d'une commande par son ID et son Utilisateur assigné
     *
     * @param id
     * @return Order
     * @throws OrderException
     */
    Order getOrder(Long id, String userName) throws OrderException, FeignException;

    /** Annulation d'une commande par son ID (admin)
     *
     * @param id
     * @return Order
     * @throws OrderException
     */
    Order cancelOrder(Long id) throws OrderException;

    /** Annulation d'une commande par son ID et son Utilisateur assigné (user)
     *
     * @param id
     * @param userName
     * @return
     * @throws OrderException
     * @throws FeignException
     */
    Order cancelOrder(Long id, String userName) throws OrderException, FeignException;

    /** Indique qu'une commande est reglée par son ID
     *
     * @param id
     * @return
     * @throws OrderException
     */
    Order paidOrder(Long id) throws OrderException;

    /** Récupère la liste des commandes en retard de réception
     *
     * @return list Order
     */
    List<Order> getListOrderLate();

    /** Récupère la liste des dates pour réception
     *
     * @return list Date
     */
    List<Date> getListDateReception();

    /** Récupère la liste des commandes le jour de leur réception
     *
     * @return list Order
     */
    List<Order> getListOrderReception();

    /** Recherche de commande par utilisateur
     *
     * @param userName
     * @param page
     * @param size
     * @param searchCriteriaList
     * @return Page Order
     * @throws CriteriaException
     * @throws FeignException
     */
    Page<Order> searchOrder(String userName, int page, int size, List<SearchCriteria> searchCriteriaList) throws CriteriaException, FeignException;

    /** Recherche de commande
     *
     * @param page
     * @param size
     * @param searchCriteriaList
     * @return
     * @throws CriteriaException
     */
    Page<Order> searchOrder(int page, int size, List<SearchCriteria> searchCriteriaList) throws CriteriaException;

    /** Création d'une référence en fonction du nombre de commande du mois et de la date
     *
     * @param order
     * @return Reference
     */
    String addReference(Order order);

    /**
     *
     * @return nombre maximum de jour pour réception
     */
    int getMaxDaysReception();

    /**
     *
     * @return nombre maximum d'heures pour annulation
     */
    int getMaxHoursCancelOrder();
}
