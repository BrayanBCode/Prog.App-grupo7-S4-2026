package ServidorCentral.WebServices;

import java.time.LocalDate;

/** Las fechas viajan por SOAP como texto ISO (yyyy-MM-dd); null o "" si no hay fecha. */
public final class Fechas {

    private Fechas() {
    }

    public static String texto(LocalDate fecha) {
        return fecha == null ? null : fecha.toString();
    }

    public static LocalDate fecha(String texto) {
        return texto == null || texto.isBlank() ? null : LocalDate.parse(texto.trim());
    }
}
