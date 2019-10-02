package org.paniergarni.order.dao;

import org.paniergarni.order.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    /**
     * requete de recherche des commande en retard de réception
     *
     * @param now --> date actuel
     *
     * @return list de commande
     */
    @Query("select o from Order o where o.reception < :y and o.paid = false")
    List<Order> getListOrderLate(@Param("y") Date now);

    /**
     * requete de recherche des commande ou le jour actuel est le jour de réception
     *
     * @param now --> date actuel
     *
     * @return list de commande
     */
    @Query("select o from Order o where o.reception = :y and o.paid = false")
    List<Order> getListOrderReception(@Param("y") Date now);

    @Query("select COUNT(o.id) from Order o where MONTH(o.date) = :y")
    int getCountOrderByMount(@Param("y") int mouth);
}
