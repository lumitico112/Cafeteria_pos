package com.cafeteria.repository;

import com.cafeteria.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByUsuario_IdUsuarioOrderByFechaDesc(Integer idUsuario);
    
    List<Pedido> findByEstadoOrderByFechaDesc(Pedido.EstadoPedido estado);
    
    @Query("SELECT p FROM Pedido p WHERE p.fecha BETWEEN :inicio AND :fin ORDER BY p.fecha DESC")
    List<Pedido> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
