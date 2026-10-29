package banco.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ContaCorrente {
    private final int id;
    private BigDecimal saldo;
    private boolean ativa;

    public ContaCorrente(int id, BigDecimal saldo) {
        if (saldo == null || saldo.signum() < 0) throw new IllegalArgumentException("Saldo inicial inválido");
        this.id = id;
        this.saldo = saldo.setScale(2, RoundingMode.UNNECESSARY);
        this.ativa = true;
    }
    public int getId() { return id; }
    public BigDecimal getSaldo() { return saldo; }
    public boolean isAtiva() { return ativa; }
    public void ativar() { ativa = true; }
    public void desativar() { ativa = false; }
    public void creditar(BigDecimal valor) { saldo = saldo.add(valor); }
    public void debitar(BigDecimal valor) { saldo = saldo.subtract(valor); }
}
