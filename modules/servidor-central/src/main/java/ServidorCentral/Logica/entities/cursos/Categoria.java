package ServidorCentral.Logica.entities.cursos;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Categoria (letra 6.1, "Alta de Categoria"): el nombre es unico y es la clave.
 * La relacion con Curso / ProgramaFormacion se agrega cuando se implemente
 * "elegir categorias" en Alta de Curso.
 */
@Entity
public class Categoria implements Serializable {

    @Id
    private String nombre;

    public Categoria() {}

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
