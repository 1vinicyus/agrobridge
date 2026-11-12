package com.agrobridge.view;

import com.agrobridge.dao.AgricultorDAO;
import com.agrobridge.dao.PropriedadeRuralDAO;
import com.agrobridge.model.PropriedadeRural;

import java.util.List;

public class MenuPropriedade extends MenuCrud {

    private final PropriedadeRuralDAO dao = new PropriedadeRuralDAO();
    private final AgricultorDAO agricultorDAO = new AgricultorDAO();

    @Override
    protected String titulo() {
        return "PROPRIEDADES RURAIS";
    }

    @Override
    protected void cadastrar() {
        Long agricultorId = Entrada.lerLong("ID do agricultor dono da propriedade");
        agricultorDAO.buscarPorId(agricultorId)
                .orElseThrow(() -> new IllegalArgumentException("Agricultor não encontrado."));
        PropriedadeRural p = new PropriedadeRural(null, agricultorId, Entrada.lerTexto("Nome da propriedade"),
                Entrada.lerTexto("Localização"), Entrada.lerDecimal("Área (hectares)"));
        dao.criar(p);
        System.out.println("  Cadastrada: " + p);
    }

    @Override
    protected void listar() {
        List<PropriedadeRural> lista = dao.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("  Nenhuma propriedade cadastrada.");
        }
        lista.forEach(p -> System.out.println("  " + p));
    }

    @Override
    protected void buscar() {
        System.out.println(dao.buscarPorId(Entrada.lerLong("ID"))
                .map(p -> "  " + p).orElse("  Propriedade não encontrada."));
    }

    @Override
    protected void atualizar() {
        PropriedadeRural p = dao.buscarPorId(Entrada.lerLong("ID da propriedade a atualizar")).orElse(null);
        if (p == null) {
            System.out.println("  Propriedade não encontrada.");
            return;
        }
        p.setNome(Entrada.lerTextoOuManter("Nome", p.getNome()));
        p.setLocalizacao(Entrada.lerTextoOuManter("Localização", p.getLocalizacao()));
        p.setAreaHectares(Entrada.lerDecimalOuManter("Área (hectares)", p.getAreaHectares()));
        System.out.println(dao.atualizar(p) ? "  Atualizada com sucesso." : "  Nada foi atualizado.");
    }

    @Override
    protected void excluir() {
        Long id = Entrada.lerLong("ID da propriedade a excluir");
        if (Entrada.confirmar("Confirma a exclusão (apaga solicitações e consultorias vinculadas)?")) {
            System.out.println(dao.excluir(id) ? "  Excluída com sucesso." : "  Propriedade não encontrada.");
        }
    }
}
