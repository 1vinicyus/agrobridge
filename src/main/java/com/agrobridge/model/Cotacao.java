package com.agrobridge.model;

import java.math.BigDecimal;

public class Cotacao {

    private Long id;
    private Long solicitacaoId;
    private Long fornecedorId;
    private BigDecimal preco;
    private int prazoEntrega;
    private String condicoesPagamento;

    public Cotacao() {
    }

    public Cotacao(Long id, Long solicitacaoId, Long fornecedorId, BigDecimal preco,
                   int prazoEntrega, String condicoesPagamento) {
        this.id = id;
        this.solicitacaoId = solicitacaoId;
        this.fornecedorId = fornecedorId;
        this.preco = preco;
        this.prazoEntrega = prazoEntrega;
        this.condicoesPagamento = condicoesPagamento;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSolicitacaoId() { return solicitacaoId; }
    public void setSolicitacaoId(Long solicitacaoId) { this.solicitacaoId = solicitacaoId; }
    public Long getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(Long fornecedorId) { this.fornecedorId = fornecedorId; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public int getPrazoEntrega() { return prazoEntrega; }
    public void setPrazoEntrega(int prazoEntrega) { this.prazoEntrega = prazoEntrega; }
    public String getCondicoesPagamento() { return condicoesPagamento; }
    public void setCondicoesPagamento(String condicoesPagamento) { this.condicoesPagamento = condicoesPagamento; }

    @Override
    public String toString() {
        return String.format("Cotação [id=%d, solicitaçãoId=%d, fornecedorId=%d, preço=R$ %s, prazo=%d dias, pagamento=%s]",
                id, solicitacaoId, fornecedorId, preco, prazoEntrega, condicoesPagamento);
    }
}
