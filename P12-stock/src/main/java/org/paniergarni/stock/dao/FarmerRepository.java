package org.paniergarni.stock.dao;

import org.paniergarni.stock.entities.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {


   Optional<Farmer> findByName(String name);
}
