package org.paniergarni.stock.dao;

import org.paniergarni.stock.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

   Optional<Category> findByName(String name);
}
