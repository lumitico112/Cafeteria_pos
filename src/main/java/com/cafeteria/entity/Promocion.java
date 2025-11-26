package com.cafeteria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "promocion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promocion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promocion")
    private Integer idPromocion;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(length = 255)
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPromocion tipo;
    
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;
    
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPromocion estado = EstadoPromocion.ACTIVA;
    
    @ManyToMany
    @JoinTable(
        name = "producto_promocion",
        joinColumns = @JoinColumn(name = "id_promocion"),
        inverseJoinColumns = @JoinColumn(name = "id_producto")
    )
    private Set<Producto> productos = new HashSet<>();
    
    public enum TipoPromocion {
        DESCUENTO, COMBO, DOS_POR_UNO
    }
    
    public enum EstadoPromocion {
        ACTIVA, INACTIVA
    }
}
