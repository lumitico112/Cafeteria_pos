package com.cafeteria.config;

import com.cafeteria.entity.Modulo;
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
    private final com.cafeteria.repository.ModuloRepository moduloRepository;
    private final com.cafeteria.repository.GrantedPermissionRepository grantedPermissionRepository;
    
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
        
        // Inicializar Módulos y Permisos
        if (moduloRepository.count() == 0) {
            log.info("Inicializando módulos y permisos...");
            
            // --- Módulos Generales (Sidebar) ---
            Modulo modDashboard = createModulo("DASHBOARD", "/dashboard", "Dashboard", "fas fa-tachometer-alt");
            Modulo modProductos = createModulo("PRODUCTOS", "/productos", "Productos", "fas fa-coffee");
            Modulo modCategorias = createModulo("CATEGORIAS", "/categorias", "Categorías", "fas fa-tags");
            Modulo modPedidos = createModulo("PEDIDOS", "/pedidos", "Pedidos", "fas fa-receipt");
            Modulo modInventario = createModulo("INVENTARIO", "/inventario", "Inventario", "fas fa-boxes");
            Modulo modUsuarios = createModulo("USUARIOS", "/usuarios", "Usuarios", "fas fa-users");
            Modulo modPromociones = createModulo("PROMOCIONES", "/promociones", "Promociones", "fas fa-percent");
            Modulo modReportes = createModulo("REPORTES", "/reportes", "Reportes", "fas fa-chart-bar");

            // --- Módulos Cliente (Navbar) ---
            Modulo modCatalogo = createModulo("CATALOGO", "/catalogo", "Catálogo", "fas fa-store");
            Modulo modCarrito = createModulo("CARRITO", "/carrito", "Carrito", "fas fa-shopping-cart");
            Modulo modMisPedidos = createModulo("MIS_PEDIDOS", "/historial", "Mis Pedidos", "fas fa-history");
            
            // --- Asignar Permisos ---

            // ADMIN: Todo el Sidebar
            assignPermission(adminRole, modDashboard);
            assignPermission(adminRole, modProductos);
            assignPermission(adminRole, modCategorias);
            assignPermission(adminRole, modPedidos);
            assignPermission(adminRole, modInventario);
            assignPermission(adminRole, modUsuarios);
            assignPermission(adminRole, modPromociones);
            assignPermission(adminRole, modReportes);
            
            // EMPLEADO: Inventario, Pedidos (y Productos opcional)
            Rol empleadoRole = rolRepository.findByNombre("EMPLEADO").orElse(null);
            if (empleadoRole != null) {
                assignPermission(empleadoRole, modInventario);
                assignPermission(empleadoRole, modPedidos);
                assignPermission(empleadoRole, modProductos); // Asumimos que pueden ver/editar productos
            }
            
            // CLIENTE: Catálogo, Carrito, Mis Pedidos
            Rol clienteRole = rolRepository.findByNombre("CLIENTE").orElse(null);
            if (clienteRole != null) {
                assignPermission(clienteRole, modCatalogo);
                assignPermission(clienteRole, modCarrito);
                assignPermission(clienteRole, modMisPedidos);
            }
            
            log.info("Módulos y permisos inicializados.");
        }

        log.info("=== DataInitializer finalizado exitosamente ===");
    }

    private Modulo createModulo(String nombre, String basePath, String label, String icon) {
        return moduloRepository.save(Modulo.builder()
                .nombre(nombre)
                .basePath(basePath)
                .label(label)
                .icon(icon)
                .build());
    }

    private void assignPermission(Rol rol, Modulo modulo) {
        grantedPermissionRepository.save(com.cafeteria.entity.GrantedPermission.builder()
                .rol(rol)
                .modulo(modulo)
                .build());
    }
}
