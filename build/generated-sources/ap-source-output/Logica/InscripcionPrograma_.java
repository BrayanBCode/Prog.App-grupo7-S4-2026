package Logica;

import Logica.Estudiante;
import Logica.ProgramaFormacion;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2026-08-13T23:04:46", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(InscripcionPrograma.class)
public class InscripcionPrograma_ { 

    public static volatile SingularAttribute<InscripcionPrograma, Estudiante> estudiante;
    public static volatile SingularAttribute<InscripcionPrograma, ProgramaFormacion> pFormacion;
    public static volatile SingularAttribute<InscripcionPrograma, Long> id;
    public static volatile SingularAttribute<InscripcionPrograma, LocalDate> fechaInscripcion;

}