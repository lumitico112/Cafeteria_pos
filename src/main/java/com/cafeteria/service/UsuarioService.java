package com.cafeteria.service;

import com.cafeteria.dto.UsuarioDTO;
import com.cafeteria.entity.PerfilCliente;
import com.cafeteria.entity.Usuario;

import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> listarTodos();
    UsuarioDTO buscarPorId(Integer id);
    UsuarioDTO buscarPorCorreo(String correo);
    UsuarioDTO registrar(UsuarioDTO usuarioDTO);
    UsuarioDTO actualizar(Integer id, UsuarioDTO usuarioDTO);
    void eliminar(Integer id);
    void cambiarEstado(Integer id, Usuario.Estado estado);
    PerfilCliente obtenerPerfilCliente(Integer idUsuario);
    PerfilCliente actualizarPerfilCliente(Integer idUsuario, PerfilCliente perfil);
}
