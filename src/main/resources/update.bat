@echo off
:: Tenta entrar na pasta onde o script está
cd /d "%~dp0"

:: Define o log na raiz se a pasta database sumir por algum motivo
set LOG_FILE=database\debug_update.log
if not exist "database" set LOG_FILE=debug_update.log

echo [%time%] === INICIO === > %LOG_FILE%

:: Atraso de 7 segundos usando PING (mais confiável que timeout em sistemas antigos)
echo [%time%] Aguardando Java fechar... >> %LOG_FILE%
ping 127.0.0.1 -n 8 > nul

:: Verificação de arquivos
if not exist "sistema-update-ready.jar" (
    echo [%time%] [ERRO] sistema-update-ready.jar nao encontrado. >> %LOG_FILE%
    goto REINICIAR
)

echo [%time%] Substituindo arquivo... >> %LOG_FILE%
:: /Y para não pedir confirmação e >nul 2>&1 para evitar travas de console
move /y "sistema-update-ready.jar" "sistema-ubs.jar" >> %LOG_FILE% 2>&1

:REINICIAR
echo [%time%] Abrindo sistema... >> %LOG_FILE%
start javaw -jar sistema-ubs.jar >> %LOG_FILE% 2>&1
echo [%time%] === FIM === >> %LOG_FILE%
exit