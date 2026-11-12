# edEXT: Programación de Aplicaciones, grupo 8

Sistema de cursos de extensión con tres partes:

| Módulo | Qué es |
|---|---|
| `modules/servidor-central` | Lógica, persistencia (JPA + MySQL) y Web Services SOAP |
| `modules/swing` | Estación de Trabajo (Swing). Usa el central directamente |
| `modules/servidor-web` | Sitio web (servlets + JSP). Habla con el central **solo por SOAP** |

```
[ MySQL ] <──JPA── [ Servidor Central ] <──SOAP── [ Servidor Web (Tomcat) ] <──HTTP── navegador
                          ▲
                   [ Estación de Trabajo (Swing) ]
```

## Requisitos

- **JDK 21** o superior
- **Maven 3.9** o superior
- **MySQL 8** o superior, con Docker o instalado
- **Tomcat 10.1** o superior, porque el web usa Jakarta Servlet 6

## 1. Base de datos

**Con Docker** (recomendado). Levanta MySQL en el puerto **3310**, con usuario `root`, contraseña `root` y la base `laboratorio_db`, y carga los datos de prueba la primera vez:

```bash
docker compose up -d
```

**Sin Docker**, con un MySQL ya instalado: crear la base y correr los scripts en orden.

```bash
mysql -uroot -p -e "CREATE DATABASE laboratorio_db CHARACTER SET utf8mb4"
```
```bash
for f in db/init/*.sql; do mysql -uroot -p laboratorio_db < "$f"; done
```

Si tu MySQL usa otro puerto (por ejemplo el 3306), cambiá `db.url` en la configuración del paso 2.

## 2. Configuración (`~/.edEXT`)

La configuración vive fuera del código, en la carpeta `.edEXT` de tu usuario (en Windows es `C:\Users\<tu usuario>\.edEXT`). Cada archivo **se crea solo** la primera vez que arranca el central o el web, con los valores de abajo. Para cambiarla no hay que recompilar: alcanza con editar el archivo y reiniciar.

`servidor.properties` (central y Estación de Trabajo):

```properties
ws.url=http://localhost:9128/edext/central
db.url=jdbc:mysql://localhost:3310/laboratorio_db?serverTimezone=UTC&zeroDateTimeBehavior=CONVERT_TO_NULL
db.usuario=root
db.contrasena=root
# vacía = ~/edext/imagenes
imagenes.carpeta=
```

`web.properties` (servidor web):

```properties
central.url=http://localhost:9128/edext/central
```

## 3. Compilar

Desde la raíz del repo:

```bash
mvn clean install
```

Compila los tres módulos y corre los tests. Con `-DskipTests` se saltean.

## 4. Ejecutar

Arrancar en este orden: base de datos, Servidor Central y Servidor Web.

### Servidor Central (Web Services)

```bash
mvn -pl modules/servidor-central exec:java -Dexec.mainClass=ServidorCentral.WebServices.Publicador
```

Cuando aparece `Web Services publicados en http://localhost:9128/edext/central?wsdl`, el central está listo. Para comprobarlo, abrí esa URL en el navegador.

Hay que correrlo **desde la raíz del repo**: así copia las imágenes de prueba de `imagenes/` a su carpeta.

En NetBeans o IntelliJ: ejecutar la clase `ServidorCentral.WebServices.Publicador`.

### Estación de Trabajo (Swing)

```bash
mvn -pl modules/swing exec:java
```

Usa la misma base que el central. También se puede ejecutar la clase `Main` del módulo `swing` desde el IDE.

### Servidor Web

1. Copiar `modules/servidor-web/target/servidor-web.war` a la carpeta `webapps/` de Tomcat. También se puede subir desde el Manager (`http://localhost:8080/manager/html`).
2. Arrancar Tomcat: `bin/startup.bat` en Windows o `bin/startup.sh` en Linux y Mac.
3. Abrir **http://localhost:8080/servidor-web/**

Desde NetBeans: agregar el Tomcat en *Servers* y hacer *Run* sobre el módulo `servidor-web`.

Si el central no está corriendo, el web muestra una página de "servicio no disponible".

## 5. Usuarios de prueba

Los datos de `db/init` traen estudiantes (por ejemplo `roro`, `weiss`, `chechi`) y docentes (por ejemplo `adri`). Las contraseñas están en la columna `CONTRASEÑA` de las tablas `estudiante` y `docente` de esos scripts.

## Tests

```bash
mvn test
```

Las pruebas de los repositories usan una base H2 en memoria, así que no tocan MySQL. El reporte de cobertura queda en `modules/servidor-central/target/site/jacoco/`.

## Problemas comunes

| Síntoma | Solución |
|---|---|
| `Address already in use` al arrancar el central | Ya hay otro central corriendo, o el puerto 9128 está ocupado: cerralo o cambiá `ws.url` |
| `Communications link failure` | MySQL no está levantado, o `db.url` apunta a otro puerto |
| El web muestra "servicio no disponible" | El central no está corriendo, o `central.url` no coincide con `ws.url` |
| Las imágenes de prueba no aparecen | Arrancar el central desde la raíz del repo, donde está la carpeta `imagenes/` |

## Distribución (Tarea 3, E3)

La rama `iter3/E3-distribucion` agrega `servidor.jar` y `web.war` ejecutables y los scripts `compilar.sh`/`compilar.bat` y `desplegar-web.sh`/`desplegar-web.bat`, para correr cada parte en su propio nodo. La guía está en `docs/DISTRIBUCION.md` de esa rama. Cuando se mergee, esta sección y la de "Ejecutar" se actualizan.

## Arquitectura del central

```
[ Controller ]      <-- 1. Recibe los datos de Swing (Presentacion) o de los Web Services
    │ (Llama a: cursoService.inscribirAlumno(5, datosAlumno))
    ▼
[ CursoService ]    <-- 2. Evalúa: ¿Hay cupos? ¿El curso existe?
    │ (Llama a: cursoRepository.save(curso))
    ▼
[ CursoRepository ] <-- 3. Ejecuta el código JPA (EntityManager)
    │ (SQL: UPDATE curso SET ...)
    ▼
[ Base de Datos ]
```
