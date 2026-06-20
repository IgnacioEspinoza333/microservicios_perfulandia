package com.example.ms_pedidos.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;
import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_pedidos.assembler.DetallePedidoModelAssembler;
import com.example.ms_pedidos.dto.DetallePedidoRequestDTO;
import com.example.ms_pedidos.dto.DetallePedidoResponseDTO;
import com.example.ms_pedidos.service.DetallePedidoService;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/detalles")
@RequiredArgsConstructor
public class DetallePedidoControllerV2 {
     private final DetallePedidoService detallePedidoService;
    private final DetallePedidoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<DetallePedidoResponseDTO>> listarDetalles() {
        List<EntityModel<DetallePedidoResponseDTO>> detalles = detallePedidoService.listarDetalles().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(detalles,
                linkTo(methodOn(DetallePedidoControllerV2.class).listarDetalles()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<DetallePedidoResponseDTO> obtenerDetalle(@PathVariable Long id) {
        DetallePedidoResponseDTO detalle = detallePedidoService.obtenerDetalle(id);
        return assembler.toModel(detalle);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DetallePedidoResponseDTO>> crearDetalle(@Valid @RequestBody DetallePedidoRequestDTO request) {
        DetallePedidoResponseDTO nuevo = detallePedidoService.crearDetalle(request);

        return ResponseEntity
                .created(linkTo(methodOn(DetallePedidoControllerV2.class).obtenerDetalle(nuevo.getId())).toUri())
                .body(assembler.toModel(nuevo));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<DetallePedidoResponseDTO>> actualizarDetalle(
            @PathVariable Long id,
            @Valid @RequestBody DetallePedidoRequestDTO request) {

        DetallePedidoResponseDTO actualizado = detallePedidoService.actualizarDetalle(id, request);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminarDetalle(@PathVariable Long id) {
        detallePedidoService.eliminarDetalle(id);
        return ResponseEntity.noContent().build();
    }

}
