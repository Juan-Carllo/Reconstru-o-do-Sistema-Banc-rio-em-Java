package banco.modelo;

public class Cliente {
    private final int id;
    private final String nome;
    private final int idade;
    private final String email;
    private final String cpf;
    private final int contaId;
    private boolean ativo;

    public Cliente(int id, String nome, int idade, String email, String cpf, int contaId) {
        if (nome == null || nome.trim().isEmpty()) throw new IllegalArgumentException("Nome obrigatório");
        if (cpf == null || cpf.trim().isEmpty()) throw new IllegalArgumentException("CPF obrigatório");
        if (idade < 18 || idade > 65) throw new IllegalArgumentException("Idade permitida: 18 a 65 anos");
        if (email == null || email.trim().isEmpty()) throw new IllegalArgumentException("E-mail obrigatório");
        this.id = id;
        this.nome = nome.trim();
        this.idade = idade;
        this.email = email.trim();
        this.cpf = cpf.trim();
        this.contaId = contaId;
        this.ativo = true;
    }
    public int getId() { return id; }
    public String getNome() { return nome; }
    public int getIdade() { return idade; }
    public String getEmail() { return email; }
    public String getCpf() { return cpf; }
    public int getContaId() { return contaId; }
    public boolean isAtivo() { return ativo; }
    public void ativar() { ativo = true; }
    public void desativar() { ativo = false; }
}
