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
        if (d.chars().distinct().count() == 1) {
            throw new IllegalArgumentException("CPF inválido.");
        }
        if (digitoCpf(d, 9) != d.charAt(9) || digitoCpf(d, 10) != d.charAt(10)) {
            throw new IllegalArgumentException("CPF inválido.");
        }
        return d;
    }

    public static String cnpj(String valor) {
        String d = somenteDigitos(valor);
        if (d.length() != 14) {
            throw new IllegalArgumentException("CNPJ deve ter 14 dígitos.");
        }
        if (d.chars().distinct().count() == 1) {
            throw new IllegalArgumentException("CNPJ inválido.");
        }
        if (digitoCnpj(d, 12) != d.charAt(12) || digitoCnpj(d, 13) != d.charAt(13)) {
            throw new IllegalArgumentException("CNPJ inválido.");
        }
        return d;
    }

    public static String telefone(String valor) {
        String d = somenteDigitos(valor);
        if (d.length() < 10 || d.length() > 11) {
            throw new IllegalArgumentException("Telefone deve ter 10 ou 11 dígitos (com DDD).");
        }
        return valor.trim();
    }

    /** Dígito verificador do CPF na posição informada (9 ou 10), pelo módulo 11. */
    private static char digitoCpf(String d, int posicao) {
        int soma = 0;
        int peso = posicao + 1;
        for (int i = 0; i < posicao; i++) {
            soma += (d.charAt(i) - '0') * peso--;
        }
        int resto = soma % 11;
        return (char) ('0' + (resto < 2 ? 0 : 11 - resto));
    }

    /** Dígito verificador do CNPJ na posição informada (12 ou 13), pelo módulo 11. */
    private static char digitoCnpj(String d, int posicao) {
        int soma = 0;
        int peso = 2;
        for (int i = posicao - 1; i >= 0; i--) {
            soma += (d.charAt(i) - '0') * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }
        int resto = soma % 11;
        return (char) ('0' + (resto < 2 ? 0 : 11 - resto));
    }

    public static String email(String valor) {
        if (valor == null || !valor.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        return valor.trim();
    }
}
