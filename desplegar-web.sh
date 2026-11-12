#!/usr/bin/env bash
# Despliega dist/web.war con el Tomcat Manager (G12345-137), en el contexto /web.
#
#   TOMCAT_URL=http://nodo-web:8080 TOMCAT_USUARIO=admin ./desplegar-web.sh
#
# El usuario necesita el rol "manager-script" en conf/tomcat-users.xml de ese Tomcat.
# Si no se pasa TOMCAT_CONTRASENA, la pide por teclado. Si ya habia una version desplegada, la reemplaza.
set -euo pipefail

RAIZ="$(cd "$(dirname "$0")" && pwd)"
WAR="$RAIZ/dist/web.war"
TOMCAT_URL="${TOMCAT_URL:-http://localhost:8080}"
TOMCAT_USUARIO="${TOMCAT_USUARIO:-admin}"
CONTEXTO="${CONTEXTO:-/web}"

[[ -f "$WAR" ]] || { echo "No existe $WAR: corre primero ./compilar.sh" >&2; exit 1; }
if [[ -z "${TOMCAT_CONTRASENA:-}" ]]; then
    read -rsp "Contrasena de $TOMCAT_USUARIO en $TOMCAT_URL: " TOMCAT_CONTRASENA
    echo
fi

echo "==> Desplegando $WAR en $TOMCAT_URL$CONTEXTO"
RESPUESTA="$(curl -sS -u "$TOMCAT_USUARIO:$TOMCAT_CONTRASENA" -T "$WAR" \
    "$TOMCAT_URL/manager/text/deploy?path=$CONTEXTO&update=true")"
echo "$RESPUESTA"
[[ "$RESPUESTA" == OK* ]] || exit 1
echo "Abrir: $TOMCAT_URL$CONTEXTO/"
