# Swing: menú por tema y textos unificados

**Objetivo:** que la Estación de Trabajo sea más fácil de usar. Hoy el menú está ordenado por tipo de acción
(Registros / Consulta / Modificaciones): para registrar una edición y después consultarla hay que ir a dos menús
distintos. Además, los nombres son disparejos ("Registro EdicionCurso", "CONSULTA DE USUARIO", "Es docente?:").

**Decidido (17/10):** el menú queda **por tema**, como el lateral del sitio web: cada menú es una cosa y adentro están
todas sus acciones.

> Las pantallas se editan con el **GUI Builder de NetBeans**. El código generado (`initComponents`,
> `//GEN-BEGIN/END`, declaración de variables) y los `.form` **no se tocan a mano**. Por eso casi todo lo de esta guía se
> hace en el diseñador. Lo único que se cambió en el código es `JIModificarUsuario`, que no tiene bloques generados
> (ver al final).

---

## 1. Menú principal (`JFInicio`)

### Cómo queda

| Menú | Ítem (texto nuevo) | Variable actual | Texto actual | Abre |
|---|---|---|---|---|
| **Inicio** | — | `jMenu1` | Inicio | cierra la pantalla abierta |
| **Usuarios** | Registrar usuario | `MIRegistoCli` | Registro Usuario | `JIRegistrarUsuario` |
| | Consultar usuario | `MIConUsuario` | Consulta Usuario | `JIConsultaUsuario` |
| | Modificar datos de usuario | `jMenuItem3` | Modificar Usuario | `JIModificarUsuario` |
| **Institutos** | Registrar instituto | `MIRegistroInsti` | Registro Instituto | `JIAltaInstituto` |
| **Cursos** | Registrar curso | `MIRegistroCur` | Registro de Curso | `JIAltaCurso` |
| | Consultar curso | `MIConCurso` | Consulta Curso | `JIConsultaCurso` |
| **Ediciones** | Registrar edición | `MIRegistroEdi` | Registro EdicionCurso | `JIRegistroEdicionCurso` |
| | Consultar edición | `MIConEdi` | Consulta Edicion Curso | `JIConsultaEdicionCurso` |
| | Inscribir estudiante | `MIinscribcionEdi` | Inscribir a EdicionCurso | `JIinscripcionEdicionCurso` |
| **Programas** | Crear programa | `jMenuItem1` | Registro Programa | `JICrearProgFormacion` |
| | Consultar programa | `jMenuItem2` | Consulta Programa | `JIConsultaProgFormacion` |
| | Modificar programa | `jMenuItem4` | Modificar Programa | `JIModificarPrograma` |
| | Agregar curso a programa | `jMenuItem5` | Agregar curso a Programa | `JIAgregarCursoPrograma` |

Orden en cada menú: **registrar → consultar → modificar → acciones extra**. El orden de los menús sigue las
dependencias (no hay curso sin instituto ni edición sin curso).

### Pasos en NetBeans

1. Abrir `JFInicio.java` → pestaña **Design**. Trabajar desde el **Navigator** (abajo a la izquierda), que muestra
   `jMenuBar1` con sus menús e ítems.
2. **Reusar los menús que ya están** (clic derecho → *Edit Text*, o la propiedad `text`):
   - `jMenu2` "Registros" → **Usuarios**
   - `jMenu3` "Consulta" → **Cursos**
   - `jMenu4` "Modificaciones" → **Programas**
3. **Agregar dos menús:** clic derecho en `jMenuBar1` → *Add Menu* → texto **Institutos**. Repetir para **Ediciones**.
4. **Ordenar los menús** arrastrándolos en el Navigator: Inicio, Usuarios, Institutos, Cursos, Ediciones, Programas.
5. **Mover cada ítem** a su menú arrastrándolo en el Navigator (tabla de arriba). El ítem se lleva su
   `ActionListener`, así que cada opción sigue abriendo la misma pantalla.
6. **Cambiar el texto** de cada ítem por el de la columna "Ítem (texto nuevo)".
7. **Borrar `MIRegistroCategoria`** ("Registro Categoría"; Categorías no corresponde al grupo): clic derecho → *Delete*.
   NetBeans borra también su método vacío `MIRegistroCategoriaActionPerformed`.
   **Después** borrar a mano estas líneas del constructor (no son código generado), si no, no compila:
   ```java
   // Alta de Categoria no corresponde al grupo (requerimiento en gris).
   // TODO: borrar el item MIRegistroCategoria desde el diseñador de NetBeans.
   MIRegistroCategoria.setVisible(false);
   ```
