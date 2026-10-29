package banco.servico;

import banco.modelo.Cliente;
import banco.modelo.ContaCorrente;
import banco.repositorio.RepositorioClientes;
import banco.repositorio.RepositorioContas;
import java.math.BigDecimal;
import java.math.RoundingMode;

/** Regras de negócio dos 13 requisitos, sem dependência do terminal. */
public class BancoService {
    private final RepositorioClientes clientes = new RepositorioClientes();
    private final RepositorioContas contas = new RepositorioContas();
    private int sequenciaCliente = 1;
    private int sequenciaConta = 1;

    // RF01 + RF06: um cadastro abre automaticamente uma conta.
    public Cliente cadastrarCliente(String nome, int idade, String email, String cpf) {
        if (clientes.porCpf(cpf) != null) throw new IllegalArgumentException("CPF já cadastrado");
        Cliente cliente = new Cliente(sequenciaCliente, nome, idade, email, cpf, sequenciaConta);
        ContaCorrente conta = new ContaCorrente(sequenciaConta, BigDecimal.ZERO);
        clientes.salvar(cliente);
        contas.salvar(conta);
        sequenciaCliente++;
        sequenciaConta++;
        return cliente;
    }

    // RF02 e RF03
    public Cliente consultarCliente(int id) { return clientes.porId(id); }
    public Cliente consultarClientePorCpf(String cpf) { return clientes.porCpf(cpf); }

    // RF04 e RF05
    public boolean ativarCliente(int id) {
        Cliente c = clientes.porId(id);
        if (c == null) return false;
        c.ativar();
        return true;
    }
    public boolean desativarCliente(int id) {
        Cliente c = clientes.porId(id);
        if (c == null) return false;
        c.desativar();
        return true;
    }

    // RF06 (criação de conta independente, se necessária)
    public ContaCorrente cadastrarConta(BigDecimal saldoInicial) {
        ContaCorrente conta = new ContaCorrente(sequenciaConta, saldoInicial);
        contas.salvar(conta);
        sequenciaConta++;
        return conta;
    }

    // RF07 e RF08
    public ContaCorrente consultarConta(int id) { return contas.porId(id); }
    public BigDecimal consultarSaldo(int id) {
        ContaCorrente conta = contas.porId(id);
        return conta == null ? null : conta.getSaldo();
    }
    public BigDecimal consultarSaldoPorCpf(String cpf) {
        Cliente c = clientes.porCpf(cpf);
        return c == null ? null : consultarSaldo(c.getContaId());
    }

    // RF09
    public boolean depositar(int contaId, BigDecimal valor) {
        ContaCorrente conta = contas.porId(contaId);
        if (conta == null || !conta.isAtiva() || !valorValido(valor)) return false;
        conta.creditar(valor);
        return true;
    }

    // RF10
    public boolean sacar(int contaId, BigDecimal valor) {
        ContaCorrente conta = contas.porId(contaId);
        if (conta == null || !conta.isAtiva() || !valorValido(valor) || conta.getSaldo().compareTo(valor) < 0) return false;
        conta.debitar(valor);
        return true;
    }

    // RF11
    public boolean transferir(int origemId, int destinoId, BigDecimal valor) {
        ContaCorrente origem = contas.porId(origemId), destino = contas.porId(destinoId);
        if (origemId == destinoId || origem == null || destino == null || !origem.isAtiva() || !destino.isAtiva()
                || !valorValido(valor) || origem.getSaldo().compareTo(valor) < 0) return false;
        origem.debitar(valor);
        destino.creditar(valor);
        return true;
    }

    // RF12: exclusão conjunta evita conta órfã.
    public boolean removerCliente(int id) {
        Cliente c = clientes.porId(id);
        if (c == null) return false;
        clientes.remover(id);
        contas.remover(c.getContaId());
        return true;
    }

    // RF13: conta de cliente existente não pode ser removida isoladamente.
    public boolean removerConta(int id) {
        if (clientes.contaVinculada(id)) return false;
        return contas.remover(id);
    }

    private boolean valorValido(BigDecimal valor) {
        if (valor == null || valor.signum() <= 0) return false;
        try { valor.setScale(2, RoundingMode.UNNECESSARY); return true; }
        catch (ArithmeticException e) { return false; }
    }
}
