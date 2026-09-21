/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Presentacion.curso;

import Logica.controller.IController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author briha
 */
public class JIAltaCurso extends javax.swing.JInternalFrame {

    private IController control;
    private final List<String> docentesNicknames = new ArrayList<>();
    // Proponer que sean un estándar estático inmutable
    private final String admitedPattern = "[^a-zA-Z0-9 áéíóúÁÉÍÓÚñÑ]";
    private final String patronDescripcion = "[^a-zA-Z0-9 áéíóúÁÉÍÓÚñÑ.,;:¿?¡!\\-\n\r]";

    /**
     * Creates new form JIAltaCurso
     */
    public JIAltaCurso() {
        initComponents();
    }

    public JIAltaCurso(IController c) {
        initComponents();
        this.control = c;

        loadInstitutos();
    }

    private void loadInstitutos() {
        for(var i : control.listarNombresInstitutos()) {
            CInstituto.addItem(i);
        }
    }

    private void loadDocentes(String instituto) {
        CDocente.removeAllItems();
        docentesNicknames.clear();

        for(var d : control.listarDocentesPorInstituto(instituto)) {
            docentesNicknames.add(d[0]); // nickname
            CDocente.addItem(d[1]);      // texto a mostrar
        }
    }

    private void loadPrevias(String instituto) {
        DefaultTableModel TPrevModel = (DefaultTableModel) TPrevias.getModel();
        TPrevModel.setRowCount(0);

        for(var p : control.listarCursosPorInstituto(instituto)) {
            TPrevModel.addRow(new Object[]{p});
        }
    }

    public void limpiarComponentes(Container contenedor) {
        // Recorremos todos los componentes dentro del contenedor actual
        for (Component componente : contenedor.getComponents()) {

            // 1. Limpia JTextField, JTextArea, JPasswordField, etc.
            if (componente instanceof JTextComponent) {
                ((JTextComponent) componente).setText("");
            }

            // 2. Limpia Tablas (JTable) eliminando todas sus filas
            else if (componente instanceof JTable) {
                JTable tabla = (JTable) componente;
                if (tabla.getModel() instanceof DefaultTableModel) {
                    DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
                    modelo.setRowCount(0); // Borra todas las filas de la tabla
                }
            }

            // 3. Limpia Spinners (JSpinner) reiniciando su valor a 0 (o al mínimo)
            else if (componente instanceof JSpinner) {
                ((JSpinner) componente).setValue(0);
            }

            // 4. RECURSIVIDAD: Si el componente es otro contenedor (un JPanel, JScrollPane, etc.)
            // llamamos a la función otra vez para revisar lo que tiene dentro.
            else if (componente instanceof Container) {
                limpiarComponentes((Container) componente);
            }
        }
    }

