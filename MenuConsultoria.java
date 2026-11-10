package com.agrobridge.view;

import com.agrobridge.dao.ConsultoriaDAO;
import com.agrobridge.model.Consultoria;
import com.agrobridge.service.AgroBridgeService;

import java.util.List;

/** RF6: registro de consultorias técnicas pelo agrônomo. */
public class MenuConsultoria extends MenuCrud {

    private final ConsultoriaDAO dao = new ConsultoriaDAO();
    private final AgroBridgeService service;

    public MenuConsultoria(AgroBridgeService service) {
        this.service = service;
    }

    @Override
    protected String titulo() {
        return "CONSULTORIAS TÉCNICAS";
    }

    @Override
    protected void cadastrar() {
        Consultoria c = new Consultoria();
        c.setPropriedadeId(Entrada.lerLong("ID da propriedade rural"));
        c.setAgronomoId(Entrada.lerLong("ID do agrônomo"));
        c.setDiagnostico(Entrada.lerTexto("Diagnóstico"));
        c.setRecomendacoes(Entrada.lerTexto("Recomendações"));
        service.registrarConsultoria(c);
        System.out.println("  Consultoria registrada: " + c);
    }

    @Override
    protected void listar() {
        List<Consultoria> lista = dao.listarTodos();
        if (lista.isEmpty()) {
            System.out.println("  Nenhuma consultoria cadastrada.");
        }
        lista.forEach(c -> System.out.println("  " + c));
    }

    @Override
    protected void buscar() {
        System.out.println(dao.buscarPorId(Entrada.lerLong("ID"))
                .map(c -> "  " + c).orElse("  Consultoria não encontrada."));
    }

    @Override
    protected void atualizar() {
        Consultoria c = dao.buscarPorId(Entrada.lerLong("ID da consultoria a atualizar")).orElse(null);
        if (c == null) {
            System.out.println("  Consultoria não encontrada.");
            return;
        }
        c.setDiagnostico(Entrada.lerTextoOuManter("Diagnóstico", c.getDiagnostico()));
        c.setRecomendacoes(Entrada.lerTextoOuManter("Recomendações", c.getRecomendacoes()));
        System.out.println(dao.atualizar(c) ? "  Atualizada com sucesso." : "  Nada foi atualizado.");
    }

    @Override
    protected void excluir() {
        Long id = Entrada.lerLong("ID da consultoria a excluir");
        if (Entrada.confirmar("Confirma a exclusão?")) {
            System.out.println(dao.excluir(id) ? "  Excluída com sucesso." : "  Consultoria não encontrada.");
        }
    }
}
