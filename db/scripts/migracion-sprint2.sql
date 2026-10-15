-- =====================================================================================
-- Migracion de una BD que YA EXISTIA (volumen de Docker creado en el Sprint 1) al Sprint 2.
-- Agrega solo lo que falte (se puede correr mas de una vez) y despues hay que cargar los datos:
--
--   docker exec -i server-mysql mysql -uroot -proot laboratorio_db < db/scripts/migracion-sprint2.sql
--   docker exec -i server-mysql mysql -uroot -proot laboratorio_db < db/init/03-datos-sprint2.sql
--
-- Una BD nueva (docker compose up con el volumen vacio) no necesita esto: 01/02/03 de db/init
-- ya crean todo.
-- =====================================================================================

SET NAMES utf8mb4;

DROP PROCEDURE IF EXISTS agregar_columna;
DELIMITER //
CREATE PROCEDURE agregar_columna(IN tabla VARCHAR(64), IN columna VARCHAR(64), IN definicion VARCHAR(255))
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.COLUMNS
                   WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = tabla AND COLUMN_NAME = columna) THEN
        SET @sql = CONCAT('ALTER TABLE `', tabla, '` ADD COLUMN `', columna, '` ', definicion);
        PREPARE st FROM @sql;
        EXECUTE st;
        DEALLOCATE PREPARE st;
    END IF;
END //
DELIMITER ;

CALL agregar_columna('DOCENTE', 'CONTRASEÑA', 'varchar(255) DEFAULT NULL');
CALL agregar_columna('ESTUDIANTE', 'CONTRASEÑA', 'varchar(255) DEFAULT NULL');
CALL agregar_columna('CURSO', 'IMAGEN', 'varchar(255) DEFAULT NULL');
CALL agregar_columna('EDICIONCURSO', 'IMAGEN', 'varchar(255) DEFAULT NULL');
CALL agregar_columna('INSCRIPCIONEDICION', 'ESTADO', 'varchar(255) NOT NULL DEFAULT ''INSCRIPTO''');

DROP PROCEDURE agregar_columna;

CREATE TABLE IF NOT EXISTS `SEGUIMIENTO` (
  `ID` bigint NOT NULL,
  `SEGUIDOR` varchar(255) NOT NULL,
  `SEGUIDO` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_SEGUIMIENTO_SEGUIDOR_SEGUIDO` (`SEGUIDOR`,`SEGUIDO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
