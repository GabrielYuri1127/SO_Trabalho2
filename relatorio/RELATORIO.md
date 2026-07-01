# Relatorio - Trabalho Pratico 2 de Sistemas Operacionais

## Integrantes

- Sabrina Brito Maciel - 22353163
- Marcele Azevedo de Paula Oliveira - 22353160
- Gabriel Yuri Cavalcante de Castro - 22350996

## Introducao

Este trabalho pratico tem como objetivo estudar o uso de threads em programas paralelos. Foram desenvolvidas duas solucoes: a primeira compara o calculo sequencial e paralelo do produto escalar entre vetores; a segunda simula uma Clinica Veterinaria em Tempo Real com e sem threads.

## Questao 1 - Produto escalar

O produto escalar entre dois vetores consiste em multiplicar os elementos de mesma posicao e somar todos os produtos. Para dois vetores `A` e `B`, com `n` posicoes, o produto escalar e:

```text
A . B = A[0] * B[0] + A[1] * B[1] + ... + A[n-1] * B[n-1]
```

### Versao sequencial

Na versao sequencial, o programa percorre os vetores do inicio ao fim em um unico laco. Apenas uma linha de execucao realiza todo o calculo.

Arquivo: `questao1_produto_escalar/ProdutoEscalarSequencial.java`

### Versao paralela

Na versao paralela, o vetor e dividido em blocos. Cada thread calcula uma soma parcial e, ao final, a thread principal aguarda todas terminarem usando `join()` e soma os resultados parciais.

Arquivo: `questao1_produto_escalar/ProdutoEscalarParalelo.java`

O programa paralelo recebe:

- tamanho do vetor;
- quantidade de threads;
- quantidade de repeticoes.

### Medicao de tempo

As medicoes usam `System.nanoTime()`. Para reduzir variacoes, o codigo faz aquecimento da JVM e considera o menor tempo entre repeticoes.

### Speedup

A aceleracao, ou speedup, mede o ganho de desempenho da versao paralela em relacao a versao sequencial:

```text
Sp = Ts / Tp
```

Onde:

- `Ts` e o tempo da versao sequencial;
- `Tp` e o tempo da versao paralela com `p` threads.

## Tabelas-modelo de resultados

### Computador 1 - 12 CPUs

| Tamanho do vetor | Threads | Tempo sequencial (ms) | Tempo paralelo (ms) | Speedup |
| --- | ---: | ---: | ---: | ---: |
| 10000 | 1 | preencher | preencher | preencher |
| 10000 | 2 | preencher | preencher | preencher |
| 10000 | 4 | preencher | preencher | preencher |
| 10000 | 8 | preencher | preencher | preencher |
| 10000 | 16 | preencher | preencher | preencher |
| 100000 | 1 | preencher | preencher | preencher |
| 100000 | 2 | preencher | preencher | preencher |
| 100000 | 4 | preencher | preencher | preencher |
| 100000 | 8 | preencher | preencher | preencher |
| 100000 | 16 | preencher | preencher | preencher |
| 1000000 | 1 | preencher | preencher | preencher |
| 1000000 | 2 | preencher | preencher | preencher |
| 1000000 | 4 | preencher | preencher | preencher |
| 1000000 | 8 | preencher | preencher | preencher |
| 1000000 | 16 | preencher | preencher | preencher |
| 5000000 | 1 | preencher | preencher | preencher |
| 5000000 | 2 | preencher | preencher | preencher |
| 5000000 | 4 | preencher | preencher | preencher |
| 5000000 | 8 | preencher | preencher | preencher |
| 5000000 | 16 | preencher | preencher | preencher |
| 10000000 | 1 | preencher | preencher | preencher |
| 10000000 | 2 | preencher | preencher | preencher |
| 10000000 | 4 | preencher | preencher | preencher |
| 10000000 | 8 | preencher | preencher | preencher |
| 10000000 | 16 | preencher | preencher | preencher |

### Computador 2 - 8 CPUs

| Tamanho do vetor | Threads | Tempo sequencial (ms) | Tempo paralelo (ms) | Speedup |
| --- | ---: | ---: | ---: | ---: |
| 10000 | 1 | preencher | preencher | preencher |
| 10000 | 2 | preencher | preencher | preencher |
| 10000 | 4 | preencher | preencher | preencher |
| 10000 | 8 | preencher | preencher | preencher |
| 10000 | 16 | preencher | preencher | preencher |
| 100000 | 1 | preencher | preencher | preencher |
| 100000 | 2 | preencher | preencher | preencher |
| 100000 | 4 | preencher | preencher | preencher |
| 100000 | 8 | preencher | preencher | preencher |
| 100000 | 16 | preencher | preencher | preencher |
| 1000000 | 1 | preencher | preencher | preencher |
| 1000000 | 2 | preencher | preencher | preencher |
| 1000000 | 4 | preencher | preencher | preencher |
| 1000000 | 8 | preencher | preencher | preencher |
| 1000000 | 16 | preencher | preencher | preencher |
| 5000000 | 1 | preencher | preencher | preencher |
| 5000000 | 2 | preencher | preencher | preencher |
| 5000000 | 4 | preencher | preencher | preencher |
| 5000000 | 8 | preencher | preencher | preencher |
| 5000000 | 16 | preencher | preencher | preencher |
| 10000000 | 1 | preencher | preencher | preencher |
| 10000000 | 2 | preencher | preencher | preencher |
| 10000000 | 4 | preencher | preencher | preencher |
| 10000000 | 8 | preencher | preencher | preencher |
| 10000000 | 16 | preencher | preencher | preencher |

