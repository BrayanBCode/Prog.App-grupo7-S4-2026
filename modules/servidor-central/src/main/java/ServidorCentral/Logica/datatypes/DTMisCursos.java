package ServidorCentral.Logica.datatypes;

import java.util.List;

/**
 * Pantalla "Mis cursos y ediciones" del docente (acceso rapido, web): los cursos que dicta y las ediciones
 * en las que participa (de la mas reciente a la mas vieja), cada una con si es la vigente.
 */
public record DTMisCursos(List<DTCursoResumen> cursos, List<Edicion> ediciones) {

    /** Una edicion del docente y si hoy es la vigente de su curso. */
    public record Edicion(DTEdicionCurso edicion, boolean vigente) {
    }

    public DTMisCursos {
        cursos = List.copyOf(cursos);
        ediciones = List.copyOf(ediciones);
    }
}
