package Swing.usuario;

import ServidorCentral.Logica.controller.IControllerV2;
import ServidorCentral.Logica.datatypes.DTDocenteResumen;
import ServidorCentral.Logica.datatypes.DTEdicionCurso;
import ServidorCentral.Logica.datatypes.DTEstudianteResumen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Información completa de una edición de curso: datos, docentes y estudiantes inscriptos. */
public class JDEdicion extends JDialog {

    private static final String[] ETIQUETAS = {
        "Nombre", "Curso", "Fecha de inicio", "Fecha de fin", "Cupo", "Fecha de publicación"
    };
    private static final int FILA_CURSO = 1;

    public static void mostrar(Window owner, IControllerV2 control, String nombreEdicion) {
        try {
            DTEdicionCurso datos = control.obtenerEdicionCurso(nombreEdicion);
            new JDEdicion(owner, control, datos, nombreEdicion).setVisible(true);
        } catch (Exception ex) {
            Ui.error(owner, ex.getMessage());
        }
    }

    private JDEdicion(Window owner, IControllerV2 control, DTEdicionCurso d, String nombreEdicion) {
        super(owner, "Información de Edición de Curso", ModalityType.MODELESS);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Mismo orden que ETIQUETAS. Los valores son los que antes venían en el String[].
        String[] valores = {
                d.nombre(),
                d.nombreCurso(),
                texto(d.fechaInicio()),
                texto(d.fechaFin()),
                String.valueOf(d.cupo()),
                texto(d.fechaPublicacion())
        };

        List<Object[]> filas = new ArrayList<>();
        for (int i = 0; i < ETIQUETAS.length && i < valores.length; i++) {
            filas.add(new Object[]{ETIQUETAS[i], valores[i]});
        }
        JTable tDatos = Ui.tabla(new String[]{"Campo", "Valor"}, filas);
        Ui.alClickear(tDatos, (f, c) -> {
            String curso = d.nombreCurso();
            if (f == FILA_CURSO && curso != null && !curso.isEmpty()) {
                JDCurso.mostrar(this, control, curso);
            }
        });

        List<Object[]> filasDoc = new ArrayList<>();
        List<DTDocenteResumen> docentes = control.listarDocentesEdicion(nombreEdicion);
        if (docentes != null) {
            for (DTDocenteResumen doc : docentes) {
                filasDoc.add(new Object[]{nombreCompleto(doc.nombre(), doc.apellido(), doc.nickname())});
            }
        }
        JTable tDocentes = Ui.tabla(new String[]{"Docentes"}, filasDoc);

        List<Object[]> filasEst = new ArrayList<>();
        List<DTEstudianteResumen> estudiantes = control.listarEstudiantesEdicion(nombreEdicion);
        if (estudiantes != null) {
            for (DTEstudianteResumen est : estudiantes) {
                filasEst.add(new Object[]{nombreCompleto(est.nombre(), est.apellido(), est.nickname())});
            }
        }
        JTable tEstudiantes = Ui.tabla(new String[]{"Estudiantes inscriptos"}, filasEst);

        JPanel abajo = new JPanel(new GridLayout(1, 2, 10, 0));
        abajo.add(Ui.scroll(tDocentes, 270, 150));
        abajo.add(Ui.scroll(tEstudiantes, 270, 150));

        JPanel contenido = new JPanel(new BorderLayout(0, 10));
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contenido.add(Ui.scroll(tDatos, 560, 145), BorderLayout.NORTH);
        contenido.add(abajo, BorderLayout.CENTER);
        contenido.add(new JLabel("Hacé click en el curso para ver su información."), BorderLayout.SOUTH);
        setContentPane(contenido);

        pack();
        Ui.ubicar(this, owner);
    }

    /** Convierte cualquier valor a texto; null queda como "" en vez de "null". */
    private static String texto(Object o) {
        return o == null ? "" : o.toString();
    }

    /** Arma "Nombre Apellido (nickname)", el formato que antes venía ya armado desde el controller. */
    private static String nombreCompleto(String nombre, String apellido, String nickname) {
        return nombre + " " + apellido + " (" + nickname + ")";
    }
}