package com.agrobridge.dao;

import com.agrobridge.model.PropriedadeRural;
import com.agrobridge.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PropriedadeRuralDAO implements CrudDAO<PropriedadeRural> {

    private static final String SELECT = "SELECT id, agricultor_id, nome, localizacao, area_hectares FROM propriedade_rural ";

    @Override
    public PropriedadeRural criar(PropriedadeRural p) {
        String sql = "INSERT INTO propriedade_rural (agricultor_id, nome, localizacao, area_hectares) "
                + "VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, p.getAgricultorId());
            ps.setString(2, p.getNome());
            ps.setString(3, p.getLocalizacao());
            ps.setBigDecimal(4, p.getAreaHectares());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                p.setId(rs.getLong(1));
            }
            return p;
        } catch (SQLException e) {
            throw new DaoException("Erro ao cadastrar propriedade rural: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<PropriedadeRural> buscarPorId(Long id) {
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT + "WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar propriedade rural: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PropriedadeRural> listarTodos() {
        return consultar(SELECT + "ORDER BY id", null);
    }

    public List<PropriedadeRural> listarPorAgricultor(Long agricultorId) {
        return consultar(SELECT + "WHERE agricultor_id = ? ORDER BY id", agricultorId);
    }

    @Override
    public boolean atualizar(PropriedadeRural p) {
        String sql = "UPDATE propriedade_rural SET nome = ?, localizacao = ?, area_hectares = ? WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getLocalizacao());
            ps.setBigDecimal(3, p.getAreaHectares());
            ps.setLong(4, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar propriedade rural: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean excluir(Long id) {
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM propriedade_rural WHERE id = ?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao excluir propriedade rural: " + e.getMessage(), e);
        }
    }

    private List<PropriedadeRural> consultar(String sql, Long parametro) {
        List<PropriedadeRural> lista = new ArrayList<>();
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
            throw new DaoException("Erro ao listar propriedades rurais: " + e.getMessage(), e);
        }
    }

    private PropriedadeRural mapear(ResultSet rs) throws SQLException {
        return new PropriedadeRural(rs.getLong("id"), rs.getLong("agricultor_id"), rs.getString("nome"),
                rs.getString("localizacao"), rs.getBigDecimal("area_hectares"));
    }
}
