package com.cafeteria.security;

import com.cafeteria.entity.Permiso;
import com.cafeteria.entity.Rol;
import com.cafeteria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final UsuarioRepository usuarioRepository;

    public List<String> getUserPermissions(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .map(usuario -> {
                    Rol rol = usuario.getRol();
                    if (rol == null || rol.getPermisos() == null) {
                        return List.<String>of();
                    }
                    
                    return rol.getPermisos().stream()
                            .map(permiso -> permiso.getModulo() + ":" + permiso.getAccion())
                            .collect(Collectors.toList());
                })
                .orElse(List.of());
    }

    public boolean hasPermission(String correo, String permisoString) {
        return getUserPermissions(correo).contains(permisoString);
    }

    public String getUserRole(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .map(usuario -> usuario.getRol() != null ? usuario.getRol().getNombre() : "")
                .orElse("");
    }  
}
