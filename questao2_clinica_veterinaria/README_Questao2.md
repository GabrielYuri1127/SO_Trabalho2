# Questao 2 - Clinica Veterinaria em Tempo Real

## Ideia da simulacao

A aplicacao representa uma clinica veterinaria com chegada de animais, recepcao, atendimento veterinario, vacinacao, banho e tosa, exames e emergencias.

Cada atendimento usa dados ficticios realistas:

- nome do pet;
- especie;
- nome, telefone e bairro do tutor;
- servico solicitado;
- ocorrencia do atendimento;
- funcionario responsavel;
- valor simulado.

## Versao sequencial

Na classe `ClinicaVeterinariaSequencial`, todos os passos acontecem um por vez:

1. Animal chega.
2. Recepcao faz o cadastro.
3. Atendimento e executado.
4. Somente depois chega o proximo animal.

Isso facilita visualizar a limitacao da programacao sequencial: mesmo quando existem varios tipos de servico, apenas uma atividade acontece por vez.

Nesta versao, a clinica e representada como um unico fluxo de execucao, equivalente a um unico funcionario atendendo um pet por vez.

## Versao com threads

Na classe `ClinicaVeterinariaThreads`, a clinica usa:

- uma thread de recepcao;
- varias threads de funcionarios;
- `PriorityBlockingQueue` para organizar os atendimentos;
- `AtomicInteger` para contar atendimentos concluidos;
- prioridade para emergencias;
- `Thread.sleep` para simular a duracao dos servicos.

## Execucao

Compilar:

```sh
javac questao2_clinica_veterinaria/*.java
```

Executar versao sequencial:

```sh
java -cp questao2_clinica_veterinaria ClinicaVeterinariaSequencial 10 200
```

Executar versao com threads:

```sh
java -cp questao2_clinica_veterinaria ClinicaVeterinariaThreads 4 20 150
```

Na saida, observe o horario, o nome da thread e a prioridade dos atendimentos de emergencia.

## Interface grafica

A classe `ClinicaVeterinariaInterface` implementa uma janela em Java Swing para demonstrar o funcionamento da clinica com threads.

A interface representa a versao com threads: recepcao e varios funcionarios atuando simultaneamente. Isso deve ser comparado com a versao sequencial, que equivale a um unico funcionario.

Executar:

```sh
java -cp questao2_clinica_veterinaria ClinicaVeterinariaInterface
```

Na interface, o usuario informa a quantidade de funcionarios, a quantidade de animais e o intervalo de chegada. A tela mostra cards de indicadores, fila prioritaria em tabela, estado de cada funcionario, dados do tutor, valores simulados, historico de atendimentos, logs em tempo real, painel visual com movimento dos pets e uma barra de progresso.

Ao final da simulacao, a interface habilita:

- salvar historico em CSV;
- salvar relatorio TXT com resumo dos atendimentos;
- consultar a receita total simulada.
