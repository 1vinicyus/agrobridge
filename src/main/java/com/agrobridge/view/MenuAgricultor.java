package com.agrobridge.view;

import com.agrobridge.dao.AgricultorDAO;
import com.agrobridge.dao.PropriedadeRuralDAO;
import com.agrobridge.model.Agricultor;
import com.agrobridge.model.PropriedadeRural;
import com.agrobridge.util.Validador;

/** RF1: cadastro de agricultores com propriedade rural vinculada. */
public class MenuAgricultor extends MenuUsuario<Agricultor> {

    private final PropriedadeRuralDAO propriedadeDAO = new PropriedadeRuralDAO();

    public MenuAgricultor() {
        super(new AgricultorDAO());
    }

    @Override
    protected String titulo() {
        return "AGRICULTORES";
    }

    @Override
    protected Agricultor novoUsuario(String nome, String email, String telefone, String senhaHash) {
        String cpf = Validador.cpf(Entrada.lerTexto("CPF"));
        return new Agricultor(null, nome, email, senhaHash, telefone, cpf);
    }

    @Override
    protected void aposCadastro(Agricultor agricultor) {
        if (Entrada.confirmar("Cadastrar uma propriedade rural para este agricultor agora?")) {
            PropriedadeRural p = new PropriedadeRural(null, agricultor.getId(),
                    Entrada.lerTexto("Nome da propriedade"), Entrada.lerTexto("Localização"),
                    Entrada.lerDecimal("Área (hectares)"));
            propriedadeDAO.criar(p);
            System.out.println("  " + p);
        }
    }

    @Override
    protected void detalhar(Agricultor agricultor) {
        agricultor.getPropriedades().forEach(p -> System.out.println("    - " + p));
    }

    @Override
    protected void atualizarCamposEspecificos(Agricultor agricultor) {
        agricultor.setCpf(Validador.cpf(Entrada.lerTextoOuManter("CPF", agricultor.getCpf())));
    }
}
