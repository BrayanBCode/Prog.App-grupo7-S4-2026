package Logica;

import Logica.EdicionCurso;
import Logica.Estudiante;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2026-08-13T23:04:46", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(InscripcionEdicion.class)
public class InscripcionEdicion_ { 

    public static volatile SingularAttribute<InscripcionEdicion, Estudiante> estudiante;
    public static volatile SingularAttribute<InscripcionEdicion, EdicionCurso> edicionCurso;
    public static volatile SingularAttribute<InscripcionEdicion, Long> id;
    public static volatile SingularAttribute<InscripcionEdicion, LocalDate> fechaInscripcion;

}