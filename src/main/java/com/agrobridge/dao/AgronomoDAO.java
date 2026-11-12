package com.agrobridge.dao;

import com.agrobridge.model.Agronomo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AgronomoDAO extends UsuarioDAO<Agronomo> {

    @Override
    protected String tabelaSubtipo() {
        return "agronomo";
    }

    @Override
    protected String tipoUsuario() {
        return "AGRONOMO";
    }

    @Override
    protected void inserirSubtipo(Connection c, Agronomo a) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO agronomo (id, registro_profissional, especialidade) VALUES (?, ?, ?)")) {
            ps.setLong(1, a.getId());
            ps.setString(2, a.getRegistroProfissional());
            ps.setString(3, a.getEspecialidade());
            ps.executeUpdate();
        }
    }

    @Override
    protected void atualizarSubtipo(Connection c, Agronomo a) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "UPDATE agronomo SET registro_profissional = ?, especialidade = ? WHERE id = ?")) {
            ps.setString(1, a.getRegistroProfissional());
            ps.setString(2, a.getEspecialidade());
            ps.setLong(3, a.getId());
            ps.executeUpdate();
        }
    }

    @Override
    protected Agronomo mapear(ResultSet rs) throws SQLException {
        return new Agronomo(rs.getLong("usuario_id"), rs.getString("nome_completo"), rs.getString("email"),
                rs.getString("senha"), rs.getString("telefone"),
                rs.getString("registro_profissional"), rs.getString("especialidade"));
    }
}
