package banco.repositorio;

import banco.modelo.Cliente;
import java.util.LinkedHashMap;
import java.util.Map;

/** Armazenamento em memória; pode ser trocado por persistência no futuro. */
public class RepositorioClientes {
    private final Map<Integer, Cliente> clientes = new LinkedHashMap<>();
    public void salvar(Cliente cliente) { clientes.put(cliente.getId(), cliente); }
    public Cliente porId(int id) { return clientes.get(id); }
    public Cliente porCpf(String cpf) {
        if (cpf == null) return null;
        for (Cliente c : clientes.values()) if (c.getCpf().equals(cpf.trim())) return c;
        return null;
    }
    public boolean remover(int id) { return clientes.remove(id) != null; }
    public boolean contaVinculada(int contaId) {
        for (Cliente c : clientes.values()) if (c.getContaId() == contaId) return true;
        return false;
    }
}
