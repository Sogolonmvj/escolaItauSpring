package com.itau.escolaItauSpring.service;

import com.itau.escolaItauSpring.config.mapper.AlunoMapper;
import com.itau.escolaItauSpring.model.Aluno;
import com.itau.escolaItauSpring.repository.AlunoRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AlunoServiceIntegrationTest {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private AlunoMapper alunoMapper;

    private static Aluno aluno;

    @BeforeAll
    public static void init() {
        aluno = new Aluno();
        aluno.setNome("Marcos");
        aluno.setCpf(12345L);
        aluno.setCursos(null);
        aluno.setEndereco(null);
        aluno.setIdade(26);
    }

    @Test
    void buscarAlunoPorCpfENome() {
        Aluno aluno = alunoRepository.save(AlunoServiceIntegrationTest.aluno);
        Aluno alunoEncontrado = alunoRepository.buscarPorCpfENome(aluno.getCpf(), aluno.getNome());

        Assertions.assertEquals(aluno.getCpf(), alunoEncontrado.getCpf());
        Assertions.assertEquals(aluno.getNome(), alunoEncontrado.getNome());
    }

    @Test
    @DisplayName("")
    void countAlunoByAtivado() {
        Aluno aluno = alunoRepository.save(AlunoServiceIntegrationTest.aluno);
        alunoService.ativar(aluno.getId());
        Long quantidadeDeAlunos = alunoRepository.countAlunoByAtivado(true);

        Assertions.assertEquals(1L, quantidadeDeAlunos);
    }

    @Test
    void deleteByCpf() {
        Aluno aluno = alunoRepository.save(AlunoServiceIntegrationTest.aluno);
        alunoService.ativar(aluno.getId());
        Long quantidadeDeAlunoSalvos = alunoRepository.countAlunoByAtivado(true);
        Assertions.assertEquals(1L, quantidadeDeAlunoSalvos);
        alunoRepository.deleteByCpf(aluno.getCpf());
        quantidadeDeAlunoSalvos = alunoRepository.countAlunoByAtivado(true);
        Assertions.assertEquals(0L, quantidadeDeAlunoSalvos);

    }

    @Test
    void findById() {
        Aluno aluno = alunoRepository.save(AlunoServiceIntegrationTest.aluno);
        Assertions.assertEquals(aluno,alunoRepository.findById(aluno.getId()));

    }
}