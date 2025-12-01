package com.cafeteria.controller;

import com.cafeteria.dto.UsuarioDTO;
import com.cafeteria.entity.PerfilCliente;
import com.cafeteria.entity.Rol;
import com.cafeteria.entity.Usuario;
import com.cafeteria.repository.RolRepository;
import com.cafeteria.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;
    
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioDTO> obtenerPerfil(Authentication authentication) {
        String correo = authentication.getName();
        UsuarioDTO usuario = usuarioService.buscarPorCorreo(correo);
        
        if ("CLIENTE".equals(usuario.getNombreRol())) {
            PerfilCliente perfil = usuarioService.obtenerPerfilCliente(usuario.getIdUsuario());
            if (perfil != null) {
                usuario.setTelefono(perfil.getTelefono());
                usuario.setDireccion(perfil.getDireccion());
                usuario.setPuntosFidelizacion(perfil.getPuntosFidelizacion());
            }
        }
        
        return ResponseEntity.ok(usuario);
    }
    
    @PutMapping("/perfil")
    public ResponseEntity<UsuarioDTO> actualizarPerfil(@Valid @RequestBody UsuarioDTO usuarioDTO,
                                   Authentication authentication) {
        
        String correo = authentication.getName();
        UsuarioDTO usuarioActual = usuarioService.buscarPorCorreo(correo);
        
        // Update basic info
        UsuarioDTO updatedUser = usuarioService.actualizar(usuarioActual.getIdUsuario(), usuarioDTO);
        
        // Update profile info if client
        if ("CLIENTE".equals(usuarioActual.getNombreRol())) {
            PerfilCliente perfil = new PerfilCliente();
            perfil.setTelefono(usuarioDTO.getTelefono());
            perfil.setDireccion(usuarioDTO.getDireccion());
            usuarioService.actualizarPerfilCliente(usuarioActual.getIdUsuario(), perfil);
            
            updatedUser.setTelefono(usuarioDTO.getTelefono());
            updatedUser.setDireccion(usuarioDTO.getDireccion());
        }
        
        return ResponseEntity.ok(updatedUser);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable Integer id, Authentication authentication) {
        UsuarioDTO usuario = usuarioService.buscarPorId(id);
        
        // Verificar permisos: Admin o el mismo usuario
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin && !usuario.getCorreo().equals(authentication.getName())) {
            return ResponseEntity.status(403).build();
        }
        
        // Si es cliente, cargar datos del perfil (puntos, etc)
        if ("CLIENTE".equals(usuario.getNombreRol())) {
            PerfilCliente perfil = usuarioService.obtenerPerfilCliente(usuario.getIdUsuario());
            if (perfil != null) {
                // Priorizar datos del perfil si existen
                if (perfil.getTelefono() != null) usuario.setTelefono(perfil.getTelefono());
                if (perfil.getDireccion() != null) usuario.setDireccion(perfil.getDireccion());
                usuario.setPuntosFidelizacion(perfil.getPuntosFidelizacion());
            }
        }
        
        return ResponseEntity.ok(usuario);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> crear(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.registrar(usuarioDTO));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.actualizar(id, usuarioDTO));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Integer id, @RequestParam Usuario.Estado estado) {
        usuarioService.cambiarEstado(id, estado);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Rol>> listarRoles() {
        return ResponseEntity.ok(rolRepository.findAll());
    }
}
