```
[ Controller ]      <-- 1. Recibe los datos de Swing (Presentacion)
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