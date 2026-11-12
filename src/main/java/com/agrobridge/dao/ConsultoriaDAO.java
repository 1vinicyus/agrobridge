package com.agrobridge.dao;

import com.agrobridge.model.Consultoria;
import com.agrobridge.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsultoriaDAO implements CrudDAO<Consultoria> {

    private static final String SELECT =
            "SELECT id, propriedade_id, agronomo_id, data_atendimento, diagnostico, recomendacoes FROM consultoria ";

    @Override
    public Consultoria criar(Consultoria cons) {
        String sql = "INSERT INTO consultoria (propriedade_id, agronomo_id, diagnostico, recomendacoes) "
                + "VALUES (?, ?, ?, ?) RETURNING id, data_atendimento";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, cons.getPropriedadeId());
            ps.setLong(2, cons.getAgronomoId());
            ps.setString(3, cons.getDiagnostico());
            ps.setString(4, cons.getRecomendacoes());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                cons.setId(rs.getLong("id"));
                cons.setDataAtendimento(rs.getTimestamp("data_atendimento").toLocalDateTime());
            }
            return cons;
        } catch (SQLException e) {
            throw new DaoException("Erro ao cadastrar consultoria: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Consultoria> buscarPorId(Long id) {
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT + "WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar consultoria: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Consultoria> listarTodos() {
        return consultar(SELECT + "ORDER BY data_atendimento", null);
    }

    public List<Consultoria> listarPorPropriedade(Long propriedadeId) {
        return consultar(SELECT + "WHERE propriedade_id = ? ORDER BY data_atendimento", propriedadeId);
    }

    @Override
    public boolean atualizar(Consultoria cons) {
        String sql = "UPDATE consultoria SET diagnostico = ?, recomendacoes = ? WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cons.getDiagnostico());
            ps.setString(2, cons.getRecomendacoes());
            ps.setLong(3, cons.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar consultoria: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean excluir(Long id) {
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM consultoria WHERE id = ?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao excluir consultoria: " + e.getMessage(), e);
        }
    }

    private List<Consultoria> consultar(String sql, Long parametro) {
        List<Consultoria> lista = new ArrayList<>();
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
            throw new DaoException("Erro ao listar consultorias: " + e.getMessage(), e);
        }
    }

    private Consultoria mapear(ResultSet rs) throws SQLException {
        Consultoria c = new Consultoria(rs.getLong("id"), rs.getLong("propriedade_id"), rs.getLong("agronomo_id"),
                rs.getString("diagnostico"), rs.getString("recomendacoes"));
        Timestamp ts = rs.getTimestamp("data_atendimento");
        c.setDataAtendimento(ts == null ? null : ts.toLocalDateTime());
        return c;
    }
}
