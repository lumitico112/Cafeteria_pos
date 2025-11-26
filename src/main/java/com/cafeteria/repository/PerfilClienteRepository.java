package com.cafeteria.repository;

import com.cafeteria.entity.PerfilCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilClienteRepository extends JpaRepository<PerfilCliente, Integer> {
    Optional<PerfilCliente> findByUsuario_IdUsuario(Integer idUsuario);
}
