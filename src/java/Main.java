package com.agrobridge;

import com.agrobridge.util.ConnectionFactory;
import com.agrobridge.view.Entrada;
import com.agrobridge.view.MenuPrincipal;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/** Ponto de entrada do AgroBridge (aplicação de console com persistência em PostgreSQL). */
public class Main {

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
        try {
            ConnectionFactory.testarConexao();
            System.out.println("Conexão com o PostgreSQL estabelecida com sucesso.");
        } catch (SQLException e) {
            System.err.println("Não foi possível conectar ao banco: " + e.getMessage());
            System.err.println("Verifique se o PostgreSQL está ativo, se o banco 'agrobridge' foi criado com o");
            System.err.println("script database/database.sql e se usuário/senha estão corretos em db.properties.");
            System.exit(1);
        }
        try {
            new MenuPrincipal().iniciar();
        } catch (Entrada.EntradaEncerrada e) {
            System.out.println("\nEntrada encerrada. Até logo!");
        }
    }
}
