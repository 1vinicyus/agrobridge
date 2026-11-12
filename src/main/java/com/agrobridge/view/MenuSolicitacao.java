package com.agrobridge.view;

import com.agrobridge.dao.SolicitacaoDAO;
import com.agrobridge.model.Solicitacao;
import com.agrobridge.service.AgroBridgeService;

import java.util.List;

/** RF4: solicitação de cotação pelo agricultor. */
public class MenuSolicitacao extends MenuCrud {

    private final SolicitacaoDAO dao = new SolicitacaoDAO();
    private final AgroBridgeService service;

    public MenuSolicitacao(AgroBridgeService service) {
        this.service = service;
    }

    @Override
    protected String titulo() {
        return "SOLICITAÇÕES DE COTAÇÃO";
    }

    @Override
    protected void cadastrar() {
        Solicitacao s = new Solicitacao();
        s.setPropriedadeId(Entrada.lerLong("ID da propriedade rural"));
        s.setProdutoDesejado(Entrada.lerTexto("Produto desejado"));
        s.setQuantidade(Entrada.lerDecimal("Quantidade"));
        service.solicitarCotacao(s);
        System.out.println("  Solicitação criada: " + s);
    }

    @Override
    protected void listar() {
        List<Solicitacao> lista = dao.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("  Nenhuma solicitação cadastrada.");
        }
        lista.forEach(s -> System.out.println("  " + s));
    }

    @Override
    protected void buscar() {
        System.out.println(dao.buscarPorId(Entrada.lerLong("ID"))
                .map(s -> "  " + s).orElse("  Solicitação não encontrada."));
    }

    @Override
    protected void atualizar() {
        Solicitacao s = dao.buscarPorId(Entrada.lerLong("ID da solicitação a atualizar")).orElse(null);
        if (s == null) {
            System.out.println("  Solicitação não encontrada.");
            return;
        }
        s.setProdutoDesejado(Entrada.lerTextoOuManter("Produto desejado", s.getProdutoDesejado()));
        s.setQuantidade(Entrada.lerDecimalOuManter("Quantidade", s.getQuantidade()));
        String status = Entrada.lerTextoOuManter("Status (ABERTA/FINALIZADA/CANCELADA)", s.getStatus()).toUpperCase();
        service.alterarStatusSolicitacao(s.getId(), status); // valida o status
        s.setStatus(status);
        System.out.println(dao.atualizar(s) ? "  Atualizada com sucesso." : "  Nada foi atualizado.");
    }

    @Override
    protected void excluir() {
        Long id = Entrada.lerLong("ID da solicitação a excluir");
        if (Entrada.confirmar("Confirma a exclusão (apaga as cotações vinculadas)?")) {
            System.out.println(dao.excluir(id) ? "  Excluída com sucesso." : "  Solicitação não encontrada.");
        }
    }
}
