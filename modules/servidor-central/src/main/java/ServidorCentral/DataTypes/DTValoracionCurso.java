package ServidorCentral.DataTypes;

import java.util.List;

/**
 * Valoraciones de un curso (caso de uso Valorar un Curso y requerimiento 7.6, Visualizacion Grafica de Puntajes).
 *
 * @param curso      nombre del curso
 * @param promedio   promedio de los puntajes (0 si nadie lo valoro)
 * @param cantidad   cuantos estudiantes lo valoraron
 * @param porPuntaje cuantas valoraciones hay de cada puntaje: posicion 0 = 1 estrella ... posicion 4 = 5 estrellas
 *                   (el resumen de la Figura 7)
 * @param miPuntaje  el puntaje que le dio el estudiante que consulta, o null si no lo valoro (o si consulta otro)
 */
public record DTValoracionCurso(String curso, double promedio, int cantidad, List<Integer> porPuntaje, Integer miPuntaje) {

    public DTValoracionCurso {
        porPuntaje = List.copyOf(porPuntaje);
    }
}
