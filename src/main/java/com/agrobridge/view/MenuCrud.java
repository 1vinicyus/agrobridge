package com.agrobridge.view;

import com.agrobridge.dao.DaoException;

/** Menu CRUD genérico (Template Method): as subclasses definem cada operação da entidade. */
public abstract class MenuCrud {

    protected abstract String titulo();

    protected abstract void cadastrar();

    protected abstract void listar();

    protected abstract void buscar();

    protected abstract void atualizar();

    protected abstract void excluir();

    public final void executar() {
        while (true) {
            System.out.println("\n--- " + titulo() + " ---");
            System.out.println("1) Cadastrar   2) Listar   3) Buscar por ID   4) Atualizar   5) Excluir   0) Voltar");
            int opcao = Entrada.lerInt("Opção");
            try {
                switch (opcao) {
                    case 1 -> cadastrar();
                    case 2 -> listar();
                    case 3 -> buscar();
                    case 4 -> atualizar();
                    case 5 -> excluir();
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("  Opção inválida.");
                }
            } catch (DaoException | IllegalArgumentException e) {
                System.out.println("  Erro: " + e.getMessage());
            }
        }
    }
}
