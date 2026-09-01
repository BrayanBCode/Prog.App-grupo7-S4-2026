/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt 
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java 
 */
package Presentacion.Dialogo;

import Logica.controller.IController;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;


public class JIRegistroCurso extends javax.swing.JInternalFrame {

    private IController control;
    // Paralelo a los items de comboDocente: docentesActuales.get(i) = {nickname, textoMostrado}
    private List<String[]> docentesActuales = new ArrayList<>();

    public JIRegistroCurso(IController c) {
        initComponents();
        this.control = c;
        cargarInstitutos();
        cargarPrevias();
        resetearFecha();
    }

    private void cargarInstitutos() {
        comboInstituto.removeAllItems();
        List<String> institutos = control.listarNombresInstitutos();
        if (institutos != null) {
            for (String i : institutos) {
                comboInstituto.addItem(i);
            }
        }
        if (comboInstituto.getItemCount() > 0) {
            comboInstituto.setSelectedIndex(0);
            cargarDocentesPorInstituto((String) comboInstituto.getSelectedItem());
        }
    }

    private void cargarDocentesPorInstituto(String nombreInstituto) {
        comboDocente.removeAllItems();
        docentesActuales = control.listarDocentesPorInstituto(nombreInstituto);
        if (docentesActuales != null) {
            for (String[] d : docentesActuales) {
                comboDocente.addItem(d[1]);
            }
        }
    }

    private void cargarPrevias() {
        DefaultListModel<String> model = new DefaultListModel<>();
        List<String> cursos = control.listarNombresCursos();
        if (cursos != null) {
            for (String c : cursos) {
                model.addElement(c);
            }
        }
        listPrevias.setModel(model);
    }

