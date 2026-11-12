package com.agrobridge.view;

import com.agrobridge.dao.DaoException;
import com.agrobridge.model.Cotacao;
import com.agrobridge.model.Consultoria;
import com.agrobridge.model.Solicitacao;
import com.agrobridge.service.AgroBridgeService;
import com.agrobridge.service.AgroBridgeService.HistoricoPropriedade;

import java.util.List;

public class MenuPrincipal {

    private final AgroBridgeService service = new AgroBridgeService();
    private final MenuAgricultor menuAgricultor = new MenuAgricultor();
    private final MenuFornecedor menuFornecedor = new MenuFornecedor();
    private final MenuAgronomo menuAgronomo = new MenuAgronomo();
    private final MenuPropriedade menuPropriedade = new MenuPropriedade();
    private final MenuSolicitacao menuSolicitacao = new MenuSolicitacao(service);
    private final MenuCotacao menuCotacao = new MenuCotacao(service);
    private final MenuConsultoria menuConsultoria = new MenuConsultoria(service);

    public void iniciar() {
        System.out.println("=======================================");
        System.out.println("   AGROBRIDGE - Conectando o campo");
        System.out.println("=======================================");
        while (true) {
            System.out.println("\n1) Agricultores            6) Cotações (comparar propostas)");
            System.out.println("2) Fornecedores            7) Consultorias técnicas");
            System.out.println("3) Agrônomos               8) Histórico de uma propriedade");
            System.out.println("4) Propriedades rurais     9) Demonstração automática");
            System.out.println("5) Solicitações de cotação 10) Aviso geral (notificação)");
            System.out.println("0) Sair");
            int opcao = Entrada.lerInt("Opção");
            try {
                switch (opcao) {
                    case 1 -> menuAgricultor.executar();
                    case 2 -> menuFornecedor.executar();
                    case 3 -> menuAgronomo.executar();
                    case 4 -> menuPropriedade.executar();
                    case 5 -> menuSolicitacao.executar();
                    case 6 -> menuCotacao.executar();
                    case 7 -> menuConsultoria.executar();
                    case 8 -> mostrarHistorico();
                    case 9 -> new Demonstracao(service).executar();
                    case 10 -> avisoGeral();
                    case 0 -> {
                        System.out.println("Até logo!");
                        return;
                    }
                    default -> System.out.println("  Opção inválida.");
                }
            } catch (DaoException | IllegalArgumentException e) {
                System.out.println("  Erro: " + e.getMessage());
            }
        }
    }

    private void avisoGeral() {
        String mensagem = Entrada.lerTexto("Mensagem do aviso");
        int total = service.notificarTodosUsuarios(mensagem);
        System.out.println("  " + total + " usuário(s) notificado(s).");
    }

    /** RF7: histórico de cotações e consultorias de uma propriedade. */
    private void mostrarHistorico() {
        HistoricoPropriedade h = service.historicoPropriedade(Entrada.lerLong("ID da propriedade rural"));
        imprimirHistorico(h);
    }

    static void imprimirHistorico(HistoricoPropriedade h) {
        System.out.println("\n  " + h.propriedade());
        System.out.println("  Solicitações e cotações:");
        if (h.solicitacoes().isEmpty()) {
            System.out.println("    (nenhuma)");
        }
        for (Solicitacao s : h.solicitacoes()) {
            System.out.println("    " + s);
            List<Cotacao> cotacoes = h.cotacoesPorSolicitacao().get(s.getId());
            cotacoes.forEach(c -> System.out.println("      -> " + c));
        }
        System.out.println("  Consultorias:");
        if (h.consultorias().isEmpty()) {
            System.out.println("    (nenhuma)");
        }
        for (Consultoria c : h.consultorias()) {
            System.out.println("    " + c);
        }
    }
}
