package com.cafeteria.controller;

import com.cafeteria.dto.PromocionDTO;
import com.cafeteria.entity.Producto;
import com.cafeteria.entity.Promocion;
import com.cafeteria.service.ProductoService;
import com.cafeteria.service.PromocionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/promociones")
@RequiredArgsConstructor
public class PromocionController {

    private final PromocionService promocionService;
    private final ProductoService productoService;
    private final com.cafeteria.mapper.PromocionMapper promocionMapper;

    @GetMapping
    public ResponseEntity<List<PromocionDTO>> listar() {
        return ResponseEntity.ok(promocionService.listAll().stream()
            .map(promocionMapper::toDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/activas")
    public ResponseEntity<List<PromocionDTO>> listarActivas() {
        return ResponseEntity.ok(promocionService.findActivas(LocalDate.now()).stream()
            .map(promocionMapper::toDTO)
            .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromocionDTO> obtenerPorId(@PathVariable Integer id) {
        Promocion promocion = promocionService.findById(id)
            .orElseThrow(() -> new RuntimeException("Promoción no encontrada"));
        return ResponseEntity.ok(promocionMapper.toDTO(promocion));
    }

    @PostMapping
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromocionDTO> crear(@Valid @RequestBody PromocionDTO dto) {
        return ResponseEntity.ok(guardarPromocion(dto));
    }
    
    @PutMapping("/{id}")
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PromocionDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody PromocionDTO dto) {
        dto.setIdPromocion(id);
        return ResponseEntity.ok(guardarPromocion(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        promocionService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
    private PromocionDTO guardarPromocion(PromocionDTO dto) {
        Promocion promocion;
        if (dto.getIdPromocion() != null) {
            promocion = promocionService.findById(dto.getIdPromocion()).orElse(new Promocion());
        } else {
            promocion = new Promocion();
        }

        Set<Producto> productos = new HashSet<>();
        if (dto.getProductoIds() != null && !dto.getProductoIds().isEmpty()) {
            for (Integer prodId : dto.getProductoIds()) {
                productoService.obtenerEntidadPorId(prodId).ifPresent(productos::add);
            }
        }
        
        promocionMapper.updateEntity(promocion, dto, productos);
        Promocion saved = promocionService.save(promocion);
        
        return promocionMapper.toDTO(saved);
    }
}
