# Checklist final do trabalho

## Ja esta pronto

- Estrutura do projeto Java.
- Questao 1 sequencial.
- Questao 1 paralela com `Thread`.
- Classe `ExecutarExperimentos`.
- Medicao com `System.nanoTime()`.
- Aquecimento da JVM.
- CSV da maquina de 8 CPUs.
- Graficos da maquina de 8 CPUs.
- Questao 2 sequencial.
- Questao 2 com threads.
- Interface grafica da clinica.
- Historico e relatorio pela interface.
- Teste comparativo da Questao 2 com 12 animais nas duas versoes.
- README principal.
- Guia para teste em outra maquina.
- Relatorio com texto completo e resultados da maquina de 8 CPUs.
- Prints da interface da Questao 2.

## Ainda falta

1. Rodar a Questao 1 em outra maquina, preferencialmente com 12 CPUs.
2. Salvar o CSV como:

```text
questao1_produto_escalar/resultados_12_cpu.csv
```

3. Gerar os graficos:

```text
questao1_produto_escalar/grafico_tempos_12_cpu.png
questao1_produto_escalar/grafico_speedup_12_cpu.png
```

4. Enviar os resultados para inserir no relatorio.
5. Atualizar a tabela da maquina de 12 CPUs no `RELATORIO.md`.
6. Escrever a comparacao final entre 8 CPUs e 12 CPUs.
7. Inserir no Overleaf o texto, a tabela comparativa e os prints da Questao 2.

## Comando principal para a colega rodar

```powershell
java -cp questao1_produto_escalar ExecutarExperimentos Computador_12_CPU
```

## Comando para gerar graficos

```powershell
python -m pip install -r requirements.txt
python scripts\gerar_grafico.py questao1_produto_escalar\resultados.csv
```

## Arquivos que a colega deve devolver

```text
resultados_12_cpu.csv
grafico_tempos_12_cpu.png
grafico_speedup_12_cpu.png
print da interface da clinica rodando
```
