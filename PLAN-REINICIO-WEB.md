# Plan de reinicio – Servidor Web

**Objetivo:** arrancar en limpio con lo que funciona, borrar el resto y rehacer los casos de uso **de a uno**,
cada uno con sus tests del Servidor Central y siguiendo el diseño de las figuras de la letra.

> ⏰ Entrega final: **18/10 23:59**. Al borrar los casos de uso que andan mal, los mínimos que dependen de ellos
> quedan **sin implementar hasta rehacerlos**. Prioridad absoluta: rehacer los mínimos (Fase 4, filas 4.1–4.8).

---

## 1. Qué se queda y qué se borra

| Pieza | Decisión |
|---|---|
| Matriz `CasoDeUso` + `AccesoFilter` | ✅ Se queda (los permisos de todos los casos de uso siguen definidos en la matriz). |
| `BaseServlet` y utilidades | ✅ Se queda, mejorando el manejo de fotos (Fase 2). |
| Marco común (`inicio-pagina`, `cabezal`, `fin-pagina`) | ✅ Se queda, ajustado al esquema de la Figura 2 (Fase 1). |
| Tags `e:imagen`, `e:fecha`, `e:estado` | ✅ Se quedan. |
| Inicio / Cierre de Sesión | ✅ Se queda. |
| Alta de Usuario (registro) | ✅ Se queda (solo se adapta al nuevo manejo de fotos). |
| Consulta de Usuario | ✅ Se queda y se mejora (Fase 3). |
| Página de inicio (`/inicio`) | ✅ Se queda. |
| Consulta de Curso, Consulta de Edición | 🗑️ Se borran y se rehacen. |
| Alta de Curso, Alta de Edición | 🗑️ Se borran y se rehacen. |
| Inscripción a Edición, Seleccionar Estudiantes, Listar Aceptados | 🗑️ Se borran y se rehacen. |
| Listar resultados de inscripciones, Seguir Usuario | 🗑️ Se borran y se rehacen. Seguir era el botón del perfil: se saca del perfil hasta rehacerlo. |

En el **Servidor Central** no se borra nada todavía: Swing usa muchos de esos métodos. Lo que quede sin uso
se limpia al rehacer cada caso de uso.

---

## Alcance: casos de uso a implementar

Todos los casos de uso de la Tarea 2 menos los que están en gris para el grupo. El detalle de cada uno pendiente
está en la Fase 4.

Estado: ✅ hecho · 🟡 parcial · ⬜ pendiente

### Mínimos (obligatorios)

| # | Caso de uso | Estado |
|---|---|---|
| 1 | Inicio de Sesión | ✅ |
| 2 | Cierre de Sesión | ✅ |
| 3 | Alta de Usuario (web, y en Swing con contraseña y confirmación) | ✅ (con aviso Ajax de nickname/correo en uso) |
| 4 | Consulta de Usuario | ✅ |
| 5 | Alta de Curso | ✅ |
| 6 | Consulta de Curso | ✅ |
| 7 | Alta de Edición de Curso | ✅ |
| 8 | Consulta de Edición de Curso | ✅ |
| 9 | Inscripción a Edición de Curso (con Ajax) | ✅ |
| 10 | **Seguir Usuario (con Ajax)** | ✅ |
| 11 | **Seleccionar Estudiantes para una Edición** | ✅ |
| 12 | **Listar Aceptados a una Edición** | ✅ |
| — | Diseño del sitio | ✅ |
| — | Imágenes | ✅ |

### Opcionales (no mínimos)

| # | Caso de uso / requerimiento | Estado |
|---|---|---|
| 13 | Listar resultados de inscripciones | ✅ (4.9) |
| 14 | Dejar de Seguir Usuario (con Ajax) | ✅ |
| 15 | Modificar Datos de Usuario | ✅ (4.11, nombre, apellido y fecha) |
| 16 | Pestañas en el perfil | ✅ (Fase 3, Figura 6) |
| 17 | Tests unitarios + JaCoCo ≥ 80% | ✅ 96% de todo `Logica` (237 tests) |
| 18 | Búsqueda de cursos | ✅ (cuadro en el cabezal) |

### No corresponden al grupo (en gris)

