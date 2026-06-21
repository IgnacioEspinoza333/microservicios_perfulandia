package com.example.ms_producto.controller;

import com.example.ms_producto.assembler.ProductoModelAssembler;
import com.example.ms_producto.dto.MessageResponseDto;
import com.example.ms_producto.dto.ProductoRequestDto;
import com.example.ms_producto.dto.ProductoResponseDto;
import com.example.ms_producto.dto.ProductoUpdateDto;
import com.example.ms_producto.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @MockitoBean
    private ProductoModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductoRequestDto requestDto;
    private ProductoUpdateDto updateDto;
    private ProductoResponseDto responseDto;
    private EntityModel<ProductoResponseDto> entityModel;

    @BeforeEach
    void setUp() {
        requestDto = new ProductoRequestDto();
        requestDto.setNombre("Notebook Gamer");
        requestDto.setSku("SKU-001");
        requestDto.setPrecio(new BigDecimal("899990"));
        requestDto.setStock(10);
        requestDto.setActivo(true);

        updateDto = new ProductoUpdateDto();
        updateDto.setNombre("Notebook Gamer Pro");
        updateDto.setSku("SKU-002");
        updateDto.setPrecio(new BigDecimal("999990"));
        updateDto.setStock(8);
        updateDto.setActivo(true);

        responseDto = new ProductoResponseDto();
        responseDto.setId(1L);
        responseDto.setNombre("Notebook Gamer");
        responseDto.setSku("SKU-001");
        responseDto.setPrecio(new BigDecimal("899990"));
        responseDto.setStock(10);
        responseDto.setActivo(true);

        entityModel = EntityModel.of(
                responseDto,
                Link.of("http://localhost/api/v2/productos/1").withSelfRel(),
                Link.of("http://localhost/api/v2/productos").withRel("productos")
        );
    }

    @Test
    public void testCreateProducto() throws Exception {
        when(productoService.crear(any(ProductoRequestDto.class))).thenReturn(responseDto);
        when(assembler.toModel(responseDto)).thenReturn(entityModel);

        mockMvc.perform(post("/api/v2/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Notebook Gamer"))
                .andExpect(jsonPath("$.sku").value("SKU-001"))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.activo").value(true));

        verify(productoService, times(1)).crear(any(ProductoRequestDto.class));
        verify(assembler, times(1)).toModel(responseDto);
    }

    @Test
    public void testGetAllProductos() throws Exception {
        when(productoService.listar()).thenReturn(List.of(responseDto));
        when(assembler.toModel(any(ProductoResponseDto.class))).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(productoService, times(1)).listar();
        verify(assembler, times(1)).toModel(any(ProductoResponseDto.class));
    }

    @Test
    public void testGetProductoById() throws Exception {
        when(productoService.obtenerPorId(1L)).thenReturn(responseDto);
        when(assembler.toModel(responseDto)).thenReturn(entityModel);

        mockMvc.perform(get("/api/v2/productos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Notebook Gamer"))
                .andExpect(jsonPath("$.sku").value("SKU-001"))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.activo").value(true));

        verify(productoService, times(1)).obtenerPorId(1L);
        verify(assembler, times(1)).toModel(responseDto);
    }

    @Test
    public void testUpdateProducto() throws Exception {
        ProductoResponseDto actualizado = new ProductoResponseDto();
        actualizado.setId(1L);
        actualizado.setNombre("Notebook Gamer Pro");
        actualizado.setSku("SKU-002");
        actualizado.setPrecio(new BigDecimal("999990"));
        actualizado.setStock(8);
        actualizado.setActivo(true);

        EntityModel<ProductoResponseDto> updatedModel = EntityModel.of(actualizado);

        when(productoService.actualizar(eq(1L), any(ProductoUpdateDto.class))).thenReturn(actualizado);
        when(assembler.toModel(actualizado)).thenReturn(updatedModel);

        mockMvc.perform(put("/api/v2/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Notebook Gamer Pro"))
                .andExpect(jsonPath("$.sku").value("SKU-002"))
                .andExpect(jsonPath("$.stock").value(8))
                .andExpect(jsonPath("$.activo").value(true));

        verify(productoService, times(1)).actualizar(eq(1L), any(ProductoUpdateDto.class));
        verify(assembler, times(1)).toModel(actualizado);
    }

    @Test
    public void testDeleteProducto() throws Exception {
        when(productoService.eliminar(1L))
                .thenReturn(new MessageResponseDto("Producto eliminado correctamente"));

        mockMvc.perform(delete("/api/v2/productos/1"))
                .andExpect(status().isNoContent());

        verify(productoService, times(1)).eliminar(1L);
    }

    @Test
    public void testCreateProductoBadRequest() throws Exception {
        ProductoRequestDto invalido = new ProductoRequestDto();

        mockMvc.perform(post("/api/v2/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());

        verify(productoService, never()).crear(any());
    }
}