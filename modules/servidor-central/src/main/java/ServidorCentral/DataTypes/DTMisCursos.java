package ServidorCentral.DataTypes;

import java.util.List;

/**
 * Pantalla "Mis cursos y ediciones" del docente (acceso rapido, web): los cursos que dicta y las ediciones
 * en las que participa (de la mas reciente a la mas vieja), cada una con si es la vigente.
 */
public record DTMisCursos(List<DTCursoResumen> cursos, List<Edicion> ediciones) {

    /** Una edicion del docente, si hoy es la vigente de su curso y si ya se cerro (con las notas cargadas). */
    public record Edicion(DTEdicionCurso edicion, boolean vigente, boolean cerrada) {
    }

    public DTMisCursos {
        cursos = List.copyOf(cursos);
        ediciones = List.copyOf(ediciones);
    }
}
