# Checkpoint 5 — Bug Hunt PetFiap

> Copie este arquivo para a raiz do seu repositório com o nome **README.md**
> e preencha todas as seções.

## Identificação

**Grupo:** 36

| Integrante | RM | Turma |
|---|---|---|
| | | |
| | | |
| | | |
| | | |

| Campo | |
|---|---|
| **Total de bugs corrigidos** | 12 / 12 |
| **Total de ajustes de Clean Code** | ___ / 6 |
| **Total de testes novos escritos** | ___ / 6 |
| **Suíte final (Run As → JUnit Test)** | ___ testes, ___ falhas |

---

## Parte 1 — Bugs encontrados

> Uma linha por bug, na ordem em que você os encontrou. Use a numeração dos seus
> commits (`fix: bug01 ...`). Preencha TODAS as colunas — metade da nota está aqui.

| # | Sintoma observado (o que fiz/vi) | Causa raiz (arquivo e linha aproximada) | Correção aplicada | Conceito da disciplina |
|---|---|---|---|---|
| bug01 |Erro IdentifierGenerationException ao tentar salvar atendimento na API (ou PSQLException de coluna id nula no banco).    |Atendimento.java (linha ~14): O atributo id continha apenas a anotação @Id, sem declarar a estratégia de geração automática de chave primária (@GeneratedValue).    |Adicionada a anotação @GeneratedValue(strategy = GenerationType.IDENTITY) no atributo id de Atendimento.java e atualizada a coluna id no PostgreSQL para suportar IDENTITY |Mapeamento Objeto-Relacional (ORM) com JPA, Estratégias de Geração de Chaves Primárias (@GeneratedValue) e persistência relacional. |
| bug02 |O protocolo ficava preso no valor 1 a cada novo agendamento e o teste deveGerarProtocolosSequenciais falhava. |GeradorProtocolo.java (linha ~19): getInstancia() instanciava um novo objeto sem guardar na variável instancia, fazendo o atributo contador reiniciar em cada chamada. |Atribuir instancia = new GeradorProtocolo() no método estático para manter o estado do contador em memória |Padrão Singleton, Estado de Objetos e Encapsulamento |
| bug03 |O nome do pet era salvo como null no banco e os testes de montagem do builder deveMontarAtendimentoCompleto falhava. |AtendimentoBuilder.java: No método comPet(), a variável this.petNome não recebia o parâmetro petNome por falta do qualificador this (petNome = petNome). |Adicionado o this.petNome = petNome para atribuir corretamente o valor ao atributo da classe. |Encapsulamento, Escopo de Variáveis e Padrão Builder . |
| bug04 |O teste deveRecusarMontagemSemNomeDoPet falhava porque nenhuma exceção era lançada ao montar sem o nome do pet. |AtendimentoBuilder.java: O método construir() delegava as validações ao controller e não verificava o parâmetro petNome. |Adicionada a validação if (petNome == null || petNome.trim().isEmpty()) lançando IllegalArgumentException. |Padrão Builder e Validação de Argumentos . |
| bug05 |O teste deveRecusarMontagemSemPorte falhava porque nenhuma exceção era lançada ao montar sem o porte. |AtendimentoBuilder.java: Ausência de verificação da variável petPorte antes do envio à fábrica. |Adicionada a validação if (petPorte == null || petPorte.trim().isEmpty()) lançando IllegalArgumentException. |Encapsulamento de Regras de Construção de Objetos. |
| bug06 |O teste deveCriarTosaQuandoTipoForTosa retornava uma instância de Banho. |AtendimentoFactory.java: No bloco de decisão do tipo "TOSA", o código instanciava new Banho(...) em vez de new Tosa(...). |Ajustada a AtendimentoFactory para retornar new Tosa(...) quando o tipo for "TOSA". |Padrão Factory Method (GoF) e Polimorfismo . |
| bug07 |O teste devePreencherOsDadosDoPetNaConsulta falhava retornando null nos getters de nome, porte e tutor |ConsultaVeterinaria.java (linha ~18): O construtor chamava super() sem parâmetros em vez de delegar os argumentos para a classe pai. |Alterada a chamada do construtor para super(protocolo, petNome, petPorte, tutorNome, dataHora). |Herança em POO, Construtores (super) e Polimorfismo . |
| bug08 |O teste deveLancarExcecaoQuandoAtendimentoNaoExiste falhava indicando que nenhuma exceção era lançada ao buscar ID inexistente. |AgendaService.java (linha ~35): Bloco try-catch genérico no método buscarPorId capturava a exceção e retornava null |Removido o bloco try-catch para que a exceção AtendimentoNaoEncontradoException seja lançada diretamente pelo orElseThrow(). |Tratamento de Exceções Customizadas e Uso de Optional no Spring Data JPA . |
| bug09 |O teste deveRecusarAgendamentoComHorarioJaOcupado falhava permitindo agendamentos duplicados no mesmo horário. |AgendaService.java: Comparação inadequada de instâncias de LocalDateTime (utilizando referência == ou ausência de validação de horário no método agendar). |Adicionada a validação de conflito de horários utilizando isEqual() em AgendaService.java e lançamento da HorarioOcupadoException. |Igualdade de Objetos vs Referência em Java, Exceções de Negócio e Tratamento no Spring Boot (Aula 11/13). |
| bug10 |A aplicação aceitava agendamentos com data e hora passadas e o teste unitário correspondente falhava. |AgendaService.java: O método agendar() não validava se dataHora era anterior a LocalDateTime.now() antes de acionar o repositório |Adicionada a validação if (dataHora.isBefore(LocalDateTime.now())) lançando IllegalArgumentException. |Validação de Regras de Negócio, Tratamento de Exceções e Manipulação de Datas (java.time). |
| bug11 |Ausência de validação do status atual do atendimento ao tentar concluir (permitindo alterar atendimentos já cancelados ou concluídos). |AgendaService.java: O método concluir() alterava o status diretamente para CONCLUIDO sem verificar o estado anterior do objeto. |Adicionada a validação no método concluir() lançando StatusInvalidoException caso o status do atendimento seja diferente de AGENDADO. |Máquina de Estados, Validação de Transição de Status e Exceções de Negócio no Spring Boot. |
| bug12 |A aplicação permitia o cancelamento de atendimentos já concluídos (CONCLUIDO). |AgendaService.java: O método cancelar() alterava o status para "CANCELADO" sem verificar se o atendimento já havia sido realizado. |Adicionada a validação no método cancelar() lançando StatusInvalidoException caso o status atual seja CONCLUIDO ou CANCELADO. |Máquina de Estados, Integridade de Regras de Negócio e Exceções Customizadas. |

