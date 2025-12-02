package com.cafeteria.repository;

import com.cafeteria.entity.GrantedPermission;
import com.cafeteria.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrantedPermissionRepository extends JpaRepository<GrantedPermission, Long> {
    List<GrantedPermission> findByRol(Rol rol);
}