Alta de Categoría, Crear Programa de Formación, Consulta de Programa de Formación, Agregar Curso a Programa,
Inscripción a Programa de Formación y Ranking.

---

## Pendiente (al 17/10, para el resto del equipo)

**Todos los mínimos están hechos.** Lo que sigue es lo que falta, aunque sea opcional, en orden de prioridad.
Todo está en la rama `web-v2`. Antes de empezar: en NetBeans, *Clean and Build* de `servidor-central` y después
del web (si no, el web no ve las clases nuevas del central).

| # | Qué falta | Tipo | Prioridad |
|---|---|---|---|
| P1 | Subir la cobertura de 57% a 80% | ✅ Hecho (96% de todo `Logica`) | — |
| P2 | `ControllerTest`: borrar o rehacer | ✅ Hecho (borrado) | — |
| P3 | 4.9 Listar resultados de inscripciones | ✅ Hecho y probado | — |
| P4 | 4.11 Modificar Datos de Usuario | ✅ Hecho y probado | — |
| P5 | Búsqueda de cursos (requerimiento 7.5) | ✅ Hecho y probado | — |

**17/10, P3–P5:** hechos y probados a mano en Tomcat con Visitante, Estudiante y Docente (detalle abajo). Central
con tests: 233 en total, todos pasan; `Logica` sigue en **96%**.

### P1 – Cobertura 57% → 80% ✅ (17/10: 96% de todo `Logica`)

- **Cómo se mide:** `mvn -pl modules/servidor-central verify` → *All coverage checks have been met*. La regla cubre
  **todo el paquete `Logica`, sin exclusiones** (antes dejaba afuera `repositories` y `programaFormacion`). El
  detalle está en `modules/servidor-central/target/site/jacoco/index.html` (el total de abajo del reporte incluye
  también `Persistencia`, que no es parte de `Logica`).
- **Antes:** 57% de `Logica` (876 de 1537 líneas, 129 tests). **Ahora:** **96%** (1481 de 1537, 220 tests, todos pasan).

| Paquete | Cobertura |
|---|---|
| services | 98% |
| repositories | 94% |
| controller | 88% |
| datatypes | 96% |
| entities (cursos, usuarios, programaFormacion) | 100% |
| seguridad, excepciones | 100% |

- **Qué se agregó:**
  - **Controller:** `ControllerV2` tiene un constructor (de paquete) que recibe los servicios ya armados
    (`ControllerV2.Servicios`); el constructor público sigue igual para Swing y el web. `ControllerV2Test` prueba con
    servicios "espía" que cada método llama al servicio correcto con los mismos datos y devuelve lo que este responde.
  - **Services** (sin base, sobreescribiendo los métodos del repositorio en una clase anónima): `UsuarioServiceTest`,
    `EstudianteServiceTest`, `DocenteServiceTest`, `InstitutoServiceTest`, `ProgramaFormacionServiceTest` y
    `EdicionCursoServiceTest` (lo que usa Swing: altas, modificar, inscripciones, consultas y listados).
  - **Repositories** (con JPA de verdad, contra una base **H2 en memoria**): cada test crea una base vacía con las
    tablas de `persistence.xml`, la carga con `Escenario` (a través de los mismos repositories) y la descarta al
    terminar. **No toca MySQL ni deja datos.** H2 es dependencia solo de test (no va en el `.war` ni en el `.jar`).
    Para eso `Conexion` tiene `conPropiedades(...)` y `cerrar()`; la app sigue usando `getInstancia()` igual que antes.
    Tests: `UsuarioRepositoryTest`, `CursoYDocenteRepositoryTest`, `EdicionCursoRepositoryTest`,
    `EstudianteProgramaYSeguimientoRepositoryTest`.
  - **Entidades:** `EntidadesTest` (igualdad por clave, setters, relación Programa – Curso).
- **Lo que queda sin cubrir** son los `catch` de rollback de los repositories y el armado con MySQL de
  `ControllerV2.Servicios.conBase`.

### P2 – `ControllerTest` ✅ (17/10: borrado)

Estaba **entero comentado** y usaba la base real. Se borró: lo reemplaza `ControllerV2Test` (sin base) más los tests
de `services/`.

### P3 – 4.9 Listar resultados de inscripciones (Estudiante) ✅ (17/10)

