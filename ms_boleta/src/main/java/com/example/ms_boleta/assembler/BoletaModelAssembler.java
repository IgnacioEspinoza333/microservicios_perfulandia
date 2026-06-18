package com.example.ms_boleta.assembler;

import com.example.ms_boleta.controller.BoletaControllerV2;
import com.example.ms_boleta.dto.BoletaResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class BoletaModelAssembler implements RepresentationModelAssembler<BoletaResponseDTO, EntityModel<BoletaResponseDTO>> {

    @Override
    public EntityModel<BoletaResponseDTO> toModel(BoletaResponseDTO boleta) {
        return EntityModel.of(
                boleta,
                linkTo(methodOn(BoletaControllerV2.class).obtener(boleta.getId())).withSelfRel(),
                linkTo(methodOn(BoletaControllerV2.class).listar()).withRel("boletas")
        );
    }
}