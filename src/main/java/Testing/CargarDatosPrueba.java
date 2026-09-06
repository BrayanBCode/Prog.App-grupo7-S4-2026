package Testing;

import Logica.cursos.Curso;
import Logica.cursos.EdicionCurso;
import Logica.cursos.InscripcionEdicion;
import Logica.cursos.Instituto;
import Logica.programaFormacion.ProgramaFormacion;
import Logica.usuarios.Docente;
import Logica.usuarios.Estudiante;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CargarDatosPrueba {

    private final EntityManager em;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CargarDatosPrueba(EntityManager em) {
        this.em = em;
    }

    private LocalDate parseFecha(String fecha) {return LocalDate.parse(fecha, fmt);}

    public boolean isBDloaded() {
        Long cantidadInstitutos = em.createQuery("SELECT COUNT(i) FROM Instituto i", Long.class)
                .getSingleResult();
        return cantidadInstitutos > 0;
    }

    public void cargar() {
        if (isBDloaded()) return;

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // 1. INSTITUTOS
            Instituto in = new Instituto("INCO");
            Instituto il = new Instituto("IMERL");
            Instituto ifi = new Instituto("Física");
            Instituto im = new Instituto("IMPII");
            Instituto ie = new Instituto("Eléctrica");
            Instituto di = new Instituto("DISI");

            em.persist(in);
            em.persist(il);
            em.persist(ifi);
            em.persist(im);
            em.persist(ie);
            em.persist(di);

            // 2. DOCENTES
            Docente ww = new Docente("heisenberg", "Walter", "White", "heisenberg@gmail.com", parseFecha("07/03/1956"), "");
            Docente ok = new Docente("benkenobi", "Obi-Wan", "Kenobi", "benKenobi@gmail.com", parseFecha("02/04/1914"), "");
            Docente ew = new Docente("waston", "Emma", "Watson", "e.watson@gmail.com", parseFecha("15/04/1990"), "");
            Docente gh = new Docente("house", "Gregory", "House", "greghouse@gmail.com", parseFecha("15/05/1959"), "");
            Docente tc = new Docente("timmy", "Tim", "Cook", "tim.cook@apple.com", parseFecha("01/11/1960"), "");
            Docente dr = new Docente("danny", "Daniel", "Riccio", "dan.riccio@gmail.com", parseFecha("05/07/1963"), "");
            Docente ps = new Docente("phils", "Philip", "Schiller", "schiller@gmail.com", parseFecha("07/10/1961"), "");
            Docente bs = new Docente("bruces", "Bruce", "Sewell", "sewell@gmail.com", parseFecha("03/12/1959"), "");
            Docente ag = new Docente("adri", "Adriana", "García", "agarcia@gmail.com", parseFecha("28/07/1978"), "");

            ww.getInstitutos().add(in); in.getDocentes().add(ww);
            ok.getInstitutos().add(in); in.getDocentes().add(ok);
            ew.getInstitutos().add(in); in.getDocentes().add(ew);
            gh.getInstitutos().add(ie); ie.getDocentes().add(gh);
            tc.getInstitutos().add(il); il.getDocentes().add(tc);
            dr.getInstitutos().add(il); il.getDocentes().add(dr);
            ps.getInstitutos().add(im); im.getDocentes().add(ps);
            bs.getInstitutos().add(di); di.getDocentes().add(bs);
            ag.getInstitutos().add(di); di.getDocentes().add(ag);

            em.persist(ww); em.persist(ok); em.persist(ew);
            em.persist(gh); em.persist(tc); em.persist(dr);
            em.persist(ps); em.persist(bs); em.persist(ag);

            // 3. ESTUDIANTES
            Estudiante el = new Estudiante("eleven11", "Eleven", "Twelve", "eleven11@gmail.com", parseFecha("31/12/1971"), "");
            Estudiante co = new Estudiante("costas", "Gerardo", "Costas", "gcostas@gmail.com", parseFecha("15/11/1983"), "");
            Estudiante ro = new Estudiante("roro", "Rodrigo", "Cotelo", "rcotelo@yahoo.com", parseFecha("02/08/1975"), "");
            Estudiante ch = new Estudiante("chechi", "Cecilia", "Garrido", "cgarrido@hotmail.com", parseFecha("12/09/1987"), "");
            Estudiante jw = new Estudiante("jeffw", "Jeff", "Williams", "jwilliams@gmail.com", parseFecha("27/11/1964"), "");
            Estudiante we = new Estudiante("weiss", "Adrian", "Weiss", "aweiss@hotmail.com", parseFecha("23/12/1978"), "");

            em.persist(el); em.persist(co); em.persist(ro);
            em.persist(ch); em.persist(jw); em.persist(we);

            // 4. CURSOS
            Curso c1 = new Curso("Talleres plenarios", "Talleres plenarios*: presentados por cuatro reconocidos matemáticos uruguayos...", 3, 15.0f, 1, parseFecha("01/02/2026"), "www.tmu.edu.uy", il);
            Curso c2 = new Curso("Seminarios de Resolución de Problemas", "Seminario, todos los jueves en Facultad de Ingeniería...", 5, 30.0f, 2, parseFecha("12/07/2026"), "www.tmu.edu.uy", il);
            Curso c3 = new Curso("Dalavuelta", "Dalavuelta es un proyecto de extensión que nace en el IIMPI...", 10, 60.0f, 4, parseFecha("25/06/2024"), "https://eva.fing.edu.uy/course/view.php?id=783#section-2", im);
            Curso c4 = new Curso("Extensionismo Industrial", "El proyecto tiene como objetivo desarrollar intervenciones curriculares...", 12, 75.0f, 5, parseFecha("16/06/2025"), "https://eva.fing.edu.uy/course/view.php?id=783#section-2", im);
            Curso c5 = new Curso("Inclusión Energética", "En el proyecto se conjuga el trabajo de docentes y estudiantes...", 6, 45.0f, 3, parseFecha("01/02/2026"), "https://eva.fing.edu.uy/course/view.php?id=783#section-2", im);
            Curso c6 = new Curso("Flor del Ceibo", "Flor de Ceibo es un proyecto central de la Universidad de la República...", 15, 150.0f, 10, parseFecha("27/07/2008"), "http://www.flordeceibo.edu.uy/", di);
            Curso c7 = new Curso("Taller de robótica educativa.", "La asignatura se organiza en dos etapas...", 8, 90.0f, 6, parseFecha("02/02/2024"), "https://eva.fing.edu.uy/course/view.php?id=1187", in);
            Curso c8 = new Curso("Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela", "Se propone desarrollar una aplicación interactiva...", 9, 45.0f, 3, parseFecha("15/06/2026"), "https://eva.fing.edu.uy/mod/folder/view.php?id=89398", in);
            Curso c9 = new Curso("Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación", "Se realizarán visitas a escuelas rurales...", 12, 60.0f, 4, parseFecha("24/05/2026"), "https://eva.fing.edu.uy/mod/folder/view.php?id=89398", in);
            Curso c10 = new Curso("MicroBit", "El Centro Ceibal se encuentra distribuyendo placas micro:bit...", 15, 105.0f, 7, parseFecha("13/03/2026"), "https://www.fing.edu.uy/noticias/extension/modulo-de-tallerextension-microbit", ie);

            c2.getPrevias().add(c1);
            c3.getPrevias().add(c1);
            c4.getPrevias().add(c1);

            em.persist(c1); em.persist(c2); em.persist(c3); em.persist(c4); em.persist(c5);
            em.persist(c6); em.persist(c7); em.persist(c8); em.persist(c9); em.persist(c10);

            // 5. EDICIONES DE CURSOS
            EdicionCurso e1 = new EdicionCurso("Flor del Ceibo - 2010", c6, parseFecha("15/03/2010"), parseFecha("07/07/2010"), -1, parseFecha("16/02/2010"));
            e1.getDocentes().add(bs);

            EdicionCurso e2 = new EdicionCurso("Flor del Ceibo - 2012", c6, parseFecha("01/08/2012"), parseFecha("20/11/2012"), -1, parseFecha("10/07/2012"));
            e2.getDocentes().add(bs); e2.getDocentes().add(ag);

            EdicionCurso e3 = new EdicionCurso("Flor del Ceibo - 2025", c6, parseFecha("10/04/2025"), parseFecha("07/08/2025"), -1, parseFecha("06/03/2025"));
            e3.getDocentes().add(bs); e3.getDocentes().add(ag);

            EdicionCurso e4 = new EdicionCurso("Dalavuelta - 2025", c3, parseFecha("20/08/2024"), parseFecha("10/11/2024"), 15, parseFecha("20/07/2024"));
            e4.getDocentes().add(ps);

            EdicionCurso e5 = new EdicionCurso("Extensionismo Industrial - 2025", c4, parseFecha("10/08/2025"), parseFecha("10/11/2025"), 15, parseFecha("08/07/2025"));
            e5.getDocentes().add(ps);

            EdicionCurso e6 = new EdicionCurso("Inclusión Energética - 2026", c5, parseFecha("15/03/2026"), parseFecha("30/04/2026"), 30, parseFecha("20/02/2026"));
            e6.getDocentes().add(ps);

            EdicionCurso e7 = new EdicionCurso("Taller de robótica educativa - 2024", c7, parseFecha("10/03/2024"), parseFecha("10/05/2024"), 10, parseFecha("15/02/2024"));
            e7.getDocentes().add(ww);

            EdicionCurso e8 = new EdicionCurso("Taller de robótica educativa - 2026", c7, parseFecha("10/03/2026"), parseFecha("10/05/2026"), 10, parseFecha("15/02/2026"));
            e8.getDocentes().add(ww); e8.getDocentes().add(ok);

            EdicionCurso e9 = new EdicionCurso("Taller de robótica educativa-2026-2", c7, parseFecha("10/09/2026"), parseFecha("08/11/2026"), 20, parseFecha("15/08/2026"));
            e9.getDocentes().add(ok); e9.getDocentes().add(ew);

            EdicionCurso e10 = new EdicionCurso("Participación en investigación sobre el empleo del juego Komikan como recurso didáctico en la Escuela - 2026", c8, parseFecha("29/07/2026"), parseFecha("07/10/2026"), 5, parseFecha("10/07/2026"));
            e10.getDocentes().add(ew);

            EdicionCurso e11 = new EdicionCurso("Herramientas de apoyo a la enseñanza de inglés. Instalación y evaluación - 26", c9, parseFecha("15/09/2026"), parseFecha("15/12/2026"), 5, parseFecha("02/06/2026"));
            e11.getDocentes().add(ww);

            EdicionCurso e12 = new EdicionCurso("MicroBit-2026", c10, parseFecha("12/08/2026"), parseFecha("05/12/2026"), 30, parseFecha("02/07/2026"));
            e12.getDocentes().add(gh);

            EdicionCurso e13 = new EdicionCurso("Talleres plenarios - 2026", c1, parseFecha("10/03/2026"), parseFecha("30/03/2026"), -1, parseFecha("02/03/2026"));
            e13.getDocentes().add(tc); e13.getDocentes().add(dr);

            EdicionCurso e14 = new EdicionCurso("Seminarios de Resolución de Problemas - 2026", c2, parseFecha("10/09/2026"), parseFecha("20/10/2026"), -1, parseFecha("12/07/2026"));
            e14.getDocentes().add(tc);

            em.persist(e1); em.persist(e2); em.persist(e3); em.persist(e4); em.persist(e5);
            em.persist(e6); em.persist(e7); em.persist(e8); em.persist(e9); em.persist(e10);
            em.persist(e11); em.persist(e12); em.persist(e13); em.persist(e14);

            // 6. INSCRIPCIONES A EDICIONES
            em.persist(new InscripcionEdicion(parseFecha("20/02/2010"), e1, el));
            em.persist(new InscripcionEdicion(parseFecha("25/02/2010"), e1, ch));
            em.persist(new InscripcionEdicion(parseFecha("12/07/2012"), e2, co));
            em.persist(new InscripcionEdicion(parseFecha("15/07/2012"), e2, ro));
            em.persist(new InscripcionEdicion(parseFecha("30/07/2012"), e2, we));
            em.persist(new InscripcionEdicion(parseFecha("10/03/2025"), e3, ro));
            em.persist(new InscripcionEdicion(parseFecha("15/03/2025"), e3, jw));
            em.persist(new InscripcionEdicion(parseFecha("25/07/2024"), e4, ch));
            em.persist(new InscripcionEdicion(parseFecha("28/07/2024"), e4, el));
            em.persist(new InscripcionEdicion(parseFecha("02/08/2024"), e4, ro));
            em.persist(new InscripcionEdicion(parseFecha("10/08/2024"), e4, co));
            em.persist(new InscripcionEdicion(parseFecha("15/08/2024"), e4, jw));
            em.persist(new InscripcionEdicion(parseFecha("18/07/2025"), e5, co));
            em.persist(new InscripcionEdicion(parseFecha("20/07/2025"), e5, ch));
            em.persist(new InscripcionEdicion(parseFecha("29/07/2025"), e5, el));
            em.persist(new InscripcionEdicion(parseFecha("05/08/2025"), e5, we));
            em.persist(new InscripcionEdicion(parseFecha("23/02/2026"), e6, ro));
            em.persist(new InscripcionEdicion(parseFecha("25/02/2026"), e6, we));
            em.persist(new InscripcionEdicion(parseFecha("28/02/2026"), e6, ch));
            em.persist(new InscripcionEdicion(parseFecha("03/03/2026"), e6, el));
            em.persist(new InscripcionEdicion(parseFecha("18/02/2017"), e7, we));
            em.persist(new InscripcionEdicion(parseFecha("20/02/2024"), e7, ro));
            em.persist(new InscripcionEdicion(parseFecha("03/03/2024"), e7, el));
            em.persist(new InscripcionEdicion(parseFecha("05/03/2024"), e7, ch));
            em.persist(new InscripcionEdicion(parseFecha("18/02/2026"), e8, jw));
            em.persist(new InscripcionEdicion(parseFecha("22/02/2026"), e8, co));
            em.persist(new InscripcionEdicion(parseFecha("18/08/2026"), e9, we));
            em.persist(new InscripcionEdicion(parseFecha("22/08/2026"), e9, ch));
            em.persist(new InscripcionEdicion(parseFecha("03/09/2026"), e9, ro));
            em.persist(new InscripcionEdicion(parseFecha("13/07/2026"), e10, ch));
            em.persist(new InscripcionEdicion(parseFecha("20/07/2026"), e10, we));
            em.persist(new InscripcionEdicion(parseFecha("22/07/2026"), e10, ro));
            em.persist(new InscripcionEdicion(parseFecha("04/06/2026"), e11, we));
            em.persist(new InscripcionEdicion(parseFecha("18/07/2026"), e11, el));
            em.persist(new InscripcionEdicion(parseFecha("20/08/2026"), e11, jw));
            em.persist(new InscripcionEdicion(parseFecha("12/07/2026"), e12, ch));
            em.persist(new InscripcionEdicion(parseFecha("14/07/2026"), e12, ro));
            em.persist(new InscripcionEdicion(parseFecha("25/07/2026"), e12, el));
            em.persist(new InscripcionEdicion(parseFecha("05/08/2026"), e12, jw));
            em.persist(new InscripcionEdicion(parseFecha("05/03/2026"), e13, co));
            em.persist(new InscripcionEdicion(parseFecha("04/03/2026"), e13, we));
            em.persist(new InscripcionEdicion(parseFecha("07/03/2026"), e13, ro));
            em.persist(new InscripcionEdicion(parseFecha("15/07/2026"), e14, we));
            em.persist(new InscripcionEdicion(parseFecha("20/07/2026"), e14, co));
            em.persist(new InscripcionEdicion(parseFecha("06/08/2026"), e14, ro));
            em.persist(new InscripcionEdicion(parseFecha("30/08/2026"), e14, ch));

            // 7. PROGRAMAS DE FORMACIÓN
            ProgramaFormacion p1 = new ProgramaFormacion("EFI Ingeniería Mecánica", "Programa mecánica", parseFecha("01/05/2026"), parseFecha("31/10/2026"), parseFecha("01/01/2026"));
            p1.getCursos().add(c3); p1.getCursos().add(c4); p1.getCursos().add(c5);

            ProgramaFormacion p2 = new ProgramaFormacion("Formación integral", "Programa varios institutos", parseFecha("15/07/2026"), parseFecha("01/01/2027"), parseFecha("01/01/2026"));
            p2.getCursos().add(c2); p2.getCursos().add(c4); p2.getCursos().add(c6); p2.getCursos().add(c8);

            ProgramaFormacion p3 = new ProgramaFormacion("EFI Robótica", "Programa robótica", parseFecha("03/09/2026"), parseFecha("18/11/2026"), parseFecha("01/01/2026"));
            p3.getCursos().add(c7); p3.getCursos().add(c10);

            em.persist(p1); em.persist(p2); em.persist(p3);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }
}