-- =====================================================================================
-- Datos de prueba del Sprint 3 (Anexo "Datos de Prueba - Tarea 3", ProgApp 2026):
-- evaluaciones (notas 1 a 12), cierre de ediciones y valoraciones de cursos.
-- Se aplica DESPUES de 03-datos-sprint2.sql y se puede volver a correr sin duplicar nada.
-- Las referencias C1..C10, E1..E14 y los estudiantes son las de 03-datos-sprint2.sql:
--   EL eleven11, CH chechi, CO costas, RO roro, WE weiss, JW jeffw.
--
-- En una base ya creada antes de este cambio, las columnas NOTA y CERRADA las agrega EclipseLink
-- (create-or-extend-tables) al levantar el Servidor Central; recien despues se puede correr este archivo.
--
-- Los estados (Aceptado / Rechazado / Inscripto) del anexo ya coinciden con los de 03-datos-sprint2.sql.
--
-- NO se cargan del anexo:
--   * Las inscripciones de OM, AK, NV, SG, AB, SO, SH y JS (en E3, E9 y E11): esos estudiantes no estan
--     en los datos de prueba del grupo.
--   * Las visitas de cada curso (Listado de tendencias) y los links de video de las inscripciones:
--     el modelo no tiene donde guardarlos.
--
-- Escenario:
--   * Toda edicion con notas en el anexo queda CERRADA (sirven para "Ver resultados", "Listar resultados"
--     y el acta): E1..E8, E10, E13 y E14.
--   * E9, E11 y E12 quedan abiertas: sus inscripciones siguen en Inscripto.
--   * El anexo pone nota 0 en E2 (weiss), E7 (eleven11) y E10 (los tres). Se cargan tal cual aunque
--     la pantalla del docente solo acepta 1 a 12.
-- =====================================================================================

SET NAMES utf8mb4;

UPDATE INSCRIPCIONEDICION SET NOTA = NULL;
UPDATE EDICIONCURSO SET CERRADA = 0, FECHACIERRE = NULL;

-- E1 Flor del Ceibo - 2010: eleven11, chechi
UPDATE INSCRIPCIONEDICION SET NOTA = 6  WHERE ID = 1;
UPDATE INSCRIPCIONEDICION SET NOTA = 8  WHERE ID = 2;
-- E2 Flor del Ceibo - 2012: costas, weiss
UPDATE INSCRIPCIONEDICION SET NOTA = 9  WHERE ID = 3;
UPDATE INSCRIPCIONEDICION SET NOTA = 0  WHERE ID = 5;
-- E3 Flor del Ceibo - 2025: roro, jeffw
UPDATE INSCRIPCIONEDICION SET NOTA = 9  WHERE ID = 6;
UPDATE INSCRIPCIONEDICION SET NOTA = 6  WHERE ID = 7;
-- E4 Dalavuelta - 2025: chechi, roro, costas
UPDATE INSCRIPCIONEDICION SET NOTA = 7  WHERE ID = 8;
UPDATE INSCRIPCIONEDICION SET NOTA = 7  WHERE ID = 10;
UPDATE INSCRIPCIONEDICION SET NOTA = 6  WHERE ID = 11;
-- E5 Extensionismo Industrial - 2025: costas, chechi, eleven11, weiss
UPDATE INSCRIPCIONEDICION SET NOTA = 9  WHERE ID = 13;
UPDATE INSCRIPCIONEDICION SET NOTA = 9  WHERE ID = 14;
UPDATE INSCRIPCIONEDICION SET NOTA = 1  WHERE ID = 15;
UPDATE INSCRIPCIONEDICION SET NOTA = 11 WHERE ID = 16;
-- E6 Inclusión Energética - 2026: roro, chechi
UPDATE INSCRIPCIONEDICION SET NOTA = 7  WHERE ID = 17;
UPDATE INSCRIPCIONEDICION SET NOTA = 11 WHERE ID = 19;
-- E7 Taller de robótica educativa - 2024: eleven11
UPDATE INSCRIPCIONEDICION SET NOTA = 0  WHERE ID = 23;
-- E8 Taller de robótica educativa - 2026: jeffw, costas
UPDATE INSCRIPCIONEDICION SET NOTA = 8  WHERE ID = 25;
UPDATE INSCRIPCIONEDICION SET NOTA = 10 WHERE ID = 26;
-- E10 Komikan - 2026: chechi, weiss, roro
UPDATE INSCRIPCIONEDICION SET NOTA = 0  WHERE ID IN (30, 31, 32);
-- E13 Talleres plenarios - 2026: weiss, roro
UPDATE INSCRIPCIONEDICION SET NOTA = 6  WHERE ID = 41;
UPDATE INSCRIPCIONEDICION SET NOTA = 7  WHERE ID = 42;
-- E14 Seminarios de Resolución de Problemas - 2026: weiss, roro
UPDATE INSCRIPCIONEDICION SET NOTA = 10 WHERE ID = 43;
UPDATE INSCRIPCIONEDICION SET NOTA = 2  WHERE ID = 45;

