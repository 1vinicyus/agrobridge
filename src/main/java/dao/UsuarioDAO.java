package com.agrobridge.dao;

import com.agrobridge.model.Usuario;
import com.agrobridge.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO abstrato para a hierarquia de usuários (herança de tabelas: usuario + tabela do subtipo).
 * As subclasses só implementam o que é específico do subtipo (Template Method).
 */
public abstract class UsuarioDAO<T extends Usuario> implements CrudDAO<T> {

    private static final String SELECT_BASE =
            "SELECT u.id AS usuario_id, u.nome_completo, u.email, u.senha, u.telefone, s.* "
                    + "FROM usuario u JOIN %s s ON s.id = u.id ";

    protected abstract String tabelaSubtipo();

    protected abstract String tipoUsuario();

    protected abstract void inserirSubtipo(Connection c, T usuario) throws SQLException;

    protected abstract void atualizarSubtipo(Connection c, T usuario) throws SQLException;

    protected abstract T mapear(ResultSet rs) throws SQLException;

    @Override
    public T criar(T usuario) {
        String sql = "INSERT INTO usuario (nome_completo, email, senha, telefone, tipo_usuario) "
                + "VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection c = ConnectionFactory.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, usuario.getNomeCompleto());
                ps.setString(2, usuario.getEmail());
                ps.setString(3, usuario.getSenha());
                ps.setString(4, usuario.getTelefone());
                ps.setString(5, tipoUsuario());
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    usuario.setId(rs.getLong(1));
                }
                inserirSubtipo(c, usuario);
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
            return usuario;
        } catch (SQLException e) {
            throw new DaoException("Erro ao cadastrar " + tipoUsuario().toLowerCase() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<T> buscarPorId(Long id) {
        String sql = String.format(SELECT_BASE, tabelaSubtipo()) + "WHERE u.id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar " + tipoUsuario().toLowerCase() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<T> listarTodos() {
        String sql = String.format(SELECT_BASE, tabelaSubtipo()) + "ORDER BY u.nome_completo";
        List<T> lista = new ArrayList<>();
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar " + tipoUsuario().toLowerCase() + "s: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean atualizar(T usuario) {
        String sql = "UPDATE usuario SET nome_completo = ?, email = ?, senha = ?, telefone = ? "
                + "WHERE id = ? AND tipo_usuario = ?";
        try (Connection c = ConnectionFactory.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, usuario.getNomeCompleto());
                ps.setString(2, usuario.getEmail());
                ps.setString(3, usuario.getSenha());
                ps.setString(4, usuario.getTelefone());
                ps.setLong(5, usuario.getId());
                ps.setString(6, tipoUsuario());
                if (ps.executeUpdate() == 0) {
                    c.rollback();
                    return false;
                }
                atualizarSubtipo(c, usuario);
                c.commit();
                return true;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar " + tipoUsuario().toLowerCase() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean excluir(Long id) {
        String sql = "DELETE FROM usuario WHERE id = ? AND tipo_usuario = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.setString(2, tipoUsuario());
            return ps.executeUpdate() > 0; // ON DELETE CASCADE remove o subtipo e os dados dependentes
        } catch (SQLException e) {
            throw new DaoException("Erro ao excluir " + tipoUsuario().toLowerCase() + ": " + e.getMessage(), e);
        }
    }
}
