-- MySQL dump 10.13  Distrib 26.7.0, for Linux (x86_64)
--
-- Host: localhost    Database: laboratorio_db
-- ------------------------------------------------------
-- Server version	26.7.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- Table structure for table `CURSO`
--

DROP TABLE IF EXISTS `CURSO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CURSO` (
  `NOMBRE` varchar(255) NOT NULL,
  `CANTCREDITOS` int DEFAULT NULL,
  `CANTHORAS` float DEFAULT NULL,
  `DESCRIPCION` varchar(255) DEFAULT NULL,
  `DURACION` int DEFAULT NULL,
  `FECHAREGISTRO` date DEFAULT NULL,
  `URL` varchar(255) DEFAULT NULL,
  `NICKNAME` varchar(255) DEFAULT NULL,
  `MAIL` varchar(255) DEFAULT NULL,
  `INSTITUTO_NOMBRE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`NOMBRE`),
  KEY `FK_CURSO_INSTITUTO_NOMBRE` (`INSTITUTO_NOMBRE`),
  KEY `FK_CURSO_MAIL` (`MAIL`,`NICKNAME`),
  CONSTRAINT `FK_CURSO_INSTITUTO_NOMBRE` FOREIGN KEY (`INSTITUTO_NOMBRE`) REFERENCES `INSTITUTO` (`NOMBRE`),
  CONSTRAINT `FK_CURSO_MAIL` FOREIGN KEY (`MAIL`, `NICKNAME`) REFERENCES `DOCENTE` (`MAIL`, `NICKNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CURSO_CURSO`
--

DROP TABLE IF EXISTS `CURSO_CURSO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CURSO_CURSO` (
  `previas_NOMBRE` varchar(255) NOT NULL,
  `esPreviaDe_NOMBRE` varchar(255) NOT NULL,
  PRIMARY KEY (`previas_NOMBRE`,`esPreviaDe_NOMBRE`),
  KEY `FK_CURSO_CURSO_esPreviaDe_NOMBRE` (`esPreviaDe_NOMBRE`),
  CONSTRAINT `FK_CURSO_CURSO_esPreviaDe_NOMBRE` FOREIGN KEY (`esPreviaDe_NOMBRE`) REFERENCES `CURSO` (`NOMBRE`),
  CONSTRAINT `FK_CURSO_CURSO_previas_NOMBRE` FOREIGN KEY (`previas_NOMBRE`) REFERENCES `CURSO` (`NOMBRE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOCENTE`
--

DROP TABLE IF EXISTS `DOCENTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOCENTE` (
  `MAIL` varchar(255) NOT NULL,
  `NICKNAME` varchar(255) NOT NULL,
  `APELLIDO` varchar(255) DEFAULT NULL,
  `IMAGEN` varchar(255) DEFAULT NULL,
  `NOMBRE` varchar(255) DEFAULT NULL,
  `FECHANAC` date DEFAULT NULL,
  PRIMARY KEY (`MAIL`,`NICKNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DOCENTE_EDICIONCURSO`
--

DROP TABLE IF EXISTS `DOCENTE_EDICIONCURSO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOCENTE_EDICIONCURSO` (
  `MAIL` varchar(255) NOT NULL,
  `NICKNAME` varchar(255) NOT NULL,
  `edicionesC_NOMBRE` varchar(255) NOT NULL,
  PRIMARY KEY (`MAIL`,`NICKNAME`,`edicionesC_NOMBRE`),
  KEY `FK_DOCENTE_EDICIONCURSO_edicionesC_NOMBRE` (`edicionesC_NOMBRE`),
  CONSTRAINT `FK_DOCENTE_EDICIONCURSO_edicionesC_NOMBRE` FOREIGN KEY (`edicionesC_NOMBRE`) REFERENCES `EDICIONCURSO` (`NOMBRE`),
  CONSTRAINT `FK_DOCENTE_EDICIONCURSO_MAIL` FOREIGN KEY (`MAIL`, `NICKNAME`) REFERENCES `DOCENTE` (`MAIL`, `NICKNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EDICIONCURSO`
--

DROP TABLE IF EXISTS `EDICIONCURSO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EDICIONCURSO` (
  `NOMBRE` varchar(255) NOT NULL,
  `CUPO` int DEFAULT NULL,
  `FECHAFIN` date DEFAULT NULL,
  `FECHAINICIO` date DEFAULT NULL,
  `FECHAPUBLICACION` date DEFAULT NULL,
  `CURSO_NOMBRE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`NOMBRE`),
  KEY `FK_EDICIONCURSO_CURSO_NOMBRE` (`CURSO_NOMBRE`),
  CONSTRAINT `FK_EDICIONCURSO_CURSO_NOMBRE` FOREIGN KEY (`CURSO_NOMBRE`) REFERENCES `CURSO` (`NOMBRE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ESTUDIANTE`
--

DROP TABLE IF EXISTS `ESTUDIANTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ESTUDIANTE` (
  `MAIL` varchar(255) NOT NULL,
  `NICKNAME` varchar(255) NOT NULL,
  `APELLIDO` varchar(255) DEFAULT NULL,
  `IMAGEN` varchar(255) DEFAULT NULL,
  `NOMBRE` varchar(255) DEFAULT NULL,
  `FECHANAC` date DEFAULT NULL,
  PRIMARY KEY (`MAIL`,`NICKNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `INSCRIPCIONEDICION`
--

DROP TABLE IF EXISTS `INSCRIPCIONEDICION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `INSCRIPCIONEDICION` (
  `ID` bigint NOT NULL,
  `FECHAINSCRIPCION` date DEFAULT NULL,
  `EDICIONCURSO_NOMBRE` varchar(255) DEFAULT NULL,
  `ESTUDIANTE_NICKNAME` varchar(255) DEFAULT NULL,
  `ESTUDIANTE_MAIL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_INSCRIPCIONEDICION_ESTUDIANTE_MAIL` (`ESTUDIANTE_MAIL`,`ESTUDIANTE_NICKNAME`),
  KEY `FK_INSCRIPCIONEDICION_EDICIONCURSO_NOMBRE` (`EDICIONCURSO_NOMBRE`),
  CONSTRAINT `FK_INSCRIPCIONEDICION_EDICIONCURSO_NOMBRE` FOREIGN KEY (`EDICIONCURSO_NOMBRE`) REFERENCES `EDICIONCURSO` (`NOMBRE`),
  CONSTRAINT `FK_INSCRIPCIONEDICION_ESTUDIANTE_MAIL` FOREIGN KEY (`ESTUDIANTE_MAIL`, `ESTUDIANTE_NICKNAME`) REFERENCES `ESTUDIANTE` (`MAIL`, `NICKNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `INSCRIPCIONPROGRAMA`
--

DROP TABLE IF EXISTS `INSCRIPCIONPROGRAMA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `INSCRIPCIONPROGRAMA` (
  `ID` bigint NOT NULL,
  `FECHAINSCRIPCION` date DEFAULT NULL,
  `ESTUDIANTE_NICKNAME` varchar(255) DEFAULT NULL,
  `ESTUDIANTE_MAIL` varchar(255) DEFAULT NULL,
  `PFORMACION_NOMBRE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_INSCRIPCIONPROGRAMA_PFORMACION_NOMBRE` (`PFORMACION_NOMBRE`),
  KEY `FK_INSCRIPCIONPROGRAMA_ESTUDIANTE_MAIL` (`ESTUDIANTE_MAIL`,`ESTUDIANTE_NICKNAME`),
  CONSTRAINT `FK_INSCRIPCIONPROGRAMA_ESTUDIANTE_MAIL` FOREIGN KEY (`ESTUDIANTE_MAIL`, `ESTUDIANTE_NICKNAME`) REFERENCES `ESTUDIANTE` (`MAIL`, `NICKNAME`),
  CONSTRAINT `FK_INSCRIPCIONPROGRAMA_PFORMACION_NOMBRE` FOREIGN KEY (`PFORMACION_NOMBRE`) REFERENCES `PROGRAMAFORMACION` (`NOMBRE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `INSTITUTO`
--

DROP TABLE IF EXISTS `INSTITUTO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `INSTITUTO` (
  `NOMBRE` varchar(255) NOT NULL,
  PRIMARY KEY (`NOMBRE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROGRAMAFORMACION`
--

DROP TABLE IF EXISTS `PROGRAMAFORMACION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROGRAMAFORMACION` (
  `NOMBRE` varchar(255) NOT NULL,
  `DESCRIPCION` varchar(255) DEFAULT NULL,
  `FECHAALTA` date DEFAULT NULL,
  `FECHAFIN` date DEFAULT NULL,
  `FECHAINICIO` date DEFAULT NULL,
  PRIMARY KEY (`NOMBRE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PROGRAMAFORMACION_CURSO`
--

DROP TABLE IF EXISTS `PROGRAMAFORMACION_CURSO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PROGRAMAFORMACION_CURSO` (
  `cursos_NOMBRE` varchar(255) NOT NULL,
  `pFormaciones_NOMBRE` varchar(255) NOT NULL,
  PRIMARY KEY (`cursos_NOMBRE`,`pFormaciones_NOMBRE`),
  KEY `FK_PROGRAMAFORMACION_CURSO_pFormaciones_NOMBRE` (`pFormaciones_NOMBRE`),
  CONSTRAINT `FK_PROGRAMAFORMACION_CURSO_cursos_NOMBRE` FOREIGN KEY (`cursos_NOMBRE`) REFERENCES `CURSO` (`NOMBRE`),
  CONSTRAINT `FK_PROGRAMAFORMACION_CURSO_pFormaciones_NOMBRE` FOREIGN KEY (`pFormaciones_NOMBRE`) REFERENCES `PROGRAMAFORMACION` (`NOMBRE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQUENCE`
--

DROP TABLE IF EXISTS `SEQUENCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQUENCE` (
  `SEQ_NAME` varchar(50) NOT NULL,
  `SEQ_COUNT` decimal(38,0) DEFAULT NULL,
  PRIMARY KEY (`SEQ_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `instituto_docente`
--

DROP TABLE IF EXISTS `instituto_docente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `instituto_docente` (
  `docente_nickname` varchar(255) NOT NULL,
  `docente_mail` varchar(255) NOT NULL,
  `instituto_nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`docente_nickname`,`docente_mail`,`instituto_nombre`),
  KEY `FK_instituto_docente_instituto_nombre` (`instituto_nombre`),
  KEY `FK_instituto_docente_docente_mail` (`docente_mail`,`docente_nickname`),
  CONSTRAINT `FK_instituto_docente_docente_mail` FOREIGN KEY (`docente_mail`, `docente_nickname`) REFERENCES `DOCENTE` (`MAIL`, `NICKNAME`),
  CONSTRAINT `FK_instituto_docente_instituto_nombre` FOREIGN KEY (`instituto_nombre`) REFERENCES `INSTITUTO` (`NOMBRE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-30  1:04:57
