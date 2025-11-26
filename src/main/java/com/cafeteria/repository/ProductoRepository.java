package com.cafeteria.repository;

import com.cafeteria.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByEstado(Producto.Estado estado);
    
    List<Producto> findByCategoria_IdCategoria(Integer idCategoria);
    
    @Query("SELECT p FROM Producto p WHERE p.estado = 'ACTIVO' ORDER BY p.nombre")
    List<Producto> findAllActivos();
}
