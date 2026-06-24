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

@RestController
@RequestMapping("/api/v2/pedidos")
@RequiredArgsConstructor
public class PedidoControllerV2 {

    private final PedidoService pedidoService;
    private final PedidoModelAssembler assembler;

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PedidoResponseDTO>> crear(@RequestBody PedidoRequestDTO request) {
        PedidoResponseDTO nuevoPedido = pedidoService.crearPedido(request);

        return ResponseEntity
                .created(linkTo(methodOn(PedidoControllerV2.class).obtener(nuevoPedido.getId())).toUri())
                .body(assembler.toModel(nuevoPedido));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<PedidoResponseDTO> obtener(@PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.obtenerPedido(id);
        return assembler.toModel(pedido);
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
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
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
public ResponseEntity<EntityModel<PedidoResponseDTO>> actualizar(
        @PathVariable Long id,
        @RequestBody PedidoRequestDTO request) {

    PedidoResponseDTO actualizado = pedidoService.actualizarPedido(id, request);
    return ResponseEntity.ok(assembler.toModel(actualizado));
}

  @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    pedidoService.eliminarPedido(id);
    return ResponseEntity.noContent().build();
}

}