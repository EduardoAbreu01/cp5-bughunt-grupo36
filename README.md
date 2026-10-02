# Checkpoint 5 — Bug Hunt PetFiap

> Copie este arquivo para a raiz do seu repositório com o nome **README.md**
> e preencha todas as seções.

## Identificação

**Grupo:** 36

| Integrante | RM | Turma |
|---|---|---|
| Eduardo Abreu | 566460 | 2CCPO |
| Gabriel dos Anjos | 565532 | 2CCPO |
| João Pedro Ferreira | 563869 | 2CCPO |

| Campo | |
|---|---|
| **Total de bugs corrigidos** | 16 / 12 (ver Parte 5) |
| **Total de ajustes de Clean Code** | 6 / 6 |
| **Total de testes novos escritos** | 6 / 6 |
| **Suíte final (Run As → JUnit Test)** | 26 testes (20 entregues + 6 novos), 0 falhas |

---

## Parte 1 — Bugs encontrados

> Uma linha por bug, na ordem em que você os encontrou. Use a numeração dos seus
> commits (`fix: bug01 ...`). Preencha TODAS as colunas — metade da nota está aqui.

| # | Sintoma observado (o que fiz/vi) | Causa raiz (arquivo e linha aproximada) | Correção aplicada | Conceito da disciplina |
|---|---|---|---|---|
| bug01 |Erro IdentifierGenerationException ao tentar salvar atendimento na API (ou erro de id nulo ao inserir no Oracle).    |Atendimento.java (linha ~14): O atributo id continha apenas a anotação @Id, sem declarar a estratégia de geração automática de chave primária (@GeneratedValue).    |Adicionada a anotação @GeneratedValue(strategy = GenerationType.IDENTITY) no atributo id de Atendimento.java, assim o Oracle gera o id como coluna IDENTITY |Mapeamento Objeto-Relacional (ORM) com JPA, Estratégias de Geração de Chaves Primárias (@GeneratedValue) e persistência relacional. |
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
| bug13 |O teste novo teste01 (BanhoTest) ficou vermelho: `expected: <60.0> but was: <100.0>`. Banho de pet pequeno estava saindo pelo preço do grande. |Banho.java, `calcularPreco()` (linha ~27): os valores de PEQUENO e GRANDE estavam trocados (100 para pequeno e 60 no retorno padrão, que é o grande). |Troquei os retornos: PEQUENO = 60, MEDIO = 80, GRANDE = 100, conforme a tabela do contrato. |Polimorfismo (regra de preço sobrescrita em cada subclasse) e testes unitários revelando bug escondido. |
| bug14 |O teste novo teste02 (TosaTest) ficou vermelho: `expected: <60> but was: <30>`. O /resumo de uma tosa mostrava 30 minutos. |Tosa.java (linha ~40): o método era `getDuracaoMinutos(String porte)`, com parâmetro. Isso é uma sobrecarga, então quem chama `getDuracaoMinutos()` pelo tipo Atendimento cai no método da classe pai (30). |Tirei o parâmetro e coloquei `@Override`, agora é sobrescrita de verdade e retorna 60. |Sobrescrita vs sobrecarga, anotação @Override, polimorfismo. |
| bug15 |O teste novo teste03 (BanhoTest) ficou vermelho: chamar `cancelar()` num atendimento CONCLUIDO não lançava nada e o status virava CANCELADO. |Atendimento.java, `cancelar()` (linha ~67): mudava o status direto sem olhar o estado atual. O bug12 só tinha tapado isso no service, o model continuava aceitando. |Coloquei a mesma checagem do `concluir()`: só cancela se estiver AGENDADO, senão lança StatusInvalidoException. |Encapsulamento (regra de transição de status mora no model) e exceções customizadas unchecked. |
| bug16 |O teste novo teste04 (AgendaServiceTest) ficou vermelho com HorarioOcupadoException: um banho CANCELADO no mesmo horário impedia agendar de novo. |AgendaService.java, `agendar()` (linha ~31): na correção do bug09 a checagem do status AGENDADO acabou saindo junto com o `==`, então qualquer atendimento no horário bloqueava. |Voltei a condição de status: só conflita se o atendimento existente estiver AGENDADO e a data/hora for igual (`isEqual`). |Regras de negócio no service, teste de regressão. |

