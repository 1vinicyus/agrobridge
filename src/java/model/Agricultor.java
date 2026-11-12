package com.agrobridge.model;

import java.util.ArrayList;
import java.util.List;

/** Agricultor herda de Usuario e compõe suas propriedades rurais (composição 1:N). */
public class Agricultor extends Usuario {

    private String cpf;
    private final List<PropriedadeRural> propriedades = new ArrayList<>();

    public Agricultor() {
        super();
    }

    public Agricultor(Long id, String nomeCompleto, String email, String senha, String telefone, String cpf) {
        super(id, nomeCompleto, email, senha, telefone);
        this.cpf = cpf;
    }

    @Override
    public String getTipoUsuario() {
        return "AGRICULTOR";
    }

    @Override
    public void enviarNotificacao(String mensagem) {
        System.out.printf("  [Notificação -> Agricultor %s] Cotação: %s%n", nomeCompleto, mensagem);
    }

    public void adicionarPropriedade(PropriedadeRural propriedade) {
        propriedades.add(propriedade);
    }

    public List<PropriedadeRural> getPropriedades() { return propriedades; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    @Override
    public String toString() {
        return super.toString() + String.format(" cpf=%s propriedades=%d", cpf, propriedades.size());
    }
}
