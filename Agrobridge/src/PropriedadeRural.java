package com.agrobridge.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Solicitação de cotação de insumo registrada por um agricultor para uma
 * de suas propriedades (RF04). É composta por múltiplas {@link Cotacao}
 * (composição 1:N), uma para cada fornecedor que responder, permitindo a
 * comparação de propostas (RF05).
 */
public class Solicitacao {

    private Long id;
    private String produtoDesejado;
    private double quantidade;
    private final Date dataCriacao;
    private String status;

    private final PropriedadeRural propriedadeRural;
    private final List<Cotacao> cotacoes = new ArrayList<>();

    public Solicitacao(String produtoDesejado, double quantidade, PropriedadeRural propriedadeRural) {
        this.produtoDesejado = produtoDesejado;
        this.quantidade = quantidade;
        this.propriedadeRural = propriedadeRural;
        this.dataCriacao = new Date();
        this.status = "ABERTA";
    }

    void adicionarCotacao(Cotacao cotacao) {
        cotacoes.add(cotacao);
        this.status = "COM_COTACOES";
    }

    /**
     * Retorna as cotações recebidas para esta solicitação ordenadas da mais
     * barata para a mais cara, possibilitando a comparação de propostas de
     * diferentes fornecedores (RF05).
     */
    public List<Cotacao> compararCotacoes() {
        List<Cotacao> ordenadas = new ArrayList<>(cotacoes);
        ordenadas.sort(Comparator.comparing(Cotacao::getPreco));
        return ordenadas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProdutoDesejado() {
        return produtoDesejado;
    }

    public void setProdutoDesejado(String produtoDesejado) {
        this.produtoDesejado = produtoDesejado;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PropriedadeRural getPropriedadeRural() {
        return propriedadeRural;
    }

    public List<Cotacao> getCotacoes() {
        return Collections.unmodifiableList(cotacoes);
    }

    @Override
    public String toString() {
        return "Solicitacao{id=" + id + ", produtoDesejado='" + produtoDesejado + "', quantidade=" + quantidade
                + ", status='" + status + "'}";
    }
}
