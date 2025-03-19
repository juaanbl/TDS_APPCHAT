package vista;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import controlador.Controlador;
import excepciones.ExcepcionAgregarContacto;

/**
 * Ventana para añadir un nuevo contacto.
 * Permite al usuario ingresar el nombre y el teléfono del contacto y añadirlo a la lista.
 */
public class AñadirContacto extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField textTlf;
    private JTextField textNombre;
    private Controlador controlador = Controlador.getInstance();


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AñadirContacto frame = new AñadirContacto(null);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Constructor de la ventana.
     * Configura el diseño y los componentes gráficos.
     * 
     * @param principal Referencia a la ventana principal para actualizar la lista de contactos.
     */
    public AñadirContacto(Principal principal) {
        // Configuración de la ventana principal
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 516, 288);
        setResizable(false); // Evita que la ventana se pueda redimensionar

        // Configuración del panel principal
        contentPane = new JPanel();
        contentPane.setBackground(UIManager.getColor("List.dropCellBackground"));
        contentPane.setBorder(new LineBorder(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"), 4));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        // Sección inferior: Botones de aceptar y cancelar
        configurarPanelInferior(principal);

        // Sección central: Entrada de datos y mensaje de alerta
        configurarPanelCentral();

        // Espaciador superior
        Component rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
        rigidArea_2.setPreferredSize(new Dimension(20, 30));
        contentPane.add(rigidArea_2, BorderLayout.NORTH);
    }

    /**
     * Configura el panel inferior con los botones de aceptar y cancelar.
     * 
     * @param principal Referencia a la ventana principal para actualizar la lista de contactos.
     */
    private void configurarPanelInferior(Principal principal) {
        JPanel abajo = new JPanel();
        abajo.setBackground(UIManager.getColor("List.dropCellBackground"));
        abajo.setBorder(new EmptyBorder(26, 0, 0, 0));
        contentPane.add(abajo, BorderLayout.SOUTH);
        abajo.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        // Botón de aceptar
        JButton aceptar = new JButton("Aceptar");
        aceptar.setFont(new Font("Liberation Mono", Font.BOLD, 12));
        aceptar.setHorizontalAlignment(SwingConstants.LEADING);
        abajo.add(aceptar);

        // Acción del botón de aceptar
        aceptar.addActionListener(ev -> {
            try {
                controlador.agregarContacto(textTlf.getText(), textNombre.getText());
                MensajeAdvertencia.mostrarConfirmacion("Contacto añadido correctamente", contentPane);
                dispose();
            } catch (ExcepcionAgregarContacto e) {
                MensajeAdvertencia.mostrarError(e.getMessage(), contentPane);
                e.printStackTrace();
            }
        });

        // Botón de cancelar
        JButton cancelar = new JButton("Cancelar");
        cancelar.setFont(new Font("Liberation Mono", Font.BOLD, 12));
        cancelar.setHorizontalAlignment(SwingConstants.LEADING);
        abajo.add(cancelar);

        // Acción del botón de cancelar
        cancelar.addActionListener(e -> dispose());

        // Actualización de la lista de contactos al cerrar la ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (principal != null) {
                    principal.actualizarListaContactos();
                }
            }
        });
    }

    /**
     * Configura el panel central con los campos de entrada de datos y el mensaje de alerta.
     */
    private void configurarPanelCentral() {
        JPanel centro = new JPanel();
        centro.setBackground(UIManager.getColor("List.dropCellBackground"));
        contentPane.add(centro, BorderLayout.CENTER);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        // Mensaje de alerta en la parte superior
        JPanel mensaje_alerta = new JPanel();
        mensaje_alerta.setBackground(UIManager.getColor("List.dropCellBackground"));
        centro.add(mensaje_alerta);
        mensaje_alerta.setLayout(new BoxLayout(mensaje_alerta, BoxLayout.X_AXIS));

        JLabel lblNewLabel_3 = new JLabel("");
        lblNewLabel_3.setIcon(new ImageIcon(AñadirContacto.class.getResource("/alerta24x24.png")));
        mensaje_alerta.add(lblNewLabel_3);

        JLabel lblNewLabel_2 = new JLabel("   Introduzca el nombre del contacto y su teléfono");
        lblNewLabel_2.setFont(new Font("Liberation Mono", Font.BOLD, 15));
        mensaje_alerta.add(lblNewLabel_2);

        // Espaciadores para separar las secciones
        centro.add(Box.createRigidArea(new Dimension(20, 20)));
        centro.add(Box.createRigidArea(new Dimension(20, 20)));

        // Campo de entrada para el nombre
        JPanel nombre = new JPanel();
        nombre.setBackground(UIManager.getColor("List.dropCellBackground"));
        nombre.setMaximumSize(new Dimension(32767, 0));
        centro.add(nombre);
        nombre.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel lblNewLabel = new JLabel("Nombre:");
        lblNewLabel.setPreferredSize(new Dimension(67, 17));
        lblNewLabel.setFont(new Font("Liberation Mono", Font.BOLD, 15));
        nombre.add(lblNewLabel);

        textNombre = new JTextField();
        nombre.add(textNombre);
        textNombre.setColumns(10);

        // Campo de entrada para el teléfono
        JPanel telefono = new JPanel();
        telefono.setBackground(UIManager.getColor("List.dropCellBackground"));
        centro.add(telefono);
        telefono.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel lblNewLabel_1 = new JLabel("Teléfono:");
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setFont(new Font("Liberation Mono", Font.BOLD, 15));
        telefono.add(lblNewLabel_1);

        textTlf = new JTextField();
        telefono.add(textTlf);
        textTlf.setColumns(10);
    }
}
