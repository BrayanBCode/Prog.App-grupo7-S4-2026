package ServidorCentral.Logica.datatypes;

import java.util.List;

/**
 * Consulta de Edicion de Curso (Figura 4): datos de la edicion, el instituto que brinda el curso,
 * los docentes asignados y si es la edicion vigente del curso (hoy entre su fecha de inicio y de fin).
 */
public record DTEdicionDetalle(DTEdicionCurso edicion, String instituto, List<DTDocenteResumen> docentes, boolean vigente) {

    public DTEdicionDetalle {
        docentes = List.copyOf(docentes);
    }
}
