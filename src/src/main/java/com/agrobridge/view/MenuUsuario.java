package com.agrobridge.view;

import com.agrobridge.dao.UsuarioDAO;
import com.agrobridge.model.Usuario;
import com.agrobridge.util.SenhaUtil;
import com.agrobridge.util.Validador;

import java.util.List;
import java.util.Optional;

/** Parte comum do CRUD de Agricultor, Fornecedor e Agrônomo (herança também na camada de visão). */
public abstract class MenuUsuario<T extends Usuario> extends MenuCrud {

    protected final UsuarioDAO<T> dao;

    protected MenuUsuario(UsuarioDAO<T> dao) {
        this.dao = dao;
    }

    /** Pergunta os campos específicos do subtipo e devolve o objeto ainda sem id. */
    protected abstract T novoUsuario(String nome, String email, String telefone, String senhaHash);

    protected abstract void atualizarCamposEspecificos(T usuario);

    protected void aposCadastro(T usuario) {
        // gancho opcional
    }

    @Override
    protected void cadastrar() {
        String nome = Entrada.lerTexto("Nome completo");
        String email = Validador.email(Entrada.lerTexto("E-mail"));
        String telefone = Entrada.lerTexto("Telefone");
        String senhaHash = SenhaUtil.gerarHash(Entrada.lerTexto("Senha"));
        T usuario = novoUsuario(nome, email, telefone, senhaHash);
        dao.criar(usuario);
        System.out.println("  Cadastrado com sucesso: " + usuario);
        aposCadastro(usuario);
    }

    @Override
    protected void listar() {
        List<T> lista = dao.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("  Nenhum registro encontrado.");
        }
        lista.forEach(u -> System.out.println("  " + u));
    }

    @Override
    protected void buscar() {
        Long id = Entrada.lerLong("ID");
        Optional<T> usuario = dao.buscarPorId(id);
        System.out.println(usuario.map(u -> "  " + u).orElse("  Registro não encontrado."));
        usuario.ifPresent(this::detalhar);
    }

    protected void detalhar(T usuario) {
        // gancho opcional (ex.: listar propriedades do agricultor)
    }

    @Override
    protected void atualizar() {
        Long id = Entrada.lerLong("ID do registro a atualizar");
        Optional<T> encontrado = dao.buscarPorId(id);
        if (encontrado.isEmpty()) {
            System.out.println("  Registro não encontrado.");
            return;
        }
        T usuario = encontrado.get();
        usuario.setNomeCompleto(Entrada.lerTextoOuManter("Nome completo", usuario.getNomeCompleto()));
        usuario.setEmail(Validador.email(Entrada.lerTextoOuManter("E-mail", usuario.getEmail())));
        usuario.setTelefone(Entrada.lerTextoOuManter("Telefone", usuario.getTelefone()));
        atualizarCamposEspecificos(usuario);
        System.out.println(dao.atualizar(usuario) ? "  Atualizado com sucesso." : "  Nada foi atualizado.");
    }

    @Override
    protected void excluir() {
        Long id = Entrada.lerLong("ID do registro a excluir");
        if (Entrada.confirmar("Confirma a exclusão (apaga também os dados vinculados)?")) {
            System.out.println(dao.excluir(id) ? "  Excluído com sucesso." : "  Registro não encontrado.");
        }
    }
}
