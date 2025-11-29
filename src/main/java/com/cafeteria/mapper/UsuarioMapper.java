package com.cafeteria.mapper;

import com.cafeteria.dto.UsuarioDTO;
import com.cafeteria.entity.Rol;
import com.cafeteria.entity.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setCorreo(usuario.getCorreo());
        dto.setEstado(usuario.getEstado());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        
        if (usuario.getRol() != null) {
            dto.setIdRol(usuario.getRol().getIdRol());
            dto.setNombreRol(usuario.getRol().getNombre());
        }
        
        if (usuario.getPerfilCliente() != null) {
            dto.setTelefono(usuario.getPerfilCliente().getTelefono());
            dto.setDireccion(usuario.getPerfilCliente().getDireccion());
            dto.setPuntosFidelizacion(usuario.getPerfilCliente().getPuntosFidelizacion());
        }

        return dto;
    }

    public Usuario toEntity(UsuarioDTO dto, Rol rol, String encodedPassword) {
        if (dto == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());
        usuario.setContrasena(encodedPassword);
        usuario.setEstado(Usuario.Estado.ACTIVO);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setRol(rol);
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());

        return usuario;
    }
    
    public void updateEntity(Usuario usuario, UsuarioDTO dto, Rol rol, String encodedPassword) {
        if (usuario == null || dto == null) {
            return;
        }
        
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        
        if (encodedPassword != null) {
            usuario.setContrasena(encodedPassword);
        }
        
        if (rol != null) {
            usuario.setRol(rol);
        }
        
        if (dto.getEstado() != null) {
            usuario.setEstado(dto.getEstado());
        }
        
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
    }
}
