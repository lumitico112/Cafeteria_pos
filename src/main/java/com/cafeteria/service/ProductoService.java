package com.cafeteria.service;

import com.cafeteria.dto.ProductoDTO;
import com.cafeteria.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<ProductoDTO> listarTodos();
    List<ProductoDTO> listarActivos();
    List<ProductoDTO> listarPorCategoria(Integer idCategoria);
    ProductoDTO buscarPorId(Integer id);
    ProductoDTO crear(ProductoDTO productoDTO);
    ProductoDTO actualizar(Integer id, ProductoDTO productoDTO);
    void eliminar(Integer id);
    void cambiarEstado(Integer id, Producto.Estado estado);

    // Nuevo: devuelve la entidad Producto (opcional) para usos internos (p.ej. asociaciones ManyToMany)
    Optional<Producto> obtenerEntidadPorId(Integer id);
}
