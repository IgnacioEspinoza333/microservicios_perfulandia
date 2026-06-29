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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/empleados")
@RequiredArgsConstructor
@Tag(name = "Empleados", description = "API para la gestión de empleados con soporte HATEOAS")
public class EmpleadoControllerV2 {

    private final EmpleadoService empleadoService;
    private final EmpleadoModelAssembler assembler;

    @GetMapping(produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Listar empleados", description = "Obtiene todos los empleados registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public CollectionModel<EntityModel<EmpleadoResponseDto>> listar() {
        List<EntityModel<EmpleadoResponseDto>> empleados = empleadoService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                empleados,
                linkTo(methodOn(EmpleadoControllerV2.class).listar()).withSelfRel()
        );
    }

    @GetMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Obtener empleado por ID", description = "Obtiene un empleado según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmpleadoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    public EntityModel<EmpleadoResponseDto> obtenerPorId(@PathVariable Long id) {
        EmpleadoResponseDto empleado = empleadoService.obtenerPorId(id);
        return assembler.toModel(empleado);
    }

    @PostMapping(produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Crear empleado", description = "Registra un nuevo empleado en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleado creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmpleadoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<EmpleadoResponseDto>> crear(
            @Valid @RequestBody EmpleadoRequestDto dto) {

        EmpleadoResponseDto nuevoEmpleado = empleadoService.crear(dto);

        return ResponseEntity
                .created(linkTo(methodOn(EmpleadoControllerV2.class)
                        .obtenerPorId(nuevoEmpleado.getId())).toUri())
                .body(assembler.toModel(nuevoEmpleado));
    }

    @PutMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Actualizar empleado", description = "Actualiza la información de un empleado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmpleadoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    public ResponseEntity<EntityModel<EmpleadoResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmpleadoRequestDto dto) {

        EmpleadoResponseDto actualizado = empleadoService.actualizar(id, dto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
    @Operation(summary = "Eliminar empleado", description = "Elimina un empleado por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empleado eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        empleadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}