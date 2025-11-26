package com.cafeteria.repository;

import com.cafeteria.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);
    
    @Query("SELECT u FROM Usuario u WHERE u.correo = :identifier OR u.nombre = :identifier")
    Optional<Usuario> findByCorreoOrNombre(String identifier);
    
    boolean existsByCorreo(String correo);
}
