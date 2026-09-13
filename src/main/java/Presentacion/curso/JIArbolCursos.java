/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Presentacion.curso;

import Logica.controller.IController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

/**
 * Pantalla que muestra en un JTree la jerarquía
 * Instituto -&gt; Curso -&gt; Edición de Curso.
 *
 * La carga es perezosa: cada nivel se consulta al controller recién
 * cuando el usuario despliega el nodo, para no traer toda la base
 * de una sola vez.
 *
 * @author maida
 */
public class JIArbolCursos extends javax.swing.JInternalFrame {

    private IController control;

    // Tipos de nodo que puede tener el árbol
    private enum Tipo { RAIZ, INSTITUTO, CURSO, EDICION }

    // Guarda el tipo y el nombre real de cada nodo (lo que se usa para
    // consultar al controller), evitando parsear el texto mostrado.
    private static class NodoInfo {
        final Tipo tipo;
        final String nombre;
        boolean cargado = false;

        NodoInfo(Tipo tipo, String nombre) {
            this.tipo = tipo;
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    /**
     * Creates new form JIArbolCursos
     */
    public JIArbolCursos(IController c) {
        initComponents();
        this.control = c;
        cargarArbol();
    }

    private void cargarArbol() {
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode(new NodoInfo(Tipo.RAIZ, "Institutos"));

        List<String> institutos = control.listarNombresInstitutos();
        for (String nombreInst : institutos) {
            DefaultMutableTreeNode nodoInst = new DefaultMutableTreeNode(new NodoInfo(Tipo.INSTITUTO, nombreInst));
            agregarHijoDummy(nodoInst);
            raiz.add(nodoInst);
        }

        DefaultTreeModel modelo = new DefaultTreeModel(raiz);
        jTreeCursos.setModel(modelo);
        jTreeCursos.setRootVisible(true);
        jTreeCursos.setShowsRootHandles(true);
    }

    // Nodo "placeholder" para que el instituto/curso muestre la flecha de
    // expandir aunque todavía no se haya consultado al controller.
    private void agregarHijoDummy(DefaultMutableTreeNode nodo) {
        nodo.add(new DefaultMutableTreeNode("Cargando..."));
    }

    private void expandirNodo(DefaultMutableTreeNode nodo) {
        Object obj = nodo.getUserObject();
        if (!(obj instanceof NodoInfo)) {
            return;
        }
        NodoInfo info = (NodoInfo) obj;
        if (info.cargado) {
            return;
        }

        DefaultTreeModel modelo = (DefaultTreeModel) jTreeCursos.getModel();

        // Saca el nodo "Cargando..." antes de poner los hijos reales
        nodo.removeAllChildren();

        switch (info.tipo) {
            case INSTITUTO:
                List<String> cursos = control.listarCursosPorInstituto(info.nombre);
                for (String nombreCurso : cursos) {
                    DefaultMutableTreeNode nodoCurso = new DefaultMutableTreeNode(new NodoInfo(Tipo.CURSO, nombreCurso));
                    agregarHijoDummy(nodoCurso);
                    nodo.add(nodoCurso);
                }
                break;
            case CURSO:
                List<String> ediciones = control.listarEdicionesCurso(info.nombre);
                for (String nombreEdicion : ediciones) {
                    nodo.add(new DefaultMutableTreeNode(new NodoInfo(Tipo.EDICION, nombreEdicion)));
                }
                break;
            default:
                break;
        }

        info.cargado = true;
        modelo.nodeStructureChanged(nodo);
    }

    private void mostrarDetalleCurso(String nombreCurso) {
        try {
            String[] d = control.obtenerDataCurso(nombreCurso);
            // {0}=nombre, {1}=descripcion, {2}=duracion, {3}=cantHoras, {4}=cantCreditos,
            // {5}=url, {6}=fechaRegistro, {7}=instituto, {8}=docente, {9}=previas
            String mensaje = "DETALLE DEL CURSO:\n\n"
                    + "Nombre: " + d[0] + "\n"
                    + "Descripción: " + d[1] + "\n"
                    + "Duración: " + d[2] + "\n"
                    + "Cant. horas: " + d[3] + "\n"
                    + "Cant. créditos: " + d[4] + "\n"
                    + "URL: " + d[5] + "\n"
                    + "Fecha de registro: " + d[6] + "\n"
                    + "Instituto: " + d[7] + "\n"
                    + "Docente: " + d[8] + "\n"
                    + "Previas: " + d[9];
            JOptionPane.showMessageDialog(this, mensaje, "Información del Curso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetalleEdicion(String nombreEdicion) {
        try {
            String[] d = control.obtenerEdicionCurso(nombreEdicion);
            String mensaje = "DETALLES DE LA EDICIÓN DE CURSO:\n\n"
                    + "Nombre: " + d[0] + "\n"
                    + "Curso: " + d[1] + "\n"
                    + "Fecha de inicio: " + d[2] + "\n"
                    + "Fecha de fin: " + d[3] + "\n"
                    + "Cupo: " + d[4] + "\n"
                    + "Fecha de publicación: " + d[5];
            JOptionPane.showMessageDialog(this, mensaje, "Información de Edición de Curso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {

        jLabelTitulo = new JLabel();
        jScrollPaneArbol = new JScrollPane();
        jTreeCursos = new JTree(new DefaultMutableTreeNode("Institutos"));

        setBackground(new Color(62, 67, 76));
        getContentPane().setLayout(new BorderLayout(10, 10));

        jLabelTitulo.setFont(new Font("Segoe UI", 0, 18));
        jLabelTitulo.setForeground(new Color(255, 255, 255));
        jLabelTitulo.setText("Institutos / Cursos / Ediciones de Curso");
        jLabelTitulo.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 10, 15));

        jTreeCursos.setRootVisible(true);
        jTreeCursos.setShowsRootHandles(true);
        jTreeCursos.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                jTreeCursosTreeWillExpand(event);
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
                // No se hace nada al colapsar
            }
        });
        jTreeCursos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                jTreeCursosMouseClicked(evt);
            }
        });

        jScrollPaneArbol.setViewportView(jTreeCursos);
        jScrollPaneArbol.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 20, 20));

        getContentPane().add(jLabelTitulo, BorderLayout.NORTH);
        getContentPane().add(jScrollPaneArbol, BorderLayout.CENTER);

        setSize(420, 480);
    }

    private void jTreeCursosTreeWillExpand(TreeExpansionEvent event) {
        TreePath path = event.getPath();
        Object componente = path.getLastPathComponent();
        if (componente instanceof DefaultMutableTreeNode) {
            expandirNodo((DefaultMutableTreeNode) componente);
        }
    }

    private void jTreeCursosMouseClicked(MouseEvent evt) {
        if (evt.getClickCount() != 2) {
            return;
        }
        TreePath path = jTreeCursos.getPathForLocation(evt.getX(), evt.getY());
        if (path == null) {
            return;
        }
        Object componente = path.getLastPathComponent();
        if (!(componente instanceof DefaultMutableTreeNode)) {
            return;
        }
        Object userObject = ((DefaultMutableTreeNode) componente).getUserObject();
        if (!(userObject instanceof NodoInfo)) {
            return;
        }
        NodoInfo info = (NodoInfo) userObject;
        if (info.tipo == Tipo.CURSO) {
            mostrarDetalleCurso(info.nombre);
        } else if (info.tipo == Tipo.EDICION) {
            mostrarDetalleEdicion(info.nombre);
        }
    }

    // Variables declaration
    private JLabel jLabelTitulo;
    private JScrollPane jScrollPaneArbol;
    private JTree jTreeCursos;
    // End of variables declaration
}
