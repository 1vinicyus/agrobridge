package com.agrobridge.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Perfil de agricultor (RF01). Possui uma ou mais {@link PropriedadeRural}
 * (composição 1:N) e é responsável por registrar solicitações de cotação
 * (RF04) para os insumos de que precisa.
 */
public class Agricultor extends Usuario {

    private String cpf;
    private final List<PropriedadeRural> propriedades = new ArrayList<>();

    public Agricultor(String nomeCompleto, String email, String senha, String telefone, String cpf) {
        super(nomeCompleto, email, senha, telefone);
        this.cpf = cpf;
    }

    /** Vincula uma propriedade rural a este agricultor (RF01). */
    public void adicionarPropriedade(PropriedadeRural propriedade) {
        propriedade.setAgricultor(this);
        propriedades.add(propriedade);
    }

    /**
     * Registra uma solicitação de cotação de insumo para uma das
     * propriedades deste agricultor (RF04).
     */
    public Solicitacao registrarSolicitacao(PropriedadeRural propriedade, String produtoDesejado, double quantidade) {
        if (!propriedades.contains(propriedade)) {
            throw new IllegalArgumentException("A propriedade informada não pertence a este agricultor.");
        }
        Solicitacao solicitacao = new Solicitacao(produtoDesejado, quantidade, propriedade);
        propriedade.adicionarSolicitacao(solicitacao);
        return solicitacao;
    }

    /**
     * Compara as cotações recebidas para uma solicitação, ordenadas do
     * menor para o maior preço (RF05).
     */
    public List<Cotacao> compararCotacoes(Solicitacao solicitacao) {
        return solicitacao.compararCotacoes();
    }

    /**
     * Notificação polimórfica do perfil Agricultor (RF08): recebida quando
     * uma nova cotação chega para uma de suas solicitações.
     */
    @Override
    public void enviarNotificacao(String mensagem) {
        System.out.println("[Agricultor " + getNomeCompleto() + "] Cotação recebida: " + mensagem);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<PropriedadeRural> getPropriedades() {
        return Collections.unmodifiableList(propriedades);
    }
}
