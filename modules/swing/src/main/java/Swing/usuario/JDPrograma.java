package Swing.usuario;

import ServidorCentral.Logica.controller.IController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Información completa de un programa de formación: datos y cursos que lo componen (clickeables). */
public class JDPrograma extends JDialog {

    private static final String[] ETIQUETAS = {
        "Nombre", "Descripción", "Fecha de inicio", "Fecha de fin", "Fecha de alta"
    };

    public static void mostrar(Window owner, IController control, String nombrePrograma) {
        try {
            String[] datos = control.obtenerDatosBasicosPrograma(nombrePrograma);
            if (datos == null) {
                Ui.error(owner, "No existe un Programa de Formación con nombre: " + nombrePrograma);
                return;
            }
            List<String> detalle = control.obtenerDataPrograma(nombrePrograma);
            new JDPrograma(owner, control, datos, detalle).setVisible(true);
        } catch (Exception ex) {
            Ui.error(owner, ex.getMessage());
        }
    }

    private JDPrograma(Window owner, IController control, String[] d, List<String> detalle) {
        super(owner, "Información de Programa de Formación", ModalityType.MODELESS);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        List<Object[]> filas = new ArrayList<>();
        for (int i = 0; i < ETIQUETAS.length && i < d.length; i++) {
            filas.add(new Object[]{ETIQUETAS[i], d[i]});
        }
        JTable tDatos = Ui.tabla(new String[]{"Campo", "Valor"}, filas);

        // obtenerDataPrograma devuelve "... , Cursos, - curso1, - curso2"
        List<Object[]> filasCursos = new ArrayList<>();
        if (detalle != null) {
            for (String linea : detalle) {
                if (linea != null && linea.startsWith("- ")) {
                    filasCursos.add(new Object[]{linea.substring(2).trim()});
                }
            }
        }
        JTable tCursos = Ui.tabla(new String[]{"Cursos del programa"}, filasCursos);
        Ui.alClickear(tCursos, (f, c) -> {
            String curso = Ui.texto(tCursos, f, 0);
            if (curso != null) JDCurso.mostrar(this, control, curso);
        });

        JPanel contenido = new JPanel(new BorderLayout(0, 10));
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contenido.add(Ui.scroll(tDatos, 520, 125), BorderLayout.NORTH);
        contenido.add(Ui.scroll(tCursos, 520, 150), BorderLayout.CENTER);
        contenido.add(new JLabel("Hacé click en un curso para ver su información."), BorderLayout.SOUTH);
        setContentPane(contenido);

        setPreferredSize(new java.awt.Dimension(1366, 768));
        setResizable(false);
        pack();
        Ui.ubicar(this, owner);
    }
}