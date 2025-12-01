package com.cafeteria.service.impl;

import com.cafeteria.entity.Inventario;
import com.cafeteria.entity.MovimientoInventario;
import com.cafeteria.entity.Producto;
import com.cafeteria.entity.Usuario;
import com.cafeteria.repository.InventarioRepository;
import com.cafeteria.repository.MovimientoInventarioRepository;
import com.cafeteria.repository.ProductoRepository;
import com.cafeteria.repository.UsuarioRepository;
import com.cafeteria.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final MovimientoInventarioRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    private final com.cafeteria.mapper.InventarioMapper inventarioMapper;

    @Override
    @Transactional(readOnly = true)
    public Inventario obtenerPorProducto(Integer idProducto) {
        return inventarioRepository.findByProducto_IdProducto(idProducto)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventario> listarProductosBajoStock() {
        return inventarioRepository.findProductosBajoStock();
    }

    @Override
    @Transactional(readOnly = true)
    public List<com.cafeteria.dto.InventarioDTO> listarTodos() {
        return inventarioRepository.findAll().stream()
                .map(inventarioMapper::toDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional
    public com.cafeteria.dto.InventarioDTO actualizar(Integer id, com.cafeteria.dto.InventarioDTO dto) {
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        // Actualizar campos permitidos
        if (dto.getStockMinimo() != null) {
            inventario.setStockMinimo(dto.getStockMinimo());
        }
        
        // Si se quiere actualizar la cantidad directamente (aunque se recomienda usar ajustarStock)
        if (dto.getCantidadActual() != null) {
            // Opcional: Registrar movimiento de ajuste automático o simplemente actualizar
            inventario.setCantidadActual(dto.getCantidadActual());
        }

        Inventario actualizado = inventarioRepository.save(inventario);
        return inventarioMapper.toDTO(actualizado);
    }

    @Override
    @Transactional
    public void registrarMovimiento(MovimientoInventario movimiento) {
        movimientoRepository.save(movimiento);

        // Actualizar inventario
        Inventario inventario = inventarioRepository.findByProducto_IdProducto(
                movimiento.getProducto().getIdProducto()
        ).orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        if (movimiento.getTipo() == MovimientoInventario.Tipo.ENTRADA) {
            inventario.setCantidadActual(inventario.getCantidadActual() + movimiento.getCantidad());
        } else {
            inventario.setCantidadActual(inventario.getCantidadActual() - movimiento.getCantidad());
        }

        inventarioRepository.save(inventario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventario> obtenerMovimientosPorProducto(Integer idProducto) {
        return movimientoRepository.findByProducto_IdProductoOrderByFechaDesc(idProducto);
    }

    @Override
    @Transactional
    public void ajustarStock(Integer idProducto, Integer cantidad, String motivo, Integer idUsuario) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setUsuario(usuario);
        movimiento.setTipo(cantidad > 0 ? MovimientoInventario.Tipo.ENTRADA : MovimientoInventario.Tipo.SALIDA);
        movimiento.setCantidad(Math.abs(cantidad));
        movimiento.setMotivo(motivo);
        movimiento.setFecha(LocalDateTime.now());

        registrarMovimiento(movimiento);
    }
}
