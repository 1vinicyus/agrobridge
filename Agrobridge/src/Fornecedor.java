package com.agrobridge.model;

/**
 * Contrato de notificação implementado de forma polimórfica pelos três perfis
 * de usuário do sistema (RF08).
 *
 * Cada perfil concreto (Agricultor, Fornecedor, Agronomo) sobrescreve
 * {@link #enviarNotificacao(String)} com sua própria regra de alerta, sem que
 * o restante do sistema precise conhecer o tipo concreto do usuário notificado.
 */
public interface Notificavel {

    void enviarNotificacao(String mensagem);
}
