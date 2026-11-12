package com.agrobridge.view;

import com.agrobridge.dao.FornecedorDAO;
import com.agrobridge.model.Fornecedor;
import com.agrobridge.util.Validador;

/** RF2: cadastro de fornecedores de insumos. */
public class MenuFornecedor extends MenuUsuario<Fornecedor> {

    public MenuFornecedor() {
        super(new FornecedorDAO());
    }

    @Override
    protected String titulo() {
        return "FORNECEDORES";
    }

    @Override
    protected Fornecedor novoUsuario(String nome, String email, String telefone, String senhaHash) {
        String cnpj = Validador.cnpj(Entrada.lerTexto("CNPJ"));
        String area = Entrada.lerTexto("Área de atuação");
        return new Fornecedor(null, nome, email, senhaHash, telefone, cnpj, area);
    }

    @Override
    protected void atualizarCamposEspecificos(Fornecedor f) {
        f.setCnpj(Validador.cnpj(Entrada.lerTextoOuManter("CNPJ", f.getCnpj())));
        f.setAreaAtuacao(Entrada.lerTextoOuManter("Área de atuação", f.getAreaAtuacao()));
    }
}
