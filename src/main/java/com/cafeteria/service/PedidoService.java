package com.cafeteria.service;

import com.cafeteria.dto.PedidoDTO;
import com.cafeteria.entity.Pedido;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoService {
    List<PedidoDTO> listarTodos();
    List<PedidoDTO> listarPorUsuario(Integer idUsuario);
    List<PedidoDTO> listarPorEstado(Pedido.EstadoPedido estado);
    List<PedidoDTO> listarPorFecha(LocalDateTime inicio, LocalDateTime fin);
    PedidoDTO buscarPorId(Integer id);
    PedidoDTO crear(PedidoDTO pedidoDTO);
    PedidoDTO cambiarEstado(Integer id, Pedido.EstadoPedido estado);
    void cancelar(Integer id);
}
