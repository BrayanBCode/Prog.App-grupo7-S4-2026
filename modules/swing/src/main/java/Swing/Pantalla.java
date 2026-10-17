package Swing;

import javax.swing.*;
import java.awt.*;

/**
 * Acomoda el contenido de una pantalla (JInternalFrame) al tamaño de la ventana:
 * lo centra con su tamaño diseñado y, si la ventana queda chica, agrega scroll.
 * Se aplica desde afuera para no tocar el código generado por el diseñador de NetBeans.
 */
final class Pantalla {

    private static final int MARGEN = 16;

    private Pantalla() {}

    static void acomodar(JInternalFrame frame) {
        Container contenido = frame.getContentPane();
        Color fondo = frame.getBackground();

        Centrado lienzo = new Centrado(contenido);
        lienzo.setBackground(fondo);

        JScrollPane scroll = new JScrollPane(lienzo);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(fondo);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);

        JPanel nuevo = new JPanel(new BorderLayout());
        nuevo.add(scroll);
        frame.setContentPane(nuevo);
    }

    /** Panel que centra horizontalmente a su único hijo, pegado arriba. */
    private static final class Centrado extends JPanel implements Scrollable {

        private final Component hijo;

        Centrado(Component hijo) {
            super(null);
            this.hijo = hijo;
            add(hijo);
        }

        /** Tamaño diseñado de la pantalla: se centra tal cual, sin estirarla. */
        private Dimension tamanioHijo() {
            return hijo.getPreferredSize();
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension h = tamanioHijo();
            return new Dimension(h.width + 2 * MARGEN, h.height + 2 * MARGEN);
        }

        @Override
        public void doLayout() {
            Dimension h = tamanioHijo();
            // Tamaño fijo (el diseñado): si no entra en la ventana, aparece el scroll
            int x = Math.max(MARGEN, (getWidth() - h.width) / 2);
            hijo.setBounds(x, MARGEN, h.width, h.height);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return getParent() instanceof JViewport v && v.getWidth() >= getPreferredSize().width;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return getParent() instanceof JViewport v && v.getHeight() >= getPreferredSize().height;
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle r, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle r, int orientation, int direction) {
            return orientation == SwingConstants.VERTICAL ? r.height : r.width;
        }
    }
}