## Parte 2 — Ajustes de Clean Code

| # | Onde estava | Qual princípio/boas práticas era violado | O que eu mudei |
|---|---|---|---|
| clean01 | AtendimentoFactory.java, método `criar(int p, String t, String n, String po, String tu, LocalDateTime d)` | Nomes significativos: parâmetro de uma letra não diz nada, `po` e `tu` dava pra confundir fácil | Renomeei para `protocolo, tipo, petNome, petPorte, tutorNome, dataHora` |
| clean02 | AtendimentoController.java, método privado `calcularDescontoFidelidade` | Código morto / YAGNI: método que ninguém chama, com comentário de "implementar no futuro" | Removi o método e o comentário. Se o time aprovar a regra, ela entra com teste |
| clean03 | AtendimentoBuilder.java, comentário em cima do `construir()` | Comentário mentiroso: dizia que a validação ficava no controller, mas depois dos bugs 04/05 ela está no próprio builder | Troquei por um comentário curto que diz o que o método faz de verdade |
| clean04 | AgendaService.java, `concluir()` e `cancelar()` | DRY / encapsulamento: o service repetia a regra de status com `setStatus` em vez de usar os métodos do model, então a regra existia em dois lugares | O service agora só chama `atendimento.concluir()` / `atendimento.cancelar()`; a regra fica só no model |
| clean05 | Atendimento.java e AgendaService.java, strings `"AGENDADO"`, `"CONCLUIDO"`, `"CANCELADO"` espalhadas | Números/strings mágicas: um erro de digitação compila normal e quebra a regra | Criei as constantes `AGENDADO`, `CONCLUIDO` e `CANCELADO` em Atendimento e usei elas no lugar das strings |
| clean06 | AgendaService.java e AtendimentoController.java, `@Autowired` direto no atributo | Injeção por campo esconde a dependência e deixa o atributo mutável | Troquei por injeção via construtor com atributo `final` (o Spring injeta pelo construtor e o `@InjectMocks` do teste também funciona) |

## Parte 3 — Testes novos (regras que estavam sem cobertura)

> Uma linha por teste novo (`test: ...`). "Regra coberta" é o comportamento do
> contrato (seção 3 do enunciado) que o teste protege. Em "Resultado", diga se o
> teste ficou vermelho ao ser escrito (revelou bug — qual?) ou verde de cara
> (regra já estava correta).

| # | Teste escrito (classe.método) | Regra coberta | Resultado ao escrever (vermelho/verde) |
|---|---|---|---|
| teste01 | BanhoTest.deveCobrarPrecoDaTabelaQuandoPorteForPequenoMedioOuGrande | Preço do banho por porte: 60 / 80 / 100 | Vermelho, revelou o bug13 (preços invertidos) |
| teste02 | TosaTest.deveDurar60MinutosQuandoForTosa | Tosa dura 60 minutos | Vermelho, revelou o bug14 (sobrecarga no lugar de sobrescrita) |
| teste03 | BanhoTest.deveRecusarCancelamentoQuandoAtendimentoJaFoiConcluido | `cancelar()` em CONCLUIDO é recusado com StatusInvalidoException | Vermelho, revelou o bug15 (model cancelava qualquer status) |
| teste04 | AgendaServiceTest.deveAgendarQuandoAtendimentoNoMesmoHorarioFoiCancelado | Só conflita com atendimento AGENDADO no mesmo horário | Vermelho, revelou o bug16 (cancelado bloqueava o horário) |
| teste05 | ConsultaVeterinariaTest.deveCustar150ReaisQuandoQualquerPorte | Consulta custa R$ 150 fixo, o porte não muda o preço | Verde de cara, regra já estava correta |
| teste06 | AgendaServiceTest.deveRecusarAgendamentoQuandoDataHoraEstaNoPassado | Data/hora no passado é recusada com IllegalArgumentException e o banco nem é consultado (`verify(..., never())`) | Verde de cara (a validação já tinha entrado no bug10) |

