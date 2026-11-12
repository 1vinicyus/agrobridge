package com.agrobridge.dao;

/** Erro de persistência (encapsula SQLException para as camadas superiores). */
public class DaoException extends RuntimeException {
    public DaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
