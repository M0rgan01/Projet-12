package org.paniergarni.stock.dao;

import org.paniergarni.stock.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {


    Optional<Product> findByName(String name);

    @Query("select p from Product p where p.category.id = :id")
    Page<Product> findByCategoryId(@Param("id") Long id, Pageable pageable);

    Page<Product> findByPromotionIsTrue(Pageable pageable);

    Page<Product> findByNameContains(String name, Pageable pageable);
}
