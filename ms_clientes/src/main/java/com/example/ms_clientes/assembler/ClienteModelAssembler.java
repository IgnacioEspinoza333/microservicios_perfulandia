package com.example.ms_clientes.assembler;


import com.example.ms_clientes.controller.ClienteController;
import com.example.ms_clientes.dto.ClienteResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ClienteModelAssembler  implements RepresentationModelAssembler<ClienteResponseDTO, EntityModel<ClienteResponseDTO>> {
   @Override
    public EntityModel<ClienteResponseDTO> toModel(ClienteResponseDTO cliente) {
        return EntityModel.of(cliente,
               
                linkTo(methodOn(ClienteController.class).buscarPorId(cliente.getId())).withSelfRel(),
                
                linkTo(methodOn(ClienteController.class).listarTodos()).withRel("clientes")
        );
    }
}
