package vista;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import controlador.Controlador;
import excepciones.ExcepcionLogin;

import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.JPasswordField;

/**
 * Clase que representa la ventana de inicio de sesión (Login).
 * Permite al usuario iniciar sesión, registrarse o cerrar la ventana.
 */
public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane; 
    private JTextField telefono; 
    private JPasswordField contrasena; 
    private Controlador controlador = Controlador.getInstance();        

    /**
     * Método principal para ejecutar la aplicación y mostrar la ventana de Login.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Login frame = new Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Constructor de la clase Login.
     * Configura los componentes y eventos de la ventana.
     */
    public Login() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setResizable(false); // Evita que la ventana pueda redimensionarse

        // Panel principal
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(UIManager.getColor("List.dropCellBackground"));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        // Sección inferior (botones de acción)
        JPanel sur = new JPanel();
        sur.setBorder(new EmptyBorder(5, 5, 5, 5));
        sur.setBackground(UIManager.getColor("List.dropCellBackground"));
        contentPane.add(sur, BorderLayout.SOUTH);
        sur.setLayout(new BoxLayout(sur, BoxLayout.X_AXIS));

        // Botón "Cancelar"
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 11));
        btnCancelar.addActionListener(ev -> dispose()); // Cierra la ventana
        sur.add(btnCancelar);

        // Espaciador
        Component glue = Box.createGlue();
        sur.add(glue);

        // Botón "Login"
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 11));
        btnLogin.addActionListener(ev -> {
            try {
                controlador.loguearUsuario(telefono.getText(), new String(contrasena.getPassword()));
                Principal ventanaMain = new Principal();
                ventanaMain.setVisible(true);
                dispose();
            } catch (ExcepcionLogin e) {
                MensajeAdvertencia.mostrarError(e.getMessage(), contentPane);
            }
        });
        sur.add(btnLogin);
        

        // Espaciador
        Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
        sur.add(rigidArea);

        // Botón "Registrarse"
        JButton btnRegistrarse = new JButton("Registrarse");
        btnRegistrarse.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 11));
        btnRegistrarse.addActionListener(e -> {
            dispose();
            DatosUsuario datos = new DatosUsuario();
            datos.setVisible(true);
        });
        sur.add(btnRegistrarse);

        // Sección central (formulario de entrada)
        JPanel center = new JPanel();
        center.setBorder(new TitledBorder(new LineBorder(new Color(25, 25, 112), 2), "Login",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        center.setBackground(UIManager.getColor("List.dropCellBackground"));
        contentPane.add(center, BorderLayout.CENTER);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        // Espaciador superior
        Component verticalGlue = Box.createVerticalGlue();
        verticalGlue.setPreferredSize(new Dimension(0, 40));
        center.add(verticalGlue);

        // Panel del icono
        JPanel icono_1 = new JPanel();
        icono_1.setBackground(UIManager.getColor("List.dropCellBackground"));
        center.add(icono_1);
        JLabel icono = new JLabel("");
        icono.setIcon(new ImageIcon(Login.class.getResource("/Appchatlogominecraft.png")));
        icono_1.add(icono);

        // Panel del campo de teléfono
        JPanel Telefono = new JPanel();
        Telefono.setBackground(UIManager.getColor("List.dropCellBackground"));
        center.add(Telefono);
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(new Font("Liberation Mono", Font.BOLD, 15));
        lblTelefono.setHorizontalAlignment(SwingConstants.RIGHT);
        Telefono.add(lblTelefono);
        telefono = new JTextField();
        telefono.setColumns(10);
        Telefono.add(telefono);

        // Panel del campo de contraseña
        JPanel Contraseña = new JPanel();
        Contraseña.setBackground(UIManager.getColor("List.dropCellBackground"));
        center.add(Contraseña);
        JLabel lblContrasea = new JLabel("Contraseña:");
        lblContrasea.setFont(new Font("Liberation Mono", Font.BOLD, 15));
        lblContrasea.setHorizontalAlignment(SwingConstants.RIGHT);
        Contraseña.add(lblContrasea);
        contrasena = new JPasswordField();
        contrasena.setPreferredSize(new Dimension(115, 21));
        Contraseña.add(contrasena);
        
        Component verticalGlue_2 = Box.createVerticalGlue();
        verticalGlue_2.setPreferredSize(new Dimension(0, 40));
        center.add(verticalGlue_2);

        // Espaciador inferior
        Component verticalGlue_1 = Box.createVerticalGlue();
        verticalGlue_1.setMaximumSize(new Dimension(0, 20));
        center.add(verticalGlue_1);
    }
}
