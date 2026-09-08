package com.agrobridge.model;

import java.math.BigDecimal;

/**
 * Cotação enviada por um {@link Fornecedor} em resposta a uma
 * {@link Solicitacao} (RF05): preço, prazo de entrega e condições de
 * pagamento. Cada solicitação pode reunir múltiplas cotações de diferentes
 * fornecedores.
 */
public class Cotacao {

    private Long id;
    private BigDecimal preco;
    private int prazoEntrega;
    private String condicoesPagamento;

    private final Solicitacao solicitacao;
    private final Fornecedor fornecedor;

    public Cotacao(BigDecimal preco, int prazoEntrega, String condicoesPagamento,
                    Solicitacao solicitacao, Fornecedor fornecedor) {
        this.preco = preco;
        this.prazoEntrega = prazoEntrega;
        this.condicoesPagamento = condicoesPagamento;
        this.solicitacao = solicitacao;
        this.fornecedor = fornecedor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getPrazoEntrega() {
        return prazoEntrega;
    }

    public void setPrazoEntrega(int prazoEntrega) {
        this.prazoEntrega = prazoEntrega;
    }

    public String getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(String condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }

    public Solicitacao getSolicitacao() {
        return solicitacao;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    @Override
    public String toString() {
        return "Cotacao{id=" + id + ", preco=" + preco + ", prazoEntrega=" + prazoEntrega
                + "d, condicoesPagamento='" + condicoesPagamento + "', fornecedor='"
                + fornecedor.getNomeCompleto() + "'}";
    }
}