- **Letra:** el estudiante ve todas sus inscripciones a ediciones y el estado de cada una.
- **Central:** `resultadosInscripciones(nick)` → `List<DTInscripcionEstudiante>` (edición, curso, fecha, estado), de la
  más nueva a la más vieja, incluidas las Rechazadas. `ReglaNegocioException` si el nickname no es de un estudiante.
  Tests: `ResultadosInscripcionesTest` (varias inscripciones en los tres estados, sin inscripciones, quien no es
  estudiante) y `ControllerV2Test`.
- **Web:** `ResultadosInscripcionesServlet` en `/inscripciones/resultados` (`LISTAR_RESULTADOS_INSCRIPCIONES` en
  `RUTAS_PROTEGIDAS`) + `inscripcion/resultados.jsp` (tabla edición / curso / fecha / estado con `e:estado` y links).
  En el lateral, el grupo **INSCRIPCIONES → Ver resultados** de la Figura 3 (solo estudiantes). "Inscribirme" no va en
  el lateral: la inscripción se hace desde la edición (4.5).

### P4 – 4.11 Modificar Datos de Usuario (Estudiante, Docente) ✅ (17/10)

- **Letra:** el usuario edita sus datos básicos, **menos nickname y correo**, y el sistema los actualiza.
  En la Figura 6 son los lápices de la pestaña General.
- **Decidido:** se editan **nombre, apellido y fecha de nacimiento**, que son los campos con lápiz en la Figura 6.
  La foto y la contraseña quedan fuera.
- **Central:** `modificarDatosUsuario(nick, nombre, apellido, fechaNac)` con `ReglaNegocioException`: usuario que no
  existe, nombre o apellido vacíos, fecha vacía o que no es anterior a hoy. El correo lo toma del usuario guardado
  (no viene del formulario). El `modificarUsuario` de Swing queda igual. Tests: `ModificarDatosUsuarioTest` y
  `ControllerV2Test`.
- **Web:** lápiz ✎ junto a Nombre, Apellido y Fecha de nacimiento en la pestaña General **del perfil propio** →
  `ModificarUsuarioServlet` en `/usuarios/modificar` (`MODIFICAR_DATOS_USUARIO`). El nickname sale de la sesión, así
  que solo se pueden modificar los datos propios. Formulario con nickname y correo deshabilitados; Post/Redirect/Get
  al perfil con flash "Se actualizaron tus datos.". Si hay error, vuelve al formulario con lo ingresado. También se
  actualiza el nombre guardado en la sesión (`SesionUsuario.actualizar`), que es el que saluda en el inicio.

### P5 – Búsqueda de cursos (requerimiento especial 7.5) ✅ (17/10)

- **Letra:** un cuadro de búsqueda **siempre visible en el cabezal** (Figura 2); lista los cursos y programas cuyo
  nombre o descripción contienen el texto (todos si está vacío). Para el grupo, **solo cursos** (programas en gris).
- **Central:** `buscarCursos(texto)` → `List<DTCursoResumen>`, ordenados por nombre. La consulta
  (`CursoRepository.buscar`) compara en minúsculas el nombre y la descripción con `LIKE`; con texto vacío devuelve
  todos. Tests: `BusquedaCursosTest` (servicio), `CursoYDocenteRepositoryTest` (consulta real contra H2) y
  `ControllerV2Test`.
- **Web:** cuadro **Buscar** en `cabezal.jspf`, en todas las páginas menos login y registro (que no tienen links en el
  cabezal), para todos los roles (`BUSCAR_CURSOS_Y_PROGRAMAS`). Va a `/buscar?q=...` (`BusquedaServlet`) y
  `curso/busqueda.jsp` muestra las mismas tarjetas que la lista de cursos de un instituto. Cada tarjeta lleva a la
  Consulta de Curso.
