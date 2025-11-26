package com.cafeteria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFilterDTO {
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String estado;
    private Integer idUsuario;
    private Integer idCategoria;
    private String tipoReporte; // VENTAS, PEDIDOS, INVENTARIO, USUARIOS, PRODUCTOS
    private String formato; // PDF, EXCEL
}
