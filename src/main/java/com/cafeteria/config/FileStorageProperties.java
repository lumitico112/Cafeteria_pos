package com.cafeteria.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file")
@Data
public class FileStorageProperties {
    
    private String uploadDir = "./uploads";
    private long maxSize = 10485760; // 10MB in bytes
    private String[] allowedTypes = {"image/jpeg", "image/png", "image/jpg", "image/gif"};
}
