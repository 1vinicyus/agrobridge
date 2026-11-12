package com.agrobridge.view;

import com.agrobridge.dao.CotacaoDAO;
import com.agrobridge.model.Cotacao;
import com.agrobridge.service.AgroBridgeService;

import java.util.List;

/** RF5: fornecedores respondem solicitações; o agricultor compara as propostas. */
public class MenuCotacao extends MenuCrud {

    private final CotacaoDAO dao = new CotacaoDAO();
    private final AgroBridgeService service;

    public MenuCotacao(AgroBridgeService service) {
        this.service = service;
    }

    @Override
    protected String titulo() {
        return "COTAÇÕES";
    }

    @Override
    protected void cadastrar() {
        Cotacao c = new Cotacao();
        c.setSolicitacaoId(Entrada.lerLong("ID da solicitação"));
        c.setFornecedorId(Entrada.lerLong("ID do fornecedor"));
        c.setPreco(Entrada.lerDecimal("Preço total (R$)"));
        c.setPrazoEntrega(Entrada.lerInt("Prazo de entrega (dias)"));
        c.setCondicoesPagamento(Entrada.lerTexto("Condições de pagamento"));
        service.registrarCotacao(c);
        System.out.println("  Cotação registrada: " + c);
    }

    /** Lista todas ou compara as propostas de uma solicitação (menor preço primeiro). */
    @Override
    protected void listar() {
        long solicitacaoId = Entrada.lerLong("ID da solicitação para comparar (0 = listar todas)");
        List<Cotacao> lista = solicitacaoId == 0 ? dao.listarTodos() : service.compararCotacoes(solicitacaoId);
        if (lista.isEmpty()) {
            System.out.println("  Nenhuma cotação encontrada.");
        }
        int posicao = 1;
        for (Cotacao c : lista) {
            System.out.println("  " + (solicitacaoId == 0 ? "" : posicao++ + "º ") + c);
        }
    }

    @Override
    protected void buscar() {
        System.out.println(dao.buscarPorId(Entrada.lerLong("ID"))
                .map(c -> "  " + c).orElse("  Cotação não encontrada."));
    }

    @Override
    protected void atualizar() {
        Cotacao c = dao.buscarPorId(Entrada.lerLong("ID da cotação a atualizar")).orElse(null);
        if (c == null) {
            System.out.println("  Cotação não encontrada.");
            return;
        }
        c.setPreco(Entrada.lerDecimalOuManter("Preço total (R$)", c.getPreco()));
        c.setPrazoEntrega(Entrada.lerIntOuManter("Prazo de entrega (dias)", c.getPrazoEntrega()));
        c.setCondicoesPagamento(Entrada.lerTextoOuManter("Condições de pagamento", c.getCondicoesPagamento()));
        System.out.println(dao.atualizar(c) ? "  Atualizada com sucesso." : "  Nada foi atualizado.");
    }

    @Override
    protected void excluir() {
        Long id = Entrada.lerLong("ID da cotação a excluir");
        if (Entrada.confirmar("Confirma a exclusão?")) {
            System.out.println(dao.excluir(id) ? "  Excluída com sucesso." : "  Cotação não encontrada.");
        }
    }
}
