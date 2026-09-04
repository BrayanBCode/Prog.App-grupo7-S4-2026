/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Presentacion.curso;

import Logica.controller.IController;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author briha
 */
public class JIConsultaCurso extends javax.swing.JInternalFrame {

    private IController control;
    
    /**
     * Creates new form JIConsultaCurso
     */
    public JIConsultaCurso(IController c) {
        initComponents();
        this.control = c;
        cargarInstitutos();
    }

    
    private void cargarInstitutos() {
        List<String> institutos = control.listarNombresInstitutos();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String inst : institutos) {
            model.addElement(inst);
        }
        comboInstituto.setModel(model);

        limpiarDetalle();
        limpiarTabla(tableCursos);
        limpiarTabla(tableEdiciones);
        limpiarTabla(tablePrograms);

        if (comboInstituto.getItemCount() > 0) {
            comboInstituto.setSelectedIndex(0);
            cargarCursosDelInstituto((String) comboInstituto.getSelectedItem());
        }
    }

    private void cargarCursosDelInstituto(String nombreInstituto) {
        limpiarTabla(tableCursos);
        limpiarTabla(tableEdiciones);
        limpiarTabla(tablePrograms);
        limpiarDetalle();

        if (nombreInstituto == null) {
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tableCursos.getModel();
        List<String[]> cursos = control.listarCursosTabla(nombreInstituto);
        for (String[] curso : cursos) {
            modelo.addRow(new Object[]{curso[0], curso[1]});
        }
    }

    private void mostrarDetalleCurso(String nombreCurso) {
        try {
            String[] datos = control.obtenerDataCurso(nombreCurso);

            String detalle = "Nombre: " + datos[0] + "\n"
                    + "Descripción: " + datos[1] + "\n"
                    + "Duración: " + datos[2] + "\n"
                    + "Cant. horas: " + datos[3] + "\n"
                    + "Cant. créditos: " + datos[4] + "\n"
                    + "URL: " + datos[5] + "\n"
                    + "Fecha de registro: " + datos[6] + "\n"
                    + "Instituto: " + datos[7] + "\n"
                    + "Docente: " + datos[8] + "\n"
                    + "Previas: " + datos[9];

            txtAreaDetalle.setText(detalle);
            txtAreaDetalle.setCaretPosition(0);

            limpiarTabla(tableEdiciones);
            List<String> ediciones = control.listarEdicionesCurso(nombreCurso);
            DefaultTableModel modeloEdiciones = (DefaultTableModel) tableEdiciones.getModel();
            if (ediciones.isEmpty()) {
                modeloEdiciones.addRow(new Object[]{"(Sin ediciones registradas)"});
            } else {
                for (String ed : ediciones) {
                    modeloEdiciones.addRow(new Object[]{ed});
                }
            }

            limpiarTabla(tablePrograms);
            List<String> programas = control.listarProgramasPorCurso(nombreCurso);
            DefaultTableModel modeloProgramas = (DefaultTableModel) tablePrograms.getModel();
            if (programas.isEmpty()) {
                modeloProgramas.addRow(new Object[]{"(Sin programas asociados)"});
            } else {
                for (String prog : programas) {
                    modeloProgramas.addRow(new Object[]{prog});
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetalleEdicion(String nombreEdicion) {
        try {
            String[] datos = control.obtenerEdicionCurso(nombreEdicion);

            String mensaje = "DETALLES DE LA EDICIÓN DE CURSO:\n\n"
                    + "Nombre: " + datos[0] + "\n"
                    + "Curso: " + datos[1] + "\n"
                    + "Fecha de inicio: " + datos[2] + "\n"
                    + "Fecha de fin: " + datos[3] + "\n"
                    + "Cupo: " + datos[4] + "\n"
                    + "Fecha de publicación: " + datos[5];

            JOptionPane.showMessageDialog(this, mensaje, "Información de Edición de Curso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetallePrograma(String nombrePrograma) {
        try {
            List<String> datos = control.obtenerDataPrograma(nombrePrograma);
            StringBuilder sb = new StringBuilder();
            for (String linea : datos) {
                sb.append(linea).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Información de Programa de Formación", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarTabla(javax.swing.JTable tabla) {
        ((DefaultTableModel) tabla.getModel()).setRowCount(0);
    }

    private void limpiarDetalle() {
        txtAreaDetalle.setText("");
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        comboInstituto = new javax.swing.JComboBox<>();
        jScrollPaneCursos = new javax.swing.JScrollPane();
        tableCursos = new javax.swing.JTable();
        jScrollPaneEdiciones = new javax.swing.JScrollPane();
        tableEdiciones = new javax.swing.JTable();
        jLabelCursos = new javax.swing.JLabel();
        jLabelDetalle = new javax.swing.JLabel();
        jLabelEdiciones = new javax.swing.JLabel();
        jScrollPanePrograms = new javax.swing.JScrollPane();
        tablePrograms = new javax.swing.JTable();
        jLabelProgramas = new javax.swing.JLabel();
        jLabelInstituto = new javax.swing.JLabel();
        jLabelTitulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaDetalle = new javax.swing.JTextArea();

        setBackground(new java.awt.Color(62, 67, 76));

        comboInstituto.setName("comboInstituto"); // NOI18N
        comboInstituto.addActionListener(this::comboInstitutoActionPerformed);

        tableCursos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Nombre", "Descripcion"
            }
        ));
        tableCursos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCursosMouseClicked(evt);
            }
        });
        jScrollPaneCursos.setViewportView(tableCursos);

        tableEdiciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Edicion del curso"
            }
        ));
        tableEdiciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableEdicionesMouseClicked(evt);
            }
        });
        jScrollPaneEdiciones.setViewportView(tableEdiciones);

        jLabelCursos.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCursos.setText("Cursos del Instituto");

        jLabelDetalle.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDetalle.setText("Datos del curso seleccionado");

        jLabelEdiciones.setForeground(new java.awt.Color(255, 255, 255));
        jLabelEdiciones.setText("Ediciones de curso");
        jLabelEdiciones.setToolTipText("");

        tablePrograms.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Programa de formación"
            }
        ));
        tablePrograms.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableProgramsMouseClicked(evt);
            }
        });
        jScrollPanePrograms.setViewportView(tablePrograms);

        jLabelProgramas.setForeground(new java.awt.Color(255, 255, 255));
        jLabelProgramas.setText("Programas de formación");

        jLabelInstituto.setForeground(new java.awt.Color(255, 255, 255));
        jLabelInstituto.setText("Instituto:");

        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitulo.setText("Consulta de Curso");
        jLabelTitulo.setToolTipText("");

        txtAreaDetalle.setColumns(20);
        txtAreaDetalle.setRows(5);
        jScrollPane1.setViewportView(txtAreaDetalle);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPaneCursos, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelEdiciones)
                            .addComponent(jScrollPaneEdiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelProgramas)
                                .addGap(0, 65, Short.MAX_VALUE))
                            .addComponent(jScrollPanePrograms, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelCursos)
                            .addComponent(jLabelDetalle)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelInstituto)
                                .addGap(23, 23, 23)
                                .addComponent(comboInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabelTitulo))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelInstituto))
                .addGap(18, 18, 18)
                .addComponent(jLabelCursos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneCursos, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelDetalle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEdiciones)
                    .addComponent(jLabelProgramas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneEdiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPanePrograms, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboInstitutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboInstitutoActionPerformed
        String institutoSeleccionado = (String) comboInstituto.getSelectedItem();
        cargarCursosDelInstituto(institutoSeleccionado);
    }//GEN-LAST:event_comboInstitutoActionPerformed

    private void tableCursosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCursosMouseClicked
        int fila = tableCursos.getSelectedRow();
        if (fila != -1) {
            String nombreCurso = tableCursos.getValueAt(fila, 0).toString();
            mostrarDetalleCurso(nombreCurso);
        }
    }//GEN-LAST:event_tableCursosMouseClicked

    private void tableEdicionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableEdicionesMouseClicked
        int fila = tableEdiciones.getSelectedRow();
        if (fila != -1) {
            String valor = tableEdiciones.getValueAt(fila, 0).toString();
            if (!valor.startsWith("(")) {
                mostrarDetalleEdicion(valor);
            }
        }
    }//GEN-LAST:event_tableEdicionesMouseClicked

    private void tableProgramsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableProgramsMouseClicked
        int fila = tablePrograms.getSelectedRow();
        if (fila != -1) {
            String valor = tablePrograms.getValueAt(fila, 0).toString();
            if (!valor.startsWith("(")) {
                mostrarDetallePrograma(valor);
            }
        }
    }//GEN-LAST:event_tableProgramsMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboInstituto;
    private javax.swing.JLabel jLabelCursos;
    private javax.swing.JLabel jLabelDetalle;
    private javax.swing.JLabel jLabelEdiciones;
    private javax.swing.JLabel jLabelInstituto;
    private javax.swing.JLabel jLabelProgramas;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneCursos;
    private javax.swing.JScrollPane jScrollPaneEdiciones;
    private javax.swing.JScrollPane jScrollPanePrograms;
    private javax.swing.JTable tableCursos;
    private javax.swing.JTable tableEdiciones;
    private javax.swing.JTable tablePrograms;
    private javax.swing.JTextArea txtAreaDetalle;
    // End of variables declaration//GEN-END:variables
}
