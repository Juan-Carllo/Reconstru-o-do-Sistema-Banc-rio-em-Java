package banco.apresentacao;

import banco.modelo.Cliente;
import banco.modelo.ContaCorrente;
import banco.servico.BancoService;
import java.math.BigDecimal;
import java.util.Scanner;

/** Interface de texto: apenas lê informações e chama BancoService. */
public class Main {
    private final Scanner entrada = new Scanner(System.in);
    private final BancoService banco = new BancoService();

    public static void main(String[] args) { new Main().executar(); }

    private void executar() {
        while (true) {
            System.out.println("\n=== SISTEMA BANCÁRIO ===");
            System.out.println("1 Cadastrar cliente e conta | 2 Consultar cliente por ID | 3 Consultar cliente por CPF");
            System.out.println("4 Ativar cliente | 5 Desativar cliente | 6 Cadastrar conta independente");
            System.out.println("7 Consultar conta | 8 Consultar saldo | 9 Depositar | 10 Sacar");
            System.out.println("11 Transferir | 12 Remover cliente | 13 Remover conta | 0 Sair");
            try {
                int opcao = inteiro("Opção: ");
                if (opcao == 0) break;
                executarOpcao(opcao);
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        entrada.close();
    }

    private void executarOpcao(int opcao) {
        switch (opcao) {
            case 1: {
                String nome = texto("Nome: ");
                int idade = inteiro("Idade: ");
                String email = texto("E-mail: ");
                String cpf = texto("CPF: ");
                Cliente novo = banco.cadastrarCliente(nome, idade, email, cpf);
                System.out.println("Cliente " + novo.getId() + " e conta " + novo.getContaId() + " cadastrados.");
                break;
            }
            case 2: mostrarCliente(banco.consultarCliente(inteiro("ID do cliente: "))); break;
            case 3: mostrarCliente(banco.consultarClientePorCpf(texto("CPF: "))); break;
            case 4: resultado(banco.ativarCliente(inteiro("ID do cliente: "))); break;
            case 5: resultado(banco.desativarCliente(inteiro("ID do cliente: "))); break;
            case 6: {
                ContaCorrente c = banco.cadastrarConta(valor("Saldo inicial: "));
                System.out.println("Conta " + c.getId() + " cadastrada.");
                break;
            }
            case 7: mostrarConta(banco.consultarConta(inteiro("ID da conta: "))); break;
            case 8: {
                String tipo = texto("Consultar por (1 conta, 2 CPF): ");
                BigDecimal saldo = "1".equals(tipo) ? banco.consultarSaldo(inteiro("ID da conta: "))
                    : "2".equals(tipo) ? banco.consultarSaldoPorCpf(texto("CPF: ")) : null;
                System.out.println(saldo == null ? "Não encontrado." : "Saldo: R$ " + saldo);
                break;
            }
            case 9: resultado(banco.depositar(inteiro("ID da conta: "), valor("Valor: "))); break;
            case 10: resultado(banco.sacar(inteiro("ID da conta: "), valor("Valor: "))); break;
            case 11: {
                int origem = inteiro("Conta origem: ");
                int destino = inteiro("Conta destino: ");
                resultado(banco.transferir(origem, destino, valor("Valor: ")));
                break;
            }
            case 12: resultado(banco.removerCliente(inteiro("ID do cliente: "))); break;
            case 13: resultado(banco.removerConta(inteiro("ID da conta: "))); break;
            default: System.out.println("Opção desconhecida.");
        }
    }

    private void mostrarCliente(Cliente c) {
        System.out.println(c == null ? "Cliente não encontrado." :
            "ID: " + c.getId() + " | Nome: " + c.getNome() + " | CPF: " + c.getCpf()
                + " | Idade: " + c.getIdade() + " | E-mail: " + c.getEmail()
                + " | Conta: " + c.getContaId() + " | Ativo: " + c.isAtivo());
    }
    private void mostrarConta(ContaCorrente c) {
        System.out.println(c == null ? "Conta não encontrada." :
            "ID: " + c.getId() + " | Saldo: R$ " + c.getSaldo() + " | Ativa: " + c.isAtiva());
    }
    private void resultado(boolean sucesso) { System.out.println(sucesso ? "Operação realizada." : "Operação recusada ou registro inexistente."); }
    private String texto(String mensagem) { System.out.print(mensagem); return entrada.nextLine().trim(); }
    private int inteiro(String mensagem) {
        try { return Integer.parseInt(texto(mensagem)); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Informe um número inteiro"); }
    }
    private BigDecimal valor(String mensagem) {
        try { return new BigDecimal(texto(mensagem).replace(',', '.')); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Informe um valor numérico"); }
    }
}
