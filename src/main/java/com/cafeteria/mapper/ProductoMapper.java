package com.cafeteria.mapper;

import com.cafeteria.dto.ProductoDTO;
import com.cafeteria.entity.Categoria;
import com.cafeteria.entity.Inventario;
import com.cafeteria.entity.Producto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductoMapper {

    public ProductoDTO toDTO(Producto producto) {
        if (producto == null) {
            return null;
        }

        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setImpuesto(producto.getImpuesto());
        dto.setEstado(producto.getEstado());
        dto.setImagenUrl(producto.getImagenUrl());

        if (producto.getCategoria() != null) {
            dto.setIdCategoria(producto.getCategoria().getIdCategoria());
            dto.setNombreCategoria(producto.getCategoria().getNombre());
        }

        if (producto.getInventario() != null) {
            dto.setCantidadActual(producto.getInventario().getCantidadActual());
            dto.setStockMinimo(producto.getInventario().getStockMinimo());
            dto.setUnidadMedida(producto.getInventario().getUnidadMedida());
        }

        return dto;
    }

    public Producto toEntity(ProductoDTO dto, Categoria categoria) {
        if (dto == null) {
            return null;
        }

        Producto producto = new Producto();
        producto.setIdProducto(dto.getIdProducto());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setImpuesto(dto.getImpuesto() != null ? dto.getImpuesto() : BigDecimal.ZERO);
        producto.setEstado(dto.getEstado() != null ? dto.getEstado() : Producto.Estado.ACTIVO);
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setCategoria(categoria);

        return producto;
    }
    
    public void updateEntity(Producto producto, ProductoDTO dto, Categoria categoria) {
        if (dto == null || producto == null) {
            return;
        }
        
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setImpuesto(dto.getImpuesto() != null ? dto.getImpuesto() : BigDecimal.ZERO);
        producto.setImagenUrl(dto.getImagenUrl());
        
        if (categoria != null) {
            producto.setCategoria(categoria);
        }
        
        if (dto.getEstado() != null) {
            producto.setEstado(dto.getEstado());
        }
    }
}
