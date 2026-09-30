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

/** Información completa de un curso: datos, ediciones y programas de formación (ambos clickeables). */
public class JDCurso extends JDialog {

    private static final String[] ETIQUETAS = {
        "Nombre", "Descripción", "Duración", "Cantidad de horas", "Créditos",
        "URL", "Fecha de registro", "Instituto", "Docente", "Previas"
    };
    private static final int FILA_INSTITUTO = 7;

    public static void mostrar(Window owner, IController control, String nombreCurso) {
        try {
            String[] datos = control.obtenerDataCurso(nombreCurso);
            new JDCurso(owner, control, datos, nombreCurso).setVisible(true);
        } catch (Exception ex) {
            Ui.error(owner, ex.getMessage());
        }
    }

    private JDCurso(Window owner, IController control, String[] d, String nombreCurso) {
        super(owner, "Información de Curso", ModalityType.MODELESS);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Datos del curso
        List<Object[]> filas = new ArrayList<>();
        for (int i = 0; i < ETIQUETAS.length && i < d.length; i++) {
            filas.add(new Object[]{ETIQUETAS[i], d[i]});
        }
        JTable tDatos = Ui.tabla(new String[]{"Campo", "Valor"}, filas);
        tDatos.getColumnModel().getColumn(0).setPreferredWidth(140);
        tDatos.getColumnModel().getColumn(1).setPreferredWidth(420);
        Ui.alClickear(tDatos, (f, c) -> {
            if (f == FILA_INSTITUTO && d.length > FILA_INSTITUTO && d[FILA_INSTITUTO] != null && !d[FILA_INSTITUTO].isEmpty()) {
                JDInstituto.mostrar(this, control, d[FILA_INSTITUTO]);
            }
        });

        // Ediciones del curso
        List<Object[]> filasEd = new ArrayList<>();
        List<String> ediciones = control.listarEdicionesCurso(nombreCurso);
        if (ediciones != null) {
            for (String e : ediciones) filasEd.add(new Object[]{e});
        }
        JTable tEdiciones = Ui.tabla(new String[]{"Ediciones"}, filasEd);
        Ui.alClickear(tEdiciones, (f, c) -> {
            String ed = Ui.texto(tEdiciones, f, 0);
            if (ed != null) JDEdicion.mostrar(this, control, ed);
        });

        // Programas de formación que incluyen el curso
        List<Object[]> filasPr = new ArrayList<>();
        List<String> programas = control.listarProgramasPorCurso(nombreCurso);
        if (programas != null) {
            for (String p : programas) filasPr.add(new Object[]{p});
        }
        JTable tProgramas = Ui.tabla(new String[]{"Programas de formación"}, filasPr);
        Ui.alClickear(tProgramas, (f, c) -> {
            String p = Ui.texto(tProgramas, f, 0);
            if (p != null) JDPrograma.mostrar(this, control, p);
        });

        JPanel abajo = new JPanel(new GridLayout(1, 2, 10, 0));
        abajo.add(Ui.scroll(tEdiciones, 270, 150));
        abajo.add(Ui.scroll(tProgramas, 270, 150));

        JPanel contenido = new JPanel(new BorderLayout(0, 10));
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contenido.add(Ui.scroll(tDatos, 560, 235), BorderLayout.NORTH);
        contenido.add(abajo, BorderLayout.CENTER);
        contenido.add(new JLabel("Hacé click en el instituto, una edición o un programa para ver su información."), BorderLayout.SOUTH);
        setContentPane(contenido);

        setPreferredSize(new java.awt.Dimension(1366, 768));
        setResizable(false);
        pack();
        Ui.ubicar(this, owner);
    }
}