package ServidorCentral.Logica.datatypes;

import java.util.List;

/**
 * Nombres de las ediciones de curso y de los programas de formacion en los
 * que esta inscripto un estudiante. Las listas nunca son null.
 */
public record DTInscripcionesEstudiante(List<String> ediciones, List<String> programas) {

    public DTInscripcionesEstudiante {
        ediciones = ediciones == null ? List.of() : List.copyOf(ediciones);
        programas = programas == null ? List.of() : List.copyOf(programas);
    }
}
