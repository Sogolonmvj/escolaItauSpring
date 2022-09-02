package com.itau.escolaItauSpring.service;

import com.itau.escolaItauSpring.config.mapper.AlunoMapper;
import com.itau.escolaItauSpring.dto.request.AlunoRequest;
import com.itau.escolaItauSpring.dto.response.AlunoResponse;
import com.itau.escolaItauSpring.exception.ItemNaoExistenteException;
import com.itau.escolaItauSpring.model.Aluno;
import com.itau.escolaItauSpring.repository.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlunoServiceTest {

    @InjectMocks
    AlunoService alunoService;

    @Mock
    AlunoRepository repository;

    @Mock
    AlunoMapper mapper;

    private AlunoResponse alunoResponse;
    private AlunoRequest alunoRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        alunoResponse = new AlunoResponse();
        alunoRequest = new AlunoRequest();

    }

    @Test
    void deveAdicionarAluno() {

        when(mapper.toModel(any())).thenReturn(new Aluno());
        when(repository.save(any())).thenReturn(new Aluno());
        when(mapper.toResponse(any())).thenReturn(alunoResponse);

        AlunoResponse response = alunoService.adicionar(new AlunoRequest());

        assertEquals(alunoResponse, response);
        assertInstanceOf(AlunoResponse.class, response);
    }

    @Test
    void deveAtivarAluno() throws ItemNaoExistenteException {
        Aluno alunoAtivado = new Aluno();
        alunoAtivado.setAtivado(true);

        when(repository.findById(any())).thenReturn(Optional.of(new Aluno()));
        when(repository.save(alunoAtivado)).thenReturn(alunoAtivado);

        alunoService.ativar(UUID.randomUUID());

        assertTrue(alunoAtivado.getAtivado());
        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(any());
    }

    @Test
    void desativarAluno() throws ItemNaoExistenteException {
        Aluno aluno = new Aluno();
        when(repository.save(any())).thenReturn(aluno);

        alunoService.desativar(aluno);

        verify(repository,atLeastOnce()).save(any());
        assertFalse(aluno.getAtivado());
    }

    @Test
    void listarTodosAlunos() {
        List<AlunoResponse> alunosResponse = List.of(new AlunoResponse(), new AlunoResponse());

        when(repository.findAll()).thenReturn(List.of(new Aluno(), new Aluno()));
        when(mapper.mapAluno(any())).thenReturn(alunosResponse);

        List<AlunoResponse> response = alunoService.listar();

        verify(repository, atLeastOnce()).findAll();
        assertEquals(alunosResponse, response);
    }

    @Test
    void localizarAlunoPorID() throws ItemNaoExistenteException {
        AlunoResponse alunoResponse = new AlunoResponse();

        when(repository.findById(any())).thenReturn(Optional.of(new Aluno()));
        when(mapper.toResponse(any())).thenReturn(alunoResponse);

        AlunoResponse response = alunoService.localizar(UUID.randomUUID());

        assertInstanceOf(AlunoResponse.class, response);
        assertEquals(alunoResponse, response);
        verify(repository,atLeastOnce()).findById(any());

    }

    @Test
    void quantidadeAlunosAtivo() {
        when(repository.countAlunoByAtivado(true)).thenReturn(3L);

        Long result = alunoService.quantidadeAlunosAtivo();

        assertEquals(3L,result);
        verify(repository,atLeastOnce()).countAlunoByAtivado(any());
    }

    @Test
    void removerPorCpf() {
        doNothing().when(repository).deleteByCpf(12312312345L);

        alunoService.removerPorCpf(12312312320L);

        verify(repository,atLeastOnce()).deleteByCpf(any());
    }

    @Test
    void buscarPorNome() {
        AlunoResponse aluno = new AlunoResponse();
        aluno.setNome("Aluno_Teste");
        List<AlunoResponse> alunos = List.of(aluno);

        when(repository.findByNomeContainingIgnoreCase(any())).thenReturn(List.of(new Aluno()));
        when(mapper.mapAluno(any())).thenReturn(alunos);

        List<AlunoResponse> response = alunoService.buscarPorNome("Aluno_Teste");

        assertEquals("Aluno_Teste", response.get(0).getNome());
        assertEquals(1,response.size());
    }
}