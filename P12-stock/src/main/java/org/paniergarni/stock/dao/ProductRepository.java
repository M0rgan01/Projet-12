package org.paniergarni.stock.dao;

import org.paniergarni.stock.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
