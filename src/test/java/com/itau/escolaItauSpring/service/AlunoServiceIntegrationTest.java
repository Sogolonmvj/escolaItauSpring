package com.itau.escolaItauSpring.service;

import com.itau.escolaItauSpring.config.mapper.AlunoMapper;
import com.itau.escolaItauSpring.model.Aluno;
import com.itau.escolaItauSpring.repository.AlunoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

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

    @AfterEach
    public void finish() {
        alunoRepository.deleteAll();
    }

    @Test
    void salvarAluno() {
        Aluno alunoSalvo = alunoRepository.save(AlunoServiceIntegrationTest.aluno);
        Assertions.assertEquals(aluno.getNome(), alunoSalvo.getNome());
    }

    @Test
    void buscarAlunoPorCpfENome() {
        Aluno aluno = alunoRepository.save(AlunoServiceIntegrationTest.aluno);
        Aluno alunoEncontrado = alunoRepository.buscarPorCpfENome(aluno.getCpf(), aluno.getNome());

        Assertions.assertEquals(aluno.getCpf(), alunoEncontrado.getCpf());
        Assertions.assertEquals(aluno.getNome(), alunoEncontrado.getNome());
    }

    @Test
    void countAlunoByAtivado() {
        Aluno aluno = alunoRepository.save(AlunoServiceIntegrationTest.aluno);
        alunoService.ativar(aluno.getId());
        Long quantidadeDeAlunos = alunoRepository.countAlunoByAtivado(true);

        Assertions.assertEquals(1L, quantidadeDeAlunos);
    }

    @Test
    void deleteByCpf() {
        Aluno aluno = alunoRepository.save(AlunoServiceIntegrationTest.aluno);
        List<Aluno> alunosSalvos = alunoRepository.findAll();
        Assertions.assertEquals(1, alunosSalvos.size());
        alunoRepository.deleteByCpf(aluno.getCpf());
        alunosSalvos = alunoRepository.findAll();
        Assertions.assertEquals(0, alunosSalvos.size());
    }

    @Test
    void findById() {
        Aluno aluno = alunoRepository.save(AlunoServiceIntegrationTest.aluno);
        Assertions.assertEquals(aluno.getNome(), alunoRepository.findById(aluno.getId()).get().getNome());
    }

    @Test
    void findByNomeContainingIgnoreCase() {
        Aluno aluno = alunoRepository.save(AlunoServiceIntegrationTest.aluno);
        List<Aluno> alunos = alunoRepository.findByNomeContainingIgnoreCase(aluno.getNome().toUpperCase());

        Assertions.assertEquals(AlunoServiceIntegrationTest.aluno.getNome(), alunoRepository.findByNomeContainingIgnoreCase(aluno.getNome().toUpperCase()).get(0).getNome());
    }

    @Test
    void findAll() {
        List<Aluno> alunos = new ArrayList<>();
        Assertions.assertEquals(alunos, alunoRepository.findAll());
    }
}