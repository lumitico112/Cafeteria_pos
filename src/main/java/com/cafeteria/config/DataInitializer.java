package com.cafeteria.config;

import com.cafeteria.entity.Rol;
import com.cafeteria.entity.Usuario;
import com.cafeteria.repository.RolRepository;
import com.cafeteria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        if (rolRepository.count() == 0) {
            Rol admin = new Rol();
            admin.setNombre("ADMIN");
            admin.setDescripcion("Administrador del sistema");
            rolRepository.save(admin);
            
            Rol empleado = new Rol();
            empleado.setNombre("EMPLEADO");
            empleado.setDescripcion("Empleado de la cafetería");
            rolRepository.save(empleado);
            
            Rol cliente = new Rol();
            cliente.setNombre("CLIENTE");
            cliente.setDescripcion("Usuario cliente de la cafetería");
            rolRepository.save(cliente);
            
            // Crear usuario admin por defecto
            if (usuarioRepository.count() == 0) {
                Usuario adminUser = new Usuario();
                adminUser.setNombre("Admin");
                adminUser.setApellido("Sistema");
                adminUser.setCorreo("admin@cafeteria.com");
                adminUser.setContrasena(passwordEncoder.encode("admin123"));
                adminUser.setEstado(Usuario.Estado.ACTIVO);
                adminUser.setRol(admin);
                usuarioRepository.save(adminUser);
            }
        }
    }
}
