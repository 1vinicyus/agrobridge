package com.agrobridge.service;

import com.agrobridge.dao.AgricultorDAO;
import com.agrobridge.dao.AgronomoDAO;
import com.agrobridge.dao.ConsultoriaDAO;
import com.agrobridge.dao.CotacaoDAO;
import com.agrobridge.dao.FornecedorDAO;
import com.agrobridge.dao.PropriedadeRuralDAO;
import com.agrobridge.dao.SolicitacaoDAO;
import com.agrobridge.model.Agricultor;
import com.agrobridge.model.Agronomo;
import com.agrobridge.model.Consultoria;
import com.agrobridge.model.Cotacao;
import com.agrobridge.model.Fornecedor;
import com.agrobridge.model.Notificavel;
import com.agrobridge.model.PropriedadeRural;
import com.agrobridge.model.Solicitacao;
import com.agrobridge.model.Usuario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Camada de serviço: regras de negócio do AgroBridge que envolvem mais de uma entidade
 * (solicitar/comparar cotações, registrar consultorias, histórico e notificações).
 */
public class AgroBridgeService {

    public static final String ABERTA = "ABERTA";
    public static final String FINALIZADA = "FINALIZADA";
    public static final String CANCELADA = "CANCELADA";

    private final AgricultorDAO agricultorDAO = new AgricultorDAO();
    private final FornecedorDAO fornecedorDAO = new FornecedorDAO();
    private final AgronomoDAO agronomoDAO = new AgronomoDAO();
    private final PropriedadeRuralDAO propriedadeDAO = new PropriedadeRuralDAO();
    private final SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
    private final CotacaoDAO cotacaoDAO = new CotacaoDAO();
    private final ConsultoriaDAO consultoriaDAO = new ConsultoriaDAO();

    /** Histórico de atendimentos de uma propriedade (cotações e consultorias). */
    public record HistoricoPropriedade(PropriedadeRural propriedade,
                                       List<Solicitacao> solicitacoes,
                                       Map<Long, List<Cotacao>> cotacoesPorSolicitacao,
                                       List<Consultoria> consultorias) {
    }

