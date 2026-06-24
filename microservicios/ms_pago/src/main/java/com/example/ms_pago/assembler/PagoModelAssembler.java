package com.example.ms_pago.assembler;

import com.example.ms_pago.controller.PagoControllerV2;
import com.example.ms_pago.dto.PagoResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PagoModelAssembler implements RepresentationModelAssembler<PagoResponseDTO, EntityModel<PagoResponseDTO>> {

    @Override
    public EntityModel<PagoResponseDTO> toModel(PagoResponseDTO pago) {
        return EntityModel.of(
                pago,
                linkTo(methodOn(PagoControllerV2.class).obtener(pago.getId())).withSelfRel(),
                linkTo(methodOn(PagoControllerV2.class).listar()).withRel("pagos"),
                linkTo(methodOn(PagoControllerV2.class).actualizarPago(pago.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(PagoControllerV2.class).eliminarPago(pago.getId())).withRel("eliminar")
        );
    }
}