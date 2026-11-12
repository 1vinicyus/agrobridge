package com.agrobridge.dao;

import com.agrobridge.model.Agricultor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AgricultorDAO extends UsuarioDAO<Agricultor> {

    @Override
    protected String tabelaSubtipo() {
        return "agricultor";
    }

    @Override
    protected String tipoUsuario() {
        return "AGRICULTOR";
    }

    @Override
    protected void inserirSubtipo(Connection c, Agricultor a) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("INSERT INTO agricultor (id, cpf) VALUES (?, ?)")) {
            ps.setLong(1, a.getId());
            ps.setString(2, a.getCpf());
            ps.executeUpdate();
        }
    }

    @Override
    protected void atualizarSubtipo(Connection c, Agricultor a) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("UPDATE agricultor SET cpf = ? WHERE id = ?")) {
            ps.setString(1, a.getCpf());
            ps.setLong(2, a.getId());
            ps.executeUpdate();
        }
    }

    @Override
    protected Agricultor mapear(ResultSet rs) throws SQLException {
        return new Agricultor(rs.getLong("usuario_id"), rs.getString("nome_completo"), rs.getString("email"),
                rs.getString("senha"), rs.getString("telefone"), rs.getString("cpf"));
    }

    /** Ao buscar um agricultor, carrega também as propriedades que ele compõe (composição 1:N). */
    @Override
    public Optional<Agricultor> buscarPorId(Long id) {
        Optional<Agricultor> agricultor = super.buscarPorId(id);
        agricultor.ifPresent(a -> new PropriedadeRuralDAO().listarPorAgricultor(a.getId())
                .forEach(a::adicionarPropriedade));
        return agricultor;
    }
}
