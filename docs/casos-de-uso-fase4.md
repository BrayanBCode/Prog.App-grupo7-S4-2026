# Fase 4 – Fichas de los casos de uso (4.1 a 4.8)

Paso 1 del ciclo de la Fase 4 (ver `PLAN-REINICIO-WEB.md`, sección 7): qué pide la letra, qué reglas y
mensajes de error tiene, qué ofrece hoy el Servidor Central y qué falta. Fuente: *ProgApp2026-Tarea2.pdf*,
sección 6.2 (casos de uso) y 7.1 (diseño); figuras en `docs/figuras/`.

## Reglas comunes a todas las fichas

- **Sin categorías:** donde la letra dice "el usuario indica el instituto **o categoría**", el grupo solo
  implementa **instituto** (categorías están en gris para el grupo).
- **Elegir curso:** varios casos de uso empiezan igual: *instituto → lista de cursos → elige uno*. En el web
  eso es el **lateral** (Institutos) → página de cursos del instituto (4.1). Desde ahí se navega a cada caso de
  uso; no se repite el "elegir instituto" en cada pantalla.
- **Errores:** el central tira `ReglaNegocioException` con el mensaje para el usuario; el servlet muestra ese
  mensaje con `mensajeDeError(e)` (cualquier otro error → mensaje genérico + log). Los mensajes de abajo son
  los que ya existen en el central y se mantienen.
- **Una llamada por vista:** cada pantalla pide al controller un DataType completo (regla 2 del plan). Los
  métodos "Hoy" que devuelven `List<String>` y obligan a una llamada por elemento se reemplazan para el web
  (los de Swing quedan, `@Deprecated` si hace falta).
- **Tests:** cada método nuevo del controller/servicio lleva su test sin base (como los de `services/`):
  caso feliz + cada error de la ficha.

---

## 4.1 Consulta de Curso — Visitante, Estudiante, Docente — Figura 3

**Letra:** el usuario indica el instituto y el sistema lista sus cursos. Elige uno y ve todos los datos del
curso, con sus **ediciones** y **programas de formación** asociados y su imagen. Desde ahí puede abrir una
edición (4.2) o un programa.

**Pantallas** (Figura 3): `GET /cursos?instituto=X` (lista) y `GET /cursos?nombre=Y` (detalle). Detalle:
imagen + nombre + instituto + URL + descripción a la izquierda; a la derecha "Ediciones del curso" (miniatura
+ nombre, link a 4.2) y "Programas de formación" (solo texto: Consulta de Programa no es del grupo).

**Reglas / errores:**
- Instituto inexistente → "El instituto seleccionado no existe."
- Curso inexistente → "No se encontró el curso llamado: 'X'" (hoy). 404 + volver a la lista.

**Central hoy:** `listarCursosPorInstituto` → `List<String>`; `listarCursosTabla` → `DTCursoResumen(nombre,
descripcion)` (sin imagen); `obtenerDataCurso` → `DTCurso` (sin ediciones ni programas);
`listarEdicionesCurso` → `List<String>`; `listarProgramasPorCurso` → `List<String>`.

**Falta:**
- `listarCursosInstituto(instituto)` → `List<DTCursoResumen>` con **imagen** (agregar campo).
- `consultarCurso(nombre)` → `DTCursoDetalle(DTCurso curso, List<DTEdicionCurso> ediciones, List<String> programas)`
  en una sola llamada.
- Lateral: los institutos pasan a ser links a `/cursos?instituto=X` (hoy son texto, `TODO 4.1`).
- Perfil: los nombres de curso pasan a ser links (hoy texto, `TODO 4.1/4.2`).

**Tests:** curso con ediciones y programas; curso sin ediciones; curso inexistente; instituto inexistente;
instituto sin cursos (lista vacía).

---

## 4.2 Consulta de Edición de Curso — Visitante, Estudiante, Docente — Figura 4

**Letra:** instituto → cursos → elige curso → lista de ediciones → elige una → todos los datos de la edición,
**docentes asignados** e imagen.

**Pantalla** (Figura 4): `GET /ediciones?nombre=E`. Imagen + nombre + instituto; links **Inscribirme** (solo si
`puede.INSCRIPCION_EDICION_CURSO` y la edición es la vigente) y **Ver información del Curso** (4.1); fecha
inicio, fecha fin, cupo, fecha de publicación; lista de docentes (nombre + link al perfil).

**Reglas / errores:** edición inexistente → "No se encontró la edición llamada: 'X'" (hoy). Cupo 0 = "sin
límite" (así quedó en el alta): se muestra "Sin límite".

