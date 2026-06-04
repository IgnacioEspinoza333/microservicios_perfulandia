package com.example.ms_usuario.repository;

import com.example.ms_usuario.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    Optional<Permiso> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
    boolean existsByCodigoAndIdNot(String codigo, Long id);
}