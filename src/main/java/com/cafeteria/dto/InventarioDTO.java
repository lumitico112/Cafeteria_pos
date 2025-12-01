package com.cafeteria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioDTO {
    
    private Integer idInventario;
    private Integer idProducto;
    private String nombreProducto;
    private Integer cantidadActual;
    private Integer stockMinimo;
    private String unidadMedida;
}
