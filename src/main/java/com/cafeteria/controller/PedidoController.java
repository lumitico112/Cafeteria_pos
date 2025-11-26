package com.cafeteria.controller;

import com.cafeteria.dto.PedidoDTO;
import com.cafeteria.entity.Pedido;
import com.cafeteria.entity.Usuario;
import com.cafeteria.repository.UsuarioRepository;
import com.cafeteria.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    
    private final PedidoService pedidoService;
    private final UsuarioRepository usuarioRepository;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<List<PedidoDTO>> listar(@RequestParam(required = false) String estado) {
        if (estado != null) {
            return ResponseEntity.ok(pedidoService.listarPorEstado(Pedido.EstadoPedido.valueOf(estado)));
        } else {
            return ResponseEntity.ok(pedidoService.listarTodos());
        }
    }
    
    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoDTO>> misPedidos(Authentication authentication) {
        String correo = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        return ResponseEntity.ok(pedidoService.listarPorUsuario(usuario.getIdUsuario()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> verDetalle(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }
    
    @PostMapping
    public ResponseEntity<PedidoDTO> crear(@RequestBody PedidoDTO pedidoDTO, Authentication authentication) {
        String correo = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        pedidoDTO.setIdUsuario(usuario.getIdUsuario());
        return ResponseEntity.ok(pedidoService.crear(pedidoDTO));
    }
    
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Integer id, @RequestParam Pedido.EstadoPedido estado) {
        pedidoService.cambiarEstado(id, estado);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Integer id) {
        pedidoService.cancelar(id);
        return ResponseEntity.ok().build();
    }
}
