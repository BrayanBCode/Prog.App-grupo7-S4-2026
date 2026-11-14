# Distribución de edEXT (Tarea 3, E3)

Cómo generar los artefactos y correr cada parte en su propio nodo (G12345-136 a G12345-139).

## 1. Compilar (G12345-138)

Hace falta JDK 21 o superior y Maven en el PATH. No se necesita IDE.

```bash
./compilar.sh              # Linux, macOS o Git Bash
./compilar.sh --sin-tests  # igual, sin correr los tests
```

En Windows (cmd) se usa `compilar.bat` con los mismos argumentos.

El resultado queda en `dist/`:

| Archivo | Qué es |
|---|---|
| `servidor.jar` | Servidor Central + Estación de Trabajo. Publica los Web Services y abre la ventana de Swing |
| `web.war` | Servidor Web, para desplegar en Tomcat 10 o superior |
| `imagenes/` | Imágenes de los datos de prueba |
| `db/` | Scripts de la base: esquema y datos de prueba |

## 2. Nodos

```
[ Nodo BD ]  MySQL  <──JDBC──  [ Nodo Central ]  servidor.jar  <──SOAP──  [ Nodo Web ]  Tomcat + web.war  <──HTTP──  navegador
```

Toda la configuración vive en `~/.edEXT/` de cada nodo. Si el archivo no existe, se crea con los valores de desarrollo la primera vez que arranca. Para cambiarla no hay que recompilar: alcanza con editar el archivo y reiniciar.

### Nodo BD

Con Docker: `docker compose up -d` en la raíz del repo. Levanta MySQL en el puerto 3310 y carga `db/init/*.sql` la primera vez.

Sin Docker: crear la base `laboratorio_db` y correr los scripts de `dist/db/` en orden.

### Nodo Central (G12345-136)

Copiar `servidor.jar` y la carpeta `imagenes/` a una misma carpeta, y arrancar desde ahí:

```bash
java -jar servidor.jar                # publica los Web Services y abre la Estación de Trabajo
java -jar servidor.jar --sin-ventana  # solo los Web Services (nodo sin pantalla)
```

Al cerrar la Estación de Trabajo termina el proceso, y con él los Web Services.

Configuración en `~/.edEXT/servidor.properties`:

```properties
# 0.0.0.0 = escuchar en todas las interfaces, para que el nodo Web llegue por la red
ws.url=http://0.0.0.0:9128/edext/central
db.url=jdbc:mysql://IP-DEL-NODO-BD:3310/laboratorio_db?serverTimezone=UTC&zeroDateTimeBehavior=CONVERT_TO_NULL
db.usuario=root
db.contrasena=root
# vacía = ~/edext/imagenes
imagenes.carpeta=
```

Con `localhost` en `ws.url`, el central solo atiende pedidos de la misma máquina. Para separar nodos hay que usar `0.0.0.0` o la IP del nodo, y abrir el puerto en el firewall.

Para comprobar que publicó, abrir `http://IP-DEL-NODO-CENTRAL:9128/edext/central?wsdl`.

### Nodo Web (G12345-137)

1. En el Tomcat de ese nodo, crear un usuario con el rol `manager-script` en `conf/tomcat-users.xml`:
   ```xml
   <role rolename="manager-script"/>
   <user username="admin" password="CAMBIAR" roles="manager-script,manager-gui"/>
   ```
2. Configurar `~/.edEXT/web.properties` del usuario que corre Tomcat:
   ```properties
   central.url=http://IP-DEL-NODO-CENTRAL:9128/edext/central
   ```
3. Desplegar con el Manager. Se puede hacer desde cualquier máquina que llegue a ese Tomcat:
   ```bash
   TOMCAT_URL=http://IP-DEL-NODO-WEB:8080 TOMCAT_USUARIO=admin ./desplegar-web.sh
   ```
   En Windows se usa `desplegar-web.bat`, con las mismas variables definidas con `set`. También se puede subir `web.war` a mano desde `http://IP-DEL-NODO-WEB:8080/manager/html`.
4. Abrir `http://IP-DEL-NODO-WEB:8080/web/`.

Si se cambia `web.properties`, alcanza con recargar la aplicación desde el Manager. No hace falta regenerar el WAR.

## 3. Prueba en nodos separados (G12345-139)

Lista para el ensayo de la defensa:

- [ ] `compilar.sh` termina sin errores y deja `servidor.jar` y `web.war` en `dist/`.
- [ ] Nodo Central: `servidor.jar` arranca y el `?wsdl` se ve **desde la máquina del nodo Web**.
- [ ] Nodo Web: `desplegar-web.sh` responde `OK - Deployed application at context path [/web]`.
- [ ] Desde un tercer equipo: login, consulta de curso con imágenes, inscripción, ver resultados y acta PDF.
- [ ] Cambiar el puerto en `ws.url` y `central.url`, reiniciar ambos **sin recompilar**, y repetir el login.
- [ ] Apagar el central: el web muestra la página de "sin servicio" en lugar de un error de Tomcat.