- **Ordenar por (17/10, figura de la página de resultados):** arriba de las tarjetas, una barra con "N resultados" y
  el desplegable **Ordenar por:** "(seleccione una opción)" / "Alfabéticamente (A-Z a-z)" / "Alfabéticamente (Z-A z-a)"
  / "Año (ascendente)" / "Año (descendente)". La figura de la letra trae solo A-Z y año descendente; los dos sentidos
  inversos se agregaron a pedido del grupo. Al elegir, se vuelve a buscar con `&orden=` `ALFABETICO_ASC`,
  `ALFABETICO_DESC`, `ANIO_ASC` o `ANIO_DESC` (sin JavaScript aparece un botón Ordenar).
  Central: `buscarCursos(texto, OrdenBusqueda)`; sin orden (o con uno que no existe) = alfabético A-Z.
  - Alfabético: por nombre, sin distinguir mayúsculas ni tildes (`Collator` en español).
  - Año: **año de registro del curso** (es la única fecha que tiene un curso); dentro del mismo año siempre A-Z; los
    cursos sin fecha van al final en los dos sentidos.
  - Tests: `BusquedaCursosTest` (los cuatro órdenes, mayúsculas, tildes, mismo año, sin fecha) y `ControllerV2Test`
    (237 tests). Probado en Tomcat: los cuatro órdenes coinciden con la base.

### Prueba manual de P3–P5 ✅ (17/10, Tomcat 10.1 + MySQL en Docker)

- **Visitante:** la búsqueda anda (por nombre: "micro"; solo por descripción: "escuelas rurales", "ceibal"; con
  mayúsculas y tildes: "ROBÓTICA"; vacía → los 10 cursos; "zzzz" → aviso de sin resultados). `/inscripciones/resultados`
  y `/usuarios/modificar` → login.
- **Estudiante (roro):** Ver resultados muestra sus 11 inscripciones (Inscripto / Aceptada / Rechazada), de la más nueva
  a la más vieja, igual que en la base. Un estudiante nuevo (`zztest_est`, ya borrado) ve el aviso de que no tiene
  inscripciones. En su perfil, 3 lápices (nombre, apellido, fecha). Errores: nombre vacío, fecha futura y fecha vacía
  muestran su mensaje y el formulario conserva lo ingresado. Un cambio válido → flash "Se actualizaron tus datos." y el
  saludo del inicio con el nombre nuevo. Los datos se dejaron como estaban.
- **Docente (house):** sin grupo INSCRIPCIONES en el lateral; `/inscripciones/resultados` → inicio. Lápices en su
  perfil y modificación correcta (revertida). En el perfil de otro usuario no hay lápices. La búsqueda anda igual.
- **Bug encontrado y corregido:** buscar `%` (o `_`) devolvía todos los cursos, porque eran comodines del `LIKE`.
  Ahora se escapan (`ESCAPE '!'`) y se buscan como texto. Test nuevo en `CursoYDocenteRepositoryTest` (237 tests).
- **Ojo al probar:** (1) si la base de Docker se creó antes del Sprint 2, faltan estados, contraseñas e imágenes:
  correr `db/init/03-datos-sprint2.sql` (se puede volver a correr). (2) Hacer *Clean and Build*: un WAR armado sin
  `clean` traía el `EdicionesServlet` viejo y Tomcat no levantaba (dos servlets en `/ediciones`).

### Cómo trabajar (lo que se usó en todo lo hecho)

Ciclo de la Fase 4: central primero (DataType completo, `ReglaNegocioException`, tests sin base) → servlet fino que
hereda de `BaseServlet` y usa `mensajeDeError(e)` → JSP según la figura → ruta en `RUTAS_PROTEGIDAS` → prueba manual
con cada rol → rama corta y merge a `web-v2`. Para Ajax: `Json.escribir` / `Json.error` y la ruta en `RUTAS_AJAX`.
Datos de prueba: usuarios con contraseña `1234` (o `123`); si se crean datos, prefijo `zztest_` y borrarlos al final.

---

## 2. Reglas para todo lo nuevo ("terminado" significa…)

1. **Diseño según la letra.** La pantalla sigue la figura correspondiente (Figuras 2 a 6 de la Tarea 2): misma
   estructura y mismos elementos. No se agregan ni se inventan pantallas. Bootstrap se usa solo como herramienta
   para armar esa estructura.
2. **Central primero.** El controller devuelve lo que la vista necesita **en una sola llamada**
   (`List<DTCurso>`, no una lista de nombres + una llamada por cada uno).
3. **Tests del controller.** Cada método nuevo o modificado del controller tiene sus tests JUnit: caso feliz y cada
   error de la letra. Con esto se va sumando a la cobertura del 80% (Fase 0).
