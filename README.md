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
├── README.md
├── integrantes.txt
├── .gitignore
├── PROMPT_CODEX.md
├── questao1_produto_escalar/
│   ├── ProdutoEscalarSequencial.java
│   ├── ProdutoEscalarParalelo.java
│   ├── ExecutarExperimentos.java
│   └── resultados_modelo.csv
├── questao2_clinica_veterinaria/
│   ├── ClinicaVeterinariaSequencial.java
│   ├── ClinicaVeterinariaThreads.java
│   └── README_Questao2.md
├── scripts/
│   ├── compilar.bat
│   ├── executar_testes.bat
│   ├── compilar.sh
│   ├── executar_testes.sh
│   └── gerar_grafico.py
└── relatorio/
    ├── RELATORIO.md
    └── FONTES_E_REFERENCIAS.md
```

## Compilacao

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
java -cp questao1_produto_escalar ExecutarExperimentos Computador_A
```

O arquivo `questao1_produto_escalar/resultados.csv` sera gerado com as colunas:

```text
computador,modo,tamanho_vetor,threads,tempo_ms,resultado,speedup
```

## Execucao da Questao 2

```sh
java -cp questao2_clinica_veterinaria ClinicaVeterinariaSequencial 10 200
java -cp questao2_clinica_veterinaria ClinicaVeterinariaThreads 4 20 150
```

Na versao com threads, os parametros sao: quantidade de funcionarios, quantidade de animais e intervalo de chegada em milissegundos.

## CSV e graficos

Depois de gerar o CSV, execute:

```sh
python scripts/gerar_grafico.py questao1_produto_escalar/resultados.csv
```

O script gera `grafico_tempos.png` e `grafico_speedup.png` na mesma pasta do CSV.
