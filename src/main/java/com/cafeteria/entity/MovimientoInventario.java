package com.cafeteria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Integer idMovimiento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(length = 255)
    private String motivo;
    
    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
    
    public enum Tipo {
        ENTRADA, SALIDA
    }
}
