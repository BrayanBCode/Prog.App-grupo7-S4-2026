-- Contrasenas de PRUEBA para los usuarios que ya estaban cargados en 02-datos.sql
-- (se cargaron antes de que existiera la contrasena, asi que tienen NULL y no podrian iniciar sesion).
--
-- IMPORTANTE: la columna CONTRASEÑA la crea EclipseLink (create-or-extend-tables) la primera vez
-- que arranca la aplicacion con la entidad actualizada. Por eso este script se corre A MANO
-- DESPUES de haber levantado la Estacion de Trabajo / el Servidor Web una vez:
--     docker exec -i server-mysql mysql -uroot -proot laboratorio_db < db/scripts/contrasenas-prueba.sql
-- (Esta fuera de db/init a proposito: lo de db/init corre en el primer arranque de Docker, cuando la columna aun no existe.)

UPDATE DOCENTE    SET CONTRASEÑA = '1234' WHERE CONTRASEÑA IS NULL;
UPDATE ESTUDIANTE SET CONTRASEÑA = '1234' WHERE CONTRASEÑA IS NULL;
