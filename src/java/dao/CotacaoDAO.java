package com.agrobridge.dao;

import com.agrobridge.model.Cotacao;
import com.agrobridge.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CotacaoDAO implements CrudDAO<Cotacao> {

    private static final String SELECT =
            "SELECT id, solicitacao_id, fornecedor_id, preco, prazo_entrega, condicoes_pagamento FROM cotacao ";

    @Override
    public Cotacao criar(Cotacao cot) {
        String sql = "INSERT INTO cotacao (solicitacao_id, fornecedor_id, preco, prazo_entrega, condicoes_pagamento) "
                + "VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, cot.getSolicitacaoId());
            ps.setLong(2, cot.getFornecedorId());
            ps.setBigDecimal(3, cot.getPreco());
            ps.setInt(4, cot.getPrazoEntrega());
            ps.setString(5, cot.getCondicoesPagamento());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                cot.setId(rs.getLong(1));
            }
            return cot;
        } catch (SQLException e) {
            throw new DaoException("Erro ao cadastrar cotação: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cotacao> buscarPorId(Long id) {
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT + "WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar cotação: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cotacao> listarTodos() {
        return consultar(SELECT + "ORDER BY id", null);
    }

    /** Cotações de uma solicitação, da mais barata para a mais cara (comparação de propostas). */
    public List<Cotacao> listarPorSolicitacao(Long solicitacaoId) {
        return consultar(SELECT + "WHERE solicitacao_id = ? ORDER BY preco ASC, prazo_entrega ASC", solicitacaoId);
    }

    @Override
    public boolean atualizar(Cotacao cot) {
        String sql = "UPDATE cotacao SET preco = ?, prazo_entrega = ?, condicoes_pagamento = ? WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBigDecimal(1, cot.getPreco());
            ps.setInt(2, cot.getPrazoEntrega());
            ps.setString(3, cot.getCondicoesPagamento());
            ps.setLong(4, cot.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar cotação: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean excluir(Long id) {
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM cotacao WHERE id = ?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao excluir cotação: " + e.getMessage(), e);
        }
    }

    private List<Cotacao> consultar(String sql, Long parametro) {
        List<Cotacao> lista = new ArrayList<>();
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (parametro != null) {
                ps.setLong(1, parametro);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar cotações: " + e.getMessage(), e);
        }
    }

    private Cotacao mapear(ResultSet rs) throws SQLException {
        return new Cotacao(rs.getLong("id"), rs.getLong("solicitacao_id"), rs.getLong("fornecedor_id"),
                rs.getBigDecimal("preco"), rs.getInt("prazo_entrega"), rs.getString("condicoes_pagamento"));
    }
}
