package com.agrobridge.model;

import java.util.Objects;

/**
 * Superclasse abstrata da hierarquia de usuários do AgroBridge.
 *
 * Concentra os dados e comportamentos comuns aos três perfis do sistema
 * (Agricultor, Fornecedor e Agronomo), que herdam desta classe e
 * reaproveitam seu código, isolando apenas as responsabilidades específicas
 * de cada perfil nas subclasses.
 *
 * Implementa {@link Notificavel} de forma abstrata: cada subclasse é
 * obrigada a fornecer sua própria implementação de
 * {@link #enviarNotificacao(String)} (polimorfismo).
 */
public abstract class Usuario implements Notificavel {

    private Long id;
    private String nomeCompleto;
    private String email;
    private String senha;
    private String telefone;

    protected Usuario(String nomeCompleto, String email, String senha, String telefone) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
    }

    /**
     * Registra o cadastro do usuário no sistema (RF01/RF02/RF03).
     * Implementação simplificada para a etapa de modelagem/planejamento;
     * na 2ª entrega deverá persistir o registro via camada de repositório.
     */
    public void cadastrar() {
        System.out.println("Cadastro efetuado: " + nomeCompleto + " <" + email + ">");
    }

    @Override
    public abstract void enviarNotificacao(String mensagem);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + id + ", nomeCompleto='" + nomeCompleto + "', email='" + email + "'}";
    }
}
