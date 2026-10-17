# Datos de prueba del Sprint 2 (Anexo "Datos de Pruebas – Tarea 2")

Lectura del PDF `ProgApp2026-DatosPrueba-AnexoSp2.pdf` (5 páginas) para confirmar que se leyó bien antes de
aplicarlo a la base. El anexo nombra cursos, ediciones y programas solo por referencia (C1…, E1…, P1…); la
correspondencia con los nombres reales es la del script `db/init/03-datos-sprint2.sql` (datos del Sprint 1).

Referencias de usuarios: EL eleven11 · CO costas · RO roro · CH chechi · JW jeffw · WE weiss (estudiantes) ·
WW heisenberg · OK benkenobi · EW waston · GH house · TC timmy · DR danny · PS phils · BS bruces · AG adri (docentes).

## 1. Usuarios: contraseña e imagen

Todas las contraseñas son `1234` (el anexo aclara que es una sugerencia). Las imágenes se guardan en una carpeta
local y en la base va la ruta relativa (Obs2 del anexo), no la URL.

| Ref | Nickname | Contraseña | Imagen en el anexo | Ruta en la BD |
|---|---|---|---|---|
| EL | eleven11 | 1234 | — | — |
| CO | costas | 1234 | — | — |
| RO | roro | 1234 | sí | `usuarios/roro.jpg` |
| CH | chechi | 1234 | sí | `usuarios/chechi.jpg` |
| JW | jeffw | 1234 | sí | `usuarios/jeffw.jpg` |
| WE | weiss | 1234 | — | — |
| WW | heisenberg | 1234 | sí (Walter White) | `usuarios/heisenberg.png` |
| OK | benkenobi | 1234 | sí (Obi-Wan) | `usuarios/benkenobi.jpg` |
| EW | waston | 1234 | sí | `usuarios/waston.jpg` |
| GH | house | 1234 | sí | `usuarios/house.jpg` |
| TC | timmy | 1234 | sí | `usuarios/timmy.jpg` |
| DR | danny | 1234 | — | — |
| PS | phils | 1234 | — | — |
| BS | bruces | 1234 | — | — |
| AG | adri | 1234 | — | — |

## 2. Cursos: imagen

| Ref | Curso | Imagen en el anexo | Ruta en la BD |
|---|---|---|---|
| C1 | Talleres plenarios | — | — |
| C2 | Seminarios de Resolución de Problemas | — | — |
| C3 | Dalavuelta | sí | `cursos/Dalavuelta.png` |
| C4 | Extensionismo Industrial | — | — |
| C5 | Inclusión Energética | sí | `cursos/Inclusion_Energetica.jpg` |
| C6 | Flor del Ceibo | sí | `cursos/Flor_del_Ceibo.png` |
| C7 | Taller de robótica educativa. | sí (Butiá) | `cursos/Taller_de_robotica_educativa.jpg` |
| C8 | Participación en investigación sobre el empleo del juego Komikan… | sí | `cursos/Komikan.png` |
| C9 | Herramientas de apoyo a la enseñanza de inglés… | — | — |
| C10 | MicroBit | sí | `cursos/MicroBit.jpg` |

## 3. Ediciones: imagen

| Ref | Edición | Imagen en el anexo | Ruta en la BD |
|---|---|---|---|
| E1 | Flor del Ceibo - 2010 | sí | `ediciones/Flor_del_Ceibo_2010.jpg` |
| E2 | Flor del Ceibo - 2012 | sí | `ediciones/Flor_del_Ceibo_2012.jpg` |
| E3 | Flor del Ceibo - 2025 | sí | `ediciones/Flor_del_Ceibo_2025.jpg` |
| E4 | Dalavuelta - 2025 | sí | `ediciones/Dalavuelta_2025.jpg` |
| E5 | Extensionismo Industrial - 2025 | — | — |
| E6 | Inclusión Energética - 2026 | — | — |
| E7 | Taller de robótica educativa - 2024 | sí | `ediciones/Taller_de_robotica_2024.jpg` |
| E8 | Taller de robótica educativa - 2026 | sí | `ediciones/Taller_de_robotica_2026.jpg` |
| E9 | Taller de robótica educativa-2026-2 | sí | `ediciones/Taller_de_robotica_2026-2.jpg` |
| E10 | Participación … Komikan … - 2026 | — | — |
| E11 | Herramientas de apoyo … - 26 | — | — |
| E12 | MicroBit-2026 | — | — |
| E13 | Talleres plenarios - 2026 | — | — |
| E14 | Seminarios de Resolución de Problemas - 2026 | — | — |

## 4. Seguidores (fila = quien sigue)

En la matriz del anexo, `X` = sigue y `O` = la diagonal (uno mismo).

| Quien sigue | Sigue a |
|---|---|
| eleven11 (EL) | roro, heisenberg, waston |
| costas (CO) | chechi, jeffw, benkenobi, danny, phils |
| roro (RO) | costas, weiss |
| chechi (CH) | eleven11, jeffw, house |
| jeffw (JW) | eleven11 |
| weiss (WE) | costas, heisenberg, timmy |
| heisenberg (WW) | roro |
| benkenobi (OK) | danny |
| waston (EW) | roro, weiss, heisenberg |
| house (GH) | jeffw, weiss, benkenobi |
| timmy (TC) | eleven11, chechi, jeffw |
| danny (DR) | costas, weiss, heisenberg |
| phils (PS) | bruces |
| bruces (BS) | phils |
| adri (AG) | heisenberg |

