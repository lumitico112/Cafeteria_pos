package com.cafeteria.service.impl;

import com.cafeteria.dto.DetallePedidoDTO;
import com.cafeteria.dto.PedidoDTO;
import com.cafeteria.entity.*;
import com.cafeteria.repository.*;
import com.cafeteria.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;
    private final com.cafeteria.mapper.PedidoMapper pedidoMapper;
    
    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
            .map(pedidoMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> listarPorUsuario(Integer idUsuario) {
        return pedidoRepository.findByUsuario_IdUsuarioOrderByFechaDesc(idUsuario).stream()
            .map(pedidoMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> listarPorEstado(Pedido.EstadoPedido estado) {
        return pedidoRepository.findByEstadoOrderByFechaDesc(estado).stream()
            .map(pedidoMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> listarPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        return pedidoRepository.findByFechaBetween(inicio, fin).stream()
            .map(pedidoMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PedidoDTO buscarPorId(Integer id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return pedidoMapper.toDTO(pedido);
    }
    
    @Override
    @Transactional
    public PedidoDTO crear(PedidoDTO pedidoDTO) {
        Usuario usuario = usuarioRepository.findById(pedidoDTO.getIdUsuario())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Pedido pedido = pedidoMapper.toEntity(pedidoDTO, usuario);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado(Pedido.EstadoPedido.PENDIENTE);
        
        // Set delivery/pickup details
        if (pedidoDTO.getTipoEntrega() != null) {
            pedido.setTipoEntrega(pedidoDTO.getTipoEntrega());
            
            if (pedidoDTO.getTipoEntrega() == Pedido.TipoEntrega.DELIVERY) {
                pedido.setDireccionEntrega(pedidoDTO.getDireccionEntrega());
            } else if (pedidoDTO.getTipoEntrega() == Pedido.TipoEntrega.RETIRO) {
                pedido.setFechaRecojo(pedidoDTO.getFechaRecojo());
            }
        }
        
        if (pedidoDTO.getAtendidoPor() != null) {
            Usuario empleado = usuarioRepository.findById(pedidoDTO.getAtendidoPor())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
            pedido.setAtendidoPor(empleado);
        }
        
        BigDecimal total = BigDecimal.ZERO;
        
        for (DetallePedidoDTO detalleDTO : pedidoDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(detalleDTO.getCantidad())));
            
            pedido.getDetalles().add(detalle);
            total = total.add(detalle.getSubtotal());
            
            // Descontar del inventario
            inventarioRepository.findByProducto_IdProducto(producto.getIdProducto()).ifPresent(inv -> {
                inv.setCantidadActual(inv.getCantidadActual() - detalleDTO.getCantidad());
                inventarioRepository.save(inv);
            });
        }
        
        pedido.setTotal(total);
        Pedido guardado = pedidoRepository.save(pedido);
        
        return pedidoMapper.toDTO(guardado);
    }
    
    @Override
    @Transactional
    public PedidoDTO cambiarEstado(Integer id, Pedido.EstadoPedido estado) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(estado);
        Pedido actualizado = pedidoRepository.save(pedido);
        return pedidoMapper.toDTO(actualizado);
    }
    
    @Override
    @Transactional
    public void cancelar(Integer id) {
        cambiarEstado(id, Pedido.EstadoPedido.CANCELADO);
    }
}
