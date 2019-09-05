package org.paniergarni.stock.dao;

import org.paniergarni.stock.entities.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {
}
