package com.cafeteria.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ExcelImportService {
    
    Map<String, Object> importUsuarios(MultipartFile file);
    
    Map<String, Object> importProductos(MultipartFile file);
    
    byte[] generateUsuariosTemplate();
    
    byte[] generateProductosTemplate();
}
