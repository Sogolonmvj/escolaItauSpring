package com.itau.escolaItauSpring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.escolaItauSpring.controller.factory.Factory;
import com.itau.escolaItauSpring.dto.request.AlunoRequest;
import com.itau.escolaItauSpring.dto.response.AlunoResponse;
import com.itau.escolaItauSpring.exception.ItemNaoExistenteException;
import com.itau.escolaItauSpring.service.AlunoService;
import com.itau.escolaItauSpring.service.exception.RecursoNaoEncontrado;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlunoController.class)
public class AlunoControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    AlunoService alunoService;
    @Autowired
    private ObjectMapper objectMapper;
    private AlunoRequest alunoRequest;
    private AlunoResponse alunoResponse;
    private String expectedSingleList;
    private String expected;

    private UUID idExistente;
    private UUID idNaoExistente;

    @BeforeEach
    void setUp() throws ItemNaoExistenteException {

        expectedSingleList = "[{\"id\":\"65cb8254-2858-428a-9344-450fe37732d8\",\"nome\":\"Lucas\",\"idade\":25,\"cpf\":123321,\"curso\":null}]";
        expected = "{\"id\":\"65cb8254-2858-428a-9344-450fe37732d8\",\"nome\":\"Lucas\",\"idade\":25,\"cpf\":123321,\"curso\":null}";

        alunoRequest = Factory.createAlunoRequest();
        alunoResponse = Factory.createAlunoResponse();

        when(alunoService.adicionar(any(AlunoRequest.class))).thenReturn(alunoResponse);
        when(alunoService.listar()).thenReturn(Collections.singletonList(alunoResponse));

        idExistente = UUID.fromString("65cb8254-1111-1111-9344-450fe11111d8");
        idNaoExistente = UUID.fromString("00000000-0000-0000-0000-450fe00000d8");

        when(alunoService.localizar(idExistente)).thenReturn(alunoResponse);
        when(alunoService.localizar(idNaoExistente)).thenThrow(RecursoNaoEncontrado.class);
        doThrow(RecursoNaoEncontrado.builder().build()).when(alunoService).ativar(idNaoExistente);
    }

    @Test
    void testeListaAlunos() throws Exception {

        var result = this.mockMvc.perform(MockMvcRequestBuilders.get("/aluno")).andReturn();

        Mockito.verify(alunoService, Mockito.times(1)).listar();
        Assertions.assertEquals(expectedSingleList, result.getResponse().getContentAsString());
        Assertions.assertEquals(200, result.getResponse().getStatus());
    }


    @Test
    void testeCadastrarRetornaCreated() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(alunoRequest);

        ResultActions result = this.mockMvc.perform(post("/aluno")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        Assertions.assertEquals(expected, result.andReturn().getResponse().getContentAsString());
        Mockito.verify(alunoService, Mockito.times(1)).adicionar(ArgumentMatchers.any(AlunoRequest.class));

    }

    @Test
    void testeBuscarAluno() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/aluno/id/{id}", idExistente).contentType(MediaType.APPLICATION_JSON));
//        Assertions.assertEquals(200, result.getResponse().getStatus());
        result.andExpect(status().isOk());
        Assertions.assertEquals(expected, result.andReturn().getResponse().getContentAsString());
    }

    @Test
    void testeBuscarDeveLancarExcecaoQuandoIdNaoExistente() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/aluno/id/{id}", idNaoExistente).contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    void testeAtivarAluno() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.patch("/aluno/{id}", idExistente).contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isAccepted());
    }

    @Test
    void testeAtivarDeveLancarExcecaoQuandoIdNaoExistente() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/aluno/{id}", idNaoExistente).contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    void deveListarQuantidadeAlunosAtivos() throws Exception {
        when(alunoService.quantidadeAlunosAtivo()).thenReturn(3L);

        MvcResult mvcResult = mockMvc.perform(get("/aluno//quantidade-ativo"))
                .andExpect(status().isOk())
                .andReturn();

        Long qtdALunos = alunoService.quantidadeAlunosAtivo();
        String responseBody = mvcResult.getResponse().getContentAsString();
        assertEquals(qtdALunos.toString(),responseBody);
        verify(alunoService,atLeastOnce()).quantidadeAlunosAtivo();
    }

    @Test
    void deveRemoverAlunoPorCpf() throws Exception {
        doNothing().when(alunoService).removerPorCpf(any());

        MvcResult response = mockMvc.perform(delete("/aluno/cpf/{cpf}", 12312312345L ))
                .andExpect(status().isOk())
                .andReturn();

        verify(alunoService,times(1)).removerPorCpf(12312312345L);
    }

    @Test
    void deveBuscarAlunoPorNome() throws Exception {

        when(alunoService.buscarPorNome("AlunoTeste")).thenReturn(List.of(alunoResponse));

        MvcResult response = mockMvc.perform(get("/aluno/busca/{nome}","AlunoTeste"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = response.getResponse().getContentAsString();

        verify(alunoService,times(1)).buscarPorNome("AlunoTeste");
        assertThat(responseBody).isEqualToIgnoringWhitespace(List.of(objectMapper.writeValueAsString(alunoResponse)).toString());
    }


}