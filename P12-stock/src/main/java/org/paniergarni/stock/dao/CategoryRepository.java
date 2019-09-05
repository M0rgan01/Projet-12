package org.paniergarni.stock.dao;

import org.paniergarni.stock.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
