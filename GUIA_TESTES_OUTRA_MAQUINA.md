# Guia para testar o projeto em outra maquina

Este guia serve para rodar o Trabalho Pratico 2 em outro computador, principalmente em uma maquina com mais CPUs, como uma maquina com 12 CPUs/logical processors.

## 1. O que precisa instalar

Antes de rodar, confira se a maquina tem:

- Java JDK instalado, com `java` e `javac` funcionando;
- Git, se for baixar pelo repositorio;
- Python 3, apenas se for gerar os graficos da Questao 1;
- pacote `matplotlib`, instalado pelo arquivo `requirements.txt`.

Para conferir Java:

```powershell
java -version
javac -version
```

Para conferir Python:

```powershell
python --version
```

Se `javac` nao funcionar, a maquina provavelmente tem apenas o Java Runtime. Nesse caso, instale um JDK.

## 2. Baixar o projeto

Opcao 1: baixar pelo Git:

```powershell
git clone https://github.com/GabrielYuri1127/SO_Trabalho2.git
cd SO_Trabalho2
```

Opcao 2: baixar pelo GitHub como ZIP:

1. Abra o repositorio no GitHub.
2. Clique em `Code`.
3. Clique em `Download ZIP`.
4. Extraia a pasta.
5. Abra o terminal dentro da pasta `SO_Trabalho2`.

## 3. Conferir quantidade de CPUs da maquina

No Windows PowerShell:

```powershell
Get-CimInstance Win32_Processor | Select-Object NumberOfCores,NumberOfLogicalProcessors
```

Use essa informacao para nomear o computador no teste, por exemplo:

```text
Computador_12_CPU
Computador_8_CPU
```

## 4. Compilar todos os arquivos Java

No Windows PowerShell, dentro da pasta `SO_Trabalho2`:

```powershell
javac questao1_produto_escalar\*.java questao2_clinica_veterinaria\*.java
```

No Linux/macOS:

```sh
javac questao1_produto_escalar/*.java questao2_clinica_veterinaria/*.java
```

Se a compilacao terminar sem mensagens de erro, esta tudo certo para executar.

## 5. Teste rapido da Questao 1

Rode primeiro um teste pequeno:

```powershell
java -cp questao1_produto_escalar ProdutoEscalarSequencial 10000 2
java -cp questao1_produto_escalar ProdutoEscalarParalelo 10000 4 2
```

Os dois resultados devem ser iguais ou praticamente iguais. Isso confirma que a versao sequencial e a paralela calculam o mesmo produto escalar.

## 6. Rodar o experimento completo da Questao 1

Na maquina de 12 CPUs, rode:

```powershell
java -cp questao1_produto_escalar ExecutarExperimentos Computador_12_CPU
```

Na maquina de 8 CPUs, rode:

```powershell
java -cp questao1_produto_escalar ExecutarExperimentos Computador_8_CPU
```

O programa gera o arquivo:

```text
questao1_produto_escalar\resultados.csv
```

Depois de rodar na maquina de 12 CPUs, salve uma copia com nome especifico:

```powershell
Copy-Item questao1_produto_escalar\resultados.csv questao1_produto_escalar\resultados_12_cpu.csv
```

Depois de rodar na maquina de 8 CPUs:

```powershell
Copy-Item questao1_produto_escalar\resultados.csv questao1_produto_escalar\resultados_8_cpu.csv
```

No Linux/macOS, use `cp`:

```sh
cp questao1_produto_escalar/resultados.csv questao1_produto_escalar/resultados_12_cpu.csv
```

## 7. Gerar graficos da Questao 1

Instale as dependencias de Python:

```powershell
python -m pip install -r requirements.txt
```

Gere os graficos:

```powershell
python scripts\gerar_grafico.py questao1_produto_escalar\resultados.csv
```

O script gera:

```text
questao1_produto_escalar\grafico_tempos.png
questao1_produto_escalar\grafico_speedup.png
```

Depois copie os graficos com o nome da maquina:

```powershell
Copy-Item questao1_produto_escalar\grafico_tempos.png questao1_produto_escalar\grafico_tempos_12_cpu.png
Copy-Item questao1_produto_escalar\grafico_speedup.png questao1_produto_escalar\grafico_speedup_12_cpu.png
```

Para Linux/macOS:

```sh
python3 -m pip install -r requirements.txt
python3 scripts/gerar_grafico.py questao1_produto_escalar/resultados.csv
cp questao1_produto_escalar/grafico_tempos.png questao1_produto_escalar/grafico_tempos_12_cpu.png
cp questao1_produto_escalar/grafico_speedup.png questao1_produto_escalar/grafico_speedup_12_cpu.png
```

## 8. Rodar a Questao 2 sem interface

Teste a versao sequencial:

```powershell
java -cp questao2_clinica_veterinaria ClinicaVeterinariaSequencial 10 200
```

O esperado e ver os atendimentos acontecendo um por vez. Essa versao representa um unico fluxo, equivalente a um unico funcionario.

Teste a versao com threads:

```powershell
java -cp questao2_clinica_veterinaria ClinicaVeterinariaThreads 4 20 150
```

O esperado e ver varios funcionarios trabalhando ao mesmo tempo, com logs mostrando o nome da thread, inicio e fim de atendimento.

Para testar com mais funcionarios:

```powershell
java -cp questao2_clinica_veterinaria ClinicaVeterinariaThreads 8 40 100
```

## 9. Rodar a interface grafica da Questao 2

Execute:

```powershell
java -cp questao2_clinica_veterinaria ClinicaVeterinariaInterface
```

Na interface:

1. Informe a quantidade de funcionarios.
2. Informe a quantidade de animais.
3. Informe o intervalo de chegada em milissegundos.
4. Clique em `Iniciar atendimento`.
5. Observe fila, funcionarios, historico, receita, logs e painel visual dos pets.
6. Ao terminar, use `Salvar historico CSV` ou `Salvar relatorio TXT`.

Teste sugerido para apresentar:

```text
Funcionarios: 4
Animais: 20
Intervalo: 150
```

Teste mais pesado:

```text
Funcionarios: 8
Animais: 40
Intervalo: 100
```

## 10. O que enviar de volta para o grupo

Depois dos testes, envie:

- `questao1_produto_escalar\resultados_12_cpu.csv`;
- `questao1_produto_escalar\grafico_tempos_12_cpu.png`;
- `questao1_produto_escalar\grafico_speedup_12_cpu.png`;
- print da interface da Questao 2 rodando;
- se possivel, um historico CSV salvo pela interface.

## 11. Como saber se deu certo

Questao 1 esta certa quando:

- os programas sequencial e paralelo mostram o mesmo resultado;
- o arquivo `resultados.csv` e criado;
- o CSV tem linhas para todos os tamanhos e threads;
- os graficos sao gerados.

Questao 2 esta certa quando:

- a versao sequencial mostra um atendimento por vez;
- a versao com threads mostra funcionarios atendendo simultaneamente;
- emergencias aparecem com prioridade;
- a interface abre, executa a simulacao e salva historico/relatorio.

## 12. Problemas comuns

Se aparecer `javac nao e reconhecido`, instale o JDK ou configure o Java no PATH.

Se aparecer erro ao gerar grafico, rode:

```powershell
python -m pip install -r requirements.txt
```

Se a interface nao abrir, confira se os arquivos foram compilados:

```powershell
javac questao1_produto_escalar\*.java questao2_clinica_veterinaria\*.java
```

Se a janela abrir pequena ou cortada, maximize a janela.
