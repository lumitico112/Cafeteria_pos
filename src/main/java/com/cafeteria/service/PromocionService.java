// PromocionService.java
package com.cafeteria.service;

import com.cafeteria.entity.Promocion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PromocionService {
    List<Promocion> listAll();
    Promocion save(Promocion promocion);
    Optional<Promocion> findById(Integer id);
    void deleteById(Integer id);
    List<Promocion> findActivas(LocalDate fecha);
}

