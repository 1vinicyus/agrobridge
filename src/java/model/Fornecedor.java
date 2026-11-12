package com.agrobridge.model;

public class Fornecedor extends Usuario {

    private String cnpj;
    private String areaAtuacao;

    public Fornecedor() {
        super();
    }

    public Fornecedor(Long id, String nomeCompleto, String email, String senha, String telefone,
                      String cnpj, String areaAtuacao) {
        super(id, nomeCompleto, email, senha, telefone);
        this.cnpj = cnpj;
        this.areaAtuacao = areaAtuacao;
    }

    @Override
    public String getTipoUsuario() {
        return "FORNECEDOR";
    }

    @Override
    public void enviarNotificacao(String mensagem) {
        System.out.printf("  [Notificação -> Fornecedor %s] Nova solicitação: %s%n", nomeCompleto, mensagem);
    }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getAreaAtuacao() { return areaAtuacao; }
    public void setAreaAtuacao(String areaAtuacao) { this.areaAtuacao = areaAtuacao; }

    @Override
    public String toString() {
        return super.toString() + String.format(" cnpj=%s area=%s", cnpj, areaAtuacao);
    }
}