Total: 34 seguimientos.

## 5. Inscripciones a programas de formación

| Estudiante | Programa | Fecha |
|---|---|---|
| eleven11 (EL) | P1 EFI Ingeniería Mecánica | 15/04/26 |
| chechi (CH) | P1 EFI Ingeniería Mecánica | 20/04/26 |
| costas (CO) | P2 Formación integral | 10/07/26 |
| chechi (CH) | P2 Formación integral | 10/07/26 |
| eleven11 (EL) | P2 Formación integral | 12/07/26 |
| weiss (WE) | P2 Formación integral | 15/07/26 |
| roro (RO) | P2 Formación integral | 18/07/26 |
| costas (CO) | P3 EFI Robótica | 11/08/26 |
| chechi (CH) | P3 EFI Robótica | 15/08/26 |
| roro (RO) | P3 EFI Robótica | 22/08/26 |
| jeffw (JW) | P3 EFI Robótica | 30/08/26 |

## 6. Inscripciones a ediciones: estados

| Edición | Estudiante | Estado |
|---|---|---|
| E1 | eleven11 | Aceptada |
| E1 | chechi | Aceptada |
| E2 | costas | Aceptada |
| E2 | roro | Rechazada |
| E2 | weiss | Aceptada |
| E3 | roro | Aceptada |
| E3 | jeffw | Aceptada |
| E4 | chechi | Aceptada |
| E4 | eleven11 | Rechazada |
| E4 | roro | Aceptada |
| E4 | costas | Aceptada |
| E4 | jeffw | Rechazada |
| E5 | costas | Aceptada |
| E5 | chechi | Aceptada |
| E5 | eleven11 | Aceptada |
| E5 | weiss | Aceptada |
| E6 | roro | Aceptada |
| E6 | weiss | Rechazada |
| E6 | chechi | Aceptada |
| E6 | eleven11 | Rechazada |
| E7 | weiss | Rechazada |
| E7 | roro | Rechazada |
| E7 | eleven11 | Aceptada |
| E8 | jeffw | Aceptada |
| E8 | costas | Aceptada |
| E8 | roro | Rechazada |
| E9 | weiss | Inscripto |
| E9 | chechi | Inscripto |
| E9 | roro | Inscripto |
| E10 | chechi | Aceptada |
| E10 | weiss | Aceptada |
| E10 | roro | Aceptada |
| E11 | weiss | Inscripto |
| E11 | eleven11 | Inscripto |
| E11 | jeffw | Inscripto |
| E12 | chechi | Inscripto |
| E12 | roro | Inscripto |
| E12 | eleven11 | Inscripto |
| E12 | jeffw | Inscripto |
| E13 | costas | Rechazada |
| E13 | weiss | Aceptada |
| E13 | roro | Aceptada |
| E14 | weiss | Aceptada |
| E14 | costas | Rechazada |
| E14 | roro | Aceptada |
| E14 | chechi | Rechazada |

## 7. Lo que no se carga (en gris para el grupo)

- **Categorías** (T1 Social, T2 Industrial, T3 Educativo, T4 Interdisciplinario) y **Categorías – Cursos**
  (T1: C3, C7, C9 · T2: C4, C5, C10 · T3: C6, C7, C8, C9, C10 · T4: C6): Alta de Categoría y todo lo de
  categorías no corresponde al grupo; el modelo ya no tiene categorías.
- **Imágenes de programas** (P1, P2, P3): los casos de uso de Programas de Formación están en gris. Las
  inscripciones a programas sí se cargan porque las muestra la Consulta de Usuario.

## 8. Aplicación a la base (18/10)

**Antes de aplicar**, la base coincidía con el anexo salvo:

- **E12 MicroBit-2026:** chechi, eleven11 y roro estaban en Aceptada (por pruebas de Seleccionar Estudiantes);
  el anexo dice Inscripto para los cuatro.
- **Datos de pruebas que no están en el anexo:** usuarios codu, pepito29 y manuppz; cursos "coso" y
  "League of Legends de Bronce a Diamante" (con su edición "2026", sus docentes y la previa MicroBit);
  inscripciones de codu a "2026", "Herramientas … - 26" y "MicroBit-2026"; seguimientos codu→adri,
  codu→pepito29 y pepito29→codu; manuppz en el instituto DISI.

**Qué se hizo** (confirmado por el usuario, en una sola transacción): E12 volvió a Inscripto para chechi,
eleven11 y roro, y se borraron todos los datos de pruebas de la lista anterior junto con sus fotos de la carpeta
de imágenes (`~/edext/imagenes/usuarios/`). Antes se tomó un respaldo completo con `mysqldump`.

**Verificación después de aplicar** (comparación automática de cada tabla contra el anexo):

| Qué | Resultado |
|---|---|
| 15 usuarios y sus contraseñas | ✅ idéntico |
| Imágenes de usuarios (8) | ✅ idéntico |
| 10 cursos y sus imágenes | ✅ idéntico |
| 14 ediciones y sus imágenes | ✅ idéntico |
| 34 seguimientos | ✅ idéntico |
| 11 inscripciones a programas | ✅ idéntico |
| 46 inscripciones a ediciones con su estado | ✅ idéntico |

La base quedó **exactamente** como el anexo, sin datos de más. El script `db/init/03-datos-sprint2.sql` ya
cargaba lo mismo (E12 incluido), así que no hubo que cambiarlo.

Relacionado: [[PLAN-REINICIO-WEB]] · [[casos-de-uso-fase4]]
