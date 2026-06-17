package com.example.ms_usuario.service;

import java.util.List;

public interface UsuarioRolService {

    void asignarRol(Long usuarioId, Long rolId);

    void quitarRol(Long usuarioId, Long rolId);

    List<String> listarRolesDeUsuario(Long usuarioId);
}