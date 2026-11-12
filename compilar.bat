@echo off
rem Script de compilacion de edEXT (G12345-138), version Windows de compilar.sh.
rem
rem   compilar.bat              compila, corre los tests y arma dist\
rem   compilar.bat --sin-tests  igual, sin correr los tests
rem
rem Resultado en dist\: servidor.jar, web.war, imagenes\ y db\ (ver compilar.sh).
setlocal
cd /d "%~dp0"

set "MVN_ARGS=-B clean package"
if "%~1"=="--sin-tests" set "MVN_ARGS=%MVN_ARGS% -DskipTests"

where mvn >nul 2>nul || (echo No se encontro Maven ^(mvn^) en el PATH. & exit /b 1)
where java >nul 2>nul || (echo No se encontro Java en el PATH. & exit /b 1)

echo ==^> Compilando (mvn %MVN_ARGS%)
call mvn %MVN_ARGS% || exit /b 1

echo ==^> Armando dist\
if exist dist rmdir /s /q dist
mkdir dist\db
copy /y modules\swing\target\servidor.jar dist\ >nul || exit /b 1
copy /y modules\servidor-web\target\web.war dist\ >nul || exit /b 1
xcopy /e /i /q /y imagenes dist\imagenes >nul || exit /b 1
copy /y db\init\*.sql dist\db\ >nul || exit /b 1

echo.
echo Listo:
dir /b dist
endlocal
