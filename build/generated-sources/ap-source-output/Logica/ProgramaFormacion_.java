package Logica;

import Logica.Curso;
import Logica.InscripcionPrograma;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2026-08-13T23:04:46", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(ProgramaFormacion.class)
public class ProgramaFormacion_ { 

    public static volatile SingularAttribute<ProgramaFormacion, String> descripcion;
    public static volatile ListAttribute<ProgramaFormacion, InscripcionPrograma> pInscripciones;
    public static volatile ListAttribute<ProgramaFormacion, Curso> cursos;
    public static volatile SingularAttribute<ProgramaFormacion, LocalDate> fechaInicio;
    public static volatile SingularAttribute<ProgramaFormacion, Long> id;
    public static volatile SingularAttribute<ProgramaFormacion, String> nombre;
    public static volatile SingularAttribute<ProgramaFormacion, LocalDate> fechaFin;

}