**Central hoy:** `obtenerEdicionCurso` → `DTEdicionCurso` (sin docentes ni instituto);
`listarDocentesEdicion` → `List<DTDocenteResumen>` (segunda llamada).

**Falta:** `consultarEdicion(nombre)` → `DTEdicionDetalle(DTEdicionCurso edicion, String instituto,
List<DTDocenteResumen> docentes, boolean vigente)` en una llamada.

**Tests:** edición con docentes; edición inexistente; `vigente` true/false según fechas.

---

## 4.3 Alta de Curso — Docente — sin figura

**Letra:** el docente indica instituto y datos básicos: **nombre (único)**, descripción, duración, cantidad de
horas, créditos, URL; **previas** (ninguna, uno o más cursos); (categorías: no aplica). El sistema da de alta
el curso en la **fecha actual**. Si el nombre existe, el docente corrige o cancela.

**Pantalla:** `GET/POST /curso/alta` (formulario + imagen opcional). El docente del curso es **el usuario
logueado** (no se elige). Éxito → PRG a la consulta del curso nuevo (4.1) con flash.

**Reglas / errores:**
- Nombre repetido → "Ya existe un curso registrado con el nombre: X" (el formulario conserva lo ingresado).
- Instituto inexistente → "El instituto seleccionado no existe."
- Validaciones de vista: campos obligatorios, números > 0, URL con formato.
- Imagen: las de `AlmacenImagenes` (JPG/PNG, 2 MB).

**Central hoy:** `altaCursoConImagen(..., String imagen)` recibe **ruta** (falta la versión con `DTImagen`).
`listarCursosPorInstituto` para elegir previas.

**Falta:** `altaCurso(..., List<String> previas, DTImagen imagen)` que tire `ReglaNegocioException`; previas
inexistentes → error.

**Tests:** alta feliz (fecha de registro = hoy, docente = el logueado); nombre repetido; instituto inexistente;
previa inexistente; con y sin imagen.

---

## 4.4 Alta de Edición de Curso — Docente — sin figura

**Letra:** instituto → cursos → elige uno → datos: **nombre de la edición (único)**, fecha inicio y fin,
**cupo (opcional)**, **docentes** que participan. Se da de alta en la fecha actual. Nombre repetido → corrige o
cancela.

**Pantalla:** `GET/POST /edicion/alta?curso=X` (se llega desde la consulta del curso, 4.1). Docentes: lista con
checkboxes de los docentes del instituto; el logueado marcado por defecto.

**Reglas / errores (hoy):**
- "Ya existe una edición de curso registrada con el nombre: X"
- "El curso seleccionado no existe."
- "La fecha de fin no puede ser anterior a la fecha de inicio."
- "Debe seleccionar al menos un docente para la edición."
- "El docente seleccionado no existe: X"
- Cupo vacío → 0 = sin límite.

**Central hoy:** `altaEdicionCurso(..., String imagen)` con ruta; `listarDocentesPorInstituto`.

**Falta:** versión con `DTImagen` + `ReglaNegocioException`. Decidir si el docente que da el alta **debe** estar
entre los docentes de la edición (la letra no lo dice; propuesta: no obligar).

**Tests:** alta feliz; nombre repetido; curso inexistente; fin < inicio; sin docentes; docente inexistente;
cupo vacío = 0.

---

## 4.5 Inscripción a Edición de Curso — Estudiante — sin figura

**Letra:** instituto → cursos → elige uno → el sistema muestra los datos básicos de la **edición vigente** del
curso, si existe → registra la inscripción en la **fecha actual**, en estado **Inscripto**. Si ya está
inscripto, corrige o cancela.

**Pantalla:** botón **Inscribirme** en la consulta de la edición vigente (Figura 4) → `POST /edicion/inscripcion`
→ PRG a la edición con flash. Si el curso no tiene edición vigente, la consulta del curso lo dice y no hay botón.

**Reglas / errores (hoy):**
- "El estudiante ya está inscripto en esta edición del curso."
- "No hay cupos disponibles para esta edición." (cupo > 0 y lleno)
- "La edición de curso seleccionada no existe."
- Nuevo: inscribirse a una edición **que no es la vigente** → error (el web solo ofrece la vigente, pero el
  central lo tiene que controlar porque el POST se puede armar a mano).

**Central hoy:** `obtenerEdicionVigente(curso)` → `String` (**primera al azar si hay dos vigentes**);
`inscribirEstudianteEdicion(nick, mail, edicion, fecha)` pide el mail además del nick.

**Falta:**
- **Criterio de edición vigente** (pendiente del plan). Propuesta: vigente = `fechaInicio <= hoy <= fechaFin`;
  si hay varias, la de **fechaInicio más reciente** (desempate por nombre). Con tests.
