package com.cafeteria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromocionDTO {
    private Integer idPromocion;

    @NotBlank(message = "Nombre es requerido")
    private String nombre;

    private String descripcion;

    @NotNull(message = "Tipo es requerido")
    private String tipo; // DESCUENTO, COMBO, DOS_POR_UNO

    @NotNull(message = "Fecha inicio es requerida")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @NotNull(message = "Fecha fin es requerida")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaFin;

    @NotNull(message = "Estado es requerido")
    private String estado; // ACTIVA, INACTIVA

    // ids de productos seleccionados en el formulario (multi-select)
    private Set<Integer> productoIds;
}
