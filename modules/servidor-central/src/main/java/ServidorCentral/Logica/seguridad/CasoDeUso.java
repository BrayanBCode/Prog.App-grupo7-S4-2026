package ServidorCentral.Logica.seguridad;

import java.util.EnumSet;
import java.util.Set;

import static ServidorCentral.Logica.seguridad.Rol.*;

/**
 * Matriz "caso de uso -> actores que pueden ejecutarlo", tomada de la letra
 * de la Tarea 2 (campo "Actores" de cada caso de uso). Es el UNICO lugar donde
 * se decide quien puede hacer que: el sitio web (menus y filtro de acceso) y la
 * Estacion de Trabajo consultan esta tabla en vez de repetir la regla.
 *
 * Para cambiar quien ve un caso de uso se toca una sola linea de este enum.
 */
public enum CasoDeUso {

    // ---------------- Servidor Web (letra 6.2) ----------------
    INICIO_SESION(VISITANTE),
    // La letra pone como actor a "Usuario" (= usuario registrado con sesion), asi que
    // un Visitante NO lo ve: no tiene sesion que cerrar.
    CIERRE_SESION(ESTUDIANTE, DOCENTE),
    ALTA_USUARIO(VISITANTE),
    CONSULTA_USUARIO(VISITANTE, ESTUDIANTE, DOCENTE),
    MODIFICAR_DATOS_USUARIO(ESTUDIANTE, DOCENTE),
    ALTA_CURSO(DOCENTE),
    CONSULTA_CURSO(VISITANTE, ESTUDIANTE, DOCENTE),
    ALTA_EDICION_CURSO(DOCENTE),
    CONSULTA_EDICION_CURSO(VISITANTE, ESTUDIANTE, DOCENTE),
    INSCRIPCION_EDICION_CURSO(ESTUDIANTE),
    CREAR_PROGRAMA_FORMACION(DOCENTE),
    AGREGAR_CURSO_A_PROGRAMA(DOCENTE),
    CONSULTA_PROGRAMA_FORMACION(VISITANTE, ESTUDIANTE, DOCENTE),
    INSCRIPCION_PROGRAMA_FORMACION(ESTUDIANTE),
    SELECCIONAR_ESTUDIANTES_EDICION(DOCENTE),
    LISTAR_ACEPTADOS_EDICION(DOCENTE),
    LISTAR_RESULTADOS_INSCRIPCIONES(ESTUDIANTE),
    SEGUIR_USUARIO(ESTUDIANTE, DOCENTE),
    DEJAR_DE_SEGUIR_USUARIO(ESTUDIANTE, DOCENTE),
    // Buscador del cabezal (letra 7.5): disponible "en todo momento".
    BUSCAR_CURSOS_Y_PROGRAMAS(VISITANTE, ESTUDIANTE, DOCENTE),

    // ---------------- Estacion de Trabajo (letra 6.1) ----------------
    ALTA_CATEGORIA(ADMINISTRADOR);

    private final Set<Rol> roles;

    CasoDeUso(Rol primero, Rol... resto) {
        this.roles = EnumSet.of(primero, resto);
    }

    public boolean permitidoPara(Rol rol) {
        return rol != null && roles.contains(rol);
    }

    public Set<Rol> getRoles() {
        return EnumSet.copyOf(roles);
    }
}
