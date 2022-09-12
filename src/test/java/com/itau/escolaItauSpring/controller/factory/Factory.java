package com.itau.escolaItauSpring.controller.factory;

import com.itau.escolaItauSpring.dto.request.AlunoRequest;
import com.itau.escolaItauSpring.dto.response.AlunoResponse;

import java.util.UUID;

public class Factory {

    public static AlunoResponse createAlunoResponse(){
        AlunoResponse alunoResponse = new AlunoResponse();

        alunoResponse.setId(UUID.fromString("65cb8254-2858-428a-9344-450fe37732d8"));
        alunoResponse.setIdade(25);
        alunoResponse.setCpf(123321L);
        alunoResponse.setNome("Lucas");

        return alunoResponse;
    }

    public static AlunoRequest createAlunoRequest(){
        AlunoRequest alunoRequest = new AlunoRequest();

        alunoRequest.setIdade(25);
        alunoRequest.setCpf(123321L);
        alunoRequest.setNome("Lucas");

        return alunoRequest;
    }


}
