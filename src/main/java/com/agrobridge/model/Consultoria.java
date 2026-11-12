package com.agrobridge.model;

import java.time.LocalDateTime;

public class Consultoria {

    private Long id;
    private Long propriedadeId;
    private Long agronomoId;
    private LocalDateTime dataAtendimento;
    private String diagnostico;
    private String recomendacoes;

    public Consultoria() {
    }

    public Consultoria(Long id, Long propriedadeId, Long agronomoId, String diagnostico, String recomendacoes) {
        this.id = id;
        this.propriedadeId = propriedadeId;
        this.agronomoId = agronomoId;
        this.diagnostico = diagnostico;
        this.recomendacoes = recomendacoes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPropriedadeId() { return propriedadeId; }
    public void setPropriedadeId(Long propriedadeId) { this.propriedadeId = propriedadeId; }
    public Long getAgronomoId() { return agronomoId; }
    public void setAgronomoId(Long agronomoId) { this.agronomoId = agronomoId; }
    public LocalDateTime getDataAtendimento() { return dataAtendimento; }
    public void setDataAtendimento(LocalDateTime dataAtendimento) { this.dataAtendimento = dataAtendimento; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getRecomendacoes() { return recomendacoes; }
    public void setRecomendacoes(String recomendacoes) { this.recomendacoes = recomendacoes; }

    @Override
    public String toString() {
        return String.format("Consultoria [id=%d, propriedadeId=%d, agrônomoId=%d, data=%s]%n    Diagnóstico: %s%n    Recomendações: %s",
                id, propriedadeId, agronomoId, dataAtendimento, diagnostico, recomendacoes);
    }
}
