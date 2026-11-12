package com.agrobridge.dao;

import java.util.List;
import java.util.Optional;

/** Contrato CRUD (Create, Read, Update, Delete) implementado por todos os DAOs. */
public interface CrudDAO<T> {
    T criar(T entidade);                    // Create
    Optional<T> buscarPorId(Long id);       // Read (um)
    List<T> listarTodos();                  // Read (todos)
    boolean atualizar(T entidade);          // Update
    boolean excluir(Long id);               // Delete
}
