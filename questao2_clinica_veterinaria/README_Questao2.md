# Questao 2 - Clinica Veterinaria em Tempo Real

## Ideia da simulacao

A aplicacao representa uma clinica veterinaria com chegada de animais, recepcao, atendimento veterinario, vacinacao, banho e tosa, exames e emergencias.

## Versao sequencial

Na classe `ClinicaVeterinariaSequencial`, todos os passos acontecem um por vez:

1. Animal chega.
2. Recepcao faz o cadastro.
3. Atendimento e executado.
4. Somente depois chega o proximo animal.

Isso facilita visualizar a limitacao da programacao sequencial: mesmo quando existem varios tipos de servico, apenas uma atividade acontece por vez.

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