-- Cerradas unos dias despues de terminar (FECHACIERRE = dia en que el docente cerro la edicion)
UPDATE EDICIONCURSO SET CERRADA = 1, FECHACIERRE = DATE_ADD(FECHAFIN, INTERVAL 7 DAY)
 WHERE NOMBRE IN (SELECT DISTINCT EDICIONCURSO_NOMBRE FROM INSCRIPCIONEDICION WHERE NOTA IS NOT NULL);

-- Al cerrar, las inscripciones que seguian pendientes quedan rechazadas
UPDATE INSCRIPCIONEDICION SET ESTADO = 'RECHAZADA'
 WHERE ESTADO = 'INSCRIPTO'
   AND EDICIONCURSO_NOMBRE IN (SELECT NOMBRE FROM EDICIONCURSO WHERE CERRADA = 1);

-- Valoraciones de cursos (Valorar un Curso, 1 a 5 estrellas). El anexo no trae fecha.
-- Los ID quedan por debajo de SEQ_GEN (1000).
DELETE FROM VALORACIONCURSO WHERE ID BETWEEN 301 AND 399;
INSERT INTO VALORACIONCURSO (ID, ESTUDIANTE, CURSO, PUNTAJE, FECHA) VALUES
 -- C6 Flor del Ceibo
 (301, 'eleven11', 'Flor del Ceibo', 3, NULL),
 (302, 'chechi',   'Flor del Ceibo', 4, NULL),
 (303, 'costas',   'Flor del Ceibo', 5, NULL),
 (304, 'weiss',    'Flor del Ceibo', 4, NULL),
 (305, 'roro',     'Flor del Ceibo', 3, NULL),
 (306, 'jeffw',    'Flor del Ceibo', 3, NULL),
 -- C3 Dalavuelta
 (307, 'chechi',   'Dalavuelta', 2, NULL),
 (308, 'roro',     'Dalavuelta', 5, NULL),
 (309, 'costas',   'Dalavuelta', 4, NULL),
 -- C4 Extensionismo Industrial
 (310, 'costas',   'Extensionismo Industrial', 1, NULL),
 (311, 'chechi',   'Extensionismo Industrial', 2, NULL),
 (312, 'eleven11', 'Extensionismo Industrial', 3, NULL),
 (313, 'weiss',    'Extensionismo Industrial', 1, NULL),
 -- C5 Inclusión Energética
 (314, 'roro',     'Inclusión Energética', 5, NULL),
 (315, 'chechi',   'Inclusión Energética', 5, NULL),
 -- C7 Taller de robótica educativa.
 (316, 'eleven11', 'Taller de robótica educativa.', 4, NULL),
 (317, 'jeffw',    'Taller de robótica educativa.', 5, NULL),
 -- C8 Komikan
 (318, 'chechi',   'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela', 4, NULL),
 (319, 'weiss',    'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela', 4, NULL),
 (320, 'roro',     'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela', 4, NULL),
 -- C1 Talleres plenarios
 (321, 'weiss',    'Talleres plenarios', 1, NULL),
 -- C2 Seminarios de Resolución de Problemas
 (322, 'roro',     'Seminarios de Resolución de Problemas', 2, NULL);
