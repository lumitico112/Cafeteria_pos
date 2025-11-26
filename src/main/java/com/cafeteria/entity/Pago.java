package com.cafeteria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodo;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;
    
    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado = EstadoPago.PENDIENTE;
    
    public enum MetodoPago {
        EFECTIVO, TARJETA, BILLETERA_DIGITAL
    }
    
    public enum EstadoPago {
        COMPLETADO, FALLIDO, PENDIENTE
    }
}
