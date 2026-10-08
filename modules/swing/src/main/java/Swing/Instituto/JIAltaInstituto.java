/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Swing.instituto;

import ServidorCentral.Logica.controller.IControllerV2;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 * Formulario JInternalFrame para dar de alta un Instituto.
 */
public class JIAltaInstituto extends javax.swing.JInternalFrame {

    private IControllerV2 control;

    // Mismo criterio de caracteres válidos que usa el Alta de Curso para nombres
    private static final Pattern CARACTERES_INVALIDOS = Pattern.compile("[^a-zA-Z0-9 áéíóúÁÉÍÓÚñÑ]");

    public JIAltaInstituto(IControllerV2 c) {
        initComponents();
        this.control = c;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLblTitulo = new javax.swing.JLabel();
        jLblNombre = new javax.swing.JLabel();
        jTxtNombre = new javax.swing.JTextField();
        jBtnCancelar = new javax.swing.JButton();
        jBtnAceptar = new javax.swing.JButton();

        setBackground(new java.awt.Color(62, 67, 76));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLblTitulo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        jLblTitulo.setText("Alta de Instituto");

        jLblNombre.setForeground(new java.awt.Color(255, 255, 255));
        jLblNombre.setText("Nombre:");

        jTxtNombre.setName("txtNombreInstituto"); // NOI18N
        jTxtNombre.addActionListener(this::jBtnAceptarActionPerformed);

        jBtnCancelar.setText("Cancelar");
        jBtnCancelar.setName("btnCancelar"); // NOI18N
        jBtnCancelar.addActionListener(this::jBtnCancelarActionPerformed);

        jBtnAceptar.setText("Aceptar");
        jBtnAceptar.setName("btnAceptar"); // NOI18N
        jBtnAceptar.addActionListener(this::jBtnAceptarActionPerformed);

        setPreferredSize(new java.awt.Dimension(1366, 768));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLblTitulo)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLblNombre)
                        .addGap(30, 30, 30)
                        .addComponent(jTxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jBtnCancelar)
                        .addGap(20, 20, 20)
                        .addComponent(jBtnAceptar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLblTitulo)
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLblNombre)
                    .addComponent(jTxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnCancelar)
                    .addComponent(jBtnAceptar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelarActionPerformed
        jTxtNombre.setText(""); // Cancelar: limpia el campo, no cierra la pantalla
        jTxtNombre.requestFocusInWindow();
    }//GEN-LAST:event_jBtnCancelarActionPerformed

    private void jBtnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnAceptarActionPerformed
        String nombre = jTxtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del instituto.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (CARACTERES_INVALIDOS.matcher(nombre).find()) {
            JOptionPane.showMessageDialog(this, "El nombre contiene caracteres inválidos. Use solo letras, números y espacios.", "Nombre inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            control.altaInstituto(nombre);
            JOptionPane.showMessageDialog(this, "Instituto registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            jTxtNombre.setText(""); // solo se limpia si el alta salió bien
            jTxtNombre.requestFocusInWindow();
        } catch (Exception e) {
            // Nombre repetido, vacío, etc.: se conserva lo escrito
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error al registrar instituto", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jBtnAceptarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnAceptar;
    private javax.swing.JButton jBtnCancelar;
    private javax.swing.JLabel jLblNombre;
    private javax.swing.JLabel jLblTitulo;
    private javax.swing.JTextField jTxtNombre;
    // End of variables declaration//GEN-END:variables
}