8. *(Opcional, recomendado)* Renombrar las variables (clic derecho → *Change Variable Name*) para que se entienda el
   código: `jMenu2` → `mnuUsuarios`, `jMenuItem3` → `miModificarUsuario`, `jMenuItem1` → `miCrearPrograma`, etc.
9. Guardar, *Clean and Build* y probar que cada opción abre su pantalla.

---

## 2. Textos de las pantallas

Criterios, para que todas digan lo mismo:
- **Título** = el texto del menú que la abre, con mayúscula solo al principio ("Registrar curso", no "Alta Curso" ni
  "CONSULTA DE USUARIO").
- **Botones:** las altas usan **Aceptar / Cancelar**; las modificaciones, **Guardar cambios / Cancelar**; las
  consultas, **Cerrar**.
- **Etiquetas** con tilde y signos de pregunta completos ("Correo electrónico:", "¿Es docente?").

En el diseñador: seleccionar el componente y cambiar la propiedad `text` (o doble clic sobre el texto).

| Pantalla | Texto actual | Texto nuevo |
|---|---|---|
| `JIRegistrarUsuario` | Registrar Usuario | Registrar usuario |
| | Correo Electronico: | Correo electrónico: |
| | Es docente?: | ¿Es docente? |
| | Instituto perteneciente: | Instituto: |
| | Confirmación contraseña: | Confirmar contraseña: |
| `JIConsultaUsuario` | CONSULTA DE USUARIO | Consultar usuario |
| `JIAltaCurso` | Alta Curso | Registrar curso |
| | Duracion: | Duración: |
| | Cant. horas: | Horas: |
| | Cant. creditos: | Créditos: |
| `JIConsultaCurso` | Consulta de Curso | Consultar curso |
| `JIRegistroEdicionCurso` | Registrar Edicion de Curso | Registrar edición |
| | Asig. Docentes | Asignar docentes |
| `JIConsultaEdicionCurso` | Consultar Edicion de Curso | Consultar edición |
| `JIinscripcionEdicionCurso` | Inscribir a Edicion de Curso | Inscribir estudiante a una edición |
| | Selec. Estudiante | Seleccionar estudiante |

### Pantallas con código generado pero **sin** `.form`

`JIAltaInstituto` y las cuatro de `programaFormacion/` tienen bloques `//GEN` pero **no tienen su `.form`** (no
está en el repo ni en los `.zip` de la carpeta). NetBeans no las puede abrir en el diseñador, así que sus textos
solo se pueden cambiar a mano. **Decidido (17/10): se dejan como están.** Si más adelante se recuperan sus `.form`,
estos serían los textos a cambiar:

| Pantalla | Texto actual | Texto nuevo |
|---|---|---|
| `JIAltaInstituto` | Alta de Instituto | Registrar instituto |
| `JICrearProgFormacion` | Crear Programa de Formación | Crear programa de formación |
| | Fecha Inicio: / Fecha Fin: | Fecha de inicio: / Fecha de fin: |
| `JIConsultaProgFormacion` | Consultar Programa de Formación | Consultar programa de formación |
| `JIModificarPrograma` | Modificar Programa de Formación | Modificar programa de formación |
| | Guardar Cambios | Guardar cambios |
| `JIAgregarCursoPrograma` | Agregar Curso a Programa de Formación | Agregar curso a programa |
| | Agregar / Cerrar | Aceptar / Cancelar |

---

## 3. Ya hecho en el código

- `JIModificarUsuario` (sin bloques generados): título "Modificar datos de usuario", "Correo electrónico:",
  botón "Guardar cambios".

## 4. Para después (no se hizo)

- Pantalla de bienvenida en **Inicio** con accesos directos (hoy el escritorio queda vacío).
- Atajos de teclado en los menús (`mnemonic`: Alt+U Usuarios, Alt+C Cursos…), desde las propiedades del diseñador.
- Borrar los `.zip` de `modules/swing/src/main/java/Swing/` (copias viejas de las pantallas, no se usan).
