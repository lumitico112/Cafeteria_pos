package com.cafeteria.repository;

import com.cafeteria.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer> {
    List<MovimientoInventario> findByProducto_IdProductoOrderByFechaDesc(Integer idProducto);
}
