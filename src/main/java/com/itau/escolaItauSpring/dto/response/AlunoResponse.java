package com.itau.escolaItauSpring.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class AlunoResponse {
    private UUID id;
    private String nome;
    private Integer idade;
    private Long cpf;
    private CursoResponse curso;

}
