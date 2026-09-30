/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Swing.usuario;

import ServidorCentral.Logica.controller.IController;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maida
 */
public class Datos_Estudiante extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Datos_Estudiante.class.getName());

    private IController control;

    /**
     * Creates new form Datos_Usuarios
     */
    public Datos_Estudiante(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * Crea el diálogo ya cargado con los datos del estudiante.
     *
     * @param datos         {nickname, email, nombre, apellido, fechaNacimiento, [rutaImagen]}
     * @param rutaImagen    ruta de la imagen del usuario (puede ser null)
     * @param inscripciones lista devuelta por control.obtenerEdicionesYProgramas(nickname)
     * @param control       controlador, para abrir el detalle de ediciones y programas al hacer click
     */
    public Datos_Estudiante(java.awt.Frame parent, boolean modal,
            String[] datos, String rutaImagen, List<String> inscripciones, IController control) {
        this(parent, modal);
        this.control = control;
        cargarDatos(datos, rutaImagen, inscripciones);
        Swing.usuario.Ui.ubicar(this, parent);
        setTitle("Información de Estudiante");
    }

    // ---------------------------------------------------------------
    // Carga de datos (fuera del bloque generado por el Form Editor)
    // ---------------------------------------------------------------
    private void cargarDatos(String[] datos, String rutaImagen, List<String> inscripciones) {

        // --- Imagen ---
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));
        boolean conImagen = false;
        if (rutaImagen != null && !rutaImagen.isEmpty()) {
            java.io.File archivo = new java.io.File(rutaImagen);
            if (archivo.exists()) {
                javax.swing.ImageIcon icono = new javax.swing.ImageIcon(rutaImagen);
                java.awt.Image esc = icono.getImage().getScaledInstance(212, 150, java.awt.Image.SCALE_SMOOTH);
                jLabel1.setText("");
                jLabel1.setIcon(new javax.swing.ImageIcon(esc));
                conImagen = true;
            }
        }
        if (!conImagen) {
            jLabel1.setIcon(null);
            jLabel1.setText("Sin imagen");
        }

        // --- Datos personales ---
        Object[][] filas = {
            {"Nickname", datos[0]},
            {"Nombre", datos[2]},
            {"Apellido", datos[3]},
            {"Fecha de nacimiento", datos[4]},
            {"Correo electrónico", datos[1]}
        };
        TablaDatosPersonales.setModel(new DefaultTableModel(filas, new String[]{"Campo", "Valor"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        // --- Ediciones de curso / Programas ---
        // La lista trae ediciones y programas mezclados; se separan preguntando
        // al controlador si el nombre corresponde a un programa de formación.
        List<String> ediciones = new ArrayList<>();
        List<String> programas = new ArrayList<>();
        if (inscripciones != null) {
            for (String item : inscripciones) {
                if (control.existePrograma(item)) {
                    programas.add(item);
                } else {
                    ediciones.add(item);
                }
            }
        }
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Ediciones de curso", "Programas Inscripto"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        int filasTotal = Math.max(ediciones.size(), programas.size());
        for (int i = 0; i < filasTotal; i++) {
            modelo.addRow(new Object[]{
                i < ediciones.size() ? ediciones.get(i) : "",
                i < programas.size() ? programas.get(i) : ""
            });
        }
        jTable1.setModel(modelo);

        // --- Click: columna 0 = edición, columna 1 = programa ---
        Ui.alClickear(jTable1, (fila, col) -> {
            String nombre = Ui.texto(jTable1, fila, col);
            if (nombre == null) {
                return;
            }
            if (col == 0) {
                JDEdicion.mostrar(this, control, nombre);
            } else {
                JDPrograma.mostrar(this, control, nombre);
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaDatosPersonales = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1366, 768));
        setResizable(false);

        jLabel1.setText("Imagen");

        TablaDatosPersonales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Campo", "Valor"
            }
        ));
        jScrollPane1.setViewportView(TablaDatosPersonales);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Ediciones de curso", "Programas Inscripto"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 561, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
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
                Datos_Estudiante dialog = new Datos_Estudiante(new javax.swing.JFrame(), true);
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
    private javax.swing.JTable TablaDatosPersonales;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}