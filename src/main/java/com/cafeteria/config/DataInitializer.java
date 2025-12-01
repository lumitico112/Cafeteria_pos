package com.cafeteria.config;

import com.cafeteria.entity.Rol;
import com.cafeteria.entity.Usuario;
import com.cafeteria.repository.RolRepository;
import com.cafeteria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.cafeteria.service.FileStorageService fileStorageService;
    
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("=== Iniciando DataInitializer ===");
        
        // Inicializar almacenamiento de archivos
        fileStorageService.init();
        log.info("Almacenamiento de archivos inicializado correctamente");
        
        long roleCount = rolRepository.count();
        log.info("Roles encontrados en base de datos: {}", roleCount);
        
        Rol adminRole = null;
        
        // Crear roles si no existen
        if (roleCount == 0) {
            log.info("No hay roles, creando roles por defecto...");
            
            Rol admin = new Rol();
            admin.setNombre("ADMIN");
            admin.setDescripcion("Administrador del sistema");
            adminRole = rolRepository.save(admin);
            log.info("Rol ADMIN creado con ID: {}", adminRole.getIdRol());
            
            Rol empleado = new Rol();
            empleado.setNombre("EMPLEADO");
            empleado.setDescripcion("Empleado de la cafetería");
            empleado = rolRepository.save(empleado);
            log.info("Rol EMPLEADO creado con ID: {}", empleado.getIdRol());
            
            Rol cliente = new Rol();
            cliente.setNombre("CLIENTE");
            cliente.setDescripcion("Usuario cliente de la cafetería");
            cliente = rolRepository.save(cliente);
            log.info("Rol CLIENTE creado con ID: {}", cliente.getIdRol());
        } else {
            log.info("Ya existen {} roles en la base de datos", roleCount);
            // Obtener el rol ADMIN existente
            adminRole = rolRepository.findByNombre("ADMIN")
                .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado en la base de datos"));
            log.info("Rol ADMIN encontrado con ID: {}", adminRole.getIdRol());
        }
        
        // Crear usuario admin por defecto (SIEMPRE verificar si no existe)
        long userCount = usuarioRepository.count();
        log.info("Usuarios encontrados en base de datos: {}", userCount);
        
        if (userCount == 0) {
            log.info("No hay usuarios, creando usuario admin por defecto...");
            
            Usuario adminUser = new Usuario();
            adminUser.setNombre("Admin");
            adminUser.setApellido("Sistema");
            adminUser.setCorreo("admin@cafeteria.com");
            adminUser.setContrasena(passwordEncoder.encode("admin123"));
            adminUser.setEstado(Usuario.Estado.ACTIVO);
            adminUser.setRol(adminRole);
            adminUser = usuarioRepository.save(adminUser);
            
            log.info("Usuario admin creado exitosamente con ID: {}", adminUser.getIdUsuario());
            log.info("Credenciales por defecto: admin@cafeteria.com / admin123");
        } else {
            log.info("Ya existen {} usuarios en la base de datos.", userCount);
        }
        
        log.info("=== DataInitializer finalizado exitosamente ===");
    }
}
