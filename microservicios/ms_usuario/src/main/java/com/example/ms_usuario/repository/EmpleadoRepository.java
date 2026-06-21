package com.example.ms_usuario.repository;

import com.example.ms_usuario.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioId(Long usuarioId);
}