    /** RF4: o agricultor solicita cotação; todos os fornecedores são notificados (RF8). */
    public Solicitacao solicitarCotacao(Solicitacao solicitacao) {
        PropriedadeRural propriedade = propriedadeDAO.buscarPorId(solicitacao.getPropriedadeId())
                .orElseThrow(() -> new IllegalArgumentException("Propriedade rural não encontrada."));
        if (solicitacao.getQuantidade() == null || solicitacao.getQuantidade().signum() <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        solicitacao.setStatus(ABERTA);
        solicitacaoDAO.criar(solicitacao);

        String mensagem = String.format("%s - %s unidade(s), propriedade \"%s\" (solicitação #%d)",
                solicitacao.getProdutoDesejado(), solicitacao.getQuantidade(), propriedade.getNome(),
                solicitacao.getId());
        notificar(fornecedorDAO.listarTodos(), mensagem);
        return solicitacao;
    }

    /** RF5: o fornecedor responde com uma cotação; o agricultor dono da propriedade é notificado (RF8). */
    public Cotacao registrarCotacao(Cotacao cotacao) {
        Solicitacao solicitacao = solicitacaoDAO.buscarPorId(cotacao.getSolicitacaoId())
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada."));
        if (!ABERTA.equals(solicitacao.getStatus())) {
            throw new IllegalArgumentException("A solicitação não está aberta para novas cotações.");
        }
        Fornecedor fornecedor = fornecedorDAO.buscarPorId(cotacao.getFornecedorId())
                .orElseThrow(() -> new IllegalArgumentException("Fornecedor não encontrado."));
        if (cotacao.getPreco() == null || cotacao.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }
        if (cotacao.getPrazoEntrega() <= 0) {
            throw new IllegalArgumentException("O prazo de entrega deve ser maior que zero.");
        }
        boolean jaCotou = cotacaoDAO.listarPorSolicitacao(solicitacao.getId()).stream()
                .anyMatch(c -> c.getFornecedorId().equals(fornecedor.getId()));
        if (jaCotou) {
            throw new IllegalArgumentException("Este fornecedor já cotou esta solicitação.");
        }
        cotacaoDAO.criar(cotacao);

        PropriedadeRural propriedade = propriedadeDAO.buscarPorId(solicitacao.getPropriedadeId()).orElseThrow();
        Agricultor agricultor = agricultorDAO.buscarPorId(propriedade.getAgricultorId()).orElseThrow();
        agricultor.enviarNotificacao(String.format("%s cotou \"%s\" por R$ %s (entrega em %d dias)",
                fornecedor.getNomeCompleto(), solicitacao.getProdutoDesejado(), cotacao.getPreco(),
                cotacao.getPrazoEntrega()));
        return cotacao;
    }

    /** RF5: lista as propostas de uma solicitação da mais barata para a mais cara. */
    public List<Cotacao> compararCotacoes(Long solicitacaoId) {
        solicitacaoDAO.buscarPorId(solicitacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada."));
        return cotacaoDAO.listarPorSolicitacao(solicitacaoId);
    }

    public void alterarStatusSolicitacao(Long solicitacaoId, String novoStatus) {
        Solicitacao s = solicitacaoDAO.buscarPorId(solicitacaoId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada."));
        if (!List.of(ABERTA, FINALIZADA, CANCELADA).contains(novoStatus)) {
            throw new IllegalArgumentException("Status inválido. Use ABERTA, FINALIZADA ou CANCELADA.");
        }
        s.setStatus(novoStatus);
        solicitacaoDAO.atualizar(s);
    }

    /** RF6: o agrônomo registra a consultoria; ele recebe a notificação do novo atendimento (RF8). */
    public Consultoria registrarConsultoria(Consultoria consultoria) {
        PropriedadeRural propriedade = propriedadeDAO.buscarPorId(consultoria.getPropriedadeId())
                .orElseThrow(() -> new IllegalArgumentException("Propriedade rural não encontrada."));
        Agronomo agronomo = agronomoDAO.buscarPorId(consultoria.getAgronomoId())
                .orElseThrow(() -> new IllegalArgumentException("Agrônomo não encontrado."));
        consultoriaDAO.criar(consultoria);
        agronomo.enviarNotificacao(String.format("atendimento registrado na propriedade \"%s\" (consultoria #%d)",
                propriedade.getNome(), consultoria.getId()));
        return consultoria;
    }

    /** RF7: histórico completo de atendimentos de uma propriedade. */
    public HistoricoPropriedade historicoPropriedade(Long propriedadeId) {
        PropriedadeRural propriedade = propriedadeDAO.buscarPorId(propriedadeId)
                .orElseThrow(() -> new IllegalArgumentException("Propriedade rural não encontrada."));
        List<Solicitacao> solicitacoes = solicitacaoDAO.listarPorPropriedade(propriedadeId);
        Map<Long, List<Cotacao>> cotacoes = new LinkedHashMap<>();
        for (Solicitacao s : solicitacoes) {
            cotacoes.put(s.getId(), cotacaoDAO.listarPorSolicitacao(s.getId()));
        }
        return new HistoricoPropriedade(propriedade, solicitacoes, cotacoes,
                consultoriaDAO.listarPorPropriedade(propriedadeId));
    }

    /**
     * RF8 (polimorfismo): percorre uma lista de Notificavel sem saber o tipo concreto;
     * cada subclasse de Usuario executa o seu próprio enviarNotificacao().
     */
    public void notificar(List<? extends Notificavel> destinatarios, String mensagem) {
        for (Notificavel destinatario : destinatarios) {
            destinatario.enviarNotificacao(mensagem);
        }
    }

    /** Envia um aviso geral a todos os usuários cadastrados e devolve quantos foram notificados. */
    public int notificarTodosUsuarios(String mensagem) {
        List<Usuario> todos = new ArrayList<>();
        todos.addAll(agricultorDAO.listarTodos());
        todos.addAll(fornecedorDAO.listarTodos());
        todos.addAll(agronomoDAO.listarTodos());
        notificar(todos, mensagem);
        return todos.size();
    }
}
