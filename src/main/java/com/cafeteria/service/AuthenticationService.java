package com.cafeteria.service;

import com.cafeteria.dto.AuthenticationResponse;
import com.cafeteria.dto.LoginRequest;
import com.cafeteria.dto.RegisterRequest;
import com.cafeteria.entity.Rol;
import com.cafeteria.entity.Usuario;
import com.cafeteria.repository.RolRepository;
import com.cafeteria.repository.UsuarioRepository;
import com.cafeteria.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    private final com.cafeteria.repository.PerfilClienteRepository perfilClienteRepository;
    private final com.cafeteria.repository.JwtTokenRepository jwtTokenRepository;
    private final com.cafeteria.repository.GrantedPermissionRepository grantedPermissionRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        Rol userRole = rolRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        var user = new Usuario();
        user.setNombre(request.getFirstname());
        user.setApellido(request.getLastname());
        user.setCorreo(request.getEmail());
        user.setContrasena(passwordEncoder.encode(request.getPassword()));
        user.setTelefono(request.getPhone());
        user.setDireccion(request.getAddress());
        user.setRol(userRole);
        user.setEstado(Usuario.Estado.ACTIVO);
        
        Usuario savedUser = usuarioRepository.save(user);
        
        // El trigger 'trg_crear_cliente_auto' se encargará de crear el registro en perfil_cliente
        // usando los datos de telefono y direccion que acabamos de guardar en usuario.
        
        // Load UserDetails to generate token (using the CustomUserDetailsService logic)
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getCorreo());
        var jwtToken = jwtService.generateToken(userDetails);
        
        saveUserToken(savedUser, jwtToken);

        // Obtener menús permitidos
        java.util.List<com.cafeteria.entity.Modulo> menus = grantedPermissionRepository.findByRol(userRole)
                .stream()
                .map(com.cafeteria.entity.GrantedPermission::getModulo)
                .collect(java.util.stream.Collectors.toList());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .idUsuario(user.getIdUsuario())
                .nombre(user.getNombre())
                .apellido(user.getApellido())
                .email(user.getCorreo())
                .rol(user.getRol().getNombre())
                .menu(menus)
                .build();
    }

    public AuthenticationResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        Usuario user = usuarioRepository.findByCorreo(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        var jwtToken = jwtService.generateToken(userDetails);
        
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        // Obtener menús permitidos
        java.util.List<com.cafeteria.entity.Modulo> menus = grantedPermissionRepository.findByRol(user.getRol())
                .stream()
                .map(com.cafeteria.entity.GrantedPermission::getModulo)
                .collect(java.util.stream.Collectors.toList());
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .idUsuario(user.getIdUsuario())
                .nombre(user.getNombre())
                .apellido(user.getApellido())
                .email(user.getCorreo())
                .rol(user.getRol().getNombre())
                .menu(menus)
                .build();
    }

    private void saveUserToken(Usuario user, String jwtToken) {
        var token = com.cafeteria.entity.JwtToken.builder()
                .usuario(user)
                .token(jwtToken)
                .isValid(true)
                .expiracion(new java.util.Date(System.currentTimeMillis() + 86400000)) // 24 hours
                .build();
        jwtTokenRepository.save(token);
    }

    private void revokeAllUserTokens(Usuario user) {
        var validUserTokens = jwtTokenRepository.findAllValidTokenByUser(user.getIdUsuario());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setValid(false);
        });
        jwtTokenRepository.saveAll(validUserTokens);
    }
}
