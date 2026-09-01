/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Presentacion.Dialogo;

import Logica.controller.IController;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;


public class JIModificarUsuario extends javax.swing.JInternalFrame {

    private IController control;

    public JIModificarUsuario(IController c) {
        initComponents();
        this.control = c;
        cargarTablaUsuarios();
        limpiarFormulario();
    }

    private void cargarTablaUsuarios() {
        List<String[]> datos = control.listarUsuariosTabla();

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        if (datos != null) {
            for (String[] fila : datos) {
                model.addRow(fila);
            }
        }
    }

    // Deja el formulario vacio y deshabilitado hasta que se elija un usuario de la tabla
    private void limpiarFormulario() {
        FNickname.setText("");
        Fmail.setText("");
        FNombre.setText("");
        FApellido.setText("");
        SDia.setValue(1);
        SMes.setValue(1);
        SAnio.setValue(2000);

        FNombre.setEnabled(false);
        FApellido.setEnabled(false);
        SDia.setEnabled(false);
        SMes.setEnabled(false);
        SAnio.setEnabled(false);
        jButton1.setEnabled(false);

        jTable1.clearSelection();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jLabel2 = new javax.swing.JLabel();
        FNickname = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        Fmail = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        FNombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        FApellido = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        SDia = new javax.swing.JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        SMes = new javax.swing.JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        SAnio = new javax.swing.JSpinner(new SpinnerNumberModel(2000, 1900, 2100, 1));

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setBackground(new java.awt.Color(62, 67, 76));
        setBorder(null);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("MODIFICAR DATOS DE USUARIO");

        jTable1.setForeground(new java.awt.Color(0, 0, 0));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Nickname", "Email" }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nickname:");
        FNickname.setEditable(false);
        FNickname.setBackground(new java.awt.Color(220, 220, 220));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Correo Electronico:");
        Fmail.setEditable(false);
        Fmail.setBackground(new java.awt.Color(220, 220, 220));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nombre:");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Apellido:");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Fecha de nacimiento (D/M/A):");

        jButton1.setText("Guardar Cambios");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("Cancelar");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                    .addComponent(FNickname, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Fmail, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(SDia, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(SMes, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(SAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FNickname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Fmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SDia)
                    .addComponent(SMes)
                    .addComponent(SAnio))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        int fila = jTable1.getSelectedRow();
        if (fila == -1) {
            return;
        }

        String nickname = jTable1.getValueAt(fila, 0).toString();
        String mail = jTable1.getValueAt(fila, 1).toString();

        String[] datos = control.obtenerDataUsuario(nickname, mail);

        if (datos == null) {
            JOptionPane.showMessageDialog(this, "El usuario seleccionado ya no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            limpiarFormulario();
            cargarTablaUsuarios();
            return;
        }

        // datos = {nickname, mail, nombre, apellido, fechaNac (yyyy-MM-dd) o ""}
        FNickname.setText(datos[0]);
        Fmail.setText(datos[1]);
        FNombre.setText(datos[2]);
        FApellido.setText(datos[3]);

        if (datos[4] != null && !datos[4].isEmpty()) {
            LocalDate f = LocalDate.parse(datos[4]);
            SDia.setValue(f.getDayOfMonth());
            SMes.setValue(f.getMonthValue());
            SAnio.setValue(f.getYear());
        } else {
            SDia.setValue(1);
            SMes.setValue(1);
            SAnio.setValue(2000);
        }

        FNombre.setEnabled(true);
        FApellido.setEnabled(true);
        SDia.setEnabled(true);
        SMes.setEnabled(true);
        SAnio.setEnabled(true);
        jButton1.setEnabled(true);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String nickname = FNickname.getText().trim();
        String mail = Fmail.getText().trim();

        if (nickname.isEmpty() || mail.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un usuario de la tabla antes de guardar cambios.",
                "Ningun usuario seleccionado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = FNombre.getText().trim();
        String apellido = FApellido.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nombre y apellido no pueden quedar vacios.",
                "Campos incompletos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate fechaNac = LocalDate.of(
            (Integer) SAnio.getValue(),
            (Integer) SMes.getValue(),
            (Integer) SDia.getValue()
        );

        try {
            control.modificarUsuario(nickname, mail, nombre, apellido, fechaNac);

            JOptionPane.showMessageDialog(this,
                "Usuario modificado con exito.",
                "Exito",
                JOptionPane.INFORMATION_MESSAGE);

            cargarTablaUsuarios();
            limpiarFormulario();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error al modificar",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        limpiarFormulario();
    }

    private javax.swing.JTextField FApellido;
    private javax.swing.JTextField FNickname;
    private javax.swing.JTextField FNombre;
    private javax.swing.JTextField Fmail;
    private javax.swing.JSpinner SAnio;
    private javax.swing.JSpinner SDia;
    private javax.swing.JSpinner SMes;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
}
