@echo off
setlocal
cd /d "%~dp0\.."
call scripts\compilar.bat
if errorlevel 1 exit /b 1
echo.
echo Teste Questao 1 - Sequencial
java -cp questao1_produto_escalar ProdutoEscalarSequencial 100000 3
echo.
echo Teste Questao 1 - Paralelo
java -cp questao1_produto_escalar ProdutoEscalarParalelo 100000 4 3
echo.
echo Teste Questao 2 - Sequencial
java -cp questao2_clinica_veterinaria ClinicaVeterinariaSequencial 5 50
echo.
echo Teste Questao 2 - Threads
java -cp questao2_clinica_veterinaria ClinicaVeterinariaThreads 3 8 50
echo.
echo Interface grafica da Questao 2:
echo java -cp questao2_clinica_veterinaria ClinicaVeterinariaInterface
endlocal
