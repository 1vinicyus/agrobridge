package com.agrobridge.model;

import java.math.BigDecimal;

public class PropriedadeRural {

    private Long id;
    private Long agricultorId;
    private String nome;
    private String localizacao;
    private BigDecimal areaHectares;

    public PropriedadeRural() {
    }

    public PropriedadeRural(Long id, Long agricultorId, String nome, String localizacao, BigDecimal areaHectares) {
        this.id = id;
        this.agricultorId = agricultorId;
        this.nome = nome;
        this.localizacao = localizacao;
        this.areaHectares = areaHectares;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAgricultorId() { return agricultorId; }
    public void setAgricultorId(Long agricultorId) { this.agricultorId = agricultorId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    public BigDecimal getAreaHectares() { return areaHectares; }
    public void setAreaHectares(BigDecimal areaHectares) { this.areaHectares = areaHectares; }

    @Override
    public String toString() {
        return String.format("Propriedade [id=%d, nome=%s, local=%s, área=%s ha, agricultorId=%d]",
                id, nome, localizacao, areaHectares, agricultorId);
    }
}
