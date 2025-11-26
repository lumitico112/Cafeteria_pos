package com.cafeteria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bitacora")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bitacora {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bitacora")
    private Integer idBitacora;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();
    
    @Column(length = 50)
    private String modulo;
    
    @Column(length = 50)
    private String accion;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
}
