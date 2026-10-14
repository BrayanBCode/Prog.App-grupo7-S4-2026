-- =====================================================================================
-- Datos de prueba del Sprint 2 (Anexo "Datos de Pruebas - Tarea 2", ProgApp 2026).
-- Se aplica DESPUES de 02-datos.sql (datos del Sprint 1). Se puede volver a correr:
-- borra y vuelve a cargar lo del Sprint 2 sin duplicar nada.
--
-- Correspondencia entre las referencias del anexo y los datos ya cargados:
--   C1 Talleres plenarios                 C6  Flor del Ceibo
--   C2 Seminarios de Resolución de ...    C7  Taller de robótica educativa.
--   C3 Dalavuelta                         C8  Participación ... Komikan ...
--   C4 Extensionismo Industrial           C9  Herramientas de apoyo a la enseñanza de inglés ...
--   C5 Inclusión Energética               C10 MicroBit
--   E1..E3 Flor del Ceibo 2010/2012/2025, E4 Dalavuelta - 2025, E5 Extensionismo Industrial - 2025,
--   E6 Inclusión Energética - 2026, E7..E9 Taller de robótica 2024/2026/2026-2, E10 Komikan - 2026,
--   E11 Herramientas ... - 26, E12 MicroBit-2026, E13 Talleres plenarios - 2026, E14 Seminarios - 2026
--   P1 EFI Ingeniería Mecánica, P2 Formación integral, P3 EFI Robótica
--
-- NO se cargan las categorias ni las imagenes de programas: Alta de Categoria y los casos de uso
-- de Programas de Formacion estan en gris en los requerimientos minimos (no corresponden al grupo).
-- Las inscripciones a programas SI se cargan porque las muestra Consulta de Usuario.
--
-- Las imagenes estan en la carpeta /imagenes del repo; en la BD va la ruta RELATIVA a esa carpeta.
-- =====================================================================================

SET NAMES utf8mb4;

-- ---------------- Contraseñas (sugeridas por el anexo) ----------------
UPDATE ESTUDIANTE SET `CONTRASEÑA` = '1234'
 WHERE NICKNAME IN ('eleven11','costas','roro','chechi','jeffw','weiss');
UPDATE DOCENTE SET `CONTRASEÑA` = '1234'
 WHERE NICKNAME IN ('heisenberg','benkenobi','waston','house','timmy','danny','phils','bruces','adri');

-- ---------------- Imágenes de usuarios ----------------
UPDATE ESTUDIANTE SET IMAGEN = NULL
 WHERE NICKNAME IN ('eleven11','costas','roro','chechi','jeffw','weiss');
UPDATE DOCENTE SET IMAGEN = NULL
 WHERE NICKNAME IN ('heisenberg','benkenobi','waston','house','timmy','danny','phils','bruces','adri');
UPDATE ESTUDIANTE SET IMAGEN = 'usuarios/roro.jpg'       WHERE NICKNAME = 'roro';
UPDATE ESTUDIANTE SET IMAGEN = 'usuarios/chechi.jpg'     WHERE NICKNAME = 'chechi';
UPDATE ESTUDIANTE SET IMAGEN = 'usuarios/jeffw.jpg'      WHERE NICKNAME = 'jeffw';
UPDATE DOCENTE    SET IMAGEN = 'usuarios/heisenberg.png' WHERE NICKNAME = 'heisenberg';
UPDATE DOCENTE    SET IMAGEN = 'usuarios/benkenobi.jpg'  WHERE NICKNAME = 'benkenobi';
UPDATE DOCENTE    SET IMAGEN = 'usuarios/waston.jpg'     WHERE NICKNAME = 'waston';
UPDATE DOCENTE    SET IMAGEN = 'usuarios/house.jpg'      WHERE NICKNAME = 'house';
UPDATE DOCENTE    SET IMAGEN = 'usuarios/timmy.jpg'      WHERE NICKNAME = 'timmy';

-- ---------------- Imágenes de cursos y ediciones ----------------
UPDATE CURSO SET IMAGEN = 'cursos/Dalavuelta.png'                   WHERE NOMBRE = 'Dalavuelta';
UPDATE CURSO SET IMAGEN = 'cursos/Inclusion_Energetica.jpg'         WHERE NOMBRE = 'Inclusión Energética';
UPDATE CURSO SET IMAGEN = 'cursos/Flor_del_Ceibo.png'               WHERE NOMBRE = 'Flor del Ceibo';
UPDATE CURSO SET IMAGEN = 'cursos/Taller_de_robotica_educativa.jpg' WHERE NOMBRE = 'Taller de robótica educativa.';
UPDATE CURSO SET IMAGEN = 'cursos/Komikan.png'
 WHERE NOMBRE = 'Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela';
UPDATE CURSO SET IMAGEN = 'cursos/MicroBit.jpg'                     WHERE NOMBRE = 'MicroBit';

UPDATE EDICIONCURSO SET IMAGEN = 'ediciones/Flor_del_Ceibo_2010.jpg'      WHERE NOMBRE = 'Flor del Ceibo - 2010';
UPDATE EDICIONCURSO SET IMAGEN = 'ediciones/Flor_del_Ceibo_2012.jpg'      WHERE NOMBRE = 'Flor del Ceibo - 2012';
UPDATE EDICIONCURSO SET IMAGEN = 'ediciones/Flor_del_Ceibo_2025.jpg'      WHERE NOMBRE = 'Flor del Ceibo - 2025';
UPDATE EDICIONCURSO SET IMAGEN = 'ediciones/Dalavuelta_2025.jpg'          WHERE NOMBRE = 'Dalavuelta - 2025';
UPDATE EDICIONCURSO SET IMAGEN = 'ediciones/Taller_de_robotica_2024.jpg'  WHERE NOMBRE = 'Taller de robótica educativa - 2024';
UPDATE EDICIONCURSO SET IMAGEN = 'ediciones/Taller_de_robotica_2026.jpg'  WHERE NOMBRE = 'Taller de robótica educativa - 2026';
UPDATE EDICIONCURSO SET IMAGEN = 'ediciones/Taller_de_robotica_2026-2.jpg' WHERE NOMBRE = 'Taller de robótica educativa-2026-2';