4. **Errores tipados.** El central tira `ReglaNegocioException` con un mensaje para el usuario. El servlet muestra
   solo ese mensaje; cualquier otro error → mensaje genérico + log.
5. **Servlet fino.** Hereda de `BaseServlet`, lee parámetros, llama **un** método del controller y elige la vista.
6. **POST → Redirect → GET** con `flash` en toda acción que modifica datos.
7. **Permiso** en la matriz `CasoDeUso` y la ruta en `RUTAS_PROTEGIDAS`.
8. **Prueba manual** con cada rol que corresponde.

---

## 3. Fase 0 – Limpieza y base de tests (½ día)

### 0.A Ramas y limpieza
- [x] Unificar ramas en `web-v2`: mergear `quitar-categoria` (sin categorías) y `ConsultaUsuarioWeb`.
      *(`web-v2` sale de `ConsultaUsuarioWeb`, que ya contenía `quitar-categoria` vía PR #5.)*
- [x] Quitar el modo debug (`DEBUG_VER_TODO`).
- [x] **Borrar del módulo web** los servlets y JSP de los casos de uso marcados 🗑️:
  - Servlets: `CursoServlet`, `EdicionesServlet`, `AltaCursoServlet`, `AltaEdicionServlet`, `InscripcionEdicionServlet`,
    `SeleccionarEstudiantesServlet`, `AceptadosServlet`, `ResultadosInscripcionesServlet`, `SeguirUsuarioServlet`.
  - Vistas: `views/curso/*`, `views/edicion/*`, `views/usuario/resultados.jsp`, `views/comun/elegir-curso.jspf`.
  - En `AccesoFilter`: las rutas de esos servlets en `RUTAS_PROTEGIDAS`.
  - En el cabezal y el perfil: los links y botones a esas páginas.
- [x] Verificar que compila y que Login, Registro, Consulta de Usuario e Inicio andan.
      *(Probado en Tomcat 15/10 con el marco nuevo: registro, login, inicio, lista y perfil, Salir.)*

### 0.B Tests y cobertura
JaCoCo ya está configurado en `servidor-central/pom.xml`; falta usarlo como control.
- [x] Medir la cobertura actual: `mvn -pl modules/servidor-central verify` → `target/site/jacoco/index.html`.
- [x] Revisar `ControllerTest` (44 tests que usan la base real): no aparecieron en la última corrida de `mvn test`.
      Hacer que corran o pasarlos a tests sin base, como los de `services/`.
      *Causa: el archivo entero estaba comentado. 17/10: borrado, lo reemplaza `ControllerV2Test` sin base.*
- [x] Agregar a JaCoCo la regla `check`: **mínimo 80% de sentencias en el paquete `Logica`**.
      *Decidido: modo aviso (`haltOnFailure=false`), sin `repositories` (necesita base) ni `programaFormacion`
      (no es del grupo). `mvn verify` muestra un WARNING con el % actual: **40% de líneas** al 15/10.*
      *17/10: la regla pasó a cubrir todo `Logica` sin exclusiones (los repositories se prueban con H2 en
      memoria): **96%**. Sigue en modo aviso.*
- [x] Anotar en este plan la cobertura de partida y en qué clases falta más.

**Cobertura de partida (15/10, 39 tests, instrucciones): `Logica` ≈ 26% (1394/5337).**

| Paquete | Cobertura | Instr. |
|---|---|---|
| `Logica.seguridad` | 100% | 316 |
| `Logica.entities.cursos` | 44% | 491 |
| `Logica.services` | 39% | 1574 |
| `Logica.datatypes` | 34% | 349 |
| `Logica.entities.usuarios` | 34% | 260 |
| `Logica.repositories` | 3% | 1691 |
| `Logica.controller` | 0% | 420 |
| `Logica.entities.programaFormacion` | 0% | 236 |

Lo que más pesa: `repositories` (32% del total, necesita base) y `controller` (sin un solo test).

**Listo cuando:** el web compila solo con lo que se queda y `mvn verify` informa la cobertura real.

---

## 4. Fase 1 – Marco según la Figura 2 (½ día)

- [x] Juntar en `docs/figuras/` las capturas de las Figuras 2 a 6 de la letra, para tenerlas a mano al maquetar.
- [x] Ajustar el marco común al esquema de la Figura 2:
  - Cabezal fijo en todas las páginas: logo, links **Iniciar sesión** y **Registrarse**; con sesión, nombre e
    imagen reducida del usuario, link a su perfil y **Cerrar sesión** (Figura 3).
  - Lateral con los **institutos** (las categorías no corresponden al grupo).
  - Parte central donde se ejecuta cada caso de uso.
- [x] Cargar Bootstrap en todo el sitio, sacar el interruptor `usarBootstrap` y borrar de `estilos.css` lo que choca.
      *Hecho: el menú de casos de uso pasó del desplegable del cabezal al lateral (`comun/lateral.jspf`), como en
      la Figura 3: Mi perfil, grupos por rol, Usuarios, Institutos y Salir. Login y Registro ahora usan el marco.
      El cuadro de búsqueda de la figura se agregó el 17/10 (P5). Cada caso de uso
      rehecho en la Fase 4 suma su link en `lateral.jspf`.*

---

## 5. Fase 2 – Fotos (½ día)

Hoy el servlet guarda la foto en un temporal, le pasa **la ruta** al central y después la borra. Ese código está
duplicado (`BaseServlet.imagenTemporal` y `AltaUsuarioServlet.guardarImagenTemporal`).

- [x] Central: `record DTImagen(byte[] contenido, String extension)`.
- [x] Central: `AlmacenImagenes.guardar(DTImagen, subcarpeta, nombreBase)`: valida JPG/PNG y tamaño máximo.
      La versión con ruta queda solo para Swing y delega en la nueva.
- [x] Web: `BaseServlet.leerImagen(req, campo)` → `DTImagen` directo desde el `Part`, sin temporales.
- [x] Alta de Usuario hereda de `BaseServlet` y usa `leerImagen`; se borra `guardarImagenTemporal`.
- [x] Tests de `AlmacenImagenes` con carpeta temporal: guarda JPG/PNG, rechaza otros formatos, limpia el nombre.
      *Hecho 15/10: 9 tests (48 en total). Tamaño máximo 2 MB (lo controla el central; `@MultipartConfig` queda en 5 MB
      para que llegue su mensaje). `altaUsuario` tiene una sobrecarga con `DTImagen` para el web; la de ruta sigue para
      Swing. Probado en Tomcat: PNG se guarda y se ve en el perfil; GIF y >2 MB muestran su error; sin foto anda.*
- [x] **Carpeta fija para las imágenes** (16/10): las fotos ya no se guardan dentro del deploy (se perdían en cada build).
      Van a `~/edext/imagenes` (o `-Dedext.imagenes` / `EDEXT_IMAGENES`), la misma para Swing y el web. Las de los
      datos de prueba se copian ahí sin pisar nada (`AlmacenImagenes.sembrar`, desde el WAR o desde `imagenes/` del repo).
      `ImagenesServlet` las sirve en `/imagenes/*` (misma URL; 404 si no existe o si la ruta se sale con `..`; 304 si no
      cambió). 5 tests nuevos (53 en total). Probado: sobrevive a un redeploy y responde aunque la base esté caída.

---

## 6. Fase 3 – Consulta de Usuario (½ día)

- [x] Central: `listarUsuarios()` → `List<DTUsuario>` en una consulta.
- [x] Central: `obtenerPerfil(nickConsultado, nickQueMira)` → `DTPerfil` con todo lo que muestra la página;
      la regla de qué ve cada rol queda en el central, con sus tests.
- [x] Web: `UsuariosServlet` con una llamada para la lista y una para el perfil.
- [x] Vista según la **Figura 6** (pestañas del perfil).
- [x] Visibilidad acordada:
  - Visitante: datos básicos + foto.
  - Estudiante: + inscripciones Inscripto/Aceptada + programas + seguidores (+ Seguir con Ajax, cuando se rehaga 4.6).
  - Docente: + todas las inscripciones, también Rechazadas.
  - El propio estudiante ve sus Rechazadas; el propio docente tiene "Ver aceptados".
      *Hecho 16/10: `PerfilService` (central) decide qué ve cada uno; 11 tests (64 en total, cobertura 40% → 47%).
      El Visitante ve solo la pestaña General. Pestañas como la Figura 6: General / Cursos / Programas (+ Seguidores).
      Probado contra la base real: Visitante (web: solo pestaña General, 404 si no existe), estudiante mirando a
      otro (sin Rechazadas), el propio y un docente (con Rechazadas), docente propio (ediciones + "Ver aceptados").
      Falta ver las pestañas en el navegador con sesión iniciada (los usuarios de prueba no tienen contraseña).*

---

## 7. Fase 4 – Casos de uso de a uno

> **Preparativos (16/10):** fichas de 4.1–4.8 en [`docs/casos-de-uso-fase4.md`](docs/casos-de-uso-fase4.md) (letra,
> reglas y mensajes de error, qué hay hoy en el central, qué falta, tests y **decisiones pendientes D1–D4**).
> `ReglaNegocioException` creada (unchecked, extiende `IllegalArgumentException` para no romper los `catch` de
> Swing) y `BaseServlet.mensajeDeError(e)`; ya la usan Alta de Usuario, Inicio de Sesión y `AlmacenImagenes`.

Orden por dependencias. Cada fila: rama corta → merge a `web-v2`. Arriba, los casos de uso del web que ya están
hechos (se quedaron del web anterior o se hicieron en las fases 0 a 3), para ver de un vistazo qué falta.

Estado: ✅ hecho · ⬜ pendiente

| # | Estado | Caso de uso | Rol | Figura | Depende de |
|---|---|---|---|---|---|
| — | ✅ | Inicio de Sesión | Visitante | — | — |
| — | ✅ | Cierre de Sesión | Est./Doc. | Figura 3 | — |
| — | ✅ | Alta de Usuario (registro, con aviso Ajax de nick/correo en uso) | Visitante | — | Fase 2 |
| F3 | ✅ | Consulta de Usuario | Todos | Figura 6 | Fase 3 |
| 4.1 | ✅ | Consulta de Curso | Todos | Figura 3 | Fase 1 |
| 4.2 | ✅ | Consulta de Edición | Todos | Figura 4 | 4.1 |
| 4.3 | ✅ | Alta de Curso | Docente | — | 4.1, Fase 2 |
| 4.4 | ✅ | Alta de Edición | Docente | — | 4.3 |
| 4.5 | ✅ | Inscripción a Edición **(con Ajax)** | Estudiante | — | 4.2 |
| 4.6 | ✅ | Seguir Usuario **(con Ajax)** | Est./Doc. | — | Fase 3 |
| 4.7 | ✅ | Seleccionar Estudiantes | Docente | Figura 5 | 4.5 |
| 4.8 | ✅ | Listar Aceptados | Docente | — | 4.7 |
| 4.9 | ✅ | *(opcional)* Listar resultados de inscripciones | Estudiante | — | 4.7 |
| 4.10 | ✅ | *(opcional)* Dejar de Seguir **(con Ajax)** | Est./Doc. | — | 4.6 |
| 4.11 | ✅ | *(opcional)* Modificar Datos de Usuario | Est./Doc. | — | Fase 2 |

Ciclo de cada caso de uso:
1. Leer el caso de uso y su figura en la letra; anotar reglas y mensajes de error.
2. Central: método del controller con DataTypes completos + `ReglaNegocioException` + **tests del controller**.
3. Web: servlet fino + JSP según la figura + permiso en la matriz.
4. Prueba manual con cada rol.
5. Revisar la cobertura (`mvn verify`); no debe bajar.
6. Merge.

### Acceso rápido del docente: "Mis cursos y ediciones" ✅ (17/10)

Pedido del usuario: llegar a los cursos y ediciones propios sin pasar por el perfil. No es un caso de uso de la letra,
es navegación. Lateral **Docencia → Mis cursos y ediciones** (`/docente/cursos`, solo docentes): los cursos que dicta
(con "+ Crear edición") y las ediciones en las que participa, de la más reciente a la más vieja, con "Vigente" y
"Seleccionar estudiantes" en la vigente y "Ver aceptados" en todas. Central: `misCursosDocente` en una
llamada, con tests.

### Ajax (pedido del docente, por rendimiento)

El docente pide usar Ajax para no recargar páginas enteras. Decidido el 17/10 dónde va:

| Dónde | Qué hace sin recargar | Estado |
|---|---|---|
| 4.6 / 4.10 Seguir y dejar de seguir | El botón del perfil cambia y se actualiza el contador de seguidores | ✅ |
| Alta de Usuario (registro) | Al salir del campo nickname o correo, avisa si ya está en uso | ✅ |
| 4.5 Inscripción a edición | "Inscribirme" registra la inscripción y el botón pasa a "✓ Inscripto" | ✅ |

Patrón común a los tres: servlet que responde **JSON** (`{"ok": true, ...}` o `{"ok": false, "error": "..."}` con el
mensaje de la `ReglaNegocioException`), JavaScript propio con `fetch` (sin librerías), botón deshabilitado mientras
espera, y en las rutas Ajax el `AccesoFilter` contesta **401/403 JSON** en vez de redirigir. Se prueba con la
pestaña Red del navegador: una petición por acción y la página no se recarga.

**Registro, nickname/correo en uso:** `GET /registro/disponible?nickname=X` (o `?mail=X`) → `{"disponible": false,
"mensaje": "El nickname ya está en uso."}`. Lo llama `registro.js` al salir del campo (`blur`) y marca el campo en
rojo con ese mensaje, igual que la validación de Bootstrap. Es solo una ayuda: el alta sigue validando en el
central al enviar. Central: `nicknameDisponible` / `mailDisponible` con tests.

#### Seguimiento entre usuarios (4.6 y 4.10)

Seguir / dejar de seguir se hace **con Ajax**, sin recargar la página del perfil.

- **Servlet** `SeguimientoServlet` en `/usuarios/seguimiento`: recibe por **POST** `nick` y `accion` (`seguir` / `dejar`)
  y responde **JSON**, no una página:
  `{"ok": true, "siguiendo": true, "seguidores": 5}` o `{"ok": false, "error": "No es posible seguirse a uno mismo."}`.
  Sin sesión → `401` con `{"ok": false, "error": "..."}` (el `AccesoFilter` hoy redirige al login; para esta ruta
  tiene que contestar 401, porque un `fetch` no sigue la redirección como un navegador).
- **JavaScript** `js/seguimiento.js` (sin librerías, con `fetch`): al hacer clic en el botón del perfil manda el POST,
  y con la respuesta cambia el botón (Seguir ↔ Dejar de seguir), actualiza el contador de seguidores y la lista,
  y muestra el error si lo hay. Mientras espera, el botón queda deshabilitado para evitar doble clic.
- **JSP del perfil:** el botón lleva `data-nick` y el estado inicial (`data-siguiendo`); ya no es un `<form>` que recarga.
- **Central:** `seguirUsuario` y `dejarDeSeguirUsuario` devuelven el nuevo total de seguidores (o se agrega
  `contarSeguidores`), con sus tests (seguirse a uno mismo, seguir dos veces, dejar de seguir a quien no se sigue,
  usuario inexistente).
- **Prueba manual:** con las herramientas de desarrollo del navegador (pestaña Red), verificar que el clic manda
  una sola petición y que **la página no se recarga**.

Ya se sabe que hay que corregir:
- **Edición vigente:** si hay dos vigentes del mismo curso hoy se toma la primera al azar. *Decidido (D1): el Alta de
  Edición no permite fechas superpuestas con otra edición del mismo curso, así hay como mucho una vigente (4.4).*

---

## 8. Fuera de alcance

- Categorías, Programas de Formación en la web y Ranking (en gris para el grupo).
- Tests del módulo web o de interfaz (la letra pide tests solo del Servidor Central).
- Rediseños que no estén en las figuras de la letra.

---

## 9. Riesgos

| Riesgo | Mitigación |
|---|---|
| Borrar casos de uso deja mínimos sin hacer al 18/10 | El código borrado queda en el historial de git (rama `quitar-categoria`, commit `0621404`); si no llega a rehacerse, se puede recuperar el viejo. Hacer 4.1–4.8 antes que cualquier opcional. |
| Cambiar firmas del controller rompe Swing | Mantener los métodos que usa Swing (`@Deprecated`) hasta adaptarlo. |
| No llegar al 80% de cobertura | Tests del controller en cada caso de uso + JaCoCo `check` desde la Fase 0 para ver el avance cada día. |
| Datos de prueba dependen de la fecha | Revisar las ediciones vigentes antes de probar. |
