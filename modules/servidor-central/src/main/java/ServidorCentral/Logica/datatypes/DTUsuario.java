package ServidorCentral.Logica.datatypes;

import java.time.LocalDate;

/**
 * Datos completos de un usuario (consulta de usuario).
 * fechaNac e imagen pueden ser null.
 */
public record DTUsuario(
        String nickname,
        String mail,
        String nombre,
        String apellido,
        LocalDate fechaNac,
        String imagen) {
}
