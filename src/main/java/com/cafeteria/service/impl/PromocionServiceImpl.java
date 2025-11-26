// PromocionServiceImpl.java
package com.cafeteria.service.impl;

import com.cafeteria.entity.Promocion;
import com.cafeteria.repository.PromocionRepository;
import com.cafeteria.service.PromocionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromocionServiceImpl implements PromocionService {

    private final PromocionRepository promocionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Promocion> listAll() {
        return promocionRepository.findAll();
    }

    @Override
    @Transactional
    public Promocion save(Promocion promocion) {
        // Aquí podrías validar fechas, rango, etc.
        return promocionRepository.save(promocion);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Promocion> findById(Integer id) {
        return promocionRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        promocionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Promocion> findActivas(LocalDate fecha) {
        return promocionRepository.findPromocionesActivas(fecha);
    }
}
