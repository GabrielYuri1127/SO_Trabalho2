# SO - Trabalho Pratico 2

Projeto do Trabalho Pratico 2 de Sistemas Operacionais, feito em Java sem `package`.

## Integrantes

- Sabrina Brito Maciel - 22353163
- Marcele Azevedo de Paula Oliveira - 22353160
- Gabriel Yuri Cavalcante de Castro - 22350996

## Objetivo

O objetivo do trabalho e praticar programacao paralela com threads, comparar desempenho entre versoes sequencial e paralela e demonstrar uma aplicacao onde threads tornam o funcionamento mais realista.

## Estrutura

```text
SO_Trabalho2/
|-- README.md
|-- GUIA_TESTES_OUTRA_MAQUINA.md
|-- integrantes.txt
|-- .gitignore
|-- requirements.txt
|-- questao1_produto_escalar/
|   |-- ProdutoEscalarSequencial.java
|   |-- ProdutoEscalarParalelo.java
|   |-- ExecutarExperimentos.java
|   |-- resultados_modelo.csv
|   |-- resultados_8_cpu.csv
|   |-- grafico_tempos_8_cpu.png
|   `-- grafico_speedup_8_cpu.png
|-- questao2_clinica_veterinaria/
|   |-- AtendimentoVeterinario.java
|   |-- ClinicaVeterinariaSequencial.java
|   |-- ClinicaVeterinariaThreads.java
|   |-- ClinicaVeterinariaInterface.java
|   `-- README_Questao2.md
|-- scripts/
|   |-- compilar.bat
|   |-- executar_testes.bat
|   |-- compilar.sh
|   |-- executar_testes.sh
|   `-- gerar_grafico.py
`-- relatorio/
    |-- RELATORIO.md
    |-- CHECKLIST_FINAL.md
    `-- FONTES_E_REFERENCIAS.md
```

## Compilacao

Para testar em outro computador, use o passo a passo completo em `GUIA_TESTES_OUTRA_MAQUINA.md`.

No Windows:

```bat
scripts\compilar.bat
```

No Linux/macOS:

```sh
chmod +x scripts/compilar.sh scripts/executar_testes.sh
./scripts/compilar.sh
```

Compilacao manual:

```sh
javac questao1_produto_escalar/*.java questao2_clinica_veterinaria/*.java
```

## Execucao da Questao 1

```sh
java -cp questao1_produto_escalar ProdutoEscalarSequencial 1000000 5
java -cp questao1_produto_escalar ProdutoEscalarParalelo 1000000 4 5
java -cp questao1_produto_escalar ExecutarExperimentos Computador_12_CPU
```

O arquivo `questao1_produto_escalar/resultados.csv` sera gerado com as colunas:

```text
computador,modo,tamanho_vetor,threads,tempo_ms,resultado,speedup
```

Para atender ao enunciado, execute os experimentos em pelo menos dois computadores com quantidades diferentes de CPU, por exemplo:

```sh
java -cp questao1_produto_escalar ExecutarExperimentos Computador_12_CPU
java -cp questao1_produto_escalar ExecutarExperimentos Computador_8_CPU
```

Depois compare os CSVs e preencha as tabelas do relatorio.

## Execucao da Questao 2

```sh
java -cp questao2_clinica_veterinaria ClinicaVeterinariaSequencial 10 200
java -cp questao2_clinica_veterinaria ClinicaVeterinariaThreads 4 20 150
java -cp questao2_clinica_veterinaria ClinicaVeterinariaInterface
```

Na versao com threads, os parametros sao: quantidade de funcionarios, quantidade de animais e intervalo de chegada em milissegundos.

Na versao sequencial da Questao 2, a clinica e simulada como um unico fluxo de execucao: equivale a um unico funcionario atendendo um pet por vez. Na versao com threads e na interface grafica, a recepcao e varios funcionarios trabalham simultaneamente.

A interface grafica usa Java Swing e permite configurar esses parametros em uma janela profissional, com cards de indicadores, fila prioritaria em tabela, painel de funcionarios, historico de atendimentos, receita simulada, logs em tempo real, barra de progresso e painel visual com movimento dos pets. A simulacao usa dados ficticios realistas, incluindo nome do pet, especie, raca, sexo, tutor, telefone, bairro, funcionario responsavel, ocorrencia do atendimento e valor cobrado. A tela tambem possui logo propria da clinica e diferencia visualmente os pets: femeas aparecem em rosa e machos em azul. Ao final, a interface pode salvar o historico em CSV e um relatorio TXT.

## CSV e graficos

Depois de gerar o CSV, execute:

```sh
python -m pip install -r requirements.txt
python scripts/gerar_grafico.py questao1_produto_escalar/resultados.csv
```

O script gera `grafico_tempos.png` e `grafico_speedup.png` na mesma pasta do CSV.
