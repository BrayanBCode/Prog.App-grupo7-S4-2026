@echo off
rem Despliega dist\web.war con el Tomcat Manager (G12345-137), en el contexto /web. Version Windows de desplegar-web.sh.
rem
rem   set TOMCAT_URL=http://nodo-web:8080
rem   set TOMCAT_USUARIO=admin
rem   set TOMCAT_CONTRASENA=...
rem   desplegar-web.bat
rem
rem El usuario necesita el rol "manager-script" en conf\tomcat-users.xml de ese Tomcat.
setlocal
cd /d "%~dp0"
if "%TOMCAT_URL%"=="" set "TOMCAT_URL=http://localhost:8080"
if "%TOMCAT_USUARIO%"=="" set "TOMCAT_USUARIO=admin"
if "%CONTEXTO%"=="" set "CONTEXTO=/web"

if not exist dist\web.war (echo No existe dist\web.war: corre primero compilar.bat & exit /b 1)
if "%TOMCAT_CONTRASENA%"=="" set /p "TOMCAT_CONTRASENA=Contrasena de %TOMCAT_USUARIO% en %TOMCAT_URL%: "

echo ==^> Desplegando dist\web.war en %TOMCAT_URL%%CONTEXTO%
curl -sS -u "%TOMCAT_USUARIO%:%TOMCAT_CONTRASENA%" -T dist\web.war "%TOMCAT_URL%/manager/text/deploy?path=%CONTEXTO%&update=true"
echo.
echo Abrir: %TOMCAT_URL%%CONTEXTO%/
endlocal
