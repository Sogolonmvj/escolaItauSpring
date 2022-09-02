package com.itau.escolaItauSpring.service;

import com.itau.escolaItauSpring.config.mapper.AlunoMapper;
import com.itau.escolaItauSpring.controller.AlunoControllerIntegrationTest;
import com.itau.escolaItauSpring.model.Aluno;
import com.itau.escolaItauSpring.repository.AlunoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AlunoServiceIntegrationTest extends AlunoControllerIntegrationTest {

    private final AlunoRepository alunoRepository;

    private final AlunoService alunoService;

    private final AlunoMapper alunoMapper;

    private static Aluno aluno;

    @BeforeAll
    public static void init() {
        aluno = new Aluno();
        aluno.setNome("Marcos");
        aluno.setCpf(12345L);
        aluno.setAtivado(true);
        aluno.setCursos(null);
        aluno.setEndereco(null);
        aluno.setIdade(26);
    }

    @Autowired
    AlunoServiceIntegrationTest(AlunoRepository alunoRepository, AlunoService alunoService, AlunoMapper alunoMapper) {
        this.alunoRepository = alunoRepository;
        this.alunoService = alunoService;
        this.alunoMapper = alunoMapper;
    }

    @Test
    void buscarAlunoPorCpfENome() {
        Aluno aluno = alunoRepository.save(AlunoServiceIntegrationTest.aluno);
        Aluno alunoEncontrado = alunoRepository.buscarPorCpfENome(aluno.getCpf(), aluno.getNome());

        Assertions.assertEquals(aluno.getCpf(), alunoEncontrado.getCpf());
        Assertions.assertEquals(aluno.getNome(), alunoEncontrado.getNome());
    }
}