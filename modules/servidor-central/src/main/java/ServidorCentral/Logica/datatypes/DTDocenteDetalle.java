package ServidorCentral.Logica.datatypes;

import java.util.List;

/**
 * Consulta de docente: nombres de institutos, cursos, ediciones y programas
 * vinculados. Las listas nunca son null (pueden estar vacias); los titulos
 * y textos tipo "(Sin cursos registrados)" los pone la presentacion.
 */
public record DTDocenteDetalle(
        List<String> institutos,
        List<String> cursos,
        List<String> ediciones,
        List<String> programas) {

    public DTDocenteDetalle {
        institutos = institutos == null ? List.of() : List.copyOf(institutos);
        cursos = cursos == null ? List.of() : List.copyOf(cursos);
        ediciones = ediciones == null ? List.of() : List.copyOf(ediciones);
        programas = programas == null ? List.of() : List.copyOf(programas);
    }
}
