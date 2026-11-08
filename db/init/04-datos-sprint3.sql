-- =====================================================================================
-- Datos de prueba del Sprint 3: notas (1 a 12) y cierre de ediciones.
-- Se aplica DESPUES de 03-datos-sprint2.sql y se puede volver a correr sin duplicar nada.
--
-- En una base ya creada antes de este cambio, las columnas NOTA y CERRADA las agrega EclipseLink
-- (create-or-extend-tables) al levantar el Servidor Central; recien despues se puede correr este archivo.
--
-- Escenario:
--   * Ediciones ya terminadas, con todos los aceptados calificados y CERRADAS (sirven para "Ver resultados"):
--     Flor del Ceibo - 2010, Flor del Ceibo - 2012, Dalavuelta - 2025, Taller de robótica educativa - 2024,
--     Extensionismo Industrial - 2025.
--   * Inclusión Energética - 2026: ABIERTA y con una sola nota (roro), para probar "Ingresar resultados"
--     y que no deja cerrar mientras falte chechi.
--   * El resto queda abierta y sin notas.
-- =====================================================================================

SET NAMES utf8mb4;

UPDATE INSCRIPCIONEDICION SET NOTA = NULL;
UPDATE EDICIONCURSO SET CERRADA = 0, FECHACIERRE = NULL;

-- Flor del Ceibo - 2010: eleven11, chechi
UPDATE INSCRIPCIONEDICION SET NOTA = 10 WHERE ID = 1;
UPDATE INSCRIPCIONEDICION SET NOTA = 7  WHERE ID = 2;
-- Flor del Ceibo - 2012: costas, weiss
UPDATE INSCRIPCIONEDICION SET NOTA = 12 WHERE ID = 3;
UPDATE INSCRIPCIONEDICION SET NOTA = 5  WHERE ID = 5;
-- Dalavuelta - 2025: chechi, roro, costas
UPDATE INSCRIPCIONEDICION SET NOTA = 9  WHERE ID = 8;
UPDATE INSCRIPCIONEDICION SET NOTA = 3  WHERE ID = 10;
UPDATE INSCRIPCIONEDICION SET NOTA = 8  WHERE ID = 11;
-- Taller de robótica educativa - 2024: eleven11
UPDATE INSCRIPCIONEDICION SET NOTA = 11 WHERE ID = 23;
-- Extensionismo Industrial - 2025: costas, chechi, eleven11, weiss
UPDATE INSCRIPCIONEDICION SET NOTA = 6  WHERE ID = 13;
UPDATE INSCRIPCIONEDICION SET NOTA = 10 WHERE ID = 14;
UPDATE INSCRIPCIONEDICION SET NOTA = 4  WHERE ID = 15;
UPDATE INSCRIPCIONEDICION SET NOTA = 9  WHERE ID = 16;
-- Inclusión Energética - 2026 (abierta): roro calificado, chechi pendiente
UPDATE INSCRIPCIONEDICION SET NOTA = 8  WHERE ID = 17;

-- Cerradas unos dias despues de terminar (FECHACIERRE = dia en que el docente cerro la edicion)
UPDATE EDICIONCURSO SET CERRADA = 1, FECHACIERRE = DATE_ADD(FECHAFIN, INTERVAL 7 DAY)
 WHERE NOMBRE IN ('Flor del Ceibo - 2010', 'Flor del Ceibo - 2012', 'Dalavuelta - 2025',
                  'Taller de robótica educativa - 2024', 'Extensionismo Industrial - 2025');

-- Al cerrar, las inscripciones que seguian pendientes quedan rechazadas
UPDATE INSCRIPCIONEDICION SET ESTADO = 'RECHAZADA'
 WHERE ESTADO = 'INSCRIPTO'
   AND EDICIONCURSO_NOMBRE IN (SELECT NOMBRE FROM EDICIONCURSO WHERE CERRADA = 1);
