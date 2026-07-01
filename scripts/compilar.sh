#!/bin/sh
set -e
cd "$(dirname "$0")/.."
echo "Compilando Questao 1..."
javac questao1_produto_escalar/*.java
echo "Compilando Questao 2..."
javac questao2_clinica_veterinaria/*.java
echo "Compilacao concluida."
