package com.cafeteria.service.impl;

import com.cafeteria.dto.UsuarioDTO;
import com.cafeteria.entity.PerfilCliente;
import com.cafeteria.entity.Rol;
import com.cafeteria.entity.Usuario;
import com.cafeteria.repository.PerfilClienteRepository;
import com.cafeteria.repository.RolRepository;
import com.cafeteria.repository.UsuarioRepository;
import com.cafeteria.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PerfilClienteRepository perfilClienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.cafeteria.mapper.UsuarioMapper usuarioMapper;
    
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
            .map(usuarioMapper::toDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuarioMapper.toDTO(usuario);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuarioMapper.toDTO(usuario);
    }
    
    @Override
    @Transactional
    public UsuarioDTO registrar(UsuarioDTO usuarioDTO) {
        // Validar que el correo no exista
        if (usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        
        // Asignar rol (por defecto CLIENTE si no se especifica)
        Integer idRol = usuarioDTO.getIdRol() != null ? usuarioDTO.getIdRol() : 3;
        Rol rol = rolRepository.findById(idRol)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO, rol, passwordEncoder.encode(usuarioDTO.getContrasena()));
        
        Usuario guardado = usuarioRepository.save(usuario);
        
        // Si es cliente, crear perfil
        if ("CLIENTE".equals(rol.getNombre())) {
            PerfilCliente perfil = new PerfilCliente();
            perfil.setUsuario(guardado);
            perfil.setTelefono(usuarioDTO.getTelefono());
            perfil.setDireccion(usuarioDTO.getDireccion());
            perfil.setPuntosFidelizacion(0);
            perfilClienteRepository.save(perfil);
        }
        
        return usuarioMapper.toDTO(guardado);
    }
    
    @Override
    @Transactional
    public UsuarioDTO actualizar(Integer id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        String encodedPassword = null;
        // Solo actualizar contraseña si se proporciona una nueva
        if (usuarioDTO.getContrasena() != null && !usuarioDTO.getContrasena().isEmpty()) {
            encodedPassword = passwordEncoder.encode(usuarioDTO.getContrasena());
        }
        
        Rol rol = null;
        // Solo admin puede cambiar rol y estado
        if (usuarioDTO.getIdRol() != null) {
            rol = rolRepository.findById(usuarioDTO.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        }
        
        usuarioMapper.updateEntity(usuario, usuarioDTO, rol, encodedPassword);
        
        Usuario actualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(actualizado);
    }
    
    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public void cambiarEstado(Integer id, Usuario.Estado estado) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstado(estado);
        usuarioRepository.save(usuario);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PerfilCliente obtenerPerfilCliente(Integer idUsuario) {
        return perfilClienteRepository.findByUsuario_IdUsuario(idUsuario)
            .orElse(null);
    }
    
    @Override
    @Transactional
    public PerfilCliente actualizarPerfilCliente(Integer idUsuario, PerfilCliente perfil) {
        PerfilCliente existente = perfilClienteRepository.findByUsuario_IdUsuario(idUsuario)
            .orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
        
        existente.setTelefono(perfil.getTelefono());
        existente.setDireccion(perfil.getDireccion());
        
        return perfilClienteRepository.save(existente);
    }
}
