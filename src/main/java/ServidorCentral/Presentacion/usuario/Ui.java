package ServidorCentral.Presentacion.usuario;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import javax.swing.JDialog;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.BiConsumer;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** Utilidades compartidas por los diálogos de detalle (tablas de solo lectura, click en celda, errores). */
public final class Ui {

    private Ui() {}

    public static JTable tabla(String[] columnas, List<Object[]> filas) {
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        for (Object[] f : filas) {
            modelo.addRow(f);
        }
        JTable t = new JTable(modelo);
        t.setRowHeight(22);
        return t;
    }

    public static JScrollPane scroll(JTable t, int ancho, int alto) {
        JScrollPane sp = new JScrollPane(t);
        sp.setPreferredSize(new Dimension(ancho, alto));
        return sp;
    }

    /** Ejecuta la acción (fila, columna) cuando se hace click sobre una celda de la tabla. */
    public static void alClickear(JTable t, BiConsumer<Integer, Integer> accion) {
        t.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = t.rowAtPoint(e.getPoint());
                int col = t.columnAtPoint(e.getPoint());
                if (fila >= 0 && col >= 0) {
                    accion.accept(fila, col);
                }
            }
        });
    }

    /** Texto de una celda o null si está vacía. */
    public static String texto(JTable t, int fila, int col) {
        Object v = t.getValueAt(fila, col);
        if (v == null) return null;
        String s = v.toString().trim();
        return s.isEmpty() ? null : s;
    }

    public static void error(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static int cascada = 0;

    /**
     * Ubica un diálogo sin taparlo del todo con los que ya hay abiertos:
     * si su dueño es otro diálogo, queda corrido hacia abajo y a la derecha;
     * si el dueño es la ventana principal, se va escalonando.
     */
    public static void ubicar(JDialog dialogo, Window owner) {
        dialogo.setLocationRelativeTo(owner);
        Point p = dialogo.getLocation();
        int desplazamiento = (owner instanceof JDialog) ? 35 : (cascada++ % 6) * 35;
        dialogo.setLocation(p.x + desplazamiento, p.y + desplazamiento);
    }
}