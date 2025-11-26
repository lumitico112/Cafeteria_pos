package com.cafeteria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permiso")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permiso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Integer idPermiso;
    
    @Column(nullable = false, length = 50)
    private String modulo;
    
    @Column(nullable = false, length = 50)
    private String accion;
}
