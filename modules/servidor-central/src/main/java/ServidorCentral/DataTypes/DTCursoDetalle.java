package ServidorCentral.DataTypes;

import java.util.List;

/**
 * Consulta de Curso (Figura 3): todos los datos del curso, sus ediciones (de la mas reciente a la
 * mas vieja), los nombres de los programas de formacion que lo incluyen y el nombre de la edicion
 * vigente (la que esta en curso hoy; null si no hay ninguna).
 */
public record DTCursoDetalle(DTCurso curso, List<DTEdicionCurso> ediciones, List<String> programas, String edicionVigente) {

    public DTCursoDetalle {
        ediciones = List.copyOf(ediciones);
        programas = List.copyOf(programas);
    }
}
