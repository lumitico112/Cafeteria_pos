package com.cafeteria.controller;

import com.cafeteria.dto.ReportFilterDTO;
import com.cafeteria.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/boleta/{pedidoId}")
    public ResponseEntity<ByteArrayResource> generarBoleta(@PathVariable Integer pedidoId) {
        byte[] pdfData = reportService.generateBoletaPDF(pedidoId);
        ByteArrayResource resource = new ByteArrayResource(pdfData);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"boleta_" + pedidoId + ".pdf\"")
                .body(resource);
    }

    @GetMapping("/ticket/{pedidoId}")
    public ResponseEntity<String> generarTicket(@PathVariable Integer pedidoId) {
        String ticketASCII = reportService.generateTicketASCII(pedidoId);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ticket_" + pedidoId + ".txt\"")
                .body(ticketASCII);
    }

    @GetMapping("/pedidos")
    public ResponseEntity<ByteArrayResource> generarReportePedidos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer idUsuario,
            @RequestParam(defaultValue = "PDF") String formato) {

        ReportFilterDTO filter = ReportFilterDTO.builder()
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .estado(estado)
                .idUsuario(idUsuario)
                .formato(formato)
                .build();

        byte[] data = reportService.generatePedidosReport(filter);
        ByteArrayResource resource = new ByteArrayResource(data);

        String filename = "reporte_pedidos." + (formato.equalsIgnoreCase("EXCEL") ? "xlsx" : "pdf");
        MediaType mediaType = formato.equalsIgnoreCase("EXCEL") ? 
            MediaType.APPLICATION_OCTET_STREAM : MediaType.APPLICATION_PDF;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @GetMapping("/productos")
    public ResponseEntity<ByteArrayResource> generarReporteProductos(
            @RequestParam(required = false) Integer idCategoria,
            @RequestParam(defaultValue = "PDF") String formato) {

        ReportFilterDTO filter = ReportFilterDTO.builder()
                .idCategoria(idCategoria)
                .formato(formato)
                .build();

        byte[] data = reportService.generateProductosReport(filter);
        ByteArrayResource resource = new ByteArrayResource(data);

        String filename = "reporte_productos." + (formato.equalsIgnoreCase("EXCEL") ? "xlsx" : "pdf");
        MediaType mediaType = formato.equalsIgnoreCase("EXCEL") ? 
            MediaType.APPLICATION_OCTET_STREAM : MediaType.APPLICATION_PDF;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ByteArrayResource> generarReporteUsuarios(
            @RequestParam(defaultValue = "PDF") String formato) {

        ReportFilterDTO filter = ReportFilterDTO.builder()
                .formato(formato)
                .build();

        byte[] data = reportService.generateUsuariosReport(filter);
        ByteArrayResource resource = new ByteArrayResource(data);

        String filename = "reporte_usuarios." + (formato.equalsIgnoreCase("EXCEL") ? "xlsx" : "pdf");
        MediaType mediaType = formato.equalsIgnoreCase("EXCEL") ? 
            MediaType.APPLICATION_OCTET_STREAM : MediaType.APPLICATION_PDF;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @GetMapping("/inventario")
    public ResponseEntity<ByteArrayResource> generarReporteInventario(
            @RequestParam(defaultValue = "PDF") String formato) {

        ReportFilterDTO filter = ReportFilterDTO.builder()
                .formato(formato)
                .build();

        byte[] data = reportService.generateInventarioReport(filter);
        ByteArrayResource resource = new ByteArrayResource(data);

        String filename = "reporte_inventario." + (formato.equalsIgnoreCase("EXCEL") ? "xlsx" : "pdf");
        MediaType mediaType = formato.equalsIgnoreCase("EXCEL") ? 
            MediaType.APPLICATION_OCTET_STREAM : MediaType.APPLICATION_PDF;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
}
