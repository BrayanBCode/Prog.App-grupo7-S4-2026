/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package ServidorCentral.Presentacion.usuario;

import ServidorCentral.Logica.controller.IController;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author maida
 */
public class Datos_Docente extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Datos_Docente.class.getName());

    // Componentes armados a mano (no dependen del Form Editor)
    private javax.swing.JLabel labelImagen;
    private javax.swing.JTable tablaDatos;
    private javax.swing.JTable tablaActividad;
    private IController control;

    /**
     * Creates new form Datos_Docente
     */
    public Datos_Docente(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * Crea el diálogo ya cargado con los datos del docente.
     *
     * @param datos      {nickname, email, nombre, apellido, fechaNacimiento, [rutaImagen]}
     * @param rutaImagen ruta de la imagen del usuario (puede ser null)
     * @param actividad  lista devuelta por control.obtenerDataDocente(nickname)
     * @param control    controlador, para abrir el detalle de institutos, cursos, ediciones y programas
     */
    public Datos_Docente(java.awt.Frame parent, boolean modal,
            String[] datos, String rutaImagen, List<String> actividad, IController control) {
        this(parent, modal);
        this.control = control;
        construirInterfaz();
        cargarDatos(datos, rutaImagen, actividad);
        setTitle("Información de Docente");
        pack();
        ServidorCentral.Presentacion.usuario.Ui.ubicar(this, parent);
    }

    // ---------------------------------------------------------------
    // Interfaz y carga de datos (fuera del bloque generado)
    // ---------------------------------------------------------------
    private void construirInterfaz() {
        getContentPane().removeAll();
        getContentPane().setLayout(new java.awt.BorderLayout(0, 10));

        // Imagen (izquierda)
        labelImagen = new javax.swing.JLabel();
        labelImagen.setPreferredSize(new java.awt.Dimension(150, 160));
        labelImagen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelImagen.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));

        // Datos personales (derecha)
        tablaDatos = new javax.swing.JTable();
        tablaDatos.setRowHeight(24);
        javax.swing.JScrollPane scrollDatos = new javax.swing.JScrollPane(tablaDatos);
        scrollDatos.setPreferredSize(new java.awt.Dimension(380, 160));

        javax.swing.JPanel panelSuperior = new javax.swing.JPanel(new java.awt.BorderLayout(10, 0));
        panelSuperior.add(labelImagen, java.awt.BorderLayout.WEST);
        panelSuperior.add(scrollDatos, java.awt.BorderLayout.CENTER);

        // Actividad académica (abajo)
        tablaActividad = new javax.swing.JTable();
        javax.swing.JScrollPane scrollActividad = new javax.swing.JScrollPane(tablaActividad);
        scrollActividad.setPreferredSize(new java.awt.Dimension(640, 200));

        javax.swing.JPanel contenido = new javax.swing.JPanel(new java.awt.BorderLayout(0, 10));
        contenido.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contenido.add(panelSuperior, java.awt.BorderLayout.NORTH);
        contenido.add(scrollActividad, java.awt.BorderLayout.CENTER);

        getContentPane().add(contenido, java.awt.BorderLayout.CENTER);
    }

    private void cargarDatos(String[] datos, String rutaImagen, List<String> actividad) {

        // --- Imagen ---
        boolean conImagen = false;
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            java.io.File archivo = new java.io.File(rutaImagen);
            if (archivo.exists()) {
                javax.swing.ImageIcon icono = new javax.swing.ImageIcon(rutaImagen);
                java.awt.Image esc = icono.getImage().getScaledInstance(150, 160, java.awt.Image.SCALE_SMOOTH);
                labelImagen.setIcon(new javax.swing.ImageIcon(esc));
                conImagen = true;
            }
        }
        if (!conImagen) {
            labelImagen.setText("Sin imagen");
        }

        // --- Datos personales ---
        Object[][] filas = {
            {"Nickname", datos[0]},
            {"Nombre", datos[2]},
            {"Apellido", datos[3]},
            {"Fecha de nacimiento", datos[4]},
            {"Correo electrónico", datos[1]}
        };
        tablaDatos.setModel(new DefaultTableModel(filas, new String[]{"Campo", "Valor"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        // --- Actividad académica, separada por columnas ---
        // obtenerDataDocente devuelve secciones ("--- INSTITUTOS ---", "--- CURSOS ---", ...)
        // seguidas de items "- nombre". Se reparten en una columna por tipo.
        List<List<String>> columnas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            columnas.add(new ArrayList<>());
        }
        int seccion = -1;
        if (actividad != null) {
            for (String linea : actividad) {
                String t = linea == null ? "" : linea.trim();
                if (t.startsWith("---")) {
                    if (t.contains("EDICIONES")) {
                        seccion = 2;
                    } else if (t.contains("CURSOS")) {
                        seccion = 1;
                    } else if (t.contains("INSTITUTOS")) {
                        seccion = 0;
                    } else if (t.contains("PROGRAMAS")) {
                        seccion = 3;
                    }
                } else if (t.startsWith("- ") && seccion >= 0) {
                    columnas.get(seccion).add(t.substring(2).trim());
                }
                // Los "(Sin ...)" se ignoran: la celda queda vacía.
            }
        }
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Instituto", "Curso", "Edición de curso", "Programa de formación"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        int filasTotal = 0;
        for (List<String> c : columnas) {
            filasTotal = Math.max(filasTotal, c.size());
        }
        for (int i = 0; i < filasTotal; i++) {
            Object[] fila = new Object[4];
            for (int c = 0; c < 4; c++) {
                fila[c] = i < columnas.get(c).size() ? columnas.get(c).get(i) : "";
            }
            modelo.addRow(fila);
        }
        tablaActividad.setModel(modelo);

        // --- Click: 0 = instituto, 1 = curso, 2 = edición, 3 = programa ---
        Ui.alClickear(tablaActividad, (fila, col) -> {
            String nombre = Ui.texto(tablaActividad, fila, col);
            if (nombre == null) {
                return;
            }
            switch (col) {
                case 0: JDInstituto.mostrar(this, control, nombre); break;
                case 1: JDCurso.mostrar(this, control, nombre); break;
                case 2: JDEdicion.mostrar(this, control, nombre); break;
                default: JDPrograma.mostrar(this, control, nombre); break;
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1366, 768));
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Datos_Docente dialog = new Datos_Docente(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}