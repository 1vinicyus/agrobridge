package com.agrobridge.dao;

import com.agrobridge.model.Fornecedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FornecedorDAO extends UsuarioDAO<Fornecedor> {

    @Override
    protected String tabelaSubtipo() {
        return "fornecedor";
    }

    @Override
    protected String tipoUsuario() {
        return "FORNECEDOR";
    }

    @Override
    protected void inserirSubtipo(Connection c, Fornecedor f) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO fornecedor (id, cnpj, area_atuacao) VALUES (?, ?, ?)")) {
            ps.setLong(1, f.getId());
            ps.setString(2, f.getCnpj());
            ps.setString(3, f.getAreaAtuacao());
            ps.executeUpdate();
        }
    }

    @Override
    protected void atualizarSubtipo(Connection c, Fornecedor f) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(
                "UPDATE fornecedor SET cnpj = ?, area_atuacao = ? WHERE id = ?")) {
            ps.setString(1, f.getCnpj());
            ps.setString(2, f.getAreaAtuacao());
            ps.setLong(3, f.getId());
            ps.executeUpdate();
        }
    }

    @Override
    protected Fornecedor mapear(ResultSet rs) throws SQLException {
        return new Fornecedor(rs.getLong("usuario_id"), rs.getString("nome_completo"), rs.getString("email"),
                rs.getString("senha"), rs.getString("telefone"), rs.getString("cnpj"), rs.getString("area_atuacao"));
    }
}
