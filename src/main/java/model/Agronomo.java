package com.agrobridge.model;

public class Agronomo extends Usuario {

    private String registroProfissional;
    private String especialidade;

    public Agronomo() {
        super();
    }

    public Agronomo(Long id, String nomeCompleto, String email, String senha, String telefone,
                    String registroProfissional, String especialidade) {
        super(id, nomeCompleto, email, senha, telefone);
        this.registroProfissional = registroProfissional;
        this.especialidade = especialidade;
    }

    @Override
    public String getTipoUsuario() {
        return "AGRONOMO";
    }

    @Override
    public void enviarNotificacao(String mensagem) {
        System.out.printf("  [Notificação -> Agrônomo %s] Nova consultoria: %s%n", nomeCompleto, mensagem);
    }

    public String getRegistroProfissional() { return registroProfissional; }
    public void setRegistroProfissional(String registroProfissional) { this.registroProfissional = registroProfissional; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    @Override
    public String toString() {
        return super.toString() + String.format(" registro=%s especialidade=%s", registroProfissional, especialidade);
    }
}