    private List<String> getPrevias() {
        List<String> previas = new ArrayList<>();

        for (int row : TPrevias.getSelectedRows()) {
            int modelRow = TPrevias.convertRowIndexToModel(row);
            Object valor = TPrevias.getModel().getValueAt(modelRow, 0);

            if (valor != null) {
                previas.add(valor.toString());
            }
        }

        return previas;
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new java.awt.Label();
        jLabel1 = new javax.swing.JLabel();
        CInstituto = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        CDocente = new javax.swing.JComboBox<>();
        txtNameCurso = new javax.swing.JTextField();
        lblNameCurso = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TxtADescripcion = new javax.swing.JTextArea();
        lblDescripcionCurso = new javax.swing.JLabel();
        SDuracion = new javax.swing.JSpinner();
        lblDuracion = new javax.swing.JLabel();
        lblCantHoras = new javax.swing.JLabel();
        SCantHoras = new javax.swing.JSpinner();
        lblCantCreditos = new javax.swing.JLabel();
        SCantCreditos = new javax.swing.JSpinner();
        lblURL = new javax.swing.JLabel();
        TxtURL = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        TPrevias = new javax.swing.JTable();
        lblPrevias = new javax.swing.JLabel();
        btnCancelar = new javax.swing.JButton();
        btnAceptar = new javax.swing.JButton();

        setBackground(new java.awt.Color(62, 67, 76));

        lblTitulo.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText("Alta Curso");

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Instituto:");

        CInstituto.addActionListener(this::CInstitutoActionPerformed);

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Docente:");

        lblNameCurso.setForeground(new java.awt.Color(255, 255, 255));
        lblNameCurso.setText("Nombre de curso:");

        TxtADescripcion.setColumns(20);
        TxtADescripcion.setRows(5);
        jScrollPane1.setViewportView(TxtADescripcion);

        lblDescripcionCurso.setForeground(new java.awt.Color(255, 255, 255));
        lblDescripcionCurso.setText("Descripción:");

        SDuracion.setModel(new javax.swing.SpinnerNumberModel());
        SDuracion.setValue(1);

        lblDuracion.setForeground(new java.awt.Color(255, 255, 255));
        lblDuracion.setText("Duracion:");

        lblCantHoras.setForeground(new java.awt.Color(255, 255, 255));
        lblCantHoras.setText("Cant. horas:");

        SCantHoras.setValue(1);

        lblCantCreditos.setForeground(new java.awt.Color(255, 255, 255));
        lblCantCreditos.setText("Cant. creditos:");

        SCantCreditos.setValue(0);

        lblURL.setForeground(new java.awt.Color(255, 255, 255));
        lblURL.setText("URL:");
        lblURL.setToolTipText("");

        TPrevias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Previas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(TPrevias);

        lblPrevias.setForeground(new java.awt.Color(255, 255, 255));
        lblPrevias.setText("(Opcional, Multi selección)");

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(this::btnAceptarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPrevias)
                            .addComponent(lblURL)
                            .addComponent(lblDescripcionCurso)
                            .addComponent(txtNameCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNameCurso)
                            .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtURL, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(CInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(CDocente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblDuracion)
                                    .addComponent(SDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(SCantHoras, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCantHoras))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblCantCreditos)
                                        .addGap(0, 44, Short.MAX_VALUE))
                                    .addComponent(SCantCreditos)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addComponent(btnAceptar)
                                .addGap(62, 62, 62)
                                .addComponent(btnCancelar)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CDocente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNameCurso)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNameCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblDescripcionCurso)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDuracion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCantHoras)
                            .addComponent(lblCantCreditos))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SCantHoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SCantCreditos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblURL)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TxtURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPrevias)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnAceptar))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        try {
            String nombreCurso = txtNameCurso.getText().trim();
            if (nombreCurso.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre del curso es obligatorio.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nombreInstituto = (String) CInstituto.getSelectedItem();
            if (nombreInstituto == null) {
                JOptionPane.showMessageDialog(this, "Tenés que elegir un instituto.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int indiceDocente = CDocente.getSelectedIndex();
            if (indiceDocente < 0) {
                JOptionPane.showMessageDialog(this, "Tenés que elegir un docente.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nicknameDocente = docentesNicknames.get(indiceDocente);
            String descripcion = TxtADescripcion.getText().trim();
            int duracion = ((Number) SDuracion.getValue()).intValue();
            float cantHoras = ((Number) SCantHoras.getValue()).floatValue();
            int cantCreditos = ((Number) SCantCreditos.getValue()).intValue();
            String url = TxtURL.getText().trim();

            List<String> previas = getPrevias();

            if (cantCreditos <= 0) {
                JOptionPane.showMessageDialog(this, "El curso debe brindar creditos");
                return;
            }

            if (duracion <= 0) {
                JOptionPane.showMessageDialog(this, "Duración de curso invalida");
                return;
            }

            if (url.isEmpty() || !url.startsWith("http")) {
                JOptionPane.showMessageDialog(this, "Debes ingresar una URL valida");
                return;
            }

            // Mediante un Regex detecta si se utilizaron caracteres invalidos "?/-_. etc"
            Pattern patternGeneric = Pattern.compile(admitedPattern);
            for(String text : new String[]{nombreCurso, nombreInstituto, nicknameDocente}) {
                Matcher m = patternGeneric.matcher(text);
                if(m.find()) {
                    JOptionPane.showMessageDialog(this, "Caracteres inválidos encontrados, revise los campos Curso, Instituto, Docente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

            }

            // La descripcion es más permisiva por ende acepta más caracteres
            Pattern patternDescripcion = Pattern.compile(patronDescripcion);
            Matcher m = patternDescripcion.matcher(descripcion);

            if(m.find()) {
                JOptionPane.showMessageDialog(this, "Descripción invalida, caracteres inválidos encontrados", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            control.altaCurso(nombreCurso, descripcion, duracion, cantHoras, cantCreditos, url, nombreInstituto, nicknameDocente, previas);

            JOptionPane.showMessageDialog(this, "Curso registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error inesperado: \nNo se pudo registrar", JOptionPane.ERROR_MESSAGE);
            
        }
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void CInstitutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CInstitutoActionPerformed
        String instituto = (String) CInstituto.getSelectedItem();
        loadDocentes(instituto);
        loadPrevias(instituto);
    }//GEN-LAST:event_CInstitutoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiarComponentes(this);
    }//GEN-LAST:event_btnCancelarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CDocente;
    private javax.swing.JComboBox<String> CInstituto;
    private javax.swing.JSpinner SCantCreditos;
    private javax.swing.JSpinner SCantHoras;
    private javax.swing.JSpinner SDuracion;
    private javax.swing.JTable TPrevias;
    private javax.swing.JTextArea TxtADescripcion;
    private javax.swing.JTextField TxtURL;
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblCantCreditos;
    private javax.swing.JLabel lblCantHoras;
    private javax.swing.JLabel lblDescripcionCurso;
    private javax.swing.JLabel lblDuracion;
    private javax.swing.JLabel lblNameCurso;
    private javax.swing.JLabel lblPrevias;
    private java.awt.Label lblTitulo;
    private javax.swing.JLabel lblURL;
    private javax.swing.JTextField txtNameCurso;
    // End of variables declaration//GEN-END:variables
}