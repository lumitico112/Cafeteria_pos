package com.cafeteria.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileStorageService {
    
    String storeFile(MultipartFile file);
    
    void init();
    
    byte[] loadFile(String filename);
    
    void deleteFile(String filename);
    
    Path getFileStorageLocation();
    
    boolean isValidFileType(String contentType);
    
    boolean isValidFileSize(long size);
}
