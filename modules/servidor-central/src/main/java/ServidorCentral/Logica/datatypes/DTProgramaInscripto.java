package ServidorCentral.Logica.datatypes;

import java.time.LocalDate;

/** Un programa de formacion al que se inscribio un estudiante, con la fecha de inscripcion. */
public record DTProgramaInscripto(String programa, LocalDate fechaInscripcion) {
}
