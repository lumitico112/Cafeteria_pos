package com.cafeteria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "caja")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Caja {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_caja")
    private Integer idCaja;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @Column(name = "fecha_apertura", nullable = false)
    private LocalDateTime fechaApertura = LocalDateTime.now();
    
    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;
    
    @Column(name = "saldo_inicial", nullable = false, precision = 10, scale = 2)
    private BigDecimal saldoInicial;
    
    @Column(name = "saldo_final", precision = 10, scale = 2)
    private BigDecimal saldoFinal;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal diferencias;
}
