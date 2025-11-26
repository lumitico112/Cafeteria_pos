package com.cafeteria.service.impl;

import com.cafeteria.entity.Categoria;
import com.cafeteria.repository.CategoriaRepository;
import com.cafeteria.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {
    
    private final CategoriaRepository categoriaRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Categoria buscarPorId(Integer id) {
        return categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }
    
    @Override
    @Transactional
    public Categoria crear(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }
    
    @Override
    @Transactional
    public Categoria actualizar(Integer id, Categoria categoria) {
        Categoria existente = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        existente.setNombre(categoria.getNombre());
        existente.setDescripcion(categoria.getDescripcion());
        
        return categoriaRepository.save(existente);
    }
    
    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoriaRepository.deleteById(id);
    }
}
