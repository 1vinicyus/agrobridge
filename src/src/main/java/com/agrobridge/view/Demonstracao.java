package com.agrobridge.view;

import com.agrobridge.dao.AgricultorDAO;
import com.agrobridge.dao.AgronomoDAO;
import com.agrobridge.dao.FornecedorDAO;
import com.agrobridge.dao.PropriedadeRuralDAO;
import com.agrobridge.model.Agricultor;
import com.agrobridge.model.Agronomo;
import com.agrobridge.model.Consultoria;
import com.agrobridge.model.Cotacao;
import com.agrobridge.model.Fornecedor;
import com.agrobridge.model.PropriedadeRural;
import com.agrobridge.model.Solicitacao;
import com.agrobridge.model.Usuario;
import com.agrobridge.service.AgroBridgeService;
import com.agrobridge.util.SenhaUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Roteiro automático para apresentação: CRUD completo no banco, polimorfismo e o fluxo de
 * cotações/consultorias. Cria dados próprios e remove tudo ao final.
 */
public class Demonstracao {

    private final AgroBridgeService service;
    private final AgricultorDAO agricultorDAO = new AgricultorDAO();
    private final FornecedorDAO fornecedorDAO = new FornecedorDAO();
    private final AgronomoDAO agronomoDAO = new AgronomoDAO();
    private final PropriedadeRuralDAO propriedadeDAO = new PropriedadeRuralDAO();

    public Demonstracao(AgroBridgeService service) {
        this.service = service;
    }

    public void executar() {
        String sufixo = String.valueOf(System.nanoTime() % 100_000_000L);
        Agricultor agricultor = null;
        Fornecedor fornecedorA = null;
        Fornecedor fornecedorB = null;
        Agronomo agronomo = null;
        try {
            titulo("1) CREATE - cadastrando agricultor e propriedade no PostgreSQL");
            agricultor = new Agricultor(null, "Demo Agricultor " + sufixo, "agri" + sufixo + "@demo.com",
                    SenhaUtil.gerarHash("123456"), "(44) 90000-0001", String.format("%011d", Long.parseLong(sufixo)));
            agricultorDAO.criar(agricultor);
            System.out.println("  " + agricultor);
            PropriedadeRural propriedade = propriedadeDAO.criar(new PropriedadeRural(null, agricultor.getId(),
                    "Sítio Demo", "Maringá - PR", new BigDecimal("15.50")));
            System.out.println("  " + propriedade);

            titulo("2) READ - buscando o agricultor (carrega as propriedades da composição)");
            Agricultor lido = agricultorDAO.buscarPorId(agricultor.getId()).orElseThrow();
            System.out.println("  " + lido);
            lido.getPropriedades().forEach(p -> System.out.println("    - " + p));
            System.out.println("  Total de agricultores no banco: " + agricultorDAO.listarTodos().size());

            titulo("3) UPDATE - alterando telefone e nome do agricultor");
            lido.setNomeCompleto("Demo Agricultor Atualizado " + sufixo);
            lido.setTelefone("(44) 99999-1234");
            agricultorDAO.atualizar(lido);
            System.out.println("  " + agricultorDAO.buscarPorId(lido.getId()).orElseThrow());

            titulo("4) Herança e polimorfismo - cada subclasse notifica do seu jeito");
            fornecedorA = fornecedorDAO.criar(new Fornecedor(null, "Demo Fornecedor A " + sufixo,
                    "fa" + sufixo + "@demo.com", SenhaUtil.gerarHash("123456"), "(44) 3000-0001",
                    String.format("%014d", Long.parseLong(sufixo)), "Fertilizantes"));
            fornecedorB = fornecedorDAO.criar(new Fornecedor(null, "Demo Fornecedor B " + sufixo,
                    "fb" + sufixo + "@demo.com", SenhaUtil.gerarHash("123456"), "(44) 3000-0002",
                    String.format("%014d", Long.parseLong(sufixo) + 1), "Sementes"));
            agronomo = agronomoDAO.criar(new Agronomo(null, "Demo Agrônomo " + sufixo,
                    "ag" + sufixo + "@demo.com", SenhaUtil.gerarHash("123456"), "(44) 98888-0001",
                    "CREA-PR " + sufixo, "Fertilidade do solo"));
            List<Usuario> usuarios = new ArrayList<>(List.of(lido, fornecedorA, agronomo));
            service.notificar(usuarios, "mensagem de teste do polimorfismo");

            titulo("5) Fluxo de negócio - solicitação, cotações e comparação de preços");
            Solicitacao solicitacao = new Solicitacao();
            solicitacao.setPropriedadeId(propriedade.getId());
            solicitacao.setProdutoDesejado("Adubo NPK 20-05-20");
            solicitacao.setQuantidade(new BigDecimal("500"));
            service.solicitarCotacao(solicitacao);
            System.out.println("  " + solicitacao);
            service.registrarCotacao(new Cotacao(null, solicitacao.getId(), fornecedorA.getId(),
                    new BigDecimal("2600.00"), 3, "30 dias"));
            service.registrarCotacao(new Cotacao(null, solicitacao.getId(), fornecedorB.getId(),
                    new BigDecimal("2450.00"), 5, "À vista com 5% de desconto"));
            System.out.println("  Propostas ordenadas da mais barata para a mais cara:");
            int posicao = 1;
            for (Cotacao c : service.compararCotacoes(solicitacao.getId())) {
                System.out.println("    " + posicao++ + "º " + c);
            }
            service.alterarStatusSolicitacao(solicitacao.getId(), AgroBridgeService.FINALIZADA);

            titulo("6) Consultoria técnica e histórico da propriedade");
            service.registrarConsultoria(new Consultoria(null, propriedade.getId(), agronomo.getId(),
                    "Solo com pH baixo.", "Aplicar calcário antes do plantio."));
            MenuPrincipal.imprimirHistorico(service.historicoPropriedade(propriedade.getId()));
        } finally {
            titulo("7) DELETE - removendo os dados da demonstração");
            if (agricultor != null && agricultor.getId() != null) {
                System.out.println("  Agricultor excluído (cascata nas propriedades, solicitações, cotações e consultorias): "
                        + agricultorDAO.excluir(agricultor.getId()));
                System.out.println("  Ainda existe no banco? " + agricultorDAO.buscarPorId(agricultor.getId()).isPresent());
            }
            if (fornecedorA != null && fornecedorA.getId() != null) {
                fornecedorDAO.excluir(fornecedorA.getId());
            }
            if (fornecedorB != null && fornecedorB.getId() != null) {
                fornecedorDAO.excluir(fornecedorB.getId());
            }
            if (agronomo != null && agronomo.getId() != null) {
                agronomoDAO.excluir(agronomo.getId());
            }
            System.out.println("  Demonstração concluída.");
        }
    }

    private static void titulo(String texto) {
        System.out.println("\n=== " + texto + " ===");
    }
}
