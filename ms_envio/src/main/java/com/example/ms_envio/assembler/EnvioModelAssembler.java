package com.example.ms_envio.assembler;

import com.example.ms_envio.controller.EnvioControllerV2;
import com.example.ms_envio.dto.EnvioResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EnvioModelAssembler implements RepresentationModelAssembler<EnvioResponseDTO, EntityModel<EnvioResponseDTO>> {

    @Override
    public EntityModel<EnvioResponseDTO> toModel(EnvioResponseDTO envio) {
        return EntityModel.of(
                envio,
                linkTo(methodOn(EnvioControllerV2.class).obtener(envio.getId())).withSelfRel(),
                linkTo(methodOn(EnvioControllerV2.class).listar()).withRel("envios"),
                 linkTo(methodOn(EnvioControllerV2.class).actualizarEnvio(envio.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(EnvioControllerV2.class).eliminarEnvio(envio.getId())).withRel("eliminar")
            );
    }
}