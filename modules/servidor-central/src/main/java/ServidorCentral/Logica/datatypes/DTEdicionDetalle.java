package ServidorCentral.Logica.datatypes;

import ServidorCentral.Logica.entities.cursos.EstadoInscripcion;

import java.util.List;

/**
 * Consulta de Edicion de Curso (Figura 4): datos de la edicion, el instituto que brinda el curso,
 * los docentes asignados y si es la edicion vigente del curso (hoy entre su fecha de inicio y de fin).
 * miInscripcion: estado de la inscripcion de quien mira (si es estudiante y esta inscripto), o null.
 */
public record DTEdicionDetalle(DTEdicionCurso edicion, String instituto, List<DTDocenteResumen> docentes,
                               boolean vigente, EstadoInscripcion miInscripcion) {

    public DTEdicionDetalle {
        docentes = List.copyOf(docentes);
    }
}
