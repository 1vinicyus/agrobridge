package com.agrobridge.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Perfil de agrônomo (RF03). Registra consultorias técnicas prestadas a
 * propriedades rurais (RF06).
 */
public class Agronomo extends Usuario {

    private String registroProfissional;
    private String especialidade;
    private final List<Consultoria> consultorias = new ArrayList<>();

    public Agronomo(String nomeCompleto, String email, String senha, String telefone,
                     String registroProfissional, String especialidade) {
        super(nomeCompleto, email, senha, telefone);
        this.registroProfissional = registroProfissional;
        this.especialidade = especialidade;
    }

    /**
     * Registra uma consultoria técnica prestada a uma propriedade rural,
     * com data do atendimento, diagnóstico e recomendações (RF06).
     */
    public Consultoria registrarConsultoria(PropriedadeRural propriedade, Date dataAtendimento,
                                             String diagnostico, String recomendacoes) {
        Consultoria consultoria = new Consultoria(dataAtendimento, diagnostico, recomendacoes, propriedade, this);
        propriedade.adicionarConsultoria(consultoria);
        consultorias.add(consultoria);
        enviarNotificacao("consultoria registrada em " + propriedade.getNome());
        return consultoria;
    }

    /**
     * Notificação polimórfica do perfil Agronomo (RF08): recebida quando
     * uma nova consultoria é registrada em seu nome.
     */
    @Override
    public void enviarNotificacao(String mensagem) {
        System.out.println("[Agrônomo " + getNomeCompleto() + "] Nova consultoria: " + mensagem);
    }

    public String getRegistroProfissional() {
        return registroProfissional;
    }

    public void setRegistroProfissional(String registroProfissional) {
        this.registroProfissional = registroProfissional;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public List<Consultoria> getConsultorias() {
        return Collections.unmodifiableList(consultorias);
    }
}