---

## Parte 4 — Perguntas de reflexão

> Responda com suas palavras, 5 a 10 linhas cada, **usando o código real do
> projeto como exemplo**. Respostas genéricas de tutorial não pontuam.

### 1. A suíte como contrato (Aula 15)
O projeto chegou com 20 testes, 9 vermelhos. Descreva como você usou as
mensagens de falha (ex.: `expected: <Rex> but was: <null>`) para caçar os bugs.
O que a suíte de testes tem de melhor do que testar tudo na mão com curl?

A gente começou rodando a suíte inteira e indo um vermelho por vez. A mensagem já
falava muito: no `deveMontarAtendimentoCompleto` veio `expected: <Rex> but was: <null>`,
então o nome entrava no builder e sumia no meio do caminho. Abrindo o `comPet()` deu pra
ver o `petNome = petNome` sem o `this`. No `deveCriarTosaQuandoTipoForTosa` a mensagem
dizia que veio um Banho, aí foi direto no `case "TOSA"` da factory. Também vimos bug em
cascata: o teste do protocolo só fez sentido depois de arrumar o singleton.
A vantagem sobre o curl é que a suíte roda em segundos, sem Oracle e sem subir a API,
e roda tudo de novo a cada correção. Foi assim que a gente percebeu, com o teste04, que
a nossa correção do bug09 tinha quebrado a regra do atendimento cancelado. No curl isso
passaria batido.

### 2. Mock e injeção de dependência (Aulas 13 a 15)
No `AgendaServiceTest`, o `@Mock` cria um `AtendimentoRepository` falso e o
`@InjectMocks` o injeta no service. Explique a relação disso com o `@Autowired`
que o Spring faz em produção — quem "injeta" em cada mundo, e por que o teste
consegue rodar sem banco e sem subir o Spring?

O `AgendaService` não cria o próprio repository, ele recebe pronto. Em produção quem
entrega é o container do Spring: ele cria a implementação do `AtendimentoRepository`
(a do Spring Data, que fala com o Oracle) e passa para o service. Depois do clean06 isso
acontece pelo construtor `AgendaService(AtendimentoRepository repository)`.
No teste quem faz esse papel é o Mockito: o `@Mock` cria um repository falso, que só
responde o que a gente ensina com `when(repository.findByPetNome("Rex")).thenReturn(...)`,
e o `@InjectMocks` chama o mesmo construtor passando o falso. Como o service só conhece
a interface, ele nem sabe que não tem banco. Por isso o teste roda sem Spring e sem rede,
e ainda dá pra conferir com `verify(repository, never()).save(any())` que nada foi salvo.

### 3. `==` vs `.equals()` (Aula 7)
Um dos bugs fazia o agendamento duplicado passar pela verificação de conflito.
Explique por que `==` entre Strings e `LocalDateTime` falhou aqui, por que ele
"funciona por sorte" com literais como `"Rex"`, e o que a sua correção mudou.

O código original fazia `a.getPetNome() == novo.getPetNome() && a.getDataHora() == novo.getDataHora()`.
O `==` em objeto compara referência, ou seja, se é o mesmo objeto na memória, e não se o
conteúdo é igual. No `deveRecusarAgendamentoComHorarioJaOcupado` o teste cria o mesmo
horário com `LocalDateTime.parse(...)`, que gera outro objeto, então o `==` dava false e
o agendamento duplicado passava.
Com `"Rex"` funciona por sorte porque literal de String vai para o String pool, e os dois
`"Rex"` do código apontam para o mesmo objeto. Mas quando o nome vem de uma requisição
HTTP é uma String nova, e aí o `==` falha igual. Na correção trocamos para
`existente.getDataHora().isEqual(novo.getDataHora())`, que compara o valor da data. O nome
nem precisa comparar, porque o `findByPetNome` já filtra pelo pet.

