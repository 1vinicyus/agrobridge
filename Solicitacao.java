package com.agrobridge.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Solicitacao {

    private Long id;
    private Long propriedadeId;
    private String produtoDesejado;
    private BigDecimal quantidade;
    private LocalDateTime dataCriacao;
    private String status;

    public Solicitacao() {
    }

    public Solicitacao(Long id, Long propriedadeId, String produtoDesejado, BigDecimal quantidade, String status) {
        this.id = id;
        this.propriedadeId = propriedadeId;
        this.produtoDesejado = produtoDesejado;
        this.quantidade = quantidade;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPropriedadeId() { return propriedadeId; }
    public void setPropriedadeId(Long propriedadeId) { this.propriedadeId = propriedadeId; }
    public String getProdutoDesejado() { return produtoDesejado; }
    public void setProdutoDesejado(String produtoDesejado) { this.produtoDesejado = produtoDesejado; }
    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Solicitação [id=%d, propriedadeId=%d, produto=%s, qtd=%s, status=%s]",
                id, propriedadeId, produtoDesejado, quantidade, status);
    }
}
