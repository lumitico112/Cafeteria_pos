package com.cafeteria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private java.util.List<com.cafeteria.entity.Modulo> menu;
}
