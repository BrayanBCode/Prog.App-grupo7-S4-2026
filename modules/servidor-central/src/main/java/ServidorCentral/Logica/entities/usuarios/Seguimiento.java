package ServidorCentral.Logica.entities.usuarios;

import javax.persistence.*;
import java.io.Serializable;

/**
 * "Usuario X sigue a usuario Y" (casos de uso Seguir / Dejar de seguir un Usuario).
 * Se guarda por nickname (que es unico entre todos los usuarios): asi no hace falta una
 * relacion JPA sobre la clave compuesta (nickname, mail) de Usuario.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"SEGUIDOR", "SEGUIDO"}))
public class Seguimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "SEGUIDOR", nullable = false)
    private String seguidor;

    @Column(name = "SEGUIDO", nullable = false)
    private String seguido;

    public Seguimiento() {}

    public Seguimiento(String seguidor, String seguido) {
        this.seguidor = seguidor;
        this.seguido = seguido;
    }

    public Long getId() { return id; }
    public String getSeguidor() { return seguidor; }
    public String getSeguido() { return seguido; }
}
