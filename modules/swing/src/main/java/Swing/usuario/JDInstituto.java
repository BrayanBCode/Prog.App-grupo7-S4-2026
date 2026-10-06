package Swing.usuario;

import ServidorCentral.Logica.controller.IControllerV2;
import ServidorCentral.Logica.datatypes.DTDocenteResumen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Información completa de un instituto: sus cursos (clickeables) y sus docentes. */
public class JDInstituto extends JDialog {

    public static void mostrar(Window owner, IControllerV2 control, String nombreInstituto) {
        try {
            new JDInstituto(owner, control, nombreInstituto).setVisible(true);
        } catch (Exception ex) {
            Ui.error(owner, ex.getMessage());
        }
    }

    private JDInstituto(Window owner, IControllerV2 control, String nombre) {
        super(owner, "Información de Instituto", ModalityType.MODELESS);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel titulo = new JLabel("Instituto: " + nombre);
        titulo.setFont(titulo.getFont().deriveFont(java.awt.Font.BOLD, 15f));

        // Cursos del instituto
        List<Object[]> filasCursos = new ArrayList<>();
        List<String> cursos = control.listarCursosPorInstituto(nombre);
        if (cursos != null) {
            for (String c : cursos) filasCursos.add(new Object[]{c});
        }
        JTable tCursos = Ui.tabla(new String[]{"Cursos"}, filasCursos);
        Ui.alClickear(tCursos, (f, c) -> {
            String curso = Ui.texto(tCursos, f, 0);
            if (curso != null) JDCurso.mostrar(this, control, curso);
        });

        // Docentes del instituto ({0}=nickname, {1}=texto a mostrar)
        List<Object[]> filasDocentes = new ArrayList<>();
        List<DTDocenteResumen> docentes = control.listarDocentesPorInstituto(nombre);
        if (docentes != null) {
            for (DTDocenteResumen d : docentes) filasDocentes.add(new Object[]{d.nombre()});
        }
        JTable tDocentes = Ui.tabla(new String[]{"Docentes"}, filasDocentes);

        JPanel centro = new JPanel(new GridLayout(1, 2, 10, 0));
        centro.add(Ui.scroll(tCursos, 260, 260));
        centro.add(Ui.scroll(tDocentes, 260, 260));

        JLabel ayuda = new JLabel("Hacé click en un curso para ver su información.");

        JPanel contenido = new JPanel(new BorderLayout(0, 10));
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contenido.add(titulo, BorderLayout.NORTH);
        contenido.add(centro, BorderLayout.CENTER);
        contenido.add(ayuda, BorderLayout.SOUTH);
        setContentPane(contenido);

        pack();
        Ui.ubicar(this, owner);
    }
}