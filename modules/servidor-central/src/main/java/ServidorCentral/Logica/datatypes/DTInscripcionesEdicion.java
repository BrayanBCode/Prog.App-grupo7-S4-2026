package ServidorCentral.Logica.datatypes;

import java.util.List;

/**
 * Seleccionar Estudiantes (Figura 5) y Listar Aceptados: los datos de la edicion (encabezado) y sus
 * inscripciones, en una sola llamada.
 */
public record DTInscripcionesEdicion(DTEdicionDetalle edicion, List<DTInscripcionEdicion> inscripciones) {

    public DTInscripcionesEdicion {
        inscripciones = List.copyOf(inscripciones);
    }
}
