package com.example.ms_usuario.controller;

import com.example.ms_usuario.assembler.RolModelAssembler;
import com.example.ms_usuario.dto.RolRequestDto;
import com.example.ms_usuario.dto.RolResponseDto;
import com.example.ms_usuario.service.RolService;
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
@RequestMapping("/api/v2/roles")
@RequiredArgsConstructor
public class RolControllerV2 {

    private final RolService rolService;
    private final RolModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<RolResponseDto>> listar() {
        List<EntityModel<RolResponseDto>> roles = rolService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                roles,
                linkTo(methodOn(RolControllerV2.class).listar()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<RolResponseDto> obtenerPorId(@PathVariable Long id) {
        RolResponseDto rol = rolService.obtenerPorId(id);
        return assembler.toModel(rol);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RolResponseDto>> crear(
            @Valid @RequestBody RolRequestDto dto) {

        RolResponseDto nuevoRol = rolService.crear(dto);

        return ResponseEntity
                .created(linkTo(methodOn(RolControllerV2.class)
                        .obtenerPorId(nuevoRol.getId())).toUri())
                .body(assembler.toModel(nuevoRol));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RolResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RolRequestDto dto) {

        RolResponseDto actualizado = rolService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        rolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}