package com.example.ms_pedidos.assembler;

import com.example.ms_pedidos.controller.PedidoControllerV2;
import com.example.ms_pedidos.dto.PedidoResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PedidoModelAssembler implements RepresentationModelAssembler<PedidoResponseDTO, EntityModel<PedidoResponseDTO>> {

    @Override
    public EntityModel<PedidoResponseDTO> toModel(PedidoResponseDTO pedido) {
        return EntityModel.of(
                pedido,
                linkTo(methodOn(PedidoControllerV2.class).obtener(pedido.getId())).withSelfRel(),
                linkTo(methodOn(PedidoControllerV2.class).listar()).withRel("pedidos"),
                linkTo(methodOn(PedidoControllerV2.class).actualizar(pedido.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(PedidoControllerV2.class).eliminar(pedido.getId())).withRel("eliminar")
        );
    }
}