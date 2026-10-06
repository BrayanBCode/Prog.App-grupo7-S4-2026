package ServidorCentral.Logica.seguridad;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Verifica que cada actor vea solo los casos de uso que le corresponden segun la letra. */
class CasoDeUsoTest {

    @Test
    void inicioDeSesion_loEjecutaSoloElVisitante() {
        assertTrue(CasoDeUso.INICIO_SESION.permitidoPara(Rol.VISITANTE));
        assertFalse(CasoDeUso.INICIO_SESION.permitidoPara(Rol.ESTUDIANTE));
        assertFalse(CasoDeUso.INICIO_SESION.permitidoPara(Rol.DOCENTE));
        assertFalse(CasoDeUso.INICIO_SESION.permitidoPara(Rol.ADMINISTRADOR));
    }

    @Test
    void cierreDeSesion_loEjecutanEstudianteYDocente_peroNoElVisitante() {
        assertTrue(CasoDeUso.CIERRE_SESION.permitidoPara(Rol.ESTUDIANTE));
        assertTrue(CasoDeUso.CIERRE_SESION.permitidoPara(Rol.DOCENTE));
        assertFalse(CasoDeUso.CIERRE_SESION.permitidoPara(Rol.VISITANTE));
        assertFalse(CasoDeUso.CIERRE_SESION.permitidoPara(Rol.ADMINISTRADOR));
    }

    @Test
    void altaDeCategoria_loEjecutaSoloElAdministrador() {
        assertTrue(CasoDeUso.ALTA_CATEGORIA.permitidoPara(Rol.ADMINISTRADOR));
        assertFalse(CasoDeUso.ALTA_CATEGORIA.permitidoPara(Rol.VISITANTE));
        assertFalse(CasoDeUso.ALTA_CATEGORIA.permitidoPara(Rol.ESTUDIANTE));
        assertFalse(CasoDeUso.ALTA_CATEGORIA.permitidoPara(Rol.DOCENTE));
    }

    @Test
    void elAdministradorNoEjecutaNingunCasoDeUsoDeLaWeb() {
        for (CasoDeUso cu : CasoDeUso.values()) {
            if (cu != CasoDeUso.ALTA_CATEGORIA) {
                assertFalse(cu.permitidoPara(Rol.ADMINISTRADOR), cu + " no debe estar disponible para el administrador");
            }
        }
    }

    @Test
    void rolNuloNoTienePermisos_yLosRolesSeExponenSinPermitirModificar() {
        assertFalse(CasoDeUso.INICIO_SESION.permitidoPara(null));
        assertTrue(CasoDeUso.CIERRE_SESION.getRoles().contains(Rol.DOCENTE));
        CasoDeUso.CIERRE_SESION.getRoles().clear();   // es una copia
        assertTrue(CasoDeUso.CIERRE_SESION.permitidoPara(Rol.DOCENTE));
    }

    @Test
    void soloEstudianteYDocenteEstanAutenticados() {
        assertTrue(Rol.ESTUDIANTE.isAutenticado());
        assertTrue(Rol.DOCENTE.isAutenticado());
        assertFalse(Rol.VISITANTE.isAutenticado());
        assertFalse(Rol.ADMINISTRADOR.isAutenticado());
        assertEquals("Docente", Rol.DOCENTE.getEtiqueta());
    }
}