-- ---------------- Inscripciones a ediciones: estados ----------------
-- Cualquier inscripcion sin estado (cargada antes del Sprint 2) queda Inscripto.
UPDATE INSCRIPCIONEDICION SET ESTADO = 'INSCRIPTO' WHERE ESTADO IS NULL OR ESTADO = '';

-- E7 segun el anexo: weiss se inscribio el 18/02/24 (en el Sprint 1 figuraba 2017) y chechi no esta.
UPDATE INSCRIPCIONEDICION SET FECHAINSCRIPCION = '2024-02-18' WHERE ID = 21;
DELETE FROM INSCRIPCIONEDICION WHERE ID = 24;
-- E8: falta roro (23/02/26, Rechazado)
DELETE FROM INSCRIPCIONEDICION WHERE ID = 47;
INSERT INTO INSCRIPCIONEDICION (ID, FECHAINSCRIPCION, EDICIONCURSO_NOMBRE, ESTUDIANTE_NICKNAME, ESTUDIANTE_MAIL, ESTADO)
VALUES (47, '2026-02-23', 'Taller de robótica educativa - 2026', 'roro', 'rcotelo@yahoo.com', 'RECHAZADA');

UPDATE INSCRIPCIONEDICION SET ESTADO = 'ACEPTADA'
 WHERE ID IN (1,2, 3,5, 6,7, 8,10,11, 13,14,15,16, 17,19, 23, 25,26, 30,31,32, 41,42, 43,45);
UPDATE INSCRIPCIONEDICION SET ESTADO = 'RECHAZADA'
 WHERE ID IN (4, 9,12, 18,20, 21,22, 40, 44,46);
UPDATE INSCRIPCIONEDICION SET ESTADO = 'INSCRIPTO'
 WHERE ID IN (27,28,29, 33,34,35, 36,37,38,39);

-- ---------------- Inscripciones a programas de formación ----------------
DELETE FROM INSCRIPCIONPROGRAMA WHERE ID BETWEEN 101 AND 111;
INSERT INTO INSCRIPCIONPROGRAMA (ID, FECHAINSCRIPCION, ESTUDIANTE_NICKNAME, ESTUDIANTE_MAIL, PFORMACION_NOMBRE) VALUES
 (101,'2026-04-15','eleven11','eleven11@gmail.com','EFI Ingeniería Mecánica'),
 (102,'2026-04-20','chechi','cgarrido@hotmail.com','EFI Ingeniería Mecánica'),
 (103,'2026-07-10','costas','gcostas@gmail.com','Formación integral'),
 (104,'2026-07-10','chechi','cgarrido@hotmail.com','Formación integral'),
 (105,'2026-07-12','eleven11','eleven11@gmail.com','Formación integral'),
 (106,'2026-07-15','weiss','aweiss@hotmail.com','Formación integral'),
 (107,'2026-07-18','roro','rcotelo@yahoo.com','Formación integral'),
 (108,'2026-08-11','costas','gcostas@gmail.com','EFI Robótica'),
 (109,'2026-08-15','chechi','cgarrido@hotmail.com','EFI Robótica'),
 (110,'2026-08-22','roro','rcotelo@yahoo.com','EFI Robótica'),
 (111,'2026-08-30','jeffw','jwilliams@gmail.com','EFI Robótica');

-- ---------------- Seguidores (fila = quien sigue, columna = a quien sigue) ----------------
DELETE FROM SEGUIMIENTO WHERE ID BETWEEN 201 AND 234;
INSERT INTO SEGUIMIENTO (ID, SEGUIDOR, SEGUIDO) VALUES
 (201,'eleven11','roro'),(202,'eleven11','heisenberg'),(203,'eleven11','waston'),
 (204,'costas','chechi'),(205,'costas','jeffw'),(206,'costas','benkenobi'),(207,'costas','danny'),(208,'costas','phils'),
 (209,'roro','costas'),(210,'roro','weiss'),
 (211,'chechi','eleven11'),(212,'chechi','jeffw'),(213,'chechi','house'),
 (214,'jeffw','eleven11'),
 (215,'weiss','costas'),(216,'weiss','heisenberg'),(217,'weiss','timmy'),
 (218,'heisenberg','roro'),
 (219,'benkenobi','danny'),
 (220,'waston','roro'),(221,'waston','weiss'),(222,'waston','heisenberg'),
 (223,'house','jeffw'),(224,'house','weiss'),(225,'house','benkenobi'),
 (226,'timmy','eleven11'),(227,'timmy','chechi'),(228,'timmy','jeffw'),
 (229,'danny','costas'),(230,'danny','weiss'),(231,'danny','heisenberg'),
 (232,'phils','bruces'),
 (233,'bruces','phils'),
 (234,'adri','heisenberg');

-- Los ID que genere la aplicacion (EclipseLink, tabla SEQUENCE) tienen que quedar por encima de los cargados aca.
UPDATE SEQUENCE SET SEQ_COUNT = 1000 WHERE SEQ_NAME = 'SEQ_GEN' AND SEQ_COUNT < 1000;
