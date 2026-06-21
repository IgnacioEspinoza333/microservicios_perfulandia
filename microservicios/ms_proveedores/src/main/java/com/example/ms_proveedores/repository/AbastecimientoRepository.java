package com.example.ms_proveedores.repository;

import com.example.ms_proveedores.model.Abastecimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbastecimientoRepository extends JpaRepository<Abastecimiento, Long> {
    List<Abastecimiento> findByProveedorId(Long proveedorId);
    List<Abastecimiento> findByProductoId(Long productoId);
    boolean existsByProveedorId(Long proveedorId);
    void deleteByProveedorId(Long proveedorId);
}
