package Presentacion.Dialogo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * Popup modal para seleccionar uno o más docentes (Ctrl/Shift + click para
 * elegir varios) que van a participar en una Edición de Curso.
 */
public class JDSeleccionarDocentes extends JDialog {

    private final JList<String> lista;
    // nicknames en el mismo orden que se muestran en la JList
    private final List<String> nicknames;
    private List<String> seleccionConfirmada = null; // null = se canceló

    public JDSeleccionarDocentes(Frame owner, List<String[]> docentesDisponibles, List<String> seleccionadosPrevios) {
        super(owner, "Seleccionar docentes", true); // true = modal

        this.nicknames = new ArrayList<>();
        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (String[] docente : docentesDisponibles) {
            this.nicknames.add(docente[0]);
            modelo.addElement(docente[1]);
        }

        lista = new JList<>(modelo);
        lista.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Preseleccionar los que ya venían elegidos
        if (seleccionadosPrevios != null) {
            for (int i = 0; i < nicknames.size(); i++) {
                if (seleccionadosPrevios.contains(nicknames.get(i))) {
                    lista.addSelectionInterval(i, i);
                }
            }
        }

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> {
            seleccionConfirmada = new ArrayList<>();
            for (int index : lista.getSelectedIndices()) {
                seleccionConfirmada.add(nicknames.get(index));
            }
            dispose();
        });

        btnCancelar.addActionListener(e -> {
            seleccionConfirmada = null; // se descarta cualquier cambio
            dispose();
        });

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);

        setLayout(new BorderLayout(8, 8));
        add(new JScrollPane(lista), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setSize(320, 300);
        setLocationRelativeTo(owner);
    }

    /**
     * @return la lista de nicknames elegidos si se presionó Aceptar,
     *         o null si se canceló el popup (hay que chequear el null).
     */
    public List<String> getSeleccionConfirmada() {
        return seleccionConfirmada;
    }
}
