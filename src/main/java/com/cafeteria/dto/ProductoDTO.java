package com.cafeteria.dto;

import com.cafeteria.entity.Producto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    
    private Integer idProducto;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String descripcion;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;
    
    private BigDecimal impuesto;
    
    private Producto.Estado estado;
    
    private String imagenUrl;
    
    private Integer idCategoria;
    
    private String nombreCategoria;
    
    // Campos de inventario
    private Integer cantidadActual;
    private Integer stockMinimo;
    private String unidadMedida;
}
