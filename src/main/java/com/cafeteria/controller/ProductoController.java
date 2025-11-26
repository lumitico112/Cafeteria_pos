package com.cafeteria.controller;

import com.cafeteria.dto.ProductoDTO;
import com.cafeteria.entity.Producto;
import com.cafeteria.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {
    
    private final ProductoService productoService;
    
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listar(@RequestParam(required = false) Integer categoria) {
        if (categoria != null) {
            return ResponseEntity.ok(productoService.listarPorCategoria(categoria));
        } else {
            return ResponseEntity.ok(productoService.listarActivos());
        }
    }
    
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductoDTO>> listarAdmin() {
        return ResponseEntity.ok(productoService.listarTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<ProductoDTO> crear(@Valid @RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoService.crear(productoDTO));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoService.actualizar(id, productoDTO));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Integer id, @RequestParam Boolean estado) {
        productoService.cambiarEstado(id, estado ? Producto.Estado.ACTIVO : Producto.Estado.INACTIVO);
        return ResponseEntity.ok().build();
    }
}
