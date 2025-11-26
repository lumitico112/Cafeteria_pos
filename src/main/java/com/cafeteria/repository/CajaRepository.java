package com.cafeteria.repository;

import com.cafeteria.entity.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CajaRepository extends JpaRepository<Caja, Integer> {
    @Query("SELECT c FROM Caja c WHERE c.usuario.idUsuario = :idUsuario AND c.fechaCierre IS NULL")
    Optional<Caja> findCajaAbiertaByUsuario(Integer idUsuario);
    
    @Query("SELECT c FROM Caja c WHERE c.fechaCierre IS NULL")
    Optional<Caja> findCajaAbierta();
}
