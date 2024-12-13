package com.Project.AppApplication.dto;

import com.Project.AppApplication.domain.candidato.Candidato;

public class CandidatoDTO {
    private boolean formularioPreenchido;
    private Candidato candidato;

    // Construtor
    public void Response(boolean formularioPreenchido, Candidato candidato) {
        this.formularioPreenchido = formularioPreenchido;
        this.candidato = candidato;
    }

    // Getters
    public boolean isFormularioPreenchido() {
        return formularioPreenchido;
    }

    public Candidato getCandidato() {
        return candidato;
    }
}
