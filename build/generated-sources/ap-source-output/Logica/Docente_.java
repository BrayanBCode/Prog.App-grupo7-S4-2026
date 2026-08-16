package Logica;

import Logica.Curso;
import Logica.EdicionCurso;
import Logica.Instituto;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2026-08-13T23:04:46", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Docente.class)
public class Docente_ extends Usuario_ {

    public static volatile ListAttribute<Docente, Curso> cursos;
    public static volatile ListAttribute<Docente, EdicionCurso> edicionesC;
    public static volatile ListAttribute<Docente, Instituto> institutos;
    public static volatile SingularAttribute<Docente, Long> id;

}