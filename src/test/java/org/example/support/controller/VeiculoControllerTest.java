package org.example.support.controller;

import org.example.support.domain.entity.Veiculo;
import org.example.support.domain.enums.StatusVeiculo;
import org.example.support.dto.veiculo.VeiculoResponse;
import org.example.support.service.VeiculoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;

@WebMvcTest(controllers = VeiculoController.class)
class VeiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VeiculoService veiculoService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deveListarVeiculosComSucesso() throws Exception {
        // dado que o service retorna uma página vazia
        Page<Veiculo> pagina = new PageImpl<>(List.of(), PageRequest.of(0, 20), 0);
        Mockito.when(veiculoService.listar(any(), any(), any(), any(), any())).thenReturn(pagina);

        mockMvc.perform(get("/api/veiculos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
