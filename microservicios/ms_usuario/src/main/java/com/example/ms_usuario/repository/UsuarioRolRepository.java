package com.example.ms_usuario.repository;

import com.example.ms_usuario.model.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, Long> {
    List<UsuarioRol> findByUsuarioId(Long usuarioId);
    List<UsuarioRol> findByRolId(Long rolId);
    boolean existsByUsuarioIdAndRolId(Long usuarioId, Long rolId);
    void deleteByUsuarioIdAndRolId(Long usuarioId, Long rolId);
    void deleteByUsuarioId(Long usuarioId);
    void deleteByRolId(Long rolId);
}
