package com.example.ms_usuario.service.impl;

import com.example.ms_usuario.model.Rol;
import com.example.ms_usuario.model.Usuario;
import com.example.ms_usuario.model.UsuarioRol;
import com.example.ms_usuario.repository.RolRepository;
import com.example.ms_usuario.repository.UsuarioRepository;
import com.example.ms_usuario.repository.UsuarioRolRepository;
import com.example.ms_usuario.service.UsuarioRolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioRolServiceImpl implements UsuarioRolService {

    private final UsuarioRolRepository usuarioRolRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Override
    public void asignarRol(Long usuarioId, Long rolId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + rolId));

        boolean existeRelacion = usuarioRolRepository.existsByUsuarioIdAndRolId(usuarioId, rolId);
        if (existeRelacion) {
            throw new RuntimeException("El usuario ya tiene asignado ese rol.");
        }

        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);

        usuarioRolRepository.save(usuarioRol);
    }

    @Override
    public void quitarRol(Long usuarioId, Long rolId) {
        boolean existeRelacion = usuarioRolRepository.existsByUsuarioIdAndRolId(usuarioId, rolId);
        if (!existeRelacion) {
            throw new RuntimeException("La relación usuario-rol no existe.");
        }

        usuarioRolRepository.deleteByUsuarioIdAndRolId(usuarioId, rolId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> listarRolesDeUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException("Usuario no encontrado con id: " + usuarioId);
        }

        return usuarioRolRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(usuarioRol -> usuarioRol.getRol().getNombre())
                .toList();
    }
}