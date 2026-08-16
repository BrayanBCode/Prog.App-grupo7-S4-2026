package Logica;

import Logica.InscripcionEdicion;
import Logica.InscripcionPrograma;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2026-08-13T23:04:46", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Estudiante.class)
public class Estudiante_ extends Usuario_ {

    public static volatile ListAttribute<Estudiante, InscripcionPrograma> pInscripcion;
    public static volatile ListAttribute<Estudiante, InscripcionEdicion> inscripciones;
    public static volatile SingularAttribute<Estudiante, Long> id;

}