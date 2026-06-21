package com.example.ms_inventario.repository;

import com.example.ms_inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    Optional<Inventario> findByProductoId(Long productoId);
    boolean existsByProductoId(Long productoId);
    boolean existsByProductoIdAndIdNot(Long productoId, Long id);
}
