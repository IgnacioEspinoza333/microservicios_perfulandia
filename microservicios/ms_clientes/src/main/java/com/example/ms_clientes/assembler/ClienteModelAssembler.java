package com.example.ms_clientes.assembler;

import com.example.ms_clientes.controller.ClienteControllerV2;
import com.example.ms_clientes.controller.DireccionControllerV2;
import com.example.ms_clientes.dto.ClienteResponseDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ClienteModelAssembler implements RepresentationModelAssembler<ClienteResponseDto, EntityModel<ClienteResponseDto>> {

    @Override
    public EntityModel<ClienteResponseDto> toModel(ClienteResponseDto cliente) {
        return EntityModel.of(
                cliente,
                linkTo(methodOn(ClienteControllerV2.class).obtenerPorId(cliente.getId())).withSelfRel(),
                linkTo(methodOn(ClienteControllerV2.class).listar()).withRel("clientes"),
                linkTo(methodOn(DireccionControllerV2.class).listarPorCliente(cliente.getId())).withRel("direcciones"),
                linkTo(methodOn(ClienteControllerV2.class).actualizar(cliente.getId(), null)).withRel("actualizar"),
                linkTo(methodOn(ClienteControllerV2.class).eliminar(cliente.getId())).withRel("eliminar")
            );
    }
}