## Parte 2 — Ajustes de Clean Code

| # | Onde estava | Qual princípio/boas práticas era violado | O que eu mudei |
|---|---|---|---|
| clean01 | | | |
| clean02 | | | |
| clean03 | | | |
| clean04 | | | |
| clean05 | | | |
| clean06 | | | |

## Parte 3 — Testes novos (regras que estavam sem cobertura)

> Uma linha por teste novo (`test: ...`). "Regra coberta" é o comportamento do
> contrato (seção 3 do enunciado) que o teste protege. Em "Resultado", diga se o
> teste ficou vermelho ao ser escrito (revelou bug — qual?) ou verde de cara
> (regra já estava correta).

| # | Teste escrito (classe.método) | Regra coberta | Resultado ao escrever (vermelho/verde) |
|---|---|---|---|
| teste01 | | | |
| teste02 | | | |
| teste03 | | | |
| teste04 | | | |
| teste05 | | | |
| teste06 | | | |

---

## Parte 4 — Perguntas de reflexão

> Responda com suas palavras, 5 a 10 linhas cada, **usando o código real do
> projeto como exemplo**. Respostas genéricas de tutorial não pontuam.

### 1. A suíte como contrato (Aula 15)
O projeto chegou com 20 testes, 9 vermelhos. Descreva como você usou as
mensagens de falha (ex.: `expected: <Rex> but was: <null>`) para caçar os bugs.
O que a suíte de testes tem de melhor do que testar tudo na mão com curl?

### 2. Mock e injeção de dependência (Aulas 13 a 15)
No `AgendaServiceTest`, o `@Mock` cria um `AtendimentoRepository` falso e o
`@InjectMocks` o injeta no service. Explique a relação disso com o `@Autowired`
que o Spring faz em produção — quem "injeta" em cada mundo, e por que o teste
consegue rodar sem banco e sem subir o Spring?

### 3. `==` vs `.equals()` (Aula 7)
Um dos bugs fazia o agendamento duplicado passar pela verificação de conflito.
Explique por que `==` entre Strings e `LocalDateTime` falhou aqui, por que ele
"funciona por sorte" com literais como `"Rex"`, e o que a sua correção mudou.

### 4. Sobrescrita vs sobrecarga (Aula 7)
Um dos bugs compilava sem nenhum erro: um método parecia sobrescrever
`getDuracaoMinutos`, mas na verdade criava uma assinatura nova. Explique a
diferença entre override e overload nesse caso e por que a anotação `@Override`
teria impedido o bug.

### 5. Singleton manual vs bean do Spring (Aula 14)
O `GeradorProtocolo` é um Singleton escrito à mão e causou um dos bugs.
Explique o que ele garante, qual foi o bug, e por que o `AgendaService`
(`@Service`) não corre o mesmo risco no container do Spring.

### 6. Cobertura de testes: onde parar? (Aula 15)
Dos 6 testes novos que você escreveu, alguns ficaram vermelhos (revelaram
bugs) e outros verdes de cara (regras já corretas). Vale a pena manter os que
ficaram verdes? Em um projeto real com prazo, o que você priorizaria testar:
caminho feliz, caminhos de erro, ou 100% de cobertura? Justifique.

---

## Parte 5 — Espaço livre (opcional)

Alguma dificuldade, dúvida ou comentário sobre o checkpoint?

```

```
