package com.example.ms_pago.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ms_pago.assembler.PagoModelAssembler;
import com.example.ms_pago.dto.PagoRequestDTO;
import com.example.ms_pago.dto.PagoResponseDTO;
import com.example.ms_pago.service.PagoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/pagos")
@RequiredArgsConstructor
public class PagoControllerV2 {

    private final PagoService pagoService;
    private final PagoModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PagoResponseDTO>> crear(@RequestBody PagoRequestDTO request) {
        PagoResponseDTO nuevoPago = pagoService.crearPago(request);

        return ResponseEntity
                .created(linkTo(methodOn(PagoControllerV2.class).obtener(nuevoPago.getId())).toUri())
                .body(assembler.toModel(nuevoPago));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<PagoResponseDTO> obtener(@PathVariable Long id) {
        PagoResponseDTO pago = pagoService.obtenerPago(id);
        return assembler.toModel(pago);
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<PagoResponseDTO>> listar() {
        List<EntityModel<PagoResponseDTO>> pagos = pagoService.listarPagos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                pagos,
                linkTo(methodOn(PagoControllerV2.class).listar()).withSelfRel()
        );
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }
      @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PagoResponseDTO>> actualizarPago(
            @PathVariable Long id,
            @Valid @RequestBody PagoRequestDTO request) {

        PagoResponseDTO actualizado = pagoService.actualizarPago(id, request);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }
}