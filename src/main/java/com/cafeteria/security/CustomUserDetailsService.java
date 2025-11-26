package com.cafeteria.security;

import com.cafeteria.entity.Usuario;
import com.cafeteria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UsuarioRepository usuarioRepository;
    
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        
        if (usuario.getEstado() == Usuario.Estado.INACTIVO) {
            throw new UsernameNotFoundException("Usuario inactivo");
        }
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre()));
        
        // Agregar permisos específicos
        usuario.getRol().getPermisos().forEach(permiso -> {
            authorities.add(new SimpleGrantedAuthority(permiso.getModulo() + "_" + permiso.getAccion()));
        });
        
        return User.builder()
            .username(usuario.getCorreo())
            .password(usuario.getContrasena())
            .authorities(authorities)
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(usuario.getEstado() == Usuario.Estado.INACTIVO)
            .build();
    }
}
