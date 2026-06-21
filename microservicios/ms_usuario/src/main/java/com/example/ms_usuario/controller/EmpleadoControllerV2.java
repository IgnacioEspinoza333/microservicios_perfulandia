package com.example.ms_usuario.controller;

import com.example.ms_usuario.assembler.EmpleadoModelAssembler;
import com.example.ms_usuario.dto.EmpleadoRequestDto;
import com.example.ms_usuario.dto.EmpleadoResponseDto;
import com.example.ms_usuario.service.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/empleados")
@RequiredArgsConstructor
public class EmpleadoControllerV2 {

    private final EmpleadoService empleadoService;
    private final EmpleadoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<EmpleadoResponseDto>> listar() {
        List<EntityModel<EmpleadoResponseDto>> empleados = empleadoService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                empleados,
                linkTo(methodOn(EmpleadoControllerV2.class).listar()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<EmpleadoResponseDto> obtenerPorId(@PathVariable Long id) {
        EmpleadoResponseDto empleado = empleadoService.obtenerPorId(id);
        return assembler.toModel(empleado);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EmpleadoResponseDto>> crear(
            @Valid @RequestBody EmpleadoRequestDto dto) {

        EmpleadoResponseDto nuevoEmpleado = empleadoService.crear(dto);

        return ResponseEntity
                .created(linkTo(methodOn(EmpleadoControllerV2.class)
                        .obtenerPorId(nuevoEmpleado.getId())).toUri())
                .body(assembler.toModel(nuevoEmpleado));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EmpleadoResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmpleadoRequestDto dto) {

        EmpleadoResponseDto actualizado = empleadoService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        empleadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}