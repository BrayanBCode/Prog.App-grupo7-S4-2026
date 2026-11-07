package ServidorCentral.Logica.entities.cursos;

import ServidorCentral.Exepciones.ReglaNegocioException;
import ServidorCentral.Logica.entities.usuarios.Estudiante;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class InscripcionEdicion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ESTUDIANTE_NICKNAME", referencedColumnName = "NICKNAME"),
            @JoinColumn(name = "ESTUDIANTE_MAIL", referencedColumnName = "MAIL")
    })
    private Estudiante estudiante;

    @ManyToOne
    private EdicionCurso edicionCurso;

    private LocalDate fechaInscripcion;

    @Enumerated(EnumType.STRING)
    private EstadoInscripcion estado = EstadoInscripcion.INSCRIPTO;

    /** Resultado de la evaluacion, de 1 a 12 (null mientras el docente no la ingresa). */
    private Integer nota;

    // Constructores
    public InscripcionEdicion() {}

    public InscripcionEdicion(LocalDate fechaInscripcion, EdicionCurso edicionCurso, Estudiante estudiante) {
        this.fechaInscripcion = fechaInscripcion;
        this.edicionCurso = edicionCurso;
        this.estudiante = estudiante;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    public Estudiante getEstudiante() { return estudiante; }

    public void setEdicionCurso(EdicionCurso edicionCurso) { this.edicionCurso = edicionCurso; }
    public EdicionCurso getEdicionCurso() { return edicionCurso; }

    public void setFechaInscripcion(LocalDate fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
    public LocalDate getFechaInscripcion() { return fechaInscripcion; }

    public EstadoInscripcion getEstado() { return estado; }
    public void setEstado(EstadoInscripcion estado) { this.estado = estado; }

    public Integer getNota() { return nota; }

    public static final int NOTA_MINIMA = 1;
    public static final int NOTA_MAXIMA = 12;

    /**
     * Registra el resultado de la evaluacion (letra 2026, "Ingresar resultados de evaluaciones"): solo para
     * estudiantes aceptados, en la escala 1 a 12 y mientras la edicion no este cerrada.
     */
    public void calificar(int nota) {
        if (estado != EstadoInscripcion.ACEPTADA) {
            throw new ReglaNegocioException("Solo se califica a estudiantes aceptados en la edición.");
        }
        if (nota < NOTA_MINIMA || nota > NOTA_MAXIMA) {
            throw new ReglaNegocioException("La nota debe estar entre " + NOTA_MINIMA + " y " + NOTA_MAXIMA + ".");
        }
        if (edicionCurso != null && edicionCurso.isCerrada()) {
            throw new ReglaNegocioException("La edición está cerrada: no se pueden modificar las notas.");
        }
        this.nota = nota;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InscripcionEdicion)) {
            return false;
        }
        InscripcionEdicion other = (InscripcionEdicion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.InscripcionEdicion[ id=" + id + " ]";
    }
}