- `inscribirse(nickEstudiante, nombreEdicion)` (fecha = hoy, por nickname, valida que sea la vigente).

**Tests:** inscripción feliz (estado Inscripto, fecha hoy); doble inscripción; sin cupo; cupo 0 sin límite;
edición no vigente; dos vigentes → elige la más reciente; curso sin vigente → null.

---

## 4.6 Seguir a un Usuario (con Ajax) — Estudiante, Docente — sin figura

**Letra:** el usuario indica a quién quiere seguir y el sistema lo registra.

**Pantalla:** botón **Seguir** en el perfil (Fase 3), sin recargar la página: `POST /usuarios/seguimiento`
responde JSON (ver el diseño completo en el plan, "Seguimiento entre usuarios con Ajax").

**Reglas / errores (hoy):**
- "No es posible seguirse a uno mismo."
- "El usuario X no existe."
- "Ya está siguiendo a X."
- Sin sesión → 401 JSON (el `AccesoFilter` hoy redirige; para esta ruta tiene que contestar 401).

**Central hoy:** `seguirUsuario`, `estaSiguiendo`, `listarSeguidores`, `listarSeguidos` (con tests).

**Falta:** que `seguirUsuario` devuelva el nuevo total de seguidores; `ReglaNegocioException`.
**Depende de:** Fase 3 (perfil).

**Tests:** los que ya existen + devuelve el total actualizado.

---

## 4.7 Seleccionar Estudiantes para una Edición — Docente — Figura 5

**Letra:** instituto → cursos → elige uno → datos básicos de la **edición vigente** → todas las inscripciones con
su estado → el docente marca **Aceptada** o **Rechazada** en cada una → el sistema registra la selección.
(El orden por ranking es "Ranking", en gris: no va.)

**Pantalla** (Figura 5): `GET/POST /edicion/seleccionar?edicion=E`, se llega desde la consulta de la edición si
el docente participa. Encabezado de la edición (imagen, nombre, fechas) y tabla: estudiante, fecha de
inscripción, estado, y para las que están en Inscripto un selector Aceptar/Rechazar. Botón **Guardar
selección**. PRG con flash.

**Reglas / errores (hoy):**
- "El docente no participa en esta edición de curso."
- "La edición de curso seleccionada no existe."
- "No hay ninguna selección para guardar."
- "El estudiante X no está inscripto en esta edición."
- "El estado debe ser Aceptada o Rechazada."
- "La inscripción de X ya fue resuelta (Aceptada)." — solo se pasa desde Inscripto (Tabla 1). Todo o nada.

**Central hoy:** `listarInscripcionesEdicion(docente, edicion)` → `List<DTInscripcionEdicion>`;
`seleccionarEstudiantes(docente, edicion, Map<nick, estado>)` (con 12 tests).

**Falta:** encabezado de la edición en la misma llamada (`DTEdicionDetalle` + inscripciones); que la edición
sea la **vigente** (la letra lo dice; definir si se permite también sobre no vigentes — propuesta: solo vigente,
igual que la inscripción).

**Tests:** los existentes + edición no vigente.

---

## 4.8 Listar Aceptados a una Edición — Docente — sin figura

**Letra:** instituto → cursos → elige uno → datos básicos de **las ediciones** del curso → elige una → lista de
inscripciones en estado **Aceptada**. (A diferencia de 4.7, no se limita a la vigente.)

**Pantalla:** `GET /edicion/aceptados?edicion=E`, se llega desde la consulta de la edición (si el docente
participa) y desde **"Ver aceptados"** en el perfil propio del docente (`TODO 4.8` en `perfil.jsp`). Tabla:
estudiante (link al perfil), nombre, fecha de inscripción.

**Reglas / errores (hoy):** "El docente no participa en esta edición de curso."; edición inexistente.

**Central hoy:** `listarAceptadosEdicion(docente, edicion)` (con tests).

**Falta:** encabezado de la edición en la misma llamada.

**Tests:** los existentes + edición sin aceptados (lista vacía).

---

## Decisiones pendientes

| # | Pregunta | Propuesta |
|---|---|---|
| D1 | Si hay dos ediciones vigentes del mismo curso, ¿cuál es "la vigente"? | La de `fechaInicio` más reciente; desempate por nombre. |
| D2 | ¿Seleccionar estudiantes solo sobre la edición vigente? | Sí, como dice la letra. |
| D3 | ¿El docente que da de alta una edición debe estar entre sus docentes? | No obligar; marcarlo por defecto. |
| D4 | ¿El alta de curso deja elegir el docente? | No: es el docente logueado. |
