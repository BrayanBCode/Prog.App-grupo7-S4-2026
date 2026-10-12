package ServidorCentral.Logica.services;

import ServidorCentral.Logica.entities.usuarios.Estudiante;
import ServidorCentral.Logica.repositories.EstudianteRepository;
import ServidorCentral.Logica.repositories.ProgramaFormacionRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas SIN base de datos de "Inscripcion a Programa de Formacion". */
class InscripcionProgramaServiceTest {

    private final Estudiante weiss = new Estudiante("weiss", "aweiss@hotmail.com", "Adrian", "Weiss",
            LocalDate.of(1978, 12, 23), null, "abcd");
    /** Inscripciones ya existentes, como "nickname|programa". */
    private final List<String> inscripciones = new ArrayList<>();
    private LocalDate fechaGuardada;

    private ProgramaFormacionService servicio() {
        ProgramaFormacionRepository repo = new ProgramaFormacionRepository(null) {
            @Override
            public boolean existe(String nombrePrograma) {
                return "EFI Robotica".equals(nombrePrograma);
            }

            @Override
            public boolean existeInscripcion(String nickname, String nombrePrograma) {
                return inscripciones.contains(nickname + "|" + nombrePrograma);
            }

            @Override
            public void inscribir(String nickname, String mail, String nombrePrograma, LocalDate fecha) {
                inscripciones.add(nickname + "|" + nombrePrograma);
                fechaGuardada = fecha;
            }
        };
        EstudianteRepository estudiantes = new EstudianteRepository(null) {
            @Override
            public Estudiante buscarPorNicknameYMail(String nickname, String mail) {
                return "weiss".equals(nickname) && "aweiss@hotmail.com".equals(mail) ? weiss : null;
            }
        };
        return new ProgramaFormacionService(repo, null, estudiantes);
    }

    @Test
    void estudianteSeInscribeAProgramaExistente() throws Exception {
        LocalDate hoy = LocalDate.of(2026, 10, 12);

        servicio().inscribirEstudiante("weiss", "aweiss@hotmail.com", "EFI Robotica", hoy);

        assertEquals(List.of("weiss|EFI Robotica"), inscripciones);
        assertEquals(hoy, fechaGuardada);
    }

    @Test
    void inscribirseDosVecesAlMismoPrograma_falla() throws Exception {
        ProgramaFormacionService s = servicio();
        s.inscribirEstudiante("weiss", "aweiss@hotmail.com", "EFI Robotica", LocalDate.now());

        assertThrows(Exception.class, () ->
                s.inscribirEstudiante("weiss", "aweiss@hotmail.com", "EFI Robotica", LocalDate.now()));
        assertEquals(1, inscripciones.size());
    }

    @Test
    void programaInexistente_falla() {
        Exception e = assertThrows(Exception.class, () ->
                servicio().inscribirEstudiante("weiss", "aweiss@hotmail.com", "No existe", LocalDate.now()));

        assertTrue(e.getMessage().contains("No existe"));
        assertTrue(inscripciones.isEmpty());
    }

    @Test
    void estudianteInexistente_falla() {
        assertThrows(Exception.class, () ->
                servicio().inscribirEstudiante("fantasma", "f@x.com", "EFI Robotica", LocalDate.now()));
        assertTrue(inscripciones.isEmpty());
    }
}
