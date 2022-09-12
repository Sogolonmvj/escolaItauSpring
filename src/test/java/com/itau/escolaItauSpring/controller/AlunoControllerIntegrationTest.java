package com.itau.escolaItauSpring.controller;

import com.itau.escolaItauSpring.config.mapper.AlunoMapper;
import com.itau.escolaItauSpring.repository.AlunoRepository;
import com.itau.escolaItauSpring.service.AlunoService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AlunoService.class, AlunoRepository.class, AlunoMapper.class})
public class AlunoControllerIntegrationTest {

}