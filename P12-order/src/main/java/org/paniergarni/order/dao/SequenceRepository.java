package org.paniergarni.order.dao;

import org.paniergarni.order.entities.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SequenceRepository extends JpaRepository<Sequence, Long> {


    Optional<Sequence> getByDateEquals(@Param("y") String date);
}
