package ServidorCentral.Logica.datatypes;

import ServidorCentral.Logica.seguridad.Rol;

import java.util.List;

/**
 * Todo lo que muestra la Consulta de Usuario, ya filtrado segun quien mira (lo decide
 * PerfilService). Lo que el que mira no puede ver viene como lista vacia.
 *
 * @param usuario        datos basicos (los ve cualquiera)
 * @param rol            ESTUDIANTE o DOCENTE (el usuario consultado)
 * @param propio         el que mira es el mismo usuario
 * @param verDetalle     el que mira inicio sesion (un Visitante solo ve datos basicos e imagen)
 * @param institutos     docente: institutos a los que pertenece (dato basico)
 * @param ediciones      docente: ediciones en las que participa
 * @param inscripciones  estudiante: inscripciones a ediciones; las Rechazadas solo si es el propio o mira un docente
 * @param programas      estudiante: programas de formacion a los que se inscribio
 * @param seguidores     nicknames de quienes lo siguen
 * @param seguidos       nicknames de a quienes sigue
 * @param loSigue        el que mira ya sigue a este usuario
 * @param verAceptados   docente mirando su propio perfil: tiene "Ver aceptados" en sus ediciones
 */
public record DTPerfil(
        DTUsuario usuario,
        Rol rol,
        boolean propio,
        boolean verDetalle,
        List<String> institutos,
        List<DTEdicionCurso> ediciones,
        List<DTInscripcionEstudiante> inscripciones,
        List<DTProgramaInscripto> programas,
        List<String> seguidores,
        List<String> seguidos,
        boolean loSigue,
        boolean verAceptados) {

    public DTPerfil {
        institutos = List.copyOf(institutos);
        ediciones = List.copyOf(ediciones);
        inscripciones = List.copyOf(inscripciones);
        programas = List.copyOf(programas);
        seguidores = List.copyOf(seguidores);
        seguidos = List.copyOf(seguidos);
    }

    public boolean esDocente() {
        return rol == Rol.DOCENTE;
    }
}
