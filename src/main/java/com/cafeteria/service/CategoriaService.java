package com.cafeteria.service;

import com.cafeteria.entity.Categoria;

import java.util.List;

public interface CategoriaService {
    List<Categoria> listarTodas();
    Categoria buscarPorId(Integer id);
    Categoria crear(Categoria categoria);
    Categoria actualizar(Integer id, Categoria categoria);
    void eliminar(Integer id);
}
