package com.cafeteria.controller;

import com.cafeteria.dto.InventarioDTO;
import com.cafeteria.entity.Inventario;
import com.cafeteria.entity.Usuario;
import com.cafeteria.repository.UsuarioRepository;
import com.cafeteria.service.InventarioService;
import com.cafeteria.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;
    private final ProductoService productoService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<com.cafeteria.dto.InventarioDTO>> listar() {
        return ResponseEntity.ok(inventarioService.listarTodos());
    }
    
    @GetMapping("/bajo-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<List<Inventario>> listarBajoStock() {
        return ResponseEntity.ok(inventarioService.listarProductosBajoStock());
    }

    @GetMapping("/producto/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Map<String, Object>> verMovimientos(@PathVariable Integer id) {
        return ResponseEntity.ok(Map.of(
            "producto", productoService.buscarPorId(id),
            "inventario", inventarioService.obtenerPorProducto(id),
            "movimientos", inventarioService.obtenerMovimientosPorProducto(id)
        ));
    }

    @PostMapping("/ajustar")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Void> ajustarStock(@RequestBody Map<String, Object> payload,
                              Authentication authentication) {
        
        Integer idProducto = (Integer) payload.get("idProducto");
        Integer cantidad = (Integer) payload.get("cantidad");
        String motivo = (String) payload.get("motivo");
        
        String correo = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        inventarioService.ajustarStock(idProducto, cantidad, motivo, usuario.getIdUsuario());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<InventarioDTO> actualizar(@PathVariable Integer id, @RequestBody InventarioDTO dto) {
        return ResponseEntity.ok(inventarioService.actualizar(id, dto));
    }
}
