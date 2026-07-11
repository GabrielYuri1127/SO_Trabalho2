# Relatorio - Trabalho Pratico 2 de Sistemas Operacionais

## Integrantes

- Sabrina Brito Maciel - 22353163
- Marcele Azevedo de Paula Oliveira - 22353160
- Gabriel Yuri Cavalcante de Castro - 22350996

## 1. Introducao

Este trabalho pratico tem como objetivo estudar o uso de threads em programas escritos em Java. Para isso, foram desenvolvidas duas atividades. A primeira compara o calculo sequencial e paralelo do produto escalar entre dois vetores. A segunda simula uma Clinica Veterinaria em Tempo Real, primeiro em uma versao sequencial e depois em uma versao com threads.

A ideia central e observar que threads podem ser usadas tanto para melhorar desempenho em tarefas numericas, como o produto escalar, quanto para representar melhor sistemas reais com varias atividades ocorrendo ao mesmo tempo, como uma clinica com recepcao, funcionarios e atendimentos prioritarios.

## 2. Questao 1 - Produto escalar

### 2.1 Explicacao do produto escalar

O produto escalar entre dois vetores consiste em multiplicar os elementos de mesma posicao e somar todos os produtos. Considerando dois vetores `A` e `B`, ambos com `n` posicoes, o calculo e:

```text
A . B = A[0] * B[0] + A[1] * B[1] + ... + A[n-1] * B[n-1]
```

No programa implementado, os vetores sao preenchidos de forma deterministica:

- vetor `A`: `a[i] = i % 100`;
- vetor `B`: `b[i] = i % 50`.

Esse preenchimento facilita a repeticao dos testes, pois os mesmos tamanhos de vetor sempre produzem os mesmos resultados numericos.

### 2.2 Versao sequencial

A versao sequencial esta no arquivo:

```text
questao1_produto_escalar/ProdutoEscalarSequencial.java
```

Nessa versao, apenas uma linha de execucao percorre os vetores do inicio ao fim. Para cada posicao, o programa multiplica os valores correspondentes e acumula o resultado em uma variavel `soma`.

Essa abordagem e simples e tem baixa sobrecarga, mas nao aproveita os varios nucleos disponiveis em processadores modernos.

### 2.3 Versao paralela

A versao paralela esta no arquivo:

```text
questao1_produto_escalar/ProdutoEscalarParalelo.java
```

Nessa versao, o vetor e dividido em blocos. Cada bloco e processado por uma thread da classe `Trabalhador`, que calcula uma soma parcial. Depois que todas as threads terminam, a thread principal usa `join()` para aguardar o fim da execucao e soma os resultados parciais.

O programa paralelo recebe como entrada:

- tamanho do vetor;
- quantidade de threads;
- quantidade de repeticoes.

### 2.4 Execucao dos experimentos

A classe abaixo automatiza os experimentos:

```text
questao1_produto_escalar/ExecutarExperimentos.java
```

Foram usados os tamanhos de vetor pedidos no enunciado:

```text
10000, 100000, 1000000, 5000000, 10000000
```

Tambem foram testadas as seguintes quantidades de threads:

```text
1, 2, 4, 8, 16
```

As medicoes de tempo foram feitas com:

```text
System.nanoTime()
```

Para reduzir variacoes causadas pela inicializacao da JVM, o codigo faz aquecimento antes da medicao. Alem disso, cada configuracao e executada mais de uma vez e o menor tempo medido e usado no CSV final.

### 2.5 Formula do speedup

O speedup mede o ganho de desempenho da versao paralela em relacao a versao sequencial:

```text
Sp = Ts / Tp
```

Onde:

- `Ts` e o tempo da versao sequencial;
- `Tp` e o tempo da versao paralela com `p` threads.

Quando o speedup e maior que 1, a versao paralela foi mais rapida. Quando o speedup e menor que 1, a sobrecarga das threads foi maior que o ganho obtido.

## 3. Resultados da Questao 1

### 3.1 Computador 1 - 8 CPUs

O primeiro teste completo ja foi executado em uma maquina identificada como `Computador_8_CPU`. O arquivo gerado foi:

```text
questao1_produto_escalar/resultados_8_cpu.csv
```

