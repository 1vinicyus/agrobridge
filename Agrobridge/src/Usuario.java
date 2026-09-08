package com.agrobridge.model;

import java.util.Date;

/**
 * Registro de uma consultoria técnica prestada por um {@link Agronomo} a
 * uma {@link PropriedadeRural} (RF06): data do atendimento, diagnóstico e
 * recomendações.
 */
public class Consultoria {

    private Long id;
    private Date dataAtendimento;
    private String diagnostico;
    private String recomendacoes;

    private final PropriedadeRural propriedadeRural;
    private final Agronomo agronomo;

    public Consultoria(Date dataAtendimento, String diagnostico, String recomendacoes,
                        PropriedadeRural propriedadeRural, Agronomo agronomo) {
        this.dataAtendimento = dataAtendimento;
        this.diagnostico = diagnostico;
        this.recomendacoes = recomendacoes;
        this.propriedadeRural = propriedadeRural;
        this.agronomo = agronomo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataAtendimento() {
        return dataAtendimento;
    }

    public void setDataAtendimento(Date dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getRecomendacoes() {
        return recomendacoes;
    }

    public void setRecomendacoes(String recomendacoes) {
        this.recomendacoes = recomendacoes;
    }

    public PropriedadeRural getPropriedadeRural() {
        return propriedadeRural;
    }

    public Agronomo getAgronomo() {
        return agronomo;
    }

    @Override
    public String toString() {
        return "Consultoria{id=" + id + ", dataAtendimento=" + dataAtendimento + ", diagnostico='" + diagnostico
                + "', recomendacoes='" + recomendacoes + "', agronomo='" + agronomo.getNomeCompleto() + "'}";
    }
}
