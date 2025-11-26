package com.cafeteria.repository;

import com.cafeteria.entity.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Integer> {
    @Query("SELECT p FROM Promocion p WHERE p.estado = 'ACTIVA' AND :fecha BETWEEN p.fechaInicio AND p.fechaFin")
    List<Promocion> findPromocionesActivas(LocalDate fecha);
}
