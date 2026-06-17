package com.example.ms_clientes.assembler;

import com.example.ms_clientes.controller.ClienteController;
import com.example.ms_clientes.dto.ClienteResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class ClienteModelAssembler implements RepresentationModelAssembler<ClienteResponseDTO, EntityModel<ClienteResponseDTO>>{
   @Override
    public EntityModel<ClienteResponseDTO> toModel(ClienteResponseDTO cliente) {
        return EntityModel.of(cliente,
                // Link "self": enlace directo al cliente específico
                linkTo(methodOn(ClienteController.class).buscarPorId(cliente.getId())).withSelfRel(),
                // Link "clientes": enlace hacia la colección completa
                linkTo(methodOn(ClienteController.class).listarTodos()).withRel("clientes")
        );
    }
}
