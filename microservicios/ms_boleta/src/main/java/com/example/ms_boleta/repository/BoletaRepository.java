package com.example.ms_boleta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_boleta.model.Boleta;

public interface BoletaRepository   extends JpaRepository<Boleta, Long> {
    boolean existsByNumero(String numero);
}
