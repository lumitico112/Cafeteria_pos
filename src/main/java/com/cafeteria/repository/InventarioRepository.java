package com.cafeteria.repository;

import com.cafeteria.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
    Optional<Inventario> findByProducto_IdProducto(Integer idProducto);
    
    @Query("SELECT i FROM Inventario i WHERE i.cantidadActual <= i.stockMinimo")
    List<Inventario> findProductosBajoStock();
}
