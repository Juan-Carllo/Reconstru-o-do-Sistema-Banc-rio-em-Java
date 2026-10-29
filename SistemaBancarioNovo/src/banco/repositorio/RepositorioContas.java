package banco.repositorio;

import banco.modelo.ContaCorrente;
import java.util.LinkedHashMap;
import java.util.Map;

public class RepositorioContas {
    private final Map<Integer, ContaCorrente> contas = new LinkedHashMap<>();
    public void salvar(ContaCorrente conta) { contas.put(conta.getId(), conta); }
    public ContaCorrente porId(int id) { return contas.get(id); }
    public boolean remover(int id) { return contas.remove(id) != null; }
}
