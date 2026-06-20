package com.example.ms_pedidos.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.ms_pedidos.controller.DetallePedidoControllerV2;
import com.example.ms_pedidos.dto.DetallePedidoResponseDTO;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
@Component
public class DetallePedidoModelAssembler implements RepresentationModelAssembler<DetallePedidoResponseDTO, EntityModel<DetallePedidoResponseDTO>> {
 @Override
    public EntityModel<DetallePedidoResponseDTO> toModel(DetallePedidoResponseDTO detalle) {
        return EntityModel.of(
                detalle,
                linkTo(methodOn(DetallePedidoControllerV2.class).obtenerDetalle(detalle.getId())).withSelfRel(),
                linkTo(methodOn(DetallePedidoControllerV2.class).listarDetalles()).withRel("detalles"),
                linkTo(methodOn(DetallePedidoControllerV2.class).actualizarDetalle(detalle.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(DetallePedidoControllerV2.class).eliminarDetalle(detalle.getId())).withRel("eliminar")
        );
    }
}
