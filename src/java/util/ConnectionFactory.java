package com.agrobridge.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Centraliza a conexão JDBC com o PostgreSQL. Lê src/main/resources/db.properties;
 * variáveis de ambiente (AGROBRIDGE_DB_*) têm prioridade sobre o arquivo.
 */
public final class ConnectionFactory {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("Arquivo db.properties não encontrado em src/main/resources.");
            }
            PROPS.load(in);
            Class.forName(PROPS.getProperty("db.driver", "org.postgresql.Driver"));
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Falha ao carregar a configuração do banco: " + e.getMessage(), e);
        }
    }

    private ConnectionFactory() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                valor("db.url", "AGROBRIDGE_DB_URL"),
                valor("db.user", "AGROBRIDGE_DB_USER"),
                valor("db.password", "AGROBRIDGE_DB_PASSWORD"));
    }

    /** Abre e fecha uma conexão para confirmar que o banco está acessível. */
    public static void testarConexao() throws SQLException {
        try (Connection c = getConnection()) {
            if (!c.isValid(3)) {
                throw new SQLException("Conexão inválida.");
            }
        }
    }

    private static String valor(String chave, String variavelAmbiente) {
        String env = System.getenv(variavelAmbiente);
        return (env != null && !env.isBlank()) ? env : PROPS.getProperty(chave);
    }
}
