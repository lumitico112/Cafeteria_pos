package com.cafeteria.service.impl;

import com.cafeteria.config.FileStorageProperties;
import com.cafeteria.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final FileStorageProperties fileStorageProperties;
    
    @Override
    public String storeFile(MultipartFile file) {
        // Validate file
        if (file.isEmpty()) {
            throw new RuntimeException("No se puede almacenar un archivo vacío");
        }
        
        if (!isValidFileType(file.getContentType())) {
            throw new RuntimeException("Tipo de archivo no permitido: " + file.getContentType());
        }
        
        if (!isValidFileSize(file.getSize())) {
            throw new RuntimeException("El archivo excede el tamaño máximo permitido");
        }
        
        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = getFileStorageLocation();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generate unique filename
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExtension;
            
            // Store file
            Path targetLocation = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return newFilename;
        } catch (IOException ex) {
            throw new RuntimeException("Error al almacenar archivo: " + ex.getMessage(), ex);
        }
    }
    
    @Override
    public byte[] loadFile(String filename) {
        try {
            Path filePath = getFileStorageLocation().resolve(filename).normalize();
            
            if (!Files.exists(filePath)) {
                throw new RuntimeException("Archivo no encontrado: " + filename);
            }
            
            return Files.readAllBytes(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Error al cargar archivo: " + ex.getMessage(), ex);
        }
    }
    
    @Override
    public void deleteFile(String filename) {
        try {
            Path filePath = getFileStorageLocation().resolve(filename).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Error al eliminar archivo: " + ex.getMessage(), ex);
        }
    }
    
    @Override
    public Path getFileStorageLocation() {
        return Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
    }
    
    @Override
    public boolean isValidFileType(String contentType) {
        if (contentType == null) {
            return false;
        }
        return Arrays.asList(fileStorageProperties.getAllowedTypes()).contains(contentType);
    }
    
    @Override
    public boolean isValidFileSize(long size) {
        return size <= fileStorageProperties.getMaxSize();
    }
}
