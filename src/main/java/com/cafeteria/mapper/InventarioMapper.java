package com.cafeteria.mapper;

import com.cafeteria.dto.InventarioDTO;
import com.cafeteria.entity.Inventario;
import org.springframework.stereotype.Component;

@Component
public class InventarioMapper {

    public InventarioDTO toDTO(Inventario inventario) {
        if (inventario == null) {
            return null;
        }

        InventarioDTO dto = new InventarioDTO();
        dto.setIdInventario(inventario.getIdInventario());
        dto.setCantidadActual(inventario.getCantidadActual());
        dto.setStockMinimo(inventario.getStockMinimo());
        dto.setUnidadMedida(inventario.getUnidadMedida());

        if (inventario.getProducto() != null) {
            dto.setIdProducto(inventario.getProducto().getIdProducto());
            dto.setNombreProducto(inventario.getProducto().getNombre());
        }

        return dto;
    }
}
