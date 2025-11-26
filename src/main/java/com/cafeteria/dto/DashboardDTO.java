package com.cafeteria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private long totalUsuarios;
    private long totalProductos;
    private long pedidosHoy;
    private BigDecimal ventasHoy;
    private List<PedidoDTO> pedidosRecientes;
    private long pedidosPendientes;
}
