package Logica;

import Logica.Curso;
import Logica.Docente;
import Logica.Facultad;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2026-08-13T23:04:46", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Instituto.class)
public class Instituto_ { 

    public static volatile ListAttribute<Instituto, Curso> cursos;
    public static volatile SingularAttribute<Instituto, String> nombre;
    public static volatile ListAttribute<Instituto, Docente> docentes;
    public static volatile SingularAttribute<Instituto, Facultad> facultad;

}