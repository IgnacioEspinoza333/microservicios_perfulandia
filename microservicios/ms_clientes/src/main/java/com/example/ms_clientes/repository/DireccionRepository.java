package com.example.ms_clientes.repository;

import com.example.ms_clientes.model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    List<Direccion> findByClienteId(Long clienteId);
    Optional<Direccion> findByClienteIdAndPrincipalTrue(Long clienteId);
    boolean existsByClienteId(Long clienteId);
    void deleteByClienteId(Long clienteId);
}