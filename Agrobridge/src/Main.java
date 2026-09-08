package com.agrobridge.app;

import com.agrobridge.model.Agricultor;
import com.agrobridge.model.Agronomo;
import com.agrobridge.model.Cotacao;
import com.agrobridge.model.Fornecedor;
import com.agrobridge.model.Notificavel;
import com.agrobridge.model.PropriedadeRural;
import com.agrobridge.model.Solicitacao;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe de demonstração do modelo de domínio do AgroBridge.
 * Exercita, em sequência, os requisitos funcionais RF01 a RF08 e evidencia
 * a herança, a composição 1:N e o polimorfismo descritos no diagrama de
 * classes (Figura 1) do documento da 1ª entrega.
 */
public class Main {

    public static void main(String[] args) {

        // RF01 - cadastro do agricultor
        Agricultor agricultor = new Agricultor(
                "João Silva", "joao@fazenda.com", "senha123", "(44) 99999-0000", "12345678900");
        agricultor.cadastrar();

        PropriedadeRural fazenda = new PropriedadeRural("Fazenda Boa Vista", "Maringá - PR", 150.0);
        agricultor.adicionarPropriedade(fazenda);

        // RF02 - cadastro do fornecedor
        Fornecedor fornecedor = new Fornecedor(
                "AgroInsumos Ltda", "contato@agroinsumos.com", "senha456", "(44) 3333-0000",
                "12.345.678/0001-99", "Fertilizantes");
        fornecedor.cadastrar();

        // RF03 - cadastro do agrônomo
        Agronomo agronomo = new Agronomo(
                "Dra. Maria Souza", "maria@agro.com", "senha789", "(44) 98888-7777",
                "CREA-PR 123456", "Manejo de solo");
        agronomo.cadastrar();

        System.out.println();

        // RF04 - agricultor registra solicitação de cotação
        Solicitacao solicitacao = agricultor.registrarSolicitacao(fazenda, "Fertilizante NPK 04-14-08", 500.0);
        System.out.println("Solicitação registrada: " + solicitacao);

        // RF05 - fornecedor responde com cotação (RF08: notifica o agricultor)
        fornecedor.responderSolicitacao(solicitacao, new BigDecimal("2500.00"), 7, "30 dias");
        fornecedor.responderSolicitacao(solicitacao, new BigDecimal("2350.00"), 12, "45 dias");

        System.out.println("\nCotações comparadas (menor -> maior preço):");
        for (Cotacao cotacao : agricultor.compararCotacoes(solicitacao)) {
            System.out.println("  " + cotacao);
        }

        // RF06 - agrônomo registra consultoria (RF08: notifica o próprio agrônomo)
        agronomo.registrarConsultoria(fazenda, new Date(),
                "Solo com baixo teor de fósforo", "Aplicar adubação fosfatada e corrigir pH");

        // RF07 - histórico de atendimentos da propriedade
        System.out.println("\nHistórico de atendimentos da propriedade '" + fazenda.getNome() + "':");
        fazenda.historicoAtendimentos().forEach(linha -> System.out.println("  " + linha));

        // RF08 - notificação polimórfica: mesmo método, comportamento distinto por perfil
        System.out.println("\nDemonstração de polimorfismo via interface Notificavel:");
        Notificavel[] usuarios = { agricultor, fornecedor, agronomo };
        for (Notificavel usuario : usuarios) {
            usuario.enviarNotificacao("evento de teste");
        }
    }
}
