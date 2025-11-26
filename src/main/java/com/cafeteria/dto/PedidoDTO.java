package com.cafeteria.dto;

import com.cafeteria.entity.Pedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    
    private Integer idPedido;
    private Integer idUsuario;
    private String nombreCliente;
    private Integer atendidoPor;
    private String nombreEmpleado;
    private LocalDateTime fecha;
    private Pedido.EstadoPedido estado;
    private BigDecimal total;
    private Pedido.TipoEntrega tipoEntrega;
    private List<DetallePedidoDTO> detalles = new ArrayList<>();
}
