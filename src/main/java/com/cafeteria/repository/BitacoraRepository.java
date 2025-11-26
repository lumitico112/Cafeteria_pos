package com.cafeteria.repository;

import com.cafeteria.entity.Bitacora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BitacoraRepository extends JpaRepository<Bitacora, Integer> {
    List<Bitacora> findByUsuario_IdUsuarioOrderByFechaHoraDesc(Integer idUsuario);
    
    List<Bitacora> findByModuloOrderByFechaHoraDesc(String modulo);
}
