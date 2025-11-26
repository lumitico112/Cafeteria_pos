package com.cafeteria.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "JWT_TOKEN")
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    
    @Column(name = "is_valid")
    private boolean isValid;
    
    private Date expiracion;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
