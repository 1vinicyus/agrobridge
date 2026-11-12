package com.agrobridge.view;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/** Leitura segura de dados digitados no console. */
public final class Entrada {

    /** Lançada quando a entrada padrão termina (Ctrl+D / fim do arquivo). */
    public static class EntradaEncerrada extends RuntimeException {
        public EntradaEncerrada() {
            super("Entrada encerrada.");
        }
    }

    private static final Scanner SCANNER = new Scanner(System.in, StandardCharsets.UTF_8);

    private Entrada() {
    }

    public static String lerTexto(String rotulo) {
        while (true) {
            System.out.print(rotulo + ": ");
            String texto = proximaLinha().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("  Campo obrigatório.");
        }
    }

    /** Em atualizações: ENTER mantém o valor atual. */
    public static String lerTextoOuManter(String rotulo, String atual) {
        System.out.printf("%s [%s]: ", rotulo, atual);
        String texto = proximaLinha().trim();
        return texto.isEmpty() ? atual : texto;
    }

    public static long lerLong(String rotulo) {
        while (true) {
            try {
                return Long.parseLong(lerTexto(rotulo));
            } catch (NumberFormatException e) {
                System.out.println("  Digite um número inteiro válido.");
            }
        }
    }

    public static int lerInt(String rotulo) {
        while (true) {
            try {
                return Integer.parseInt(lerTexto(rotulo));
            } catch (NumberFormatException e) {
                System.out.println("  Digite um número inteiro válido.");
            }
        }
    }

    public static BigDecimal lerDecimal(String rotulo) {
        while (true) {
            try {
                return new BigDecimal(lerTexto(rotulo).replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("  Digite um número válido (ex.: 12,5).");
            }
        }
    }

    public static BigDecimal lerDecimalOuManter(String rotulo, BigDecimal atual) {
        while (true) {
            System.out.printf("%s [%s]: ", rotulo, atual);
            String texto = proximaLinha().trim();
            if (texto.isEmpty()) {
                return atual;
            }
            try {
                return new BigDecimal(texto.replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("  Digite um número válido (ex.: 12,5).");
            }
        }
    }

    public static int lerIntOuManter(String rotulo, int atual) {
        while (true) {
            System.out.printf("%s [%d]: ", rotulo, atual);
            String texto = proximaLinha().trim();
            if (texto.isEmpty()) {
                return atual;
            }
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("  Digite um número inteiro válido.");
            }
        }
    }

        public static boolean confirmar(String pergunta) {
        while (true) {
            System.out.print(pergunta + " (s/n): ");
            String resposta = proximaLinha().trim().toLowerCase(java.util.Locale.ROOT);
            if (resposta.equals("s") || resposta.equals("sim") || resposta.equals("y")) {
                return true;
            }
            if (resposta.equals("n") || resposta.equals("nao") || resposta.equals("não")) {
                System.out.println("  Operação cancelada.");
                return false;
            }
            System.out.println("  Responda com 's' para sim ou 'n' para não.");
        }
    }

    private static String proximaLinha() {
        if (!SCANNER.hasNextLine()) {
            throw new EntradaEncerrada();
        }
        return SCANNER.nextLine();
    }
}