| Tamanho do vetor | Threads | Tempo sequencial (ms) | Tempo paralelo (ms) | Speedup |
| ---: | ---: | ---: | ---: | ---: |
| 10000 | 1 | 0.134599 | 0.452201 | 0.297653 |
| 10000 | 2 | 0.134599 | 0.354899 | 0.379260 |
| 10000 | 4 | 0.134599 | 0.388900 | 0.346102 |
| 10000 | 8 | 0.134599 | 0.634001 | 0.212301 |
| 10000 | 16 | 0.134599 | 1.212201 | 0.111037 |
| 100000 | 1 | 0.134600 | 0.260601 | 0.516498 |
| 100000 | 2 | 0.134600 | 0.272299 | 0.494310 |
| 100000 | 4 | 0.134600 | 0.377200 | 0.356840 |
| 100000 | 8 | 0.134600 | 0.708799 | 0.189899 |
| 100000 | 16 | 0.134600 | 1.193300 | 0.112796 |
| 1000000 | 1 | 1.252600 | 1.685400 | 0.743206 |
| 1000000 | 2 | 1.252600 | 1.256000 | 0.997293 |
| 1000000 | 4 | 1.252600 | 0.922401 | 1.357978 |
| 1000000 | 8 | 1.252600 | 0.926300 | 1.352262 |
| 1000000 | 16 | 1.252600 | 1.323299 | 0.946574 |
| 5000000 | 1 | 6.141300 | 7.001100 | 0.877191 |
| 5000000 | 2 | 6.141300 | 4.505600 | 1.363037 |
| 5000000 | 4 | 6.141300 | 3.474201 | 1.767687 |
| 5000000 | 8 | 6.141300 | 3.463500 | 1.773149 |
| 5000000 | 16 | 6.141300 | 3.681600 | 1.668106 |
| 10000000 | 1 | 12.098501 | 12.541400 | 0.964685 |
| 10000000 | 2 | 12.098501 | 7.396200 | 1.635773 |
| 10000000 | 4 | 12.098501 | 6.621900 | 1.827044 |
| 10000000 | 8 | 12.098501 | 6.420800 | 1.884267 |
| 10000000 | 16 | 12.098501 | 6.609601 | 1.830443 |

### 3.2 Resumo dos melhores resultados no computador de 8 CPUs

| Tamanho do vetor | Melhor quantidade de threads | Melhor speedup |
| ---: | ---: | ---: |
| 10000 | 2 | 0.379260 |
| 100000 | 1 | 0.516498 |
| 1000000 | 4 | 1.357978 |
| 5000000 | 8 | 1.773149 |
| 10000000 | 8 | 1.884267 |

### 3.3 Computador 2 - 12 CPUs

O segundo teste ainda precisa ser executado em outra maquina, preferencialmente com 12 CPUs ou 12 processadores logicos. O comando indicado e:

```powershell
java -cp questao1_produto_escalar ExecutarExperimentos Computador_12_CPU
```

Depois da execucao, o arquivo `resultados.csv` deve ser salvo como:

```text
questao1_produto_escalar/resultados_12_cpu.csv
```

Tabela a ser preenchida quando os resultados da segunda maquina forem recebidos:

| Tamanho do vetor | Threads | Tempo sequencial (ms) | Tempo paralelo (ms) | Speedup |
| ---: | ---: | ---: | ---: | ---: |
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

## 4. Analise dos resultados da Questao 1

No computador de 8 CPUs, os vetores pequenos nao apresentaram ganho com threads. Para `10000` e `100000` posicoes, todos os speedups ficaram abaixo de 1. Isso acontece porque o tempo de criar, iniciar e sincronizar threads fica maior que o tempo necessario para realizar o calculo sequencial.

A partir de `1000000` posicoes, a versao paralela passou a apresentar ganho. Com 4 threads, o speedup foi de aproximadamente `1.36`. Para `5000000` posicoes, o melhor resultado ocorreu com 8 threads, com speedup de aproximadamente `1.77`. Para `10000000` posicoes, o melhor resultado tambem ocorreu com 8 threads, com speedup de aproximadamente `1.88`.

Tambem foi observado que usar 16 threads nem sempre melhora o desempenho. Em alguns casos, o speedup diminui em relacao a 8 threads. Isso indica que, depois de certo ponto, a sobrecarga de gerenciamento das threads, a disputa por recursos do processador e o acesso a memoria podem limitar o ganho.

