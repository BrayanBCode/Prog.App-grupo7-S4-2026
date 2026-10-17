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
- [ ] Revisar `ControllerTest` (44 tests que usan la base real): no aparecieron en la última corrida de `mvn test`.
      Hacer que corran o pasarlos a tests sin base, como los de `services/`.
      *Causa: el archivo entero está comentado (`/* ... */`), por eso no corre.*
- [x] Agregar a JaCoCo la regla `check`: **mínimo 80% de sentencias en el paquete `Logica`**.
      *Decidido: modo aviso (`haltOnFailure=false`), sin `repositories` (necesita base) ni `programaFormacion`
      (no es del grupo). `mvn verify` muestra un WARNING con el % actual: **40% de líneas** al 15/10.*
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
      El cuadro de búsqueda de la figura no se dibuja (Búsqueda no es mínimo para el grupo). Cada caso de uso
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
| — | ✅ | Alta de Usuario (registro) | Visitante | — | Fase 2 |
| F3 | ✅ | Consulta de Usuario | Todos | Figura 6 | Fase 3 |
| 4.1 | ⬜ | Consulta de Curso | Todos | Figura 3 | Fase 1 |
| 4.2 | ⬜ | Consulta de Edición | Todos | Figura 4 | 4.1 |
| 4.3 | ⬜ | Alta de Curso | Docente | — | 4.1, Fase 2 |
| 4.4 | ⬜ | Alta de Edición | Docente | — | 4.3 |
| 4.5 | ⬜ | Inscripción a Edición | Estudiante | — | 4.2 |
| 4.6 | ⬜ | Seguir Usuario **(con Ajax)** | Est./Doc. | — | Fase 3 |
| 4.7 | ⬜ | Seleccionar Estudiantes | Docente | Figura 5 | 4.5 |
| 4.8 | ⬜ | Listar Aceptados | Docente | — | 4.7 |
| 4.9 | ⬜ | *(opcional)* Listar resultados de inscripciones | Estudiante | — | 4.7 |
| 4.10 | ⬜ | *(opcional)* Dejar de Seguir **(con Ajax)** | Est./Doc. | — | 4.6 |
| 4.11 | ⬜ | *(opcional)* Modificar Datos de Usuario | Est./Doc. | — | Fase 2 |

Ciclo de cada caso de uso:
1. Leer el caso de uso y su figura en la letra; anotar reglas y mensajes de error.
2. Central: método del controller con DataTypes completos + `ReglaNegocioException` + **tests del controller**.
3. Web: servlet fino + JSP según la figura + permiso en la matriz.
4. Prueba manual con cada rol.
5. Revisar la cobertura (`mvn verify`); no debe bajar.
6. Merge.

### Seguimiento entre usuarios con Ajax (4.6 y 4.10)

Pedido del curso: seguir / dejar de seguir se hace **con Ajax**, sin recargar la página del perfil.

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
