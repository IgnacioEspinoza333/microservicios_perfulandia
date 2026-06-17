package com.example.ms_usuario.controller;

import com.example.ms_usuario.assembler.PermisoModelAssembler;
import com.example.ms_usuario.dto.PermisoRequestDto;
import com.example.ms_usuario.dto.PermisoResponseDto;
import com.example.ms_usuario.service.PermisoService;
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
@RequestMapping("/api/v2/permisos")
@RequiredArgsConstructor
public class PermisoControllerV2 {

    private final PermisoService permisoService;
    private final PermisoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<PermisoResponseDto>> listar() {
        List<EntityModel<PermisoResponseDto>> permisos = permisoService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                permisos,
                linkTo(methodOn(PermisoControllerV2.class).listar()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<PermisoResponseDto> obtenerPorId(@PathVariable Long id) {
        PermisoResponseDto permiso = permisoService.obtenerPorId(id);
        return assembler.toModel(permiso);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PermisoResponseDto>> crear(
            @Valid @RequestBody PermisoRequestDto dto) {

        PermisoResponseDto nuevoPermiso = permisoService.crear(dto);

        return ResponseEntity
                .created(linkTo(methodOn(PermisoControllerV2.class)
                        .obtenerPorId(nuevoPermiso.getId())).toUri())
                .body(assembler.toModel(nuevoPermiso));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PermisoResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PermisoRequestDto dto) {

        PermisoResponseDto actualizado = permisoService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        permisoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
