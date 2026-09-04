/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt 
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java 
 */
package Presentacion.curso;

import Logica.controller.IController;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Caso de uso: Consulta de Curso.
 *
 * El administrador elige un instituto, el sistema lista los cursos que
 * brinda. Al elegir un curso se muestran todos sus datos junto con las
 * ediciones y los programas de formación asociados. Al seleccionar una
 * edición o un programa se muestra su información detallada (reutilizando
 * los mismos datos que usan los casos de uso Consulta de Edición de Curso y
 * Consulta de Programa de Formación).
 */
public class JIConsultaCursotemp extends javax.swing.JInternalFrame {

    private IController control;

    public JIConsultaCursotemp(IController c) {
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

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabelTitulo = new javax.swing.JLabel();
        jLabelInstituto = new javax.swing.JLabel();
        comboInstituto = new javax.swing.JComboBox<>();

        jLabelCursos = new javax.swing.JLabel();
        jScrollPaneCursos = new javax.swing.JScrollPane();
        tableCursos = new javax.swing.JTable();

        jLabelDetalle = new javax.swing.JLabel();
        jScrollPaneDetalle = new javax.swing.JScrollPane();
        txtAreaDetalle = new javax.swing.JTextArea(8, 30);

        jLabelEdiciones = new javax.swing.JLabel();
        jScrollPaneEdiciones = new javax.swing.JScrollPane();
        tableEdiciones = new javax.swing.JTable();

        jLabelProgramas = new javax.swing.JLabel();
        jScrollPanePrograms = new javax.swing.JScrollPane();
        tablePrograms = new javax.swing.JTable();

        setBackground(new java.awt.Color(62, 67, 76));
        setBorder(null);

        jLabelTitulo.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabelTitulo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitulo.setText("CONSULTAR CURSO");

        jLabelInstituto.setForeground(new java.awt.Color(255, 255, 255));
        jLabelInstituto.setText("Instituto:");
        comboInstituto.setName("CBoxSelectInstitute");
        comboInstituto.addActionListener(this::comboInstitutoActionPerformed);

        jLabelCursos.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCursos.setText("Cursos del instituto:");

        tableCursos.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Nombre", "Descripción"}
        ));
        tableCursos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCursosMouseClicked(evt);
            }
        });
        jScrollPaneCursos.setViewportView(tableCursos);

        jLabelDetalle.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDetalle.setText("Datos del curso seleccionado:");

        txtAreaDetalle.setEditable(false);
        txtAreaDetalle.setLineWrap(true);
        txtAreaDetalle.setWrapStyleWord(true);
        jScrollPaneDetalle.setViewportView(txtAreaDetalle);

        jLabelEdiciones.setForeground(new java.awt.Color(255, 255, 255));
        jLabelEdiciones.setText("Ediciones del curso:");

        tableEdiciones.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Edición del curso"}
        ));
        tableEdiciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableEdicionesMouseClicked(evt);
            }
        });
        jScrollPaneEdiciones.setViewportView(tableEdiciones);

        jLabelProgramas.setForeground(new java.awt.Color(255, 255, 255));
        jLabelProgramas.setText("Programas de formación asociados:");

        tablePrograms.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Programa de formación"}
        ));
        tablePrograms.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableProgramsMouseClicked(evt);
            }
        });
        jScrollPanePrograms.setViewportView(tablePrograms);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTitulo)

                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelInstituto)
                        .addGap(10, 10, 10)
                        .addComponent(comboInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))

                    .addComponent(jLabelCursos)
                    .addComponent(jScrollPaneCursos, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)

                    .addComponent(jLabelDetalle)
                    .addComponent(jScrollPaneDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)

                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelEdiciones)
                            .addComponent(jScrollPaneEdiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelProgramas)
                            .addComponent(jScrollPanePrograms, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo)
                .addGap(14, 14, 14)

                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelInstituto)
                    .addComponent(comboInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))

                .addGap(14, 14, 14)
                .addComponent(jLabelCursos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneCursos, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)

                .addGap(14, 14, 14)
                .addComponent(jLabelDetalle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)

                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelEdiciones)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneEdiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelProgramas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPanePrograms, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))

                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    private void comboInstitutoActionPerformed(java.awt.event.ActionEvent evt) {
        String institutoSeleccionado = (String) comboInstituto.getSelectedItem();
        cargarCursosDelInstituto(institutoSeleccionado);
    }

    private void tableCursosMouseClicked(java.awt.event.MouseEvent evt) {
        int fila = tableCursos.getSelectedRow();
        if (fila != -1) {
            String nombreCurso = tableCursos.getValueAt(fila, 0).toString();
            mostrarDetalleCurso(nombreCurso);
        }
    }

    private void tableEdicionesMouseClicked(java.awt.event.MouseEvent evt) {
        int fila = tableEdiciones.getSelectedRow();
        if (fila != -1) {
            String valor = tableEdiciones.getValueAt(fila, 0).toString();
            if (!valor.startsWith("(")) {
                mostrarDetalleEdicion(valor);
            }
        }
    }

    private void tableProgramsMouseClicked(java.awt.event.MouseEvent evt) {
        int fila = tablePrograms.getSelectedRow();
        if (fila != -1) {
            String valor = tablePrograms.getValueAt(fila, 0).toString();
            if (!valor.startsWith("(")) {
                mostrarDetallePrograma(valor);
            }
        }
    }

    private javax.swing.JComboBox<String> comboInstituto;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelInstituto;
    private javax.swing.JLabel jLabelCursos;
    private javax.swing.JLabel jLabelDetalle;
    private javax.swing.JLabel jLabelEdiciones;
    private javax.swing.JLabel jLabelProgramas;
    private javax.swing.JScrollPane jScrollPaneCursos;
    private javax.swing.JScrollPane jScrollPaneDetalle;
    private javax.swing.JScrollPane jScrollPaneEdiciones;
    private javax.swing.JScrollPane jScrollPanePrograms;
    private javax.swing.JTable tableCursos;
    private javax.swing.JTable tableEdiciones;
    private javax.swing.JTable tablePrograms;
    private javax.swing.JTextArea txtAreaDetalle;
}
