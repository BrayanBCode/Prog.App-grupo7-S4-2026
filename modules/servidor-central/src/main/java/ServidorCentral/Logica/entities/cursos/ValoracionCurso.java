package ServidorCentral.Logica.entities.cursos;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * "El estudiante X le dio N estrellas al curso Y" (caso de uso Valorar un Curso, Tarea 3).
 * La valoracion es del curso, no de la edicion: el estudiante la hace desde cualquier edicion en la que participo,
 * y una sola vez por curso (de ahi la restriccion unica). Se guarda por nickname y nombre del curso, como Seguimiento.
 */
@Entity
@Table(name = "VALORACIONCURSO", uniqueConstraints = @UniqueConstraint(columnNames = {"ESTUDIANTE", "CURSO"}))
public class ValoracionCurso implements Serializable {

    public static final int PUNTAJE_MINIMO = 1;
    public static final int PUNTAJE_MAXIMO = 5;

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ESTUDIANTE", nullable = false)
    private String estudiante;

    @Column(name = "CURSO", nullable = false)
    private String curso;

    @Column(name = "PUNTAJE", nullable = false)
    private int puntaje;

    @Column(name = "FECHA")
    private LocalDate fecha;

    public ValoracionCurso() {}

    public ValoracionCurso(String estudiante, String curso, int puntaje, LocalDate fecha) {
        this.estudiante = estudiante;
        this.curso = curso;
        this.puntaje = puntaje;
        this.fecha = fecha;
    }

    public Long getId() { return id; }
    public String getEstudiante() { return estudiante; }
    public String getCurso() { return curso; }
    public int getPuntaje() { return puntaje; }
    public LocalDate getFecha() { return fecha; }
}
