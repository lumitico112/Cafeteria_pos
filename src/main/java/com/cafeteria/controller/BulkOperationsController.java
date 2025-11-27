package com.cafeteria.controller;

import com.cafeteria.service.ExcelImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/bulk")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class BulkOperationsController {

    private final ExcelImportService excelImportService;

    @PostMapping("/usuarios/import")
    public ResponseEntity<Map<String, Object>> importUsuarios(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El archivo está vacío"));
        }

        if (!file.getOriginalFilename().endsWith(".xlsx")) {
            return ResponseEntity.badRequest().body(Map.of("message", "El archivo debe ser formato .xlsx"));
        }

        Map<String, Object> result = excelImportService.importUsuarios(file);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/productos/import")
    public ResponseEntity<Map<String, Object>> importProductos(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El archivo está vacío"));
        }

        if (!file.getOriginalFilename().endsWith(".xlsx")) {
            return ResponseEntity.badRequest().body(Map.of("message", "El archivo debe ser formato .xlsx"));
        }

        Map<String, Object> result = excelImportService.importProductos(file);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/template/usuarios")
    public ResponseEntity<ByteArrayResource> downloadUsuariosTemplate() {
        byte[] template = excelImportService.generateUsuariosTemplate();
        ByteArrayResource resource = new ByteArrayResource(template);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"plantilla_usuarios.xlsx\"")
                .body(resource);
    }

    @GetMapping("/template/productos")
    public ResponseEntity<ByteArrayResource> downloadProductosTemplate() {
        byte[] template = excelImportService.generateProductosTemplate();
        ByteArrayResource resource = new ByteArrayResource(template);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"plantilla_productos.xlsx\"")
                .body(resource);
    }
}