    private void resetearFecha() {
        LocalDate hoy = LocalDate.now();
        SDia.setValue(hoy.getDayOfMonth());
        SMes.setValue(hoy.getMonthValue());
        SAnio.setValue(hoy.getYear());
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtDuracion.setText("");
        txtCantHoras.setText("");
        txtCantCreditos.setText("");
        txtUrl.setText("");
        resetearFecha();
        listPrevias.clearSelection();
        if (comboInstituto.getItemCount() > 0) {
            comboInstituto.setSelectedIndex(0);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        jLabel2 = new javax.swing.JLabel();
        comboInstituto = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        comboDocente = new javax.swing.JComboBox<>();

        jLabel4 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();

        jLabel5 = new javax.swing.JLabel();
        jScrollPaneDescripcion = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea(3, 20);

        jLabel6 = new javax.swing.JLabel();
        txtDuracion = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtCantHoras = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCantCreditos = new javax.swing.JTextField();

        jLabel9 = new javax.swing.JLabel();
        txtUrl = new javax.swing.JTextField();

        jLabel10 = new javax.swing.JLabel();
        SDia = new javax.swing.JSpinner(new javax.swing.SpinnerNumberModel(1, 1, 31, 1));
        SMes = new javax.swing.JSpinner(new javax.swing.SpinnerNumberModel(1, 1, 12, 1));
        SAnio = new javax.swing.JSpinner(new javax.swing.SpinnerNumberModel(2026, 1900, 2100, 1));

        jLabel11 = new javax.swing.JLabel();
        jScrollPanePrevias = new javax.swing.JScrollPane();
        listPrevias = new javax.swing.JList<>();
        listPrevias.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jScrollPanePrevias.setViewportView(listPrevias);

        jButtonAceptar = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();

        setBackground(new java.awt.Color(62, 67, 76));
        setBorder(null);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ALTA DE CURSO");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Instituto:");
        comboInstituto.addItemListener(evt -> {
            if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                cargarDocentesPorInstituto((String) evt.getItem());
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Docente (integra el instituto elegido):");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nombre del curso:");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Descripcion:");
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        jScrollPaneDescripcion.setViewportView(txtDescripcion);

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Duracion (semanas):");
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Cant. horas:");
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Cant. creditos:");

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("URL:");

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Fecha de alta (D/M/A):");

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Previas (opcional, seleccion multiple):");

        jButtonAceptar.setText("Registrar Curso");
        jButtonAceptar.addActionListener(this::jButtonAceptarActionPerformed);

        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.addActionListener(this::jButtonCancelarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)

                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(comboInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(comboDocente, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))

                    .addComponent(jLabel4)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)

                    .addComponent(jLabel5)
                    .addComponent(jScrollPaneDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)

                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(txtDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(txtCantHoras, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txtCantCreditos, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))

                    .addComponent(jLabel9)
                    .addComponent(txtUrl, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)

                    .addComponent(jLabel10)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(SDia, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(SMes, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(SAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))

                    .addComponent(jLabel11)
                    .addComponent(jScrollPanePrevias, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)

                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonAceptar)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonCancelar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(14, 14, 14)

                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboInstituto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboDocente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))

                .addGap(14, 14, 14)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)

                .addGap(14, 14, 14)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)

                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantHoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantCreditos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))

                .addGap(14, 14, 14)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)

                .addGap(14, 14, 14)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SDia)
                    .addComponent(SMes)
                    .addComponent(SAnio))

                .addGap(14, 14, 14)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPanePrevias, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)

                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAceptar)
                    .addComponent(jButtonCancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    private void jButtonAceptarActionPerformed(java.awt.event.ActionEvent evt) {

        if (comboInstituto.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "No hay institutos registrados. Primero hay que dar de alta un instituto.", "Sin institutos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idxDocente = comboDocente.getSelectedIndex();
        if (idxDocente == -1 || docentesActuales.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El instituto elegido no tiene docentes que lo integren. Elegi otro instituto o cargá el docente correspondiente primero.",
                "Sin docentes en el instituto", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String url = txtUrl.getText().trim();

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y descripcion son obligatorios.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int duracion;
        float cantHoras;
        int cantCreditos;
        try {
            duracion = Integer.parseInt(txtDuracion.getText().trim());
            cantHoras = Float.parseFloat(txtCantHoras.getText().trim());
            cantCreditos = Integer.parseInt(txtCantCreditos.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Duracion y cantidad de creditos deben ser numeros enteros; cantidad de horas puede llevar decimales.",
                "Datos numericos invalidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate fechaRegistro = LocalDate.of(
            (Integer) SAnio.getValue(),
            (Integer) SMes.getValue(),
            (Integer) SDia.getValue()
        );

        String nombreInstituto = (String) comboInstituto.getSelectedItem();
        String nicknameDocente = docentesActuales.get(idxDocente)[0];
        List<String> previasElegidas = listPrevias.getSelectedValuesList();

        try {
            control.altaCurso(nombre, descripcion, duracion, cantHoras, cantCreditos, url,
                fechaRegistro, nombreInstituto, nicknameDocente, previasElegidas);

            JOptionPane.showMessageDialog(this, "Curso registrado con exito.", "Exito", JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();
            cargarPrevias(); // el curso recien creado ya puede elegirse como previa de otro

        } catch (Exception ex) {
            // Nombre duplicado, docente que no integra el instituto, instituto/previa inexistente, etc.
            // El formulario NO se limpia: el administrador puede corregir el dato puntual y reintentar.
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al registrar el curso", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        limpiarFormulario();
    }

    private javax.swing.JSpinner SAnio;
    private javax.swing.JSpinner SDia;
    private javax.swing.JSpinner SMes;
    private javax.swing.JButton jButtonAceptar;
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JComboBox<String> comboDocente;
    private javax.swing.JComboBox<String> comboInstituto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> listPrevias;
    private javax.swing.JScrollPane jScrollPaneDescripcion;
    private javax.swing.JScrollPane jScrollPanePrevias;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtCantCreditos;
    private javax.swing.JTextField txtCantHoras;
    private javax.swing.JTextField txtDuracion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtUrl;
}