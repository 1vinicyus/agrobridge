package com.agrobridge.model;

/** Superclasse abstrata de Agricultor, Fornecedor e Agronomo (herança). */
public abstract class Usuario implements Notificavel {

    protected Long id;
    protected String nomeCompleto;
    protected String email;
    protected String senha;
    protected String telefone;

    protected Usuario() {
    }

    protected Usuario(Long id, String nomeCompleto, String email, String senha, String telefone) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
    }

    /** Tipo gravado na coluna usuario.tipo_usuario. */
    public abstract String getTipoUsuario();

    @Override
    public abstract void enviarNotificacao(String mensagem);

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    @Override
    public String toString() {
        return String.format("%s [id=%d, nome=%s, email=%s, telefone=%s]",
                getClass().getSimpleName(), id, nomeCompleto, email, telefone);
    }
}
