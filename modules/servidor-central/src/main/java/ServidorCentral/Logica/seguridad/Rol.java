package ServidorCentral.Logica.seguridad;

/**
 * Los cuatro actores del sistema (ver letra, seccion 5).
 * <ul>
 *   <li>VISITANTE: entra al sitio web sin haber iniciado sesion.</li>
 *   <li>ESTUDIANTE / DOCENTE: usuarios registrados que iniciaron sesion en el sitio web.</li>
 *   <li>ADMINISTRADOR: usa SOLO la Estacion de Trabajo (Swing); no puede iniciar sesion
 *       en la aplicacion web, por eso nunca sale de {@code iniciarSesion}.</li>
 * </ul>
 */
public enum Rol {
    VISITANTE("Visitante"),
    ESTUDIANTE("Estudiante"),
    DOCENTE("Docente"),
    ADMINISTRADOR("Administrador");

    private final String etiqueta;

    Rol(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /** Texto para mostrar en pantalla (tambien accesible desde EL como ${rol.etiqueta}). */
    public String getEtiqueta() {
        return etiqueta;
    }

    /** true para Estudiante y Docente (usuarios con sesion iniciada en la web). */
    public boolean isAutenticado() {
        return this == ESTUDIANTE || this == DOCENTE;
    }
}