Quando os resultados da maquina com 12 CPUs forem obtidos, espera-se que ela aproveite melhor quantidades maiores de threads, principalmente nos vetores maiores. Mesmo assim, o melhor numero de threads pode nao ser exatamente igual ao numero de CPUs, pois o desempenho tambem depende do escalonamento do sistema operacional, da memoria cache, da carga da maquina no momento do teste e da sobrecarga de criacao das threads.

## 5. Questao 2 - Clinica Veterinaria em Tempo Real

### 5.1 Descricao da simulacao

A segunda questao simula uma clinica veterinaria em funcionamento. A simulacao inclui:

- chegada de animais;
- recepcao;
- atendimento veterinario;
- vacinacao;
- banho e tosa;
- exames laboratoriais;
- emergencias;
- historico dos atendimentos;
- valores simulados dos servicos.

Cada atendimento possui dados ficticios realistas, como nome do pet, especie, raca, sexo, tutor, telefone, bairro, servico solicitado, ocorrencia, funcionario responsavel e valor cobrado.

### 5.2 Versao sequencial

A versao sequencial esta no arquivo:

```text
questao2_clinica_veterinaria/ClinicaVeterinariaSequencial.java
```

Nessa versao, todos os passos acontecem um por vez. Um animal chega, passa pela recepcao, recebe atendimento e somente depois o proximo animal inicia seu processo.

Essa versao representa uma clinica sem uso de threads. Para fins de comparacao, ela equivale a um unico funcionario atendendo um pet por vez em um unico fluxo de execucao.

### 5.3 Versao com threads

A versao com threads esta no arquivo:

```text
questao2_clinica_veterinaria/ClinicaVeterinariaThreads.java
```

Essa versao usa:

- uma thread de recepcao;
- varias threads de funcionarios;
- `PriorityBlockingQueue` para organizar os atendimentos;
- `AtomicInteger` para contar atendimentos concluidos;
- prioridade para emergencias;
- `Thread.sleep` para simular a duracao dos servicos;
- prints com horario e nome da thread.

Na simulacao, a recepcao cadastra os animais e coloca cada atendimento na fila. Os funcionarios retiram atendimentos da fila e executam os servicos em paralelo. Quando um atendimento e de emergencia, ele recebe prioridade na `PriorityBlockingQueue`.

### 5.4 Interface grafica

A interface grafica esta no arquivo:

```text
questao2_clinica_veterinaria/ClinicaVeterinariaInterface.java
```

Ela foi criada em Java Swing para facilitar a demonstracao da versao com threads. A tela permite informar:

- quantidade de funcionarios;
- quantidade de animais;
- intervalo de chegada em milissegundos.

Durante a execucao, a interface mostra:

- cards com quantidade na fila, concluidos, emergencias, receita e tempo;
- tabela da fila prioritaria;
- tabela da equipe em atendimento;
- historico de atendimentos;
- logs em tempo real;
- barra de progresso;
- painel visual com movimento dos pets;
- logo propria da clinica;
- nomes em rosa para femeas e azul para machos;
- desenhos diferentes conforme especie/raca.

Ao final da simulacao, a interface permite salvar:

- historico em CSV;
- relatorio TXT dos atendimentos.

### 5.5 Evidencias geradas para o relatorio

Foram geradas evidencias de execucao em console e prints da interface grafica. Os arquivos de log estao em:

```text
relatorio/evidencias/teste_q2_sequencial.txt
relatorio/evidencias/teste_q2_threads.txt
```

As imagens geradas para inserir no relatorio estao em:

```text
relatorio/imagens/q2_interface_inicial.png
relatorio/imagens/q2_interface_execucao.png
relatorio/imagens/q2_interface_final_historico.png
```

### 5.6 Comparacao direta entre as versoes

Tambem foi feito um teste com a mesma quantidade de animais nas duas versoes, para observar a diferenca entre o fluxo sequencial e o fluxo com threads.

| Modo | Funcionarios/threads | Animais | Intervalo (ms) | Tempo aproximado | Observacao |
|---|---:|---:|---:|---:|---|
| Sequencial | 1 fluxo | 12 | 40 | 8358,26 ms | Um pet atendido por vez |
| Com threads | 4 | 12 | 40 | 2134,33 ms | Recepcao e funcionarios em paralelo |

