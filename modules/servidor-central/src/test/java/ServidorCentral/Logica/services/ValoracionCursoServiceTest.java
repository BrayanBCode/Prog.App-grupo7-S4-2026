package ServidorCentral.Logica.services;

import ServidorCentral.DataTypes.DTValoracionCurso;
import ServidorCentral.Exepciones.ReglaNegocioException;
import ServidorCentral.Logica.entities.cursos.Curso;
import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.repositories.CursoRepository;
import ServidorCentral.Logica.repositories.EstudianteRepository;
import ServidorCentral.Logica.repositories.ValoracionCursoRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas SIN base de "Valorar un Curso". roro y weiss participan de MicroBit (aceptados en alguna edicion);
 * roro tambien de Komikan; nuevo es estudiante pero no participa de nada; house no es estudiante.
 */
class ValoracionCursoServiceTest {

    private static final LocalDate NAC = LocalDate.of(1999, 1, 1);

    /** Valoraciones guardadas: "nick|curso" -> puntaje. */
    private final Map<String, Integer> guardadas = new LinkedHashMap<>();
    private final Map<String, Set<String>> participa = Map.of(
            "roro", Set.of("MicroBit", "Komikan"),
            "weiss", Set.of("MicroBit"),
            "nuevo", Set.of());

    private final ValoracionCursoService servicio = new ValoracionCursoService(
            new EstudianteRepository(null) {
                @Override
                public Estudiante buscarPorNickname(String nickname) {
                    return participa.containsKey(nickname)
                            ? new Estudiante(nickname, nickname + "@x.com", "N", "A", NAC, null, "1234") : null;
                }
            },
            new CursoRepository(null) {
                @Override
                public Curso buscarPorNombre(String nombre) {
                    return Set.of("MicroBit", "Komikan", "Cocina").contains(nombre)
                            ? new Curso(nombre, "d", 4, 40f, 4, NAC, null, null) : null;
                }
            },
            new ValoracionCursoRepository(null) {
                @Override
                public Integer puntajeDe(String nickname, String curso) {
                    return guardadas.get(nickname + "|" + curso);
                }

                @Override
                public List<Integer> puntajesDe(String curso) {
                    List<Integer> r = new ArrayList<>();
                    guardadas.forEach((k, v) -> {
                        if (k.endsWith("|" + curso)) {
                            r.add(v);
                        }
                    });
                    return r;
                }

                @Override
                public boolean participa(String nickname, String curso) {
                    return participa.getOrDefault(nickname, Set.of()).contains(curso);
                }

                @Override
                public List<String> cursosDondeParticipa(String nickname) {
                    return participa.getOrDefault(nickname, Set.of()).stream().sorted().toList();
                }

                @Override
                public void guardar(String nickname, String curso, int puntaje, LocalDate fecha) {
                    assertEquals(LocalDate.now(), fecha);
                    guardadas.put(nickname + "|" + curso, puntaje);
                }
            });

    @Test
    void cursosParaValorar_losQueParticipa() {
        assertEquals(List.of("Komikan", "MicroBit"), servicio.cursosParaValorar("roro"));
        assertTrue(servicio.cursosParaValorar("nuevo").isEmpty());
    }

    @Test
    void valorar_actualizaPromedioResumenYMiPuntaje() {
        DTValoracionCurso d = servicio.valorarCurso("roro", "MicroBit", 4);
        assertEquals(new DTValoracionCurso("MicroBit", 4.0, 1, List.of(0, 0, 0, 1, 0), 4), d);

        d = servicio.valorarCurso("weiss", "MicroBit", 1);
        assertEquals(2.5, d.promedio(), 1e-9);
        assertEquals(2, d.cantidad());
        assertEquals(List.of(1, 0, 0, 1, 0), d.porPuntaje());
        assertEquals(1, d.miPuntaje());
    }

    @Test
    void consultar_sinValoraciones_promedioCeroYSinPuntajePropio() {
        assertEquals(new DTValoracionCurso("Komikan", 0, 0, List.of(0, 0, 0, 0, 0), null),
                servicio.consultarValoracion("Komikan", "roro"));
    }

    @Test
    void consultar_muestraElPuntajeDeQuienConsulta() {
        servicio.valorarCurso("roro", "MicroBit", 5);
        assertEquals(5, servicio.consultarValoracion("MicroBit", "roro").miPuntaje());
        assertNull(servicio.consultarValoracion("MicroBit", "weiss").miPuntaje(), "weiss no lo valoro");
        assertNull(servicio.consultarValoracion("MicroBit", null).miPuntaje(), "visitante");
        assertEquals(1, servicio.consultarValoracion("MicroBit", null).cantidad());
    }

    @Test
    void unaSolaVezPorCurso() {
        servicio.valorarCurso("roro", "MicroBit", 3);
        ReglaNegocioException e = assertThrows(ReglaNegocioException.class, () -> servicio.valorarCurso("roro", "MicroBit", 5));
        assertEquals("Ya valoraste este curso: cada curso se valora una sola vez.", e.getMessage());
        assertEquals(3, guardadas.get("roro|MicroBit"), "no cambia el primer puntaje");
        servicio.valorarCurso("roro", "Komikan", 5);   // otro curso si se puede
    }

    @Test
    void puntajeFueraDeEscala_seRechaza() {
        for (int p : new int[]{0, 6, -1}) {
            ReglaNegocioException e = assertThrows(ReglaNegocioException.class, () -> servicio.valorarCurso("roro", "MicroBit", p));
            assertEquals("El puntaje debe ser de 1 a 5 estrellas.", e.getMessage());
        }
        assertTrue(guardadas.isEmpty());
    }

    @Test
    void soloSiParticipaDelCurso() {
        ReglaNegocioException e = assertThrows(ReglaNegocioException.class, () -> servicio.valorarCurso("weiss", "Komikan", 4));
        assertEquals("Solo podés valorar cursos en los que participaste.", e.getMessage());
        assertThrows(ReglaNegocioException.class, () -> servicio.valorarCurso("nuevo", "MicroBit", 4));
        assertTrue(guardadas.isEmpty());
    }

    @Test
    void soloEstudiantesYCursosQueExisten() {
        for (String nick : new String[]{"house", null}) {
            ReglaNegocioException e = assertThrows(ReglaNegocioException.class, () -> servicio.valorarCurso(nick, "MicroBit", 4));
            assertEquals("Solo un estudiante puede valorar cursos.", e.getMessage());
            assertThrows(ReglaNegocioException.class, () -> servicio.cursosParaValorar(nick));
        }
        ReglaNegocioException e = assertThrows(ReglaNegocioException.class, () -> servicio.valorarCurso("roro", "Nada", 4));
        assertEquals("No existe el curso \"Nada\".", e.getMessage());
        assertThrows(ReglaNegocioException.class, () -> servicio.consultarValoracion(null, "roro"));
    }
}
