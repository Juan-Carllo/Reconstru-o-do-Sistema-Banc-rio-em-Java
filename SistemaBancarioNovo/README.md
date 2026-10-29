# Sistema Bancário reconstruído em Java puro

Projeto acadêmico refeito com base nos 13 requisitos extraídos do sistema legado por uma adaptação da metodologia SC2Req. Todas as classes de produção neste projeto são novas e organizadas em camadas **sem interface gráfica, framework web ou API REST**. O menu é no terminal.

## Organização

```text
src/banco/modelo/Cliente.java
src/banco/modelo/ContaCorrente.java
src/banco/repositorio/RepositorioClientes.java
src/banco/repositorio/RepositorioContas.java
src/banco/servico/BancoService.java
src/banco/apresentacao/Main.java
test/banco/BancoServiceTest.java
lib/junit-4.13.2.jar
lib/hamcrest-core-1.3.jar
```

- **Modelo:** representa clientes e contas e valida os dados fundamentais.
- **Repositórios:** armazenam clientes e contas em memória.
- **Serviço:** implementa e coordena os 13 requisitos, incluindo movimentações.
- **Apresentação:** menu de texto; sem regra de negócio embutida.

## Requisitos implementados

| Código | Operação | Método principal |
| --- | --- | --- |
| RF01 | Cadastrar cliente | `cadastrarCliente` |
| RF02 | Consultar cliente por ID | `consultarCliente` |
| RF03 | Consultar cliente por CPF | `consultarClientePorCpf` |
| RF04 | Ativar cliente | `ativarCliente` |
| RF05 | Desativar cliente | `desativarCliente` |
| RF06 | Cadastrar conta | `cadastrarConta` (também automaticamente com o cliente) |
| RF07 | Consultar conta | `consultarConta` |
| RF08 | Consultar saldo por conta ou CPF | `consultarSaldo`, `consultarSaldoPorCpf` |
| RF09 | Depositar | `depositar` |
| RF10 | Sacar | `sacar` |
| RF11 | Transferir | `transferir` |
| RF12 | Remover cliente | `removerCliente` |
| RF13 | Remover conta | `removerConta` |

## Regras adotadas nesta reconstrução

- Cliente precisa ter nome, e-mail, CPF não vazio, idade de **18 a 65 anos** e CPF não repetido (não há validação de dígitos do CPF).
- Ao cadastrar cliente, cria-se conta ativa com saldo R$ 0,00.
- Depósitos, saques e transferências aceitam somente valores positivos com até duas casas decimais, e exigem contas ativas. Saques e transferências não podem superar o saldo.
- Transferência para a própria conta é recusada.
- Remover um cliente remove também sua conta associada. Uma conta vinculada a cliente não pode ser removida separadamente. Contas independentes podem ser removidas.
- IDs são sequenciais e não são reutilizados.
- Saldos usam `BigDecimal`, não `double`.
- Todas as informações são mantidas em memória e desaparecem ao encerrar.

Essas regras são decisões de reconstrução: algumas aperfeiçoam o legado e **não devem ser confundidas com equivalência exata**. O legado não verificava situação da conta nas movimentações e usava `double`.

## Executar no Windows

Com JDK 17 e terminal PowerShell aberto na raiz `SistemaBancarioNovo`:

```powershell
New-Item -ItemType Directory -Force build | Out-Null
$arquivos = Get-ChildItem -Path src,test -Filter *.java -Recurse | ForEach-Object FullName
javac -encoding UTF-8 -cp "lib/*" -d build $arquivos
java -cp "build;lib/*" org.junit.runner.JUnitCore banco.BancoServiceTest
java -cp "build;lib/*" banco.apresentacao.Main
```

Em Linux/macOS, troque `;` por `:` no classpath e use `find` para listar os arquivos Java.

## Evidência dos testes

Compilação realizada com Java 17; suíte JUnit 4.13.2: **13 testes executados, 13 aprovados**. Os testes cobrem os 13 requisitos e o cenário principal. Fluxo: cadastrar Ana e Bruno → consultar saldo inicial → depositar R$ 500 em Ana → sacar R$ 100 → transferir R$ 150 para Bruno → conferir saldos finais (Ana R$ 250 e Bruno R$ 150).

## Metodologia e uso de IA

A análise do código anterior foi orientada pela proposta SC2Req de Xu et al. (2024): extrair requisitos a partir do código com auxílio de GPT, revisar as regras, reconstruir o sistema e testar os resultados. A ferramenta **ChatGPT (OpenAI)** auxiliou na análise do legado, requisitos, arquitetura, código, testes e documentação. A revisão final e a apresentação são de responsabilidade dos alunos.

Referência: XU, R. et al. *Bridging the Gap between Source Code and Requirements Using GPT (Student Abstract)*. AAAI, 2024. https://doi.org/10.1609/aaai.v38i21.30526
