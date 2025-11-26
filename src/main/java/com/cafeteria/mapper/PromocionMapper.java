package com.cafeteria.mapper;

import com.cafeteria.dto.PromocionDTO;
import com.cafeteria.entity.Producto;
import com.cafeteria.entity.Promocion;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PromocionMapper {

    public PromocionDTO toDTO(Promocion promocion) {
        if (promocion == null) {
            return null;
        }

        PromocionDTO dto = new PromocionDTO();
        dto.setIdPromocion(promocion.getIdPromocion());
        dto.setNombre(promocion.getNombre());
        dto.setDescripcion(promocion.getDescripcion());
        dto.setTipo(promocion.getTipo() != null ? promocion.getTipo().name() : null);
        dto.setFechaInicio(promocion.getFechaInicio());
        dto.setFechaFin(promocion.getFechaFin());
        dto.setEstado(promocion.getEstado() != null ? promocion.getEstado().name() : null);

        if (promocion.getProductos() != null) {
            dto.setProductoIds(promocion.getProductos().stream()
                .map(Producto::getIdProducto)
                .collect(Collectors.toSet()));
        }

        return dto;
    }

    public Promocion toEntity(PromocionDTO dto, Set<Producto> productos) {
        if (dto == null) {
            return null;
        }

        Promocion promocion = new Promocion();
        promocion.setIdPromocion(dto.getIdPromocion());
        promocion.setNombre(dto.getNombre());
        promocion.setDescripcion(dto.getDescripcion());
        promocion.setTipo(dto.getTipo() != null ? Promocion.TipoPromocion.valueOf(dto.getTipo()) : null);
        promocion.setFechaInicio(dto.getFechaInicio());
        promocion.setFechaFin(dto.getFechaFin());
        promocion.setEstado(dto.getEstado() != null ? Promocion.EstadoPromocion.valueOf(dto.getEstado()) : Promocion.EstadoPromocion.ACTIVA);
        promocion.setProductos(productos != null ? productos : new HashSet<>());

        return promocion;
    }
    
    public void updateEntity(Promocion promocion, PromocionDTO dto, Set<Producto> productos) {
        if (promocion == null || dto == null) {
            return;
        }
        
        promocion.setNombre(dto.getNombre());
        promocion.setDescripcion(dto.getDescripcion());
        promocion.setTipo(dto.getTipo() != null ? Promocion.TipoPromocion.valueOf(dto.getTipo()) : null);
        promocion.setFechaInicio(dto.getFechaInicio());
        promocion.setFechaFin(dto.getFechaFin());
        promocion.setEstado(dto.getEstado() != null ? Promocion.EstadoPromocion.valueOf(dto.getEstado()) : Promocion.EstadoPromocion.ACTIVA);
        
        if (productos != null) {
            promocion.setProductos(productos);
        }
    }
}
