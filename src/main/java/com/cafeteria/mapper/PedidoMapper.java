package com.cafeteria.mapper;

import com.cafeteria.dto.DetallePedidoDTO;
import com.cafeteria.dto.PedidoDTO;
import com.cafeteria.entity.DetallePedido;
import com.cafeteria.entity.Pedido;
import com.cafeteria.entity.Producto;
import com.cafeteria.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    public PedidoDTO toDTO(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        PedidoDTO dto = new PedidoDTO();
        dto.setIdPedido(pedido.getIdPedido());
        dto.setIdUsuario(pedido.getUsuario().getIdUsuario());
        if (pedido.getNombreCliente() != null && !pedido.getNombreCliente().isEmpty()) {
            dto.setNombreCliente(pedido.getNombreCliente());
        } else {
            dto.setNombreCliente(pedido.getUsuario().getNombre() + " " + pedido.getUsuario().getApellido());
        }
        
        if (pedido.getAtendidoPor() != null) {
            dto.setAtendidoPor(pedido.getAtendidoPor().getIdUsuario());
            dto.setNombreEmpleado(pedido.getAtendidoPor().getNombre() + " " + pedido.getAtendidoPor().getApellido());
        }
        
        dto.setFecha(pedido.getFecha());
        dto.setEstado(pedido.getEstado());
        dto.setTotal(pedido.getTotal());
        dto.setTipoEntrega(pedido.getTipoEntrega());
        
        if (pedido.getDetalles() != null) {
            dto.setDetalles(pedido.getDetalles().stream()
                .map(this::toDetalleDTO)
                .collect(Collectors.toList()));
        }

        return dto;
    }

    public DetallePedidoDTO toDetalleDTO(DetallePedido detalle) {
        if (detalle == null) {
            return null;
        }

        DetallePedidoDTO dto = new DetallePedidoDTO();
        dto.setIdDetalle(detalle.getIdDetalle());
        dto.setIdProducto(detalle.getProducto().getIdProducto());
        dto.setNombreProducto(detalle.getProducto().getNombre());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        
        return dto;
    }
    
    public Pedido toEntity(PedidoDTO dto, Usuario usuario) {
        if (dto == null) {
            return null;
        }
        
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setTipoEntrega(dto.getTipoEntrega());
        pedido.setNombreCliente(dto.getNombreCliente());
        // Otros campos se establecen en el servicio (fecha, estado, total)
        
        return pedido;
    }
}