### 4. Sobrescrita vs sobrecarga (Aula 7)
Um dos bugs compilava sem nenhum erro: um método parecia sobrescrever
`getDuracaoMinutos`, mas na verdade criava uma assinatura nova. Explique a
diferença entre override e overload nesse caso e por que a anotação `@Override`
teria impedido o bug.

Na Tosa estava `public int getDuracaoMinutos(String porte)`. Sobrescrita (override) é quando
a subclasse escreve um método com a mesma assinatura do pai, e aí o polimorfismo escolhe a
versão da subclasse. Como esse tinha um parâmetro a mais, virou sobrecarga (overload): um
método novo que convive com o `getDuracaoMinutos()` herdado de Atendimento.
O controller chama `atendimento.getDuracaoMinutos()` pela referência abstrata, sem argumento,
então executava o do pai e devolvia 30 em vez de 60. Compilava normal porque sobrecarga é
válida. Se tivesse `@Override` em cima, o compilador ia dar erro dizendo que o método não
sobrescreve nada, e o bug nem chegaria a rodar. No Banho tinha `@Override` e funcionava,
foi comparando os dois que achamos (bug14).

### 5. Singleton manual vs bean do Spring (Aula 14)
O `GeradorProtocolo` é um Singleton escrito à mão e causou um dos bugs.
Explique o que ele garante, qual foi o bug, e por que o `AgendaService`
(`@Service`) não corre o mesmo risco no container do Spring.

O Singleton garante que existe uma única instância do `GeradorProtocolo`, então o `contador`
é compartilhado e os protocolos saem 1, 2, 3... O construtor é privado e o acesso é só pelo
`getInstancia()`. O bug era que o método fazia `return new GeradorProtocolo()` sem guardar
em `instancia`, então ela continuava null e toda chamada criava um gerador novo com contador
zerado. Todo atendimento ganhava protocolo 1 (dava pra ver o "GeradorProtocolo criado!"
aparecendo várias vezes no console). A correção foi `instancia = new GeradorProtocolo()`.
O `AgendaService` não tem esse risco porque quem cria ele é o container do Spring: bean
anotado com `@Service` é singleton por padrão, o Spring cria uma vez na subida e injeta a
mesma instância em todo mundo. A gente não escreve esse controle na mão, então não tem
como esquecer de guardar a instância.

### 6. Cobertura de testes: onde parar? (Aula 15)
Dos 6 testes novos que você escreveu, alguns ficaram vermelhos (revelaram
bugs) e outros verdes de cara (regras já corretas). Vale a pena manter os que
ficaram verdes? Em um projeto real com prazo, o que você priorizaria testar:
caminho feliz, caminhos de erro, ou 100% de cobertura? Justifique.

Vale manter. O teste05 (consulta a R$ 150 em qualquer porte) e o teste06 (data no passado
sem consultar o banco) passaram de primeira, mas agora protegem a regra. Se alguém mexer no
`agendar()` e colocar a validação depois do `findByPetNome`, o `verify(..., never())` pega na
hora. E o nosso próprio teste04 mostrou isso: uma correção nossa tinha quebrado uma regra
sem ninguém perceber.
Com prazo, a gente priorizaria as regras de negócio e os caminhos de erro, mais do que 100%
de cobertura. Quase todos os bugs daqui estavam em caminho de erro ou em regra de valor
(conflito de horário, status, preço por porte), não no caminho feliz. Testar getter e setter
só para subir a porcentagem gasta tempo e não pega nada. Primeiro as regras da tabela do
contrato e as exceções, depois o caminho feliz principal de cada serviço.

---

## Parte 5 — Espaço livre (opcional)

Alguma dificuldade, dúvida ou comentário sobre o checkpoint?

```
A tabela da Parte 1 ficou com 16 linhas porque registramos tudo na ordem em que foi
achado. Algumas correções se sobrepõem: o bug11 e o bug12 colocaram a validação de
status no service, e depois o bug15 + clean04 levaram essa regra de volta para o model
(Atendimento.concluir/cancelar), que é onde o enunciado diz que ela mora. O bug16 foi
uma regressão da nossa própria correção do bug09, pega pelo teste04.
```
