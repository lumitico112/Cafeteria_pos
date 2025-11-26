package com.cafeteria.service;

import com.cafeteria.entity.Inventario;
import com.cafeteria.entity.MovimientoInventario;

import java.util.List;

public interface InventarioService {
    Inventario obtenerPorProducto(Integer idProducto);
    List<Inventario> listarProductosBajoStock();
    void registrarMovimiento(MovimientoInventario movimiento);
    List<MovimientoInventario> obtenerMovimientosPorProducto(Integer idProducto);
    void ajustarStock(Integer idProducto, Integer cantidad, String motivo, Integer idUsuario);

    // Nuevo método
    List<Inventario> listarTodos();
}
