package com.example.ms_categoria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_categoria.modelo.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
  boolean existsByNombre(String nombre);
}
