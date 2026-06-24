package com.example.ms_clientes.assembler;

import com.example.ms_clientes.controller.ClienteControllerV2;
import com.example.ms_clientes.controller.DireccionControllerV2;
import com.example.ms_clientes.dto.ClienteResponseDto;
import com.example.ms_clientes.dto.ClienteUpdateDto;
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
                linkTo(methodOn(ClienteControllerV2.class).obtenerClientePorId(cliente.getId())).withSelfRel(),
                linkTo(methodOn(ClienteControllerV2.class).listarClientes()).withRel("clientes"),
                linkTo(methodOn(DireccionControllerV2.class).listarDireccionesPorCliente(cliente.getId())).withRel("direcciones"),
                linkTo(methodOn(ClienteControllerV2.class).actualizarCliente(cliente.getId(), new ClienteUpdateDto())).withRel("actualizar"),
                linkTo(methodOn(ClienteControllerV2.class).eliminarCliente(cliente.getId())).withRel("eliminar")
        );
    }
}