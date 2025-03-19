package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JPasswordField;
import java.awt.Dimension;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;
import com.toedter.calendar.JDateChooser;
import controlador.Controlador;
import excepciones.ExcepcionRegistro;

public class DatosUsuario extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nombre;
	private JTextField apellidos;
	private JLabel lblTelfono;
	private JTextField telefono;
	private JLabel lblContrasea;
	private JPasswordField contrasena;
	private JLabel lblRepitaContrasea;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JTextArea mensajeSaludo;
	private JButton btnCancelar;
	private JButton btnAceptar;
	private JLabel lblNewLabel_2;
	private JLabel imagenPerfil;
	private JLabel label;
	private JLabel label_1;
	private JDateChooser fechaNacimiento;
	private JButton btnCambiarFoto;
	private JLabel lblEmail;
	private JTextField email;
	private Controlador controlador = Controlador.getInstance();
	String imagenUserPath;
	private JPasswordField contrasenaRepe;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DatosUsuario frame = new DatosUsuario();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DatosUsuario() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 933, 486);
	    setResizable(false);             // Evita que la ventana se pueda redimensionar
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("List.dropCellBackground"));
		contentPane.setSize(new Dimension(800, 800));
		contentPane.setPreferredSize(new Dimension(15, 15));
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);
		
				label_1 = new JLabel("");
				label_1.setIcon(new ImageIcon(DatosUsuario.class.getResource("/REGISTRO.png")));
				GridBagConstraints gbc_label_1 = new GridBagConstraints();
				gbc_label_1.gridwidth = 13;
				gbc_label_1.insets = new Insets(0, 0, 5, 0);
				gbc_label_1.gridx = 0;
				gbc_label_1.gridy = 0;
				contentPane.add(label_1, gbc_label_1);

		label = new JLabel("");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 3;
		gbc_label.gridy = 1;
		contentPane.add(label, gbc_label);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Courier 10 Pitch", Font.BOLD, 14));
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.anchor = GridBagConstraints.EAST;
		gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre.gridx = 1;
		gbc_lblNombre.gridy = 2;
		contentPane.add(lblNombre, gbc_lblNombre);

		nombre = new JTextField();
		GridBagConstraints gbc_nombre = new GridBagConstraints();
		gbc_nombre.gridwidth = 9;
		gbc_nombre.insets = new Insets(0, 0, 5, 5);
		gbc_nombre.fill = GridBagConstraints.HORIZONTAL;
		gbc_nombre.gridx = 2;
		gbc_nombre.gridy = 2;
		contentPane.add(nombre, gbc_nombre);
		nombre.setColumns(10);

		JLabel lblApellidos = new JLabel("Apellidos:");
		lblApellidos.setFont(new Font("Courier 10 Pitch", Font.BOLD, 14));
		GridBagConstraints gbc_lblApellidos = new GridBagConstraints();
		gbc_lblApellidos.anchor = GridBagConstraints.EAST;
		gbc_lblApellidos.insets = new Insets(0, 0, 5, 5);
		gbc_lblApellidos.gridx = 1;
		gbc_lblApellidos.gridy = 3;
		contentPane.add(lblApellidos, gbc_lblApellidos);

		apellidos = new JTextField();
		GridBagConstraints gbc_apellidos = new GridBagConstraints();
		gbc_apellidos.gridwidth = 9;
		gbc_apellidos.insets = new Insets(0, 0, 5, 5);
		gbc_apellidos.fill = GridBagConstraints.HORIZONTAL;
		gbc_apellidos.gridx = 2;
		gbc_apellidos.gridy = 3;
		contentPane.add(apellidos, gbc_apellidos);
		apellidos.setColumns(10);

		lblTelfono = new JLabel("Teléfono:");
		lblTelfono.setFont(new Font("Courier 10 Pitch", Font.BOLD, 14));
		GridBagConstraints gbc_lblTelfono = new GridBagConstraints();
		gbc_lblTelfono.anchor = GridBagConstraints.EAST;
		gbc_lblTelfono.insets = new Insets(0, 0, 5, 5);
		gbc_lblTelfono.gridx = 1;
		gbc_lblTelfono.gridy = 4;
		contentPane.add(lblTelfono, gbc_lblTelfono);

		telefono = new JTextField();
		GridBagConstraints gbc_telefono = new GridBagConstraints();
		gbc_telefono.fill = GridBagConstraints.HORIZONTAL;
		gbc_telefono.gridwidth = 3;
		gbc_telefono.insets = new Insets(0, 0, 5, 5);
		gbc_telefono.gridx = 2;
		gbc_telefono.gridy = 4;
		contentPane.add(telefono, gbc_telefono);
		telefono.setColumns(10);

		lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font("Courier 10 Pitch", Font.BOLD, 14));
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.anchor = GridBagConstraints.EAST;
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 5;
		gbc_lblEmail.gridy = 4;
		contentPane.add(lblEmail, gbc_lblEmail);

		email = new JTextField();
		GridBagConstraints gbc_email = new GridBagConstraints();
		gbc_email.gridwidth = 5;
		gbc_email.insets = new Insets(0, 0, 5, 5);
		gbc_email.fill = GridBagConstraints.HORIZONTAL;
		gbc_email.gridx = 6;
		gbc_email.gridy = 4;
		contentPane.add(email, gbc_email);
		email.setColumns(10);

		lblContrasea = new JLabel("Contraseña:");
		lblContrasea.setFont(new Font("Courier 10 Pitch", Font.BOLD, 14));
		GridBagConstraints gbc_lblContrasea = new GridBagConstraints();
		gbc_lblContrasea.anchor = GridBagConstraints.EAST;
		gbc_lblContrasea.insets = new Insets(0, 0, 5, 5);
		gbc_lblContrasea.gridx = 1;
		gbc_lblContrasea.gridy = 5;
		contentPane.add(lblContrasea, gbc_lblContrasea);

		contrasena = new JPasswordField();
		GridBagConstraints gbc_contrasena = new GridBagConstraints();
		gbc_contrasena.gridwidth = 3;
		gbc_contrasena.insets = new Insets(0, 0, 5, 5);
		gbc_contrasena.fill = GridBagConstraints.BOTH;
		gbc_contrasena.gridx = 2;
		gbc_contrasena.gridy = 5;
		contentPane.add(contrasena, gbc_contrasena);

		lblRepitaContrasea = new JLabel("Repita contraseña:");
		lblRepitaContrasea.setFont(new Font("Courier 10 Pitch", Font.BOLD, 14));
		GridBagConstraints gbc_lblRepitaContrasea = new GridBagConstraints();
		gbc_lblRepitaContrasea.anchor = GridBagConstraints.EAST;
		gbc_lblRepitaContrasea.insets = new Insets(0, 0, 5, 5);
		gbc_lblRepitaContrasea.gridx = 5;
		gbc_lblRepitaContrasea.gridy = 5;
		contentPane.add(lblRepitaContrasea, gbc_lblRepitaContrasea);

		contrasenaRepe = new JPasswordField();
		GridBagConstraints gbc_contrasenaRepe = new GridBagConstraints();
		gbc_contrasenaRepe.gridwidth = 5;
		gbc_contrasenaRepe.insets = new Insets(0, 0, 5, 5);
		gbc_contrasenaRepe.fill = GridBagConstraints.HORIZONTAL;
		gbc_contrasenaRepe.gridx = 6;
		gbc_contrasenaRepe.gridy = 5;
		contentPane.add(contrasenaRepe, gbc_contrasenaRepe);

		lblNewLabel = new JLabel("Fecha de Nacimiento:");
		lblNewLabel.setFont(new Font("Courier 10 Pitch", Font.BOLD, 14));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 7;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		fechaNacimiento = new JDateChooser();
		GridBagConstraints gbc_fechaNacimiento = new GridBagConstraints();
		gbc_fechaNacimiento.gridwidth = 3;
		gbc_fechaNacimiento.insets = new Insets(0, 0, 5, 5);
		gbc_fechaNacimiento.fill = GridBagConstraints.BOTH;
		gbc_fechaNacimiento.gridx = 2;
		gbc_fechaNacimiento.gridy = 7;
		contentPane.add(fechaNacimiento, gbc_fechaNacimiento);
		
				lblNewLabel_2 = new JLabel("Foto:");
				lblNewLabel_2.setFont(new Font("Courier 10 Pitch", Font.BOLD, 14));
				GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
				gbc_lblNewLabel_2.gridwidth = 6;
				gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
				gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_2.gridx = 6;
				gbc_lblNewLabel_2.gridy = 8;
				contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);

		lblNewLabel_1 = new JLabel("Biografía:");
		lblNewLabel_1.setFont(new Font("Courier 10 Pitch", Font.BOLD, 14));
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.BELOW_BASELINE_TRAILING;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 9;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);

		mensajeSaludo = new JTextArea();
		mensajeSaludo.setWrapStyleWord(true);
		mensajeSaludo.setLineWrap(true);
		mensajeSaludo.setPreferredSize(new Dimension(5, 5));
		GridBagConstraints gbc_mensajeSaludo = new GridBagConstraints();
		gbc_mensajeSaludo.gridheight = 2;
		gbc_mensajeSaludo.gridwidth = 3;
		gbc_mensajeSaludo.insets = new Insets(0, 0, 5, 5);
		gbc_mensajeSaludo.fill = GridBagConstraints.BOTH;
		gbc_mensajeSaludo.gridx = 2;
		gbc_mensajeSaludo.gridy = 9;
		contentPane.add(mensajeSaludo, gbc_mensajeSaludo);
		
				imagenPerfil = new JLabel("");
				imagenPerfil.setIcon(new ImageIcon(DatosUsuario.class.getResource("/nueva_cuenta(1).png")));
				GridBagConstraints gbc_imagenPerfil = new GridBagConstraints();
				gbc_imagenPerfil.gridwidth = 3;
				gbc_imagenPerfil.insets = new Insets(0, 0, 5, 5);
				gbc_imagenPerfil.gridx = 8;
				gbc_imagenPerfil.gridy = 9;
				contentPane.add(imagenPerfil, gbc_imagenPerfil);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setMaximumSize(new Dimension(100, 27));
		btnCancelar.setPreferredSize(new Dimension(100, 27));
		btnCancelar.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 12));
		GridBagConstraints gbc_btnCancelar = new GridBagConstraints();
		gbc_btnCancelar.anchor = GridBagConstraints.WEST;
		gbc_btnCancelar.insets = new Insets(0, 0, 0, 5);
		gbc_btnCancelar.gridx = 2;
		gbc_btnCancelar.gridy = 11;
		contentPane.add(btnCancelar, gbc_btnCancelar);
		
		// Acción correspondiente al botón cancelar
		btnCancelar.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				 	Login login = new Login();
	                login.setVisible(true);
	                dispose(); 				
			}			
		});
	
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 12));
		btnAceptar.addActionListener( e -> {
							llamarControladorRegistro();								
						});
		
		GridBagConstraints gbc_btnAceptar = new GridBagConstraints();
		gbc_btnAceptar.anchor = GridBagConstraints.EAST;
		gbc_btnAceptar.insets = new Insets(0, 0, 0, 5);
		gbc_btnAceptar.gridx = 4;
		gbc_btnAceptar.gridy = 11;
		contentPane.add(btnAceptar, gbc_btnAceptar);
	
	
	btnCambiarFoto = new JButton("Cambiar foto");
	btnCambiarFoto.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 12));
	GridBagConstraints gbc_btnCambiarFoto = new GridBagConstraints();
	gbc_btnCambiarFoto.insets = new Insets(0, 0, 0, 5);
	gbc_btnCambiarFoto.gridx = 9;
	gbc_btnCambiarFoto.gridy = 11;
	contentPane.add(btnCambiarFoto, gbc_btnCambiarFoto);						
						btnCambiarFoto.addActionListener( e -> imagenUserPath = cambiarImagenPerfil());
	}

	private String cambiarImagenPerfil() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Selecciona una nueva imagen de perfil");

		int resultado = fileChooser.showOpenDialog(contentPane);
		if (resultado == JFileChooser.APPROVE_OPTION) {
			File archivoSeleccionado = fileChooser.getSelectedFile();
			try {
				BufferedImage imagen = ImageIO.read(archivoSeleccionado);
				if (imagen == null) {
					throw new Exception("El archivo seleccionado no es una imagen válida.");
				}
				//La muestra un poco rara
				Image imagenEscalada = imagen.getScaledInstance(imagenPerfil.getWidth(), imagenPerfil.getHeight(),
						Image.SCALE_SMOOTH);
				imagenPerfil.setIcon(new ImageIcon(imagenEscalada)); // Aplicar la imagen escalada al JLabel
			} catch (Exception e) {
				e.printStackTrace();
				MensajeAdvertencia.mostrarError(e.getMessage(), contentPane);
			}
			
			return archivoSeleccionado.getAbsolutePath();
		}
		
		return null;		
	}
	
	private void llamarControladorRegistro() {		
		try {
            // Intentar el registro
			controlador.registrarUsuario(
                nombre.getText(),
                apellidos.getText(),
                telefono.getText(),
                new String(contrasena.getPassword()),
                new String(contrasenaRepe.getPassword()),
                email.getText(),
                fechaNacimiento.getDate(),
                imagenUserPath,
                mensajeSaludo.getText()
            );

            // Si el registro es exitoso, mostrar confirmación.
            
                MensajeAdvertencia.mostrarConfirmacion("Registro completado satisfactoriamente", contentPane);
                Login login = new Login();
                login.setVisible(true);
                dispose(); 
            
            
        } catch (ExcepcionRegistro e1) {
        	e1.printStackTrace();
			MensajeAdvertencia.mostrarError(e1.getMessage(), contentPane);
        }
    }
	
		
}


