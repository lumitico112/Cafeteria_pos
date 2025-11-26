package com.cafeteria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer idReserva;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Producto producto;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoReserva tipo;
    
    @Column(name = "fecha_reserva", nullable = false)
    private LocalDate fechaReserva;
    
    @Column(name = "hora_reserva", nullable = false)
    private LocalTime horaReserva;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReserva estado = EstadoReserva.ACTIVA;
    
    public enum TipoReserva {
        MESA, PRODUCTO
    }
    
    public enum EstadoReserva {
        ACTIVA, VENCIDA, CANCELADA, COMPLETADA
    }
}
