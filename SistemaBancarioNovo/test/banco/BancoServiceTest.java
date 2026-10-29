package banco;

import banco.modelo.Cliente;
import banco.modelo.ContaCorrente;
import banco.servico.BancoService;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BancoServiceTest {
    private BancoService banco;
    private Cliente ana;
    private Cliente bruno;
    private BigDecimal dinheiro(String valor) { return new BigDecimal(valor); }

    @Before public void preparar() {
        banco = new BancoService();
        ana = banco.cadastrarCliente("Ana", 25, "ana@email.com", "11122233344");
        bruno = banco.cadastrarCliente("Bruno", 30, "bruno@email.com", "55566677788");
    }

    @Test public void rf01e06CadastrarClienteAbreConta() {
        assertNotNull(banco.consultarCliente(ana.getId()));
        assertEquals(0, dinheiro("0.00").compareTo(banco.consultarSaldo(ana.getContaId())));
    }
    @Test public void rf02e03ConsultarCliente() {
        assertEquals("Ana", banco.consultarCliente(ana.getId()).getNome());
        assertEquals(ana.getId(), banco.consultarClientePorCpf(ana.getCpf()).getId());
        assertNull(banco.consultarCliente(999));
    }
    @Test public void rf04e05AtivarDesativarCliente() {
        assertTrue(banco.desativarCliente(ana.getId()));
        assertFalse(banco.consultarCliente(ana.getId()).isAtivo());
        assertTrue(banco.ativarCliente(ana.getId()));
        assertTrue(banco.consultarCliente(ana.getId()).isAtivo());
        assertFalse(banco.ativarCliente(999));
    }
    @Test public void rf06e07CadastrarConsultarConta() {
        ContaCorrente nova = banco.cadastrarConta(dinheiro("25.00"));
        assertEquals(0, dinheiro("25.00").compareTo(banco.consultarConta(nova.getId()).getSaldo()));
    }
    @Test public void rf08ConsultarSaldoPorContaECpf() {
        assertEquals(0, banco.consultarSaldo(ana.getContaId()).compareTo(banco.consultarSaldoPorCpf(ana.getCpf())));
        assertNull(banco.consultarSaldoPorCpf("inexistente"));
    }
    @Test public void rf09Depositar() {
        assertTrue(banco.depositar(ana.getContaId(), dinheiro("10.50")));
        assertEquals(0, dinheiro("10.50").compareTo(banco.consultarSaldo(ana.getContaId())));
        assertFalse(banco.depositar(ana.getContaId(), dinheiro("-1")));
    }
    @Test public void rf10Sacar() {
        banco.depositar(ana.getContaId(), dinheiro("50"));
        assertTrue(banco.sacar(ana.getContaId(), dinheiro("20")));
        assertFalse(banco.sacar(ana.getContaId(), dinheiro("50")));
        assertEquals(0, dinheiro("30").compareTo(banco.consultarSaldo(ana.getContaId())));
    }
    @Test public void rf11Transferir() {
        banco.depositar(ana.getContaId(), dinheiro("100"));
        assertTrue(banco.transferir(ana.getContaId(), bruno.getContaId(), dinheiro("60")));
        assertFalse(banco.transferir(ana.getContaId(), bruno.getContaId(), dinheiro("50")));
        assertFalse(banco.transferir(ana.getContaId(), ana.getContaId(), dinheiro("10")));
        assertEquals(0, dinheiro("40").compareTo(banco.consultarSaldo(ana.getContaId())));
        assertEquals(0, dinheiro("60").compareTo(banco.consultarSaldo(bruno.getContaId())));
    }
    @Test public void rf12RemoverClienteEContaAssociada() {
        assertTrue(banco.removerCliente(ana.getId()));
        assertNull(banco.consultarCliente(ana.getId()));
        assertNull(banco.consultarConta(ana.getContaId()));
        assertFalse(banco.removerCliente(ana.getId()));
    }
    @Test public void rf13RemoverContaIndependente() {
        assertFalse(banco.removerConta(ana.getContaId()));
        ContaCorrente independente = banco.cadastrarConta(dinheiro("0"));
        assertTrue(banco.removerConta(independente.getId()));
        assertNull(banco.consultarConta(independente.getId()));
    }
    @Test public void cenarioPrincipalCincoRequisitos() {
        assertTrue(banco.depositar(ana.getContaId(), dinheiro("500")));
        assertTrue(banco.sacar(ana.getContaId(), dinheiro("100")));
        assertTrue(banco.transferir(ana.getContaId(), bruno.getContaId(), dinheiro("150")));
        assertEquals(0, dinheiro("250").compareTo(banco.consultarSaldoPorCpf(ana.getCpf())));
        assertEquals(0, dinheiro("150").compareTo(banco.consultarSaldoPorCpf(bruno.getCpf())));
    }
    @Test public void limitesDeCadastroEUnicidadeCpf() {
        try { banco.cadastrarCliente("Menor", 17, "m@email.com", "123"); fail("Deveria rejeitar idade"); }
        catch (IllegalArgumentException esperado) { /* esperado */ }
        try { banco.cadastrarCliente("Outro", 30, "o@email.com", ana.getCpf()); fail("Deveria rejeitar duplicidade"); }
        catch (IllegalArgumentException esperado) { /* esperado */ }
    }
    @Test public void valoresInvalidosEContaInativa() {
        assertFalse(banco.depositar(ana.getContaId(), dinheiro("1.001")));
        assertFalse(banco.sacar(ana.getContaId(), dinheiro("0")));
        banco.consultarConta(ana.getContaId()).desativar();
        assertFalse(banco.depositar(ana.getContaId(), dinheiro("10")));
        assertFalse(banco.transferir(ana.getContaId(), bruno.getContaId(), dinheiro("10")));
    }
}
