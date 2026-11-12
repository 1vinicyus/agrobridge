package com.agrobridge.view;

import com.agrobridge.dao.AgronomoDAO;
import com.agrobridge.model.Agronomo;

/** RF3: cadastro de agrônomos. */
public class MenuAgronomo extends MenuUsuario<Agronomo> {

    public MenuAgronomo() {
        super(new AgronomoDAO());
    }

    @Override
    protected String titulo() {
        return "AGRÔNOMOS";
    }

    @Override
    protected Agronomo novoUsuario(String nome, String email, String telefone, String senhaHash) {
        String registro = Entrada.lerTexto("Registro profissional (CREA)");
        String especialidade = Entrada.lerTexto("Especialidade");
        return new Agronomo(null, nome, email, senhaHash, telefone, registro, especialidade);
    }

    @Override
    protected void atualizarCamposEspecificos(Agronomo a) {
        a.setRegistroProfissional(Entrada.lerTextoOuManter("Registro profissional", a.getRegistroProfissional()));
        a.setEspecialidade(Entrada.lerTextoOuManter("Especialidade", a.getEspecialidade()));
    }
}
