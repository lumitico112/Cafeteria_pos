package com.cafeteria.repository;

import com.cafeteria.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

    @Query(value = """
      select t from JwtToken t inner join Usuario u\s
      on t.usuario.idUsuario = u.idUsuario\s
      where u.idUsuario = :idUsuario and (t.isValid = true)\s
      """)
    List<JwtToken> findAllValidTokenByUser(Integer idUsuario);

    Optional<JwtToken> findByToken(String token);
}
