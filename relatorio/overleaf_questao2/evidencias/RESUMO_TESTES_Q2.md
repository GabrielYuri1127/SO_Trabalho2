# Resumo dos testes da Questao 2

## Teste 1 - Versao sequencial

Comando:

```powershell
java -cp questao2_clinica_veterinaria ClinicaVeterinariaSequencial 8 20
```

Resultado observado:

- A clinica executou sem threads.
- O log informou que a simulacao representa um unico funcionario em fluxo sequencial.
- Os atendimentos aconteceram um por vez.
- Foram atendidos 8 animais.
- O teste incluiu emergencia no atendimento da Maya.
- Tempo total aproximado: 5517,05 ms.

Arquivo de evidencia:

```text
relatorio/evidencias/teste_q2_sequencial.txt
```

## Teste 2 - Versao com threads

Comando:

```powershell
java -cp questao2_clinica_veterinaria ClinicaVeterinariaThreads 4 12 40
```

Resultado observado:

- A clinica executou com 4 funcionarios em threads separadas.
- Os logs mostraram recepcao e funcionarios trabalhando simultaneamente.
- A emergencia da Maya foi priorizada quando ficou disponivel na fila.
- Foram atendidos 12 animais.
- Tempo total aproximado: 2151,11 ms.

Arquivo de evidencia:

```text
relatorio/evidencias/teste_q2_threads.txt
```

## Teste 3 - Comparacao direta entre sequencial e threads

Para comparar as duas versoes com a mesma carga, foram executados 12 atendimentos nas duas simulacoes.

| Modo | Funcionarios/threads | Animais | Intervalo (ms) | Tempo aproximado | Observacao |
|---|---:|---:|---:|---:|---|
| Sequencial | 1 fluxo | 12 | 40 | 8358,26 ms | Um pet atendido por vez |
| Com threads | 4 | 12 | 40 | 2134,33 ms | Recepcao e funcionarios trabalhando em paralelo |

A aceleracao observada nessa execucao foi de aproximadamente `3,92x`, calculada por:

```text
8358,26 / 2134,33 = 3,92
```

Arquivos de evidencia:

```text
relatorio/evidencias/teste_q2_comparativo_sequencial_12.txt
relatorio/evidencias/teste_q2_comparativo_threads_12.txt
```

## Teste 4 - Interface grafica

Configuracao usada:

```text
Funcionarios: 4
Animais: 20
Intervalo: 150 ms
```

Resultado observado:

- A interface abriu corretamente.
- A simulacao foi executada ate o final.
- A tela mostrou fila, equipe, historico, logs, progresso, receita e painel visual.
- Foram concluidos 20 atendimentos.
- Foram registradas 2 emergencias.
- Receita simulada exibida: R$ 3485,00.
- Tempo total exibido na interface: aproximadamente 3,93 s.

Prints gerados:

```text
relatorio/imagens/q2_interface_inicial.png
relatorio/imagens/q2_interface_execucao.png
relatorio/imagens/q2_interface_final_historico.png
```
