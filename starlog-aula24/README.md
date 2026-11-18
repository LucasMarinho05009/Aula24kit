# StarLog Logistics • Laboratório MVC com Java Swing (Aula 24)

Integração da interface gráfica (View, Java Swing) com o domínio (Model) e a orquestração (Controller), sem misturar regra de negócio dentro de botões.

## Estrutura

```
src/br/com/starlog/
├── model/
│   └── Carga.java                 # Model: Fail-Fast e cálculo do seguro (sem Swing)
├── view/
│   └── TelaCadastroCarga.java     # View: casca visual, apenas delega ao controller
├── controller/
│   └── CargaController.java       # Controller: parse, tratamento de erros e fluxo
├── main/
│   └── MainApp.java               # Bootstrap na Event Dispatch Thread
└── test/
    └── CargaMvcTest.java          # Suíte JUnit 5 (6 testes)
```

## O que foi implementado

**Missão 2 • `Carga` (Model)**
- Barreira Fail-Fast no construtor, antes de qualquer atribuição: código e categoria não podem ser nulos ou em branco; peso e valor devem ser maiores que zero. As verificações numéricas também rejeitam `NaN` e infinito (que o `Double.parseDouble` aceitaria se digitados).
- `calcularSeguro()`: 2% do valor declarado, mais sobretaxa fixa de R$ 150,00 se a categoria for `PERIGOSA` ou o peso for superior a 100 kg.

**Missão 3 • `CargaController`**
- Verifica peso e valor vazios/nulos, troca vírgula por ponto e converte com `Double.parseDouble`.
- Instancia a `Carga`, guarda no repositório e notifica a View (`exibirMensagemSucesso` + `atualizarRelatorio`).
- `NumberFormatException` é capturada **antes** de `IllegalArgumentException`, pois é subclasse dela; caso contrário o erro de formato cairia no bloco errado.
- Erros de regra de negócio chegam à tela por `tela.exibirMensagemErro(...)` com mensagem limpa.

## Missão 4 • Teste de resiliência (resultado)

| Entrada | Resultado |
|---------|-----------|
| Peso `abc` | "Erro de formato numérico: Digite apenas números válidos para Peso e Valor!" |
| Valor `-50` | "Regra de Negócio Violada: Valor declarado deve ser maior que zero." |
| Peso vazio / valor nulo / código em branco | Mensagem de regra de negócio, nada é cadastrado |
| 3 cargas válidas (uma com peso `20,5`) | Relatório atualizado: 3 cargas, 190,50 kg, seguro acumulado R$ 360,00 |

## Como executar

Pela IDE: rode `MainApp.java` para abrir a interface e `CargaMvcTest.java` para os testes.

Pelo terminal (ajuste o caminho do jar do JUnit):

```bash
javac -encoding UTF-8 -cp junit-platform-console-standalone-1.10.2.jar -d out src/br/com/starlog/*/*.java
java -cp out br.com.starlog.main.MainApp
java -jar junit-platform-console-standalone-1.10.2.jar execute -cp out --select-class br.com.starlog.test.CargaMvcTest
```

Resultado esperado dos testes: `6 tests successful`, `0 tests failed`.
