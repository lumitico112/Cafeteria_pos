package com.cafeteria.controller;

import com.cafeteria.dto.DashboardDTO;
import com.cafeteria.dto.PedidoDTO;
import com.cafeteria.dto.ProductoDTO;
import com.cafeteria.dto.UsuarioDTO;
import com.cafeteria.entity.Pedido;
import com.cafeteria.service.PedidoService;
import com.cafeteria.service.ProductoService;
import com.cafeteria.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    
    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    private final PedidoService pedidoService;
    
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> dashboard() {
        // Obtener estadísticas
        List<UsuarioDTO> usuarios = usuarioService.listarTodos();
        List<ProductoDTO> productos = productoService.listarTodos();
        
        // Pedidos de hoy
        LocalDateTime inicioHoy = LocalDate.now().atStartOfDay();
        LocalDateTime finHoy = LocalDate.now().atTime(23, 59, 59);
        List<PedidoDTO> pedidosHoyList = pedidoService.listarPorFecha(inicioHoy, finHoy);
        
        // Calcular ventas de hoy
        BigDecimal ventasHoy = pedidosHoyList.stream()
            .filter(p -> p.getEstado() != Pedido.EstadoPedido.CANCELADO)
            .map(PedidoDTO::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Pedidos recientes (últimos 10)
        List<PedidoDTO> pedidosRecientes = pedidoService.listarTodos()
            .stream()
            .sorted((p1, p2) -> p2.getFecha().compareTo(p1.getFecha()))
            .limit(10)
            .collect(Collectors.toList());
        
        // Pedidos pendientes
        List<PedidoDTO> pedidosPendientesList = pedidoService.listarPorEstado(Pedido.EstadoPedido.PENDIENTE);
        
        DashboardDTO dashboard = DashboardDTO.builder()
            .totalUsuarios(usuarios.size())
            .totalProductos(productos.size())
            .pedidosHoy(pedidosHoyList.size())
            .ventasHoy(ventasHoy)
            .pedidosRecientes(pedidosRecientes)
            .pedidosPendientes(pedidosPendientesList.size())
            .build();
            
        return ResponseEntity.ok(dashboard);
    }
}
