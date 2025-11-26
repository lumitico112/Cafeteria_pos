package com.cafeteria.repository;

import com.cafeteria.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByUsuario_IdUsuarioOrderByFechaReservaDesc(Integer idUsuario);
    
    List<Reserva> findByFechaReservaAndEstado(LocalDate fecha, Reserva.EstadoReserva estado);
}
