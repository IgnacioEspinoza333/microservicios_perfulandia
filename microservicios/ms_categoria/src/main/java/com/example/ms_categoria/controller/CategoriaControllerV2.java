package com.example.ms_categoria.controller;

import com.example.ms_categoria.assembler.CategoriaModelAssembler;
import com.example.ms_categoria.dto.CategoriaRequestDTO;
import com.example.ms_categoria.dto.CategoriaResponseDTO;
import com.example.ms_categoria.service.CategoriaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/categorias")
@RequiredArgsConstructor
public class CategoriaControllerV2 {

    private final CategoriaService categoriaService;
    private final CategoriaModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CategoriaResponseDTO>> crear(@RequestBody CategoriaRequestDTO request) {
        CategoriaResponseDTO nuevaCategoria = categoriaService.crearCategoria(request);

        return ResponseEntity
                .created(linkTo(methodOn(CategoriaControllerV2.class).obtener(nuevaCategoria.getId())).toUri())
                .body(assembler.toModel(nuevaCategoria));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<CategoriaResponseDTO> obtener(@PathVariable Long id) {
        CategoriaResponseDTO categoria = categoriaService.obtenerCategoria(id);
        return assembler.toModel(categoria);
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<CategoriaResponseDTO>> listar() {
        List<EntityModel<CategoriaResponseDTO>> categorias = categoriaService.listarCategorias().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                categorias,
                linkTo(methodOn(CategoriaControllerV2.class).listar()).withSelfRel()
        );
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<CategoriaResponseDTO>> actualizarCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO request) {

        CategoriaResponseDTO actualizada = categoriaService.actualizarCategoria(id, request);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }
}