## Analise esperada

Espera-se que a versao paralela apresente maior vantagem em vetores grandes, pois o custo de criar e coordenar threads passa a ser compensado pelo trabalho dividido. Para vetores pequenos, a versao sequencial pode ser igual ou mais rapida, ja que a sobrecarga das threads pode ser maior que o ganho.

Tambem se espera que o computador com 12 CPUs aproveite melhor quantidades maiores de threads que o computador com 8 CPUs. Mesmo assim, depois de certo ponto, aumentar threads pode nao melhorar o desempenho por causa de sobrecarga, disputa por CPU, memoria cache e escalonamento do sistema operacional.

## Questao 2 - Clinica Veterinaria em Tempo Real

A aplicacao escolhida foi uma Clinica Veterinaria em Tempo Real. A simulacao envolve chegada de animais, recepcao, atendimento veterinario, vacinacao, banho e tosa, exames e emergencias. Para aproximar a simulacao de uma situacao real, cada atendimento possui nome do pet, especie, dados ficticios do tutor, funcionario responsavel, descricao da ocorrencia e valor simulado.

### Versao sequencial

Na versao sequencial, cada animal passa por todas as etapas antes que o proximo seja atendido. Isso mostra uma limitacao importante: mesmo que existam varios funcionarios ou setores em uma clinica real, o programa sem threads representa tudo acontecendo um por vez. Portanto, para fins de comparacao, essa versao equivale a um unico funcionario em um unico fluxo de atendimento.

Arquivo: `questao2_clinica_veterinaria/ClinicaVeterinariaSequencial.java`

### Versao com threads

Na versao com threads, ha uma thread de recepcao e varias threads de funcionarios. A recepcao cadastra os animais e coloca cada atendimento em uma `PriorityBlockingQueue`. Os funcionarios retiram animais da fila e fazem os atendimentos em paralelo.

Arquivo: `questao2_clinica_veterinaria/ClinicaVeterinariaThreads.java`

Recursos usados:

- `Thread` para recepcao e funcionarios;
- `PriorityBlockingQueue` para a fila de atendimentos;
- `AtomicInteger` para contar atendimentos concluidos;
- prioridade para emergencias;
- `Thread.sleep` para simular duracao dos servicos;
- muitos prints com horario e nome da thread.

### Interface grafica

Tambem foi criada uma interface grafica em Swing para facilitar a demonstracao da versao com threads. A tela permite informar a quantidade de funcionarios, a quantidade de animais e o intervalo de chegada. Durante a simulacao, a interface apresenta cards de indicadores, fila prioritaria em tabela, estado de cada funcionario, dados do tutor, valores cobrados, historico de atendimentos, receita simulada, logs em tempo real, painel visual com movimento dos pets e uma barra de progresso com a quantidade de atendimentos concluidos. Ao final, e possivel salvar o historico em CSV e gerar um relatorio TXT.

Arquivo: `questao2_clinica_veterinaria/ClinicaVeterinariaInterface.java`

## Decisoes de implementacao

A emergencia recebe prioridade na fila para simular uma situacao real de clinica. Os demais atendimentos seguem a ordem de chegada. A duracao de cada servico varia conforme o tipo de atendimento para deixar a execucao mais visual. Os nomes de pets, tutores, telefones, bairros, ocorrencias e valores sao ficticios e gerados de forma deterministica para facilitar a repeticao dos testes.

Na questao 1, os vetores sao preenchidos de forma deterministica, permitindo comparar resultados entre execucoes. A classe `ExecutarExperimentos` automatiza os tamanhos de vetor e quantidades de threads pedidos.

## Conclusao

O trabalho demonstrou que threads podem melhorar o desempenho quando existe trabalho suficiente para ser dividido, como no produto escalar de vetores grandes. Tambem mostrou que threads permitem simular sistemas mais proximos da realidade, como uma clinica com recepcao, funcionarios e atendimento prioritario a emergencias.

Apesar dos ganhos, o uso de threads tambem traz custos de criacao, sincronizacao e escalonamento. Por isso, a melhor quantidade de threads depende do tamanho do problema e da capacidade do computador usado.
