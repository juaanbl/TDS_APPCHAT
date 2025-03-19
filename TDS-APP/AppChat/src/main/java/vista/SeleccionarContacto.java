package vista;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import javax.swing.*;

public class SeleccionarContacto extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel panelCombo = new JPanel();
    private JButton cancelButton;
    private JButton okButton;
    private JComboBox<String> comboBox;
    private String contactoSeleccionado;
    private String rutaSeleccionada;
    private JTextField textFieldRuta;

    public SeleccionarContacto(Frame owner, List<String> nombresContactos) {
        super(owner, true); 
        setBounds(100, 100, 506, 306);
        getContentPane().setBackground(UIManager.getColor("List.dropCellBackground"));
        getContentPane().setLayout(new BorderLayout());

        // Panel superior con el título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(UIManager.getColor("List.dropCellBackground"));
        getContentPane().add(panelTitulo, BorderLayout.NORTH);
        panelTitulo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel lblSeleccioneElContacto = new JLabel("Seleccione el contacto y la ruta para exportar la conversación");
        lblSeleccioneElContacto.setFont(new Font("Dialog", Font.BOLD, 13));
        panelTitulo.add(lblSeleccioneElContacto);

        // Panel central con el ComboBox y selección de ruta
        JPanel panelComboBox = new JPanel();
        panelComboBox.setBackground(UIManager.getColor("List.dropCellBackground"));
        panelCombo.add(panelComboBox);
        comboBox = new JComboBox<>(nombresContactos.toArray(new String[0]));
        comboBox.setPreferredSize(new Dimension(250, 26));
        panelComboBox.add(comboBox);
        getContentPane().add(panelCombo, BorderLayout.CENTER);
        panelCombo.setLayout(new BoxLayout(panelCombo, BoxLayout.Y_AXIS));

        // Botón y campo de texto para seleccionar ruta
        JPanel panelRuta = new JPanel();
        panelRuta.setBackground(UIManager.getColor("List.dropCellBackground"));
        panelRuta.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton btnSeleccionarRuta = new JButton("Seleccionar ruta");
        btnSeleccionarRuta.setFont(new Font("Liberation Mono", Font.BOLD, 12));
        textFieldRuta = new JTextField();
        textFieldRuta.setPreferredSize(new Dimension(250, 26));
        textFieldRuta.setEditable(false);

        btnSeleccionarRuta.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int seleccion = fileChooser.showOpenDialog(this);
            if (seleccion == JFileChooser.APPROVE_OPTION) {
                File ruta = fileChooser.getSelectedFile();
                rutaSeleccionada = ruta.getAbsolutePath();
                textFieldRuta.setText(rutaSeleccionada);
            }
        });
        
        Component horizontalGlue = Box.createHorizontalGlue();
        horizontalGlue.setPreferredSize(new Dimension(50, 0));
        panelRuta.add(horizontalGlue);

        panelRuta.add(btnSeleccionarRuta);
        panelRuta.add(textFieldRuta);
        panelCombo.add(panelRuta);

        // Panel inferior con los botones
        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(UIManager.getColor("List.dropCellBackground"));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // Botón Aceptar
        okButton = new JButton("Aceptar");
        okButton.setFont(new Font("Liberation Mono", Font.BOLD, 12));
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                contactoSeleccionado = (String) comboBox.getSelectedItem(); // Guarda el contacto seleccionado
                if(contactoSeleccionado != null && rutaSeleccionada != null) {
                	 dispose(); // Cierra el diálogo
                }else {
                	 MensajeAdvertencia.mostrarError("Seleccione una ruta y un contacto válidos", panelComboBox);
                }
               
            }
        });
        buttonPane.add(okButton);

        // Espaciadores horizontales
        Component horizontalGlue1 = Box.createHorizontalGlue();
        horizontalGlue1.setPreferredSize(new Dimension(80, 0));
        buttonPane.add(horizontalGlue1);

        JLabel lblIcono = new JLabel("");
        lblIcono.setIcon(new ImageIcon(SeleccionarContacto.class.getResource("/archivo(1).png")));
        buttonPane.add(lblIcono);

        Component horizontalGlue2 = Box.createHorizontalGlue();
        horizontalGlue2.setPreferredSize(new Dimension(80, 0));
        buttonPane.add(horizontalGlue2);

        // Botón Cancelar
        cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Liberation Mono", Font.BOLD, 12));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                contactoSeleccionado = null; // No guarda ningún contacto seleccionado
                rutaSeleccionada = null; // No guarda ninguna ruta seleccionada
                dispose(); // Cierra el diálogo
            }
        });
        buttonPane.add(cancelButton);
    }

    /**
     * Devuelve el contacto y la ruta seleccionados.
     * 
     * @return Un array de Strings donde [0] es el contacto seleccionado y [1] la ruta seleccionada.
     */
    public String[] getContactoYRutaSeleccionados() {
        return new String[]{contactoSeleccionado, rutaSeleccionada};
             
    }

    /**
     * Muestra el diálogo y devuelve el contacto y la ruta seleccionados.
     * 
     * @param owner        La ventana principal desde la que se llama.
     * @param nombresContactos Lista de nombres de los contactos para seleccionar.
     * @return Un array de Strings donde [0] es el contacto seleccionado y [1] la ruta seleccionada.
     */
    public static String[] mostrarDialogo(Frame owner, List<String> nombresContactos) {
        SeleccionarContacto dialog = new SeleccionarContacto(owner, nombresContactos);
        dialog.setVisible(true);
        return dialog.getContactoYRutaSeleccionados();
    }
}
