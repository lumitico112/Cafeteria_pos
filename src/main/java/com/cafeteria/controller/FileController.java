package com.cafeteria.controller;

import com.cafeteria.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String filename = fileStorageService.storeFile(file);
        
        Map<String, String> response = new HashMap<>();
        response.put("filename", filename);
        response.put("message", "Archivo subido exitosamente");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String filename) {
        byte[] data = fileStorageService.loadFile(filename);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @DeleteMapping("/{filename:.+}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<Map<String, String>> deleteFile(@PathVariable String filename) {
        fileStorageService.deleteFile(filename);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Archivo eliminado exitosamente");
        
        return ResponseEntity.ok(response);
    }
}