Nesse teste, a versao com threads apresentou aceleracao aproximada de `3,92x`. Esse valor foi obtido dividindo o tempo sequencial pelo tempo da versao com threads:

```text
8358,26 / 2134,33 = 3,92
```

Os tempos nao representam uma clinica real, pois foram simulados com `Thread.sleep`, mas ajudam a demonstrar a vantagem da execucao concorrente quando existem varios atendimentos independentes.

As legendas sugeridas e o codigo LaTeX para inserir essas figuras no Overleaf estao nos arquivos:

```text
relatorio/LEGENDAS_IMAGENS_Q2.md
relatorio/OVERLEAF_FIGURAS_Q2.tex
```

## 6. Decisoes de implementacao

Na Questao 1, os vetores foram preenchidos de forma deterministica para permitir comparacao entre execucoes. Foi usado aquecimento da JVM e repeticao das medicoes para reduzir variacoes. O speedup foi calculado dividindo o tempo sequencial pelo tempo paralelo.

Na Questao 2, a classe `AtendimentoVeterinario` centraliza os dados dos animais e dos servicos. A fila de atendimentos usa `PriorityBlockingQueue`, permitindo que emergencias sejam atendidas antes dos casos normais. A contagem de atendimentos finalizados usa `AtomicInteger`, pois varias threads podem concluir atendimentos ao mesmo tempo.

A duracao dos servicos foi simulada com `Thread.sleep`. Esse recurso nao representa o tempo real de um procedimento veterinario, mas ajuda a visualizar a diferenca entre o comportamento sequencial e o comportamento concorrente.

## 7. Validacao dos programas

Foram realizados testes de compilacao e execucao. Os comandos principais usados foram:

```powershell
javac questao1_produto_escalar\*.java questao2_clinica_veterinaria\*.java
java -cp questao1_produto_escalar ProdutoEscalarSequencial 10000 2
java -cp questao1_produto_escalar ProdutoEscalarParalelo 10000 4 2
java -cp questao2_clinica_veterinaria ClinicaVeterinariaSequencial 2 10
java -cp questao2_clinica_veterinaria ClinicaVeterinariaThreads 2 6 20
java -cp questao2_clinica_veterinaria ClinicaVeterinariaInterface
```

Nos testes da Questao 1, as versoes sequencial e paralela apresentaram o mesmo resultado numerico para o produto escalar. Nos testes da Questao 2, a versao sequencial mostrou atendimentos um por vez, enquanto a versao com threads mostrou funcionarios atuando simultaneamente.

## 8. O que falta para fechar a entrega

O codigo da Questao 2 esta pronto, compilando e com evidencias geradas. Para finalizar a parte da clinica veterinaria no relatorio, ainda falta:

- inserir no Overleaf o texto preparado em `relatorio/OVERLEAF_TEXTO_Q2.tex`;
- subir as imagens da pasta `relatorio/imagens`;
- conferir se a tabela comparativa da Questao 2 ficou legivel no PDF;
- juntar esta secao com a parte da Questao 1 preparada pela equipe.

## 9. Conclusao

O trabalho mostrou que o uso de threads pode melhorar o desempenho quando existe uma quantidade suficiente de trabalho para dividir entre varias linhas de execucao. No produto escalar, os vetores pequenos nao se beneficiaram do paralelismo, pois a sobrecarga das threads foi maior que o ganho. Nos vetores maiores, principalmente com `5000000` e `10000000` posicoes, a versao paralela apresentou melhor desempenho.

Tambem foi observado que aumentar indefinidamente a quantidade de threads nao garante melhora. No computador de 8 CPUs, os melhores resultados ocorreram com 8 threads para os maiores vetores, enquanto 16 threads nao trouxe ganho adicional significativo.

Na simulacao da clinica veterinaria, a versao sequencial demonstrou o comportamento de um unico fluxo de atendimento, enquanto a versao com threads representou melhor uma clinica real, com recepcao e varios funcionarios atuando simultaneamente. O uso de fila prioritaria permitiu demonstrar o atendimento preferencial de emergencias.

Assim, o trabalho evidencia duas aplicacoes importantes de threads: ganho de desempenho em computacao numerica e modelagem de sistemas concorrentes mais proximos de situacoes reais.
