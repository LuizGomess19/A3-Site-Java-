@echo off
title Walnut Brewery — Java Backend
chcp 65001 > nul
cls

echo ============================================================
echo           WALNUT BREWERY — APLICAÇÃO JAVA SPRING BOOT
echo ============================================================
echo.
echo Este script auxiliar tenta executar o projeto localmente.
echo.
echo [!] REQUISITO: Certifique-se de ter o JDK 17 (ou superior) instalado e no seu PATH.
echo.
echo [DICA] O jeito mais rápido e prático para o seu professor avaliar:
echo 1. Abrir esta pasta no IntelliJ IDEA, Eclipse ou VS Code.
echo 2. Aguardar a IDE importar o projeto Maven (pom.xml) automaticamente.
echo 3. Executar o arquivo 'WalnutBreweryApplication.java' clicando em Run.
echo.
echo ============================================================
echo Tentando iniciar o servidor via Maven Wrapper...
echo ============================================================
echo.

REM Verifica se existe o mvnw local e tenta executar
if exist "mvnw.cmd" (
    call mvnw.cmd spring-boot:run
) else (
    echo [ERR] Arquivo mvnw.cmd não foi encontrado na pasta raiz.
    echo Por favor, abra e execute o projeto diretamente pela sua IDE (IntelliJ, VS Code, Eclipse).
)

echo.
echo Finalizado. Pressione qualquer tecla para sair...
pause > nul
