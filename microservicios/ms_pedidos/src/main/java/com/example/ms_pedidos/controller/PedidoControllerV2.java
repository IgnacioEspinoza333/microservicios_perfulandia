package com.example.ms_pedidos.controller;

import com.example.ms_pedidos.assembler.PedidoModelAssembler;
import com.example.ms_pedidos.dto.PedidoRequestDTO;
import com.example.ms_pedidos.dto.PedidoResponseDTO;
import com.example.ms_pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v2/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "API para la gestión de pedidos con soporte HATEOAS")
public class PedidoControllerV2 {

    private final PedidoService pedidoService;
    private final PedidoModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear pedido", description = "Registra un nuevo pedido en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EntityModel<PedidoResponseDTO>> crear(@RequestBody PedidoRequestDTO request) {
        PedidoResponseDTO nuevoPedido = pedidoService.crearPedido(request);

        return ResponseEntity
                .created(linkTo(methodOn(PedidoControllerV2.class).obtener(nuevoPedido.getId())).toUri())
                .body(assembler.toModel(nuevoPedido));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener pedido por ID", description = "Obtiene un pedido según su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public EntityModel<PedidoResponseDTO> obtener(@PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.obtenerPedido(id);
        return assembler.toModel(pedido);
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar pedidos", description = "Obtiene todos los pedidos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public CollectionModel<EntityModel<PedidoResponseDTO>> listar() {
        List<EntityModel<PedidoResponseDTO>> pedidos = pedidoService.listarPedidos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                pedidos,
                linkTo(methodOn(PedidoControllerV2.class).listar()).withSelfRel()
        );
    }

    @PutMapping(value = "/{id}/cancelar", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Cancelar pedido", description = "Cancela un pedido existente mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido cancelado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar pedido", description = "Actualiza la información de un pedido existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<EntityModel<PedidoResponseDTO>> actualizar(
            @PathVariable Long id,
            @RequestBody PedidoRequestDTO request) {

        PedidoResponseDTO actualizado = pedidoService.actualizarPedido(id, request);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar pedido", description = "Elimina un pedido por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}