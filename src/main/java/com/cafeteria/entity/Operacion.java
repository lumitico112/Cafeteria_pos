package com.cafeteria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OPERACION")
public class Operacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    
    @Column(name = "http_method")
    private String httpMethod;
    
    private String path;
    
    @Column(name = "permit_all")
    private boolean permitAll;
    
    @ManyToOne
    @JoinColumn(name = "modulo_id")
    private Modulo modulo;
}
