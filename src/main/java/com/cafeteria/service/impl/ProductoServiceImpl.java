package com.cafeteria.service.impl;

import com.cafeteria.dto.ProductoDTO;
import com.cafeteria.entity.Categoria;
import com.cafeteria.entity.Inventario;
import com.cafeteria.entity.Producto;
import com.cafeteria.repository.CategoriaRepository;
import com.cafeteria.repository.InventarioRepository;
import com.cafeteria.repository.ProductoRepository;
import com.cafeteria.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final InventarioRepository inventarioRepository;
    private final com.cafeteria.mapper.ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarTodos() {
        return productoRepository.findAll().stream()
            .map(productoMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarActivos() {
        return productoRepository.findAllActivos().stream()
            .map(productoMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarPorCategoria(Integer idCategoria) {
        return productoRepository.findByCategoria_IdCategoria(idCategoria).stream()
            .map(productoMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDTO buscarPorId(Integer id) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return productoMapper.toDTO(producto);
    }

    @Override
    @Transactional
    public ProductoDTO crear(ProductoDTO productoDTO) {
        Categoria categoria = null;
        if (productoDTO.getIdCategoria() != null) {
            categoria = categoriaRepository.findById(productoDTO.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        }

        Producto producto = productoMapper.toEntity(productoDTO, categoria);
        Producto guardado = productoRepository.save(producto);

        // Crear inventario inicial
        Inventario inventario = new Inventario();
        inventario.setProducto(guardado);
        inventario.setCantidadActual(productoDTO.getCantidadActual() != null ? productoDTO.getCantidadActual() : 0);
        inventario.setStockMinimo(productoDTO.getStockMinimo() != null ? productoDTO.getStockMinimo() : 10);
        inventario.setUnidadMedida(productoDTO.getUnidadMedida() != null ? productoDTO.getUnidadMedida() : "unidad");
        inventarioRepository.save(inventario);

        return productoMapper.toDTO(guardado);
    }

    @Override
    @Transactional
    public ProductoDTO actualizar(Integer id, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Categoria categoria = null;
        if (productoDTO.getIdCategoria() != null) {
            categoria = categoriaRepository.findById(productoDTO.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        }

        productoMapper.updateEntity(producto, productoDTO, categoria);
        Producto actualizado = productoRepository.save(producto);

        // Actualizar inventario si existe
        inventarioRepository.findByProducto_IdProducto(id).ifPresent(inventario -> {
            if (productoDTO.getCantidadActual() != null) {
                inventario.setCantidadActual(productoDTO.getCantidadActual());
            }
            if (productoDTO.getStockMinimo() != null) {
                inventario.setStockMinimo(productoDTO.getStockMinimo());
            }
            if (productoDTO.getUnidadMedida() != null) {
                inventario.setUnidadMedida(productoDTO.getUnidadMedida());
            }
            inventarioRepository.save(inventario);
        });

        return productoMapper.toDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void cambiarEstado(Integer id, Producto.Estado estado) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setEstado(estado);
        productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> obtenerEntidadPorId(Integer id) {
        return productoRepository.findById(id);
    }
}
