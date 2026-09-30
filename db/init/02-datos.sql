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
-- Dumping data for table `CURSO`
--

LOCK TABLES `CURSO` WRITE;
/*!40000 ALTER TABLE `CURSO` DISABLE KEYS */;
INSERT INTO `CURSO` (`NOMBRE`, `CANTCREDITOS`, `CANTHORAS`, `DESCRIPCION`, `DURACION`, `FECHAREGISTRO`, `URL`, `NICKNAME`, `MAIL`, `INSTITUTO_NOMBRE`) VALUES ('Dalavuelta',4,60,'Dalavuelta es un proyecto de extensión que nace en el IIMPI...',10,'2024-06-25','https://eva.fing.edu.uy/course/view.php?id=783#section-2','phils','schiller@gmail.com','IMPII'),('Extensionismo Industrial',5,75,'El proyecto tiene como objetivo desarrollar intervenciones curriculares...',12,'2025-06-16','https://eva.fing.edu.uy/course/view.php?id=783#section-2','phils','schiller@gmail.com','IMPII'),('Flor del Ceibo',10,150,'Flor de Ceibo es un proyecto central de la Universidad de la República...',15,'2008-07-27','http://www.flordeceibo.edu.uy/','bruces','sewell@gmail.com','DISI'),('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación',4,60,'Se realizarán visitas a escuelas rurales...',12,'2026-05-24','https://eva.fing.edu.uy/mod/folder/view.php?id=89398','heisenberg','heisenberg@gmail.com','INCO'),('Inclusión Energética',3,45,'En el proyecto se conjuga el trabajo de docentes y estudiantes...',6,'2026-02-01','https://eva.fing.edu.uy/course/view.php?id=783#section-2','phils','schiller@gmail.com','IMPII'),('MicroBit',7,105,'El Centro Ceibal se encuentra distribuyendo placas micro:bit...',15,'2026-03-13','https://www.fing.edu.uy/noticias/extension/modulo-de-tallerextension-microbit','house','greghouse@gmail.com','Eléctrica'),('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela',3,45,'Se propone desarrollar una aplicación interactiva...',9,'2026-06-15','https://eva.fing.edu.uy/mod/folder/view.php?id=89398','waston','e.watson@gmail.com','INCO'),('Seminarios de Resolución de Problemas',2,30,'Seminario, todos los jueves en Facultad de Ingeniería...',5,'2026-07-12','www.tmu.edu.uy','timmy','tim.cook@apple.com','IMERL'),('Taller de robótica educativa.',6,90,'La asignatura se organiza en dos etapas...',8,'2024-02-02','https://eva.fing.edu.uy/course/view.php?id=1187','heisenberg','heisenberg@gmail.com','INCO'),('Talleres plenarios',1,15,'Talleres plenarios*: presentados por cuatro reconocidos matemáticos uruguayos...',3,'2026-02-01','www.tmu.edu.uy','timmy','tim.cook@apple.com','IMERL');
/*!40000 ALTER TABLE `CURSO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `CURSO_CURSO`
--

LOCK TABLES `CURSO_CURSO` WRITE;
/*!40000 ALTER TABLE `CURSO_CURSO` DISABLE KEYS */;
INSERT INTO `CURSO_CURSO` (`previas_NOMBRE`, `esPreviaDe_NOMBRE`) VALUES ('Talleres plenarios','Dalavuelta'),('Talleres plenarios','Extensionismo Industrial'),('Talleres plenarios','Seminarios de Resolución de Problemas');
/*!40000 ALTER TABLE `CURSO_CURSO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `DOCENTE`
--

LOCK TABLES `DOCENTE` WRITE;
/*!40000 ALTER TABLE `DOCENTE` DISABLE KEYS */;
INSERT INTO `DOCENTE` (`MAIL`, `NICKNAME`, `APELLIDO`, `IMAGEN`, `NOMBRE`, `FECHANAC`) VALUES ('agarcia@gmail.com','adri','García','placeholder.png','Adriana','1978-07-28'),('benKenobi@gmail.com','benkenobi','Kenobi','placeholder.png','Obi-Wan','1914-04-02'),('dan.riccio@gmail.com','danny','Riccio','placeholder.png','Daniel','1963-07-05'),('e.watson@gmail.com','waston','Watson','placeholder.png','Emma','1990-04-15'),('greghouse@gmail.com','house','House','placeholder.png','Gregory','1959-05-15'),('heisenberg@gmail.com','heisenberg','White','placeholder.png','Walter','1956-03-07'),('schiller@gmail.com','phils','Schiller','placeholder.png','Philip','1961-10-07'),('sewell@gmail.com','bruces','Sewell','placeholder.png','Bruce','1959-12-03'),('tim.cook@apple.com','timmy','Cook','','Tim','1960-11-01');
/*!40000 ALTER TABLE `DOCENTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `DOCENTE_EDICIONCURSO`
--

LOCK TABLES `DOCENTE_EDICIONCURSO` WRITE;
/*!40000 ALTER TABLE `DOCENTE_EDICIONCURSO` DISABLE KEYS */;
INSERT INTO `DOCENTE_EDICIONCURSO` (`MAIL`, `NICKNAME`, `edicionesC_NOMBRE`) VALUES ('schiller@gmail.com','phils','Dalavuelta - 2025'),('schiller@gmail.com','phils','Extensionismo Industrial - 2025'),('sewell@gmail.com','bruces','Flor del Ceibo - 2010'),('agarcia@gmail.com','adri','Flor del Ceibo - 2012'),('sewell@gmail.com','bruces','Flor del Ceibo - 2012'),('agarcia@gmail.com','adri','Flor del Ceibo - 2025'),('sewell@gmail.com','bruces','Flor del Ceibo - 2025'),('heisenberg@gmail.com','heisenberg','Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26'),('schiller@gmail.com','phils','Inclusión Energética - 2026'),('greghouse@gmail.com','house','MicroBit-2026'),('e.watson@gmail.com','waston','Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026'),('tim.cook@apple.com','timmy','Seminarios de Resolución de Problemas - 2026'),('heisenberg@gmail.com','heisenberg','Taller de robótica educativa - 2024'),('benKenobi@gmail.com','benkenobi','Taller de robótica educativa - 2026'),('heisenberg@gmail.com','heisenberg','Taller de robótica educativa - 2026'),('benKenobi@gmail.com','benkenobi','Taller de robótica educativa-2026-2'),('e.watson@gmail.com','waston','Taller de robótica educativa-2026-2'),('dan.riccio@gmail.com','danny','Talleres plenarios - 2026'),('tim.cook@apple.com','timmy','Talleres plenarios - 2026');
/*!40000 ALTER TABLE `DOCENTE_EDICIONCURSO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `EDICIONCURSO`
--

LOCK TABLES `EDICIONCURSO` WRITE;
/*!40000 ALTER TABLE `EDICIONCURSO` DISABLE KEYS */;
INSERT INTO `EDICIONCURSO` (`NOMBRE`, `CUPO`, `FECHAFIN`, `FECHAINICIO`, `FECHAPUBLICACION`, `CURSO_NOMBRE`) VALUES ('Dalavuelta - 2025',15,'2024-11-10','2024-08-20','2024-07-20','Dalavuelta'),('Extensionismo Industrial - 2025',15,'2025-11-10','2025-08-10','2025-07-08','Extensionismo Industrial'),('Flor del Ceibo - 2010',-1,'2010-07-07','2010-03-15','2010-02-16','Flor del Ceibo'),('Flor del Ceibo - 2012',-1,'2012-11-20','2012-08-01','2012-07-10','Flor del Ceibo'),('Flor del Ceibo - 2025',-1,'2025-08-07','2025-04-10','2025-03-06','Flor del Ceibo'),('Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26',5,'2026-12-15','2026-09-15','2026-06-02','Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación'),('Inclusión Energética - 2026',30,'2026-04-30','2026-03-15','2026-02-20','Inclusión Energética'),('MicroBit-2026',30,'2026-12-05','2026-08-12','2026-07-02','MicroBit'),('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026',5,'2026-10-07','2026-07-29','2026-07-10','Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela'),('Seminarios de Resolución de Problemas - 2026',-1,'2026-10-20','2026-09-10','2026-07-12','Seminarios de Resolución de Problemas'),('Taller de robótica educativa - 2024',10,'2024-05-10','2024-03-10','2024-02-15','Taller de robótica educativa.'),('Taller de robótica educativa - 2026',10,'2026-05-10','2026-03-10','2026-02-15','Taller de robótica educativa.'),('Taller de robótica educativa-2026-2',20,'2026-11-08','2026-09-10','2026-08-15','Taller de robótica educativa.'),('Talleres plenarios - 2026',-1,'2026-03-30','2026-03-10','2026-03-02','Talleres plenarios');
/*!40000 ALTER TABLE `EDICIONCURSO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `ESTUDIANTE`
--

LOCK TABLES `ESTUDIANTE` WRITE;
/*!40000 ALTER TABLE `ESTUDIANTE` DISABLE KEYS */;
INSERT INTO `ESTUDIANTE` (`MAIL`, `NICKNAME`, `APELLIDO`, `IMAGEN`, `NOMBRE`, `FECHANAC`) VALUES ('aweiss@hotmail.com','weiss','Weiss','','Adrian','1978-12-23'),('cgarrido@hotmail.com','chechi','Garrido','','Cecilia','1987-09-12'),('eleven11@gmail.com','eleven11','Twelve','','Eleven','1971-12-31'),('gcostas@gmail.com','costas','Costas','','Gerardo','1983-11-15'),('jwilliams@gmail.com','jeffw','Williams','','Jeff','1964-11-27'),('rcotelo@yahoo.com','roro','Cotelo','','Rodrigo','1975-08-02');
/*!40000 ALTER TABLE `ESTUDIANTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `INSCRIPCIONEDICION`
--

LOCK TABLES `INSCRIPCIONEDICION` WRITE;
/*!40000 ALTER TABLE `INSCRIPCIONEDICION` DISABLE KEYS */;
INSERT INTO `INSCRIPCIONEDICION` (`ID`, `FECHAINSCRIPCION`, `EDICIONCURSO_NOMBRE`, `ESTUDIANTE_NICKNAME`, `ESTUDIANTE_MAIL`) VALUES (1,'2010-02-20','Flor del Ceibo - 2010','eleven11','eleven11@gmail.com'),(2,'2010-02-25','Flor del Ceibo - 2010','chechi','cgarrido@hotmail.com'),(3,'2012-07-12','Flor del Ceibo - 2012','costas','gcostas@gmail.com'),(4,'2012-07-15','Flor del Ceibo - 2012','roro','rcotelo@yahoo.com'),(5,'2012-07-30','Flor del Ceibo - 2012','weiss','aweiss@hotmail.com'),(6,'2025-03-10','Flor del Ceibo - 2025','roro','rcotelo@yahoo.com'),(7,'2025-03-15','Flor del Ceibo - 2025','jeffw','jwilliams@gmail.com'),(8,'2024-07-25','Dalavuelta - 2025','chechi','cgarrido@hotmail.com'),(9,'2024-07-28','Dalavuelta - 2025','eleven11','eleven11@gmail.com'),(10,'2024-08-02','Dalavuelta - 2025','roro','rcotelo@yahoo.com'),(11,'2024-08-10','Dalavuelta - 2025','costas','gcostas@gmail.com'),(12,'2024-08-15','Dalavuelta - 2025','jeffw','jwilliams@gmail.com'),(13,'2025-07-18','Extensionismo Industrial - 2025','costas','gcostas@gmail.com'),(14,'2025-07-20','Extensionismo Industrial - 2025','chechi','cgarrido@hotmail.com'),(15,'2025-07-29','Extensionismo Industrial - 2025','eleven11','eleven11@gmail.com'),(16,'2025-08-05','Extensionismo Industrial - 2025','weiss','aweiss@hotmail.com'),(17,'2026-02-23','Inclusión Energética - 2026','roro','rcotelo@yahoo.com'),(18,'2026-02-25','Inclusión Energética - 2026','weiss','aweiss@hotmail.com'),(19,'2026-02-28','Inclusión Energética - 2026','chechi','cgarrido@hotmail.com'),(20,'2026-03-03','Inclusión Energética - 2026','eleven11','eleven11@gmail.com'),(21,'2017-02-18','Taller de robótica educativa - 2024','weiss','aweiss@hotmail.com'),(22,'2024-02-20','Taller de robótica educativa - 2024','roro','rcotelo@yahoo.com'),(23,'2024-03-03','Taller de robótica educativa - 2024','eleven11','eleven11@gmail.com'),(24,'2024-03-05','Taller de robótica educativa - 2024','chechi','cgarrido@hotmail.com'),(25,'2026-02-18','Taller de robótica educativa - 2026','jeffw','jwilliams@gmail.com'),(26,'2026-02-22','Taller de robótica educativa - 2026','costas','gcostas@gmail.com'),(27,'2026-08-18','Taller de robótica educativa-2026-2','weiss','aweiss@hotmail.com'),(28,'2026-08-22','Taller de robótica educativa-2026-2','chechi','cgarrido@hotmail.com'),(29,'2026-09-03','Taller de robótica educativa-2026-2','roro','rcotelo@yahoo.com'),(30,'2026-07-13','Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026','chechi','cgarrido@hotmail.com'),(31,'2026-07-20','Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026','weiss','aweiss@hotmail.com'),(32,'2026-07-22','Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026','roro','rcotelo@yahoo.com'),(33,'2026-06-04','Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26','weiss','aweiss@hotmail.com'),(34,'2026-07-18','Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26','eleven11','eleven11@gmail.com'),(35,'2026-08-20','Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26','jeffw','jwilliams@gmail.com'),(36,'2026-07-12','MicroBit-2026','chechi','cgarrido@hotmail.com'),(37,'2026-07-14','MicroBit-2026','roro','rcotelo@yahoo.com'),(38,'2026-07-25','MicroBit-2026','eleven11','eleven11@gmail.com'),(39,'2026-08-05','MicroBit-2026','jeffw','jwilliams@gmail.com'),(40,'2026-03-05','Talleres plenarios - 2026','costas','gcostas@gmail.com'),(41,'2026-03-04','Talleres plenarios - 2026','weiss','aweiss@hotmail.com'),(42,'2026-03-07','Talleres plenarios - 2026','roro','rcotelo@yahoo.com'),(43,'2026-07-15','Seminarios de Resolución de Problemas - 2026','weiss','aweiss@hotmail.com'),(44,'2026-07-20','Seminarios de Resolución de Problemas - 2026','costas','gcostas@gmail.com'),(45,'2026-08-06','Seminarios de Resolución de Problemas - 2026','roro','rcotelo@yahoo.com'),(46,'2026-08-30','Seminarios de Resolución de Problemas - 2026','chechi','cgarrido@hotmail.com');
/*!40000 ALTER TABLE `INSCRIPCIONEDICION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `INSCRIPCIONPROGRAMA`
--

LOCK TABLES `INSCRIPCIONPROGRAMA` WRITE;
/*!40000 ALTER TABLE `INSCRIPCIONPROGRAMA` DISABLE KEYS */;
/*!40000 ALTER TABLE `INSCRIPCIONPROGRAMA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `INSTITUTO`
--

LOCK TABLES `INSTITUTO` WRITE;
/*!40000 ALTER TABLE `INSTITUTO` DISABLE KEYS */;
INSERT INTO `INSTITUTO` (`NOMBRE`) VALUES ('DISI'),('Eléctrica'),('Física'),('IMERL'),('IMPII'),('INCO');
/*!40000 ALTER TABLE `INSTITUTO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `PROGRAMAFORMACION`
--

LOCK TABLES `PROGRAMAFORMACION` WRITE;
/*!40000 ALTER TABLE `PROGRAMAFORMACION` DISABLE KEYS */;
INSERT INTO `PROGRAMAFORMACION` (`NOMBRE`, `DESCRIPCION`, `FECHAALTA`, `FECHAFIN`, `FECHAINICIO`) VALUES ('EFI Ingeniería Mecánica','Programa mecánica','2026-01-01','2026-10-31','2026-05-01'),('EFI Robótica','Programa robótica','2026-01-01','2026-11-18','2026-09-03'),('Formación integral','Programa varios institutos','2026-01-01','2027-01-01','2026-07-15');
/*!40000 ALTER TABLE `PROGRAMAFORMACION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `PROGRAMAFORMACION_CURSO`
--

LOCK TABLES `PROGRAMAFORMACION_CURSO` WRITE;
/*!40000 ALTER TABLE `PROGRAMAFORMACION_CURSO` DISABLE KEYS */;
INSERT INTO `PROGRAMAFORMACION_CURSO` (`cursos_NOMBRE`, `pFormaciones_NOMBRE`) VALUES ('Dalavuelta','EFI Ingeniería Mecánica'),('Extensionismo Industrial','EFI Ingeniería Mecánica'),('Inclusión Energética','EFI Ingeniería Mecánica'),('MicroBit','EFI Robótica'),('Taller de robótica educativa.','EFI Robótica'),('Extensionismo Industrial','Formación integral'),('Flor del Ceibo','Formación integral'),('Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela','Formación integral'),('Seminarios de Resolución de Problemas','Formación integral');
/*!40000 ALTER TABLE `PROGRAMAFORMACION_CURSO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `SEQUENCE`
--

LOCK TABLES `SEQUENCE` WRITE;
/*!40000 ALTER TABLE `SEQUENCE` DISABLE KEYS */;
INSERT INTO `SEQUENCE` (`SEQ_NAME`, `SEQ_COUNT`) VALUES ('SEQ_GEN',50);
/*!40000 ALTER TABLE `SEQUENCE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `instituto_docente`
--

LOCK TABLES `instituto_docente` WRITE;
/*!40000 ALTER TABLE `instituto_docente` DISABLE KEYS */;
INSERT INTO `instituto_docente` (`docente_nickname`, `docente_mail`, `instituto_nombre`) VALUES ('adri','agarcia@gmail.com','DISI'),('bruces','sewell@gmail.com','DISI'),('house','greghouse@gmail.com','Eléctrica'),('danny','dan.riccio@gmail.com','IMERL'),('timmy','tim.cook@apple.com','IMERL'),('phils','schiller@gmail.com','IMPII'),('benkenobi','benKenobi@gmail.com','INCO'),('heisenberg','heisenberg@gmail.com','INCO'),('waston','e.watson@gmail.com','INCO');
/*!40000 ALTER TABLE `instituto_docente` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-30  1:16:55
