#!/usr/bin/env bash
# Script de compilacion de edEXT (G12345-138): genera los artefactos de la entrega sin IDE.
#
#   ./compilar.sh              compila, corre los tests y arma dist/
#   ./compilar.sh --sin-tests  igual, sin correr los tests
#
# Resultado en dist/:
#   servidor.jar   Servidor Central + Estacion de Trabajo (java -jar servidor.jar [--sin-ventana])
#   web.war        Servidor Web, para desplegar con el Tomcat Manager (ver desplegar-web.sh)
#   imagenes/      imagenes de los datos de prueba (el central las copia a su carpeta al arrancar)
#   db/            scripts de la base (esquema y datos de prueba)
#
# Requiere JDK 21 o superior y Maven en el PATH. La configuracion de cada nodo vive en ~/.edEXT
# (servidor.properties y web.properties): se cambia sin recompilar.
set -euo pipefail

RAIZ="$(cd "$(dirname "$0")" && pwd)"
cd "$RAIZ"

MVN_ARGS=(-B clean package)
if [[ "${1:-}" == "--sin-tests" ]]; then
    MVN_ARGS+=(-DskipTests)
fi

command -v mvn >/dev/null || { echo "No se encontro Maven (mvn) en el PATH." >&2; exit 1; }
command -v java >/dev/null || { echo "No se encontro Java en el PATH." >&2; exit 1; }

echo "==> Compilando (mvn ${MVN_ARGS[*]})"
mvn "${MVN_ARGS[@]}"

echo "==> Armando dist/"
rm -rf dist
mkdir -p dist/db
cp modules/swing/target/servidor.jar dist/
cp modules/servidor-web/target/web.war dist/
cp -r imagenes dist/imagenes
cp db/init/*.sql dist/db/

echo
echo "Listo:"
ls -lh dist/servidor.jar dist/web.war
