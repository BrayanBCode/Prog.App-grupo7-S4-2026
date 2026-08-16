package Logica;

import Logica.Curso;
import Logica.Docente;
import Logica.EdicionCurso;
import Logica.Instituto;
import Logica.ProgramaFormacion;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2026-08-13T23:04:46", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Curso.class)
public class Curso_ { 

    public static volatile SingularAttribute<Curso, String> descripcion;
    public static volatile ListAttribute<Curso, Curso> previas;
    public static volatile SingularAttribute<Curso, LocalDate> fechaRegistro;
    public static volatile SingularAttribute<Curso, Docente> docente;
    public static volatile SingularAttribute<Curso, String> nombre;
    public static volatile SingularAttribute<Curso, Integer> cantCreditos;
    public static volatile SingularAttribute<Curso, String> url;
    public static volatile SingularAttribute<Curso, Float> canthoras;
    public static volatile SingularAttribute<Curso, Instituto> instituto;
    public static volatile ListAttribute<Curso, EdicionCurso> edCursos;
    public static volatile SingularAttribute<Curso, Integer> duracion;
    public static volatile ListAttribute<Curso, ProgramaFormacion> pFormaciones;
    public static volatile ListAttribute<Curso, Curso> esPreviaDe;

}