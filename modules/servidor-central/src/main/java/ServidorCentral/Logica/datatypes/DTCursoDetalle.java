package ServidorCentral.Logica.datatypes;

import java.util.List;

/**
 * Consulta de Curso (Figura 3): todos los datos del curso, sus ediciones (de la mas reciente a la
 * mas vieja) y los nombres de los programas de formacion que lo incluyen.
 */
public record DTCursoDetalle(DTCurso curso, List<DTEdicionCurso> ediciones, List<String> programas) {

    public DTCursoDetalle {
        ediciones = List.copyOf(ediciones);
        programas = List.copyOf(programas);
    }
}
