# Plan Tarea 3 (edExt)

Entregamos el **15/11 a las 23:59**: el web deja de usar el JAR del central y lo consume por SOAP, y cada nodo corre por su cuenta. Los monitoreos son los martes 3/11 y 10/11, y se puede pedir otro si algo se traba.

La letra también pide planificación y registro de horas actualizados en cada monitoreo.

- Mapa: [GlooMaps](https://www.gloomaps.com/9zZYKkQjs4)
- Tarjetas: [tablero de Jira](https://estudiantes-grupo8-2026.atlassian.net/jira/software/projects/G12345/boards/4), todavía sin asignar

## Alcance

| Grupo | Requerimientos | Qué hacemos |
|---|---|---|
| Mínimos | Publicar SOAP, adaptar servlets, ingresar resultados, ver resultados, valorar curso, tramitar certificado, generar acta PDF, visualización gráfica de puntajes, configuración de conexión, distribución final | Tarjetas con fecha |
| No mínimos | Inscripción con video, desistir, selección con video, listar resultados de evaluaciones, móvil, reproducción de videos, listado de tendencias | En rojo en el mapa; tarjetas opcionales sin fecha |
| En gris en el PDF | Responsive CUs, validación de datos | No se hacen si somos 3; en rojo en el mapa y sin tarjeta |

## Etapas

| Etapa | Fechas | Cierra en | Objetivo |
|---|---|---|---|
| E1 Base SOAP | hasta el 3/11 | Monitoreo 3/11 | Planificación cargada, rama lista, un servicio SOAP de punta a punta y duda del acta planteada al docente |
| E2 Funcionalidad y migración | 4 al 10/11 | Monitoreo 10/11 | Configuración externa, web consumiendo SOAP, notas, acta del docente y valoraciones |
| E3 Acta y distribución | 11 al 15/11 | Entrega 15/11, 23:59 | Acta PDF, `servidor.jar`, `web.war`, script de compilación y prueba en nodos separados |

## Tareas

La columna "Depende de" usa las claves de Jira, sin el prefijo `G12345-`.

### Gestión y base

| Clave | Tarea | Etapa | Depende de | Nota |
|---|---|---|---|---|
| G12345-119 | Crear la rama `tarea3` desde `master` | E1 | | La reorganización de paquetes pedida por el docente ya está en `master` (`6df43e2`) |
| G12345-120 | Consultar al docente la variante del acta para grupos de 3 | E1 | | Bloquea G12345-135. Plantearla en el monitoreo del 3/11; la letra la menciona, pero no la describe |
| G12345-121 | Registro de horas y actualizar herramientas en cada monitoreo | E1 a E3 | | Pedido por la letra; recurrente |

### Web Services

| Clave | Tarea | Etapa | Depende de | Nota |
|---|---|---|---|---|
| G12345-122 | Prueba de concepto: `Endpoint.publish` y cliente wsimport en Maven | E1 | 119 | Verificar compatibilidad con la JDK y el Tomcat del proyecto |
| G12345-123 | Configuración externa en `~/.edEXT` (7.7): URL de publicación, URL del WS, BD e imágenes | E2 | 122 | Hoy la BD está fija en `persistence.xml`; reutilizar `Conexion.java:35` y `AlmacenImagenes.java:55` |
| G12345-124 | Contrato SOAP: DTOs compatibles y adaptadores | E2 | 122 | Son 20 records; `LocalDate`, `Map` en `IControllerV2:107`, 3 sobrecargas y excepciones a faults |
| G12345-125 | Publicar las operaciones que usa el web | E2 | 124 | 25 de los 61 métodos de `IControllerV2`, más los nuevos de Evaluaciones y Valoraciones. Swing sigue llamando directo |
| G12345-126 | Imágenes por WS (subida y lectura) | E2 | 125 | `ImagenesServlet` lee el disco, algo imposible en otro nodo |
| G12345-127 | Adaptar servlets y `AccesoFilter` al cliente SOAP | E2 | 125 | 16 servlets, `LoginServlet:28` y `AccesoFilter:82`. Cachear institutos: `AccesoFilter:122` llama en cada request |
| G12345-128 | Adaptar JSP y tags; sacar central, EclipseLink y driver del WAR | E2 | 127 | Unas 208 expresiones `${x.campo()}` a getters; `estado.tag:2` usa la entidad directamente |

### Evaluaciones

| Clave | Tarea | Etapa | Depende de | Nota |
|---|---|---|---|---|
| G12345-129 | Modelo: nota 1 a 12 y cierre de edición, con esquema y datos de prueba | E2 | 119 | `InscripcionEdicion:28`, `EdicionCurso:32`, `db/init/01-esquema.sql:154` |
| G12345-130 | Ingresar resultados y cerrar edición (docente) | E2 | 129 | Tras el cierre no se modifican notas; entrada desde `/docente/cursos` |
| G12345-131 | Ver resultados: acta del docente en edición cerrada | E2 | 130 | Solo ediciones en que participa |

### Valoraciones

| Clave | Tarea | Etapa | Depende de | Nota |
|---|---|---|---|---|
| G12345-132 | Valorar curso: entidad y servicio | E2 | 119 | 1 a 5, una sola vez por estudiante, promedio |
| G12345-133 | Valorar curso: pantalla del estudiante | E2 | 132 | Lista de cursos, promedio y puntaje propio |
| G12345-134 | Estrellas en búsqueda y detalle; resumen en detalle | E2 | 132 | `curso/busqueda.jsp:48`, `curso/detalle.jsp`; sumar el puntaje a `DTCursoResumen` |

### Acta de estudio

| Clave | Tarea | Etapa | Depende de | Nota |
|---|---|---|---|---|
| G12345-135 | Tramitar acta y generar PDF con iText | E3 | 120, 129 | Generado en cada pedido, con enlace de descarga desde el perfil. Mientras no responda el docente: cursos aprobados del estudiante, con resultado y fecha |

### Distribución

| Clave | Tarea | Etapa | Depende de | Nota |
|---|---|---|---|---|
| G12345-136 | `servidor.jar` ejecutable: publica WS y abre la Estación de Trabajo | E3 | 123, 125 | Central no tiene `main`; el shade está en `swing/pom.xml:32` |
| G12345-137 | `web.war` y despliegue con el Tomcat Manager | E3 | 128 | Hoy `finalName` es `servidor-web` |
| G12345-138 | Script de compilación | E3 | 136, 137 | Genera los artefactos sin IDE |
| G12345-139 | Prueba en nodos separados y ensayo de defensa | E3 | 138 | Cambiar configuración sin recompilar |

### Opcionales

`G12345-140` a `G12345-145`: inscripción con video, desistir de inscripción, selección con video y reproducción, listar resultados de evaluaciones, app móvil y listado de tendencias.

## Riesgos

- **E2 es la etapa más cargada:** en una semana se juntan la migración a SOAP y las funciones nuevas. Si el 10/11 no está terminada, conviene pedir un monitoreo extra.
- **Migración a SOAP:** afecta 16 servlets y unas 208 expresiones en los JSP. Los records no sirven tal cual para SOAP, y por eso va primero la prueba de concepto.
- **Acta de estudio:** es mínima, pero la letra remite a una variante para grupos de 3 que no describe, y el web no tiene programas de formación.

## Para decidir entre todos

- [ ] Repartir las tareas entre los integrantes
- [ ] Llevar la consulta del acta al monitoreo del 3/11
- [ ] Confirmar que somos 3: si somos más, Responsive y Validación de datos pasan a ser opcionales con tarjeta
