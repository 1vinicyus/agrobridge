package com.agrobridge.model;

/** Contrato de notificação: cada perfil de usuário notifica de um jeito (polimorfismo). */
public interface Notificavel {
    void enviarNotificacao(String mensagem);
}
