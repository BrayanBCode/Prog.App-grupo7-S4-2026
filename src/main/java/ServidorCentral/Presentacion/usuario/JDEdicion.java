package ServidorCentral.Presentacion.usuario;

import ServidorCentral.Logica.controller.IController;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

/** Información completa de una edición de curso: datos, docentes y estudiantes inscriptos. */
public class JDEdicion extends JDialog {

    private static final String[] ETIQUETAS = {
        "Nombre", "Curso", "Fecha de inicio", "Fecha de fin", "Cupo", "Fecha de publicación"
    };
    private static final int FILA_CURSO = 1;

    public static void mostrar(Window owner, IController control, String nombreEdicion) {
        try {
            String[] datos = control.obtenerEdicionCurso(nombreEdicion);
            new JDEdicion(owner, control, datos, nombreEdicion).setVisible(true);
        } catch (Exception ex) {
            Ui.error(owner, ex.getMessage());
        }
    }

    private JDEdicion(Window owner, IController control, String[] d, String nombreEdicion) {
        super(owner, "Información de Edición de Curso", ModalityType.MODELESS);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        List<Object[]> filas = new ArrayList<>();
        for (int i = 0; i < ETIQUETAS.length && i < d.length; i++) {
            filas.add(new Object[]{ETIQUETAS[i], d[i]});
        }
        JTable tDatos = Ui.tabla(new String[]{"Campo", "Valor"}, filas);
        Ui.alClickear(tDatos, (f, c) -> {
            if (f == FILA_CURSO && d.length > FILA_CURSO && d[FILA_CURSO] != null && !d[FILA_CURSO].isEmpty()) {
                JDCurso.mostrar(this, control, d[FILA_CURSO]);
            }
        });

        List<Object[]> filasDoc = new ArrayList<>();
        List<String> docentes = control.listarDocentesEdicion(nombreEdicion);
        if (docentes != null) {
            for (String x : docentes) filasDoc.add(new Object[]{x});
        }
        JTable tDocentes = Ui.tabla(new String[]{"Docentes"}, filasDoc);

        List<Object[]> filasEst = new ArrayList<>();
        List<String> estudiantes = control.listarEstudiantesEdicion(nombreEdicion);
        if (estudiantes != null) {
            for (String x : estudiantes) filasEst.add(new Object[]{x});
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

        setPreferredSize(new java.awt.Dimension(1366, 768));
        setResizable(false);
        pack();
        Ui.ubicar(this, owner);
    }
}