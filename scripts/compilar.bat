@echo off
setlocal
cd /d "%~dp0\.."
echo Compilando Questao 1...
javac questao1_produto_escalar\*.java
if errorlevel 1 exit /b 1
echo Compilando Questao 2...
javac questao2_clinica_veterinaria\*.java
if errorlevel 1 exit /b 1
echo Compilacao concluida.
endlocal
