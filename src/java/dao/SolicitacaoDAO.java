package com.agrobridge.dao;

import com.agrobridge.model.Solicitacao;
import com.agrobridge.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SolicitacaoDAO implements CrudDAO<Solicitacao> {

    private static final String SELECT =
            "SELECT id, propriedade_id, produto_desejado, quantidade, data_criacao, status FROM solicitacao ";

    @Override
    public Solicitacao criar(Solicitacao s) {
        String sql = "INSERT INTO solicitacao (propriedade_id, produto_desejado, quantidade, status) "
                + "VALUES (?, ?, ?, ?) RETURNING id, data_criacao";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, s.getPropriedadeId());
            ps.setString(2, s.getProdutoDesejado());
            ps.setBigDecimal(3, s.getQuantidade());
            ps.setString(4, s.getStatus());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                s.setId(rs.getLong("id"));
                s.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
            }
            return s;
        } catch (SQLException e) {
            throw new DaoException("Erro ao cadastrar solicitação: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Solicitacao> buscarPorId(Long id) {
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT + "WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar solicitação: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Solicitacao> listarTodos() {
        return consultar(SELECT + "ORDER BY id", null);
    }

    public List<Solicitacao> listarPorPropriedade(Long propriedadeId) {
        return consultar(SELECT + "WHERE propriedade_id = ? ORDER BY data_criacao", propriedadeId);
    }

    @Override
    public boolean atualizar(Solicitacao s) {
        String sql = "UPDATE solicitacao SET produto_desejado = ?, quantidade = ?, status = ? WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getProdutoDesejado());
            ps.setBigDecimal(2, s.getQuantidade());
            ps.setString(3, s.getStatus());
            ps.setLong(4, s.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar solicitação: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean excluir(Long id) {
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM solicitacao WHERE id = ?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao excluir solicitação: " + e.getMessage(), e);
        }
    }

    private List<Solicitacao> consultar(String sql, Long parametro) {
        List<Solicitacao> lista = new ArrayList<>();
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
            throw new DaoException("Erro ao listar solicitações: " + e.getMessage(), e);
        }
    }

    private Solicitacao mapear(ResultSet rs) throws SQLException {
        Solicitacao s = new Solicitacao(rs.getLong("id"), rs.getLong("propriedade_id"),
                rs.getString("produto_desejado"), rs.getBigDecimal("quantidade"), rs.getString("status"));
        Timestamp ts = rs.getTimestamp("data_criacao");
        s.setDataCriacao(ts == null ? null : ts.toLocalDateTime());
        return s;
    }
}
