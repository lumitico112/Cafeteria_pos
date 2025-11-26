package com.cafeteria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "perfil_cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilCliente {
    
    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    
    @Column(length = 20)
    private String telefono;
    
    @Column(length = 255)
    private String direccion;
    
    @Column(name = "puntos_fidelizacion")
    private Integer puntosFidelizacion = 0;
}
