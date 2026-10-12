package ServidorCentral.Logica.entities.cursos;

/**
 * Ciclo de vida de una inscripcion a edicion de curso (letra, Tabla 1):
 * INSCRIPTO al inscribirse; ACEPTADA o RECHAZADA cuando el docente selecciona estudiantes.
 */
public enum EstadoInscripcion {
    INSCRIPTO("Inscripto"),
    ACEPTADA("Aceptada"),
    RECHAZADA("Rechazada");

    private final String etiqueta;

    EstadoInscripcion(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /** Texto para mostrar al usuario ("Inscripto", "Aceptada", "Rechazada"). */
    public String getEtiqueta() {
        return etiqueta;
    }
}
