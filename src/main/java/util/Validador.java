package com.agrobridge.util;

/** Validações simples de entrada usadas pela camada de visão. */
public final class Validador {

    private Validador() {
    }

    public static String somenteDigitos(String valor) {
        return valor == null ? "" : valor.replaceAll("\\D", "");
    }

    public static String cpf(String valor) {
        String d = somenteDigitos(valor);
        if (d.length() != 11) {
            throw new IllegalArgumentException("CPF deve ter 11 dígitos.");
        }
        return d;
    }

    public static String cnpj(String valor) {
        String d = somenteDigitos(valor);
        if (d.length() != 14) {
            throw new IllegalArgumentException("CNPJ deve ter 14 dígitos.");
        }
        return d;
    }

    public static String email(String valor) {
        if (valor == null || !valor.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        return valor.trim();
    }
}
