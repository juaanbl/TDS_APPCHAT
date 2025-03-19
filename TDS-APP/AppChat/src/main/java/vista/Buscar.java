package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.TitledBorder;

import controlador.Controlador;
import dominio.Mensaje;

import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import java.awt.Font;

public class Buscar extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField Contacto;
	private JTextField telefono;
	private JTextField txtTextoABuscar;
	private Controlador controlador = Controlador.getInstance();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Buscar frame = new Buscar();
					frame.addWindowFocusListener(new java.awt.event.WindowAdapter() {

						@Override
						public void windowGainedFocus(java.awt.event.WindowEvent e) {
							frame.requestFocusInWindow();
						}
					});
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
	public Buscar() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 811, 540);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("List.dropCellBackground"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JPanel icon = new JPanel();
		icon.setBackground(UIManager.getColor("List.dropCellBackground"));
		contentPane.add(icon);
		icon.setLayout(new BoxLayout(icon, BoxLayout.X_AXIS));

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Buscar.class.getResource("/buscando(3).png")));
		icon.add(lblNewLabel);

		JPanel busqueda = new JPanel();
		busqueda.setBackground(UIManager.getColor("List.dropCellBackground"));
		busqueda.setBorder(new TitledBorder(UIManager.getBorder("InternalFrame.optionDialogBorder"), "Buscar",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		contentPane.add(busqueda);
		GridBagLayout gbl_busqueda = new GridBagLayout();
		gbl_busqueda.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_busqueda.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_busqueda.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_busqueda.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		busqueda.setLayout(gbl_busqueda);

		// Textos de ejemplo
		String placeholderText1 = "Texto a buscar...";
		String placeholderText2 = "Teléfono...";
		String placeholderText3 = "Contacto...";

		JLabel lupa1 = new JLabel("");
		lupa1.setIcon(new ImageIcon(Buscar.class.getResource("/miniLupa.png")));
		GridBagConstraints gbc_lupa1 = new GridBagConstraints();
		gbc_lupa1.insets = new Insets(0, 0, 5, 5);
		gbc_lupa1.anchor = GridBagConstraints.EAST;
		gbc_lupa1.gridx = 1;
		gbc_lupa1.gridy = 0;
		busqueda.add(lupa1, gbc_lupa1);

		txtTextoABuscar = new JTextField(placeholderText1);

		GridBagConstraints gbc_txtTextoABuscar = new GridBagConstraints();
		gbc_txtTextoABuscar.gridwidth = 14;
		gbc_txtTextoABuscar.insets = new Insets(0, 0, 5, 5);
		gbc_txtTextoABuscar.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTextoABuscar.gridx = 2;
		gbc_txtTextoABuscar.gridy = 0;
		busqueda.add(txtTextoABuscar, gbc_txtTextoABuscar);
		txtTextoABuscar.setColumns(10);

		addPlaceholderBehavior(txtTextoABuscar, placeholderText1);

		JLabel lupa2 = new JLabel("");
		lupa2.setIcon(new ImageIcon(Buscar.class.getResource("/miniLupa.png")));
		GridBagConstraints gbc_lupa2 = new GridBagConstraints();
		gbc_lupa2.insets = new Insets(0, 0, 5, 5);
		gbc_lupa2.anchor = GridBagConstraints.EAST;
		gbc_lupa2.gridx = 1;
		gbc_lupa2.gridy = 1;
		busqueda.add(lupa2, gbc_lupa2);

		telefono = new JTextField(placeholderText2);
		GridBagConstraints gbc_telefono = new GridBagConstraints();
		gbc_telefono.insets = new Insets(0, 0, 5, 5);
		gbc_telefono.fill = GridBagConstraints.HORIZONTAL;
		gbc_telefono.gridx = 2;
		gbc_telefono.gridy = 1;
		busqueda.add(telefono, gbc_telefono);
		telefono.setColumns(10);

		addPlaceholderBehavior(telefono, placeholderText2);

		JLabel lupa2_1 = new JLabel("");
		lupa2_1.setIcon(new ImageIcon(Buscar.class.getResource("/miniLupa.png")));
		GridBagConstraints gbc_lupa2_1 = new GridBagConstraints();
		gbc_lupa2_1.insets = new Insets(0, 0, 5, 5);
		gbc_lupa2_1.anchor = GridBagConstraints.EAST;
		gbc_lupa2_1.gridx = 4;
		gbc_lupa2_1.gridy = 1;
		busqueda.add(lupa2_1, gbc_lupa2_1);

		Contacto = new JTextField(placeholderText3);
		GridBagConstraints gbc_contacto = new GridBagConstraints();
		gbc_contacto.gridwidth = 6;
		gbc_contacto.insets = new Insets(0, 0, 5, 5);
		gbc_contacto.fill = GridBagConstraints.HORIZONTAL;
		gbc_contacto.gridx = 5;
		gbc_contacto.gridy = 1;
		busqueda.add(Contacto, gbc_contacto);
		Contacto.setColumns(10);

		addPlaceholderBehavior(Contacto, placeholderText3);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setFont(new Font("Liberation Mono", Font.BOLD, 13));
		GridBagConstraints gbc_btnBuscar = new GridBagConstraints();
		gbc_btnBuscar.gridwidth = 2;
		gbc_btnBuscar.insets = new Insets(0, 0, 5, 5);
		gbc_btnBuscar.gridx = 14;
		gbc_btnBuscar.gridy = 1;
		busqueda.add(btnBuscar, gbc_btnBuscar);
		btnBuscar.addActionListener(e -> {
		    String texto = txtTextoABuscar.getText().equals("Texto a buscar...") ? "" : txtTextoABuscar.getText();
		    String tel = telefono.getText().equals("Teléfono...") ? "" : telefono.getText();
		    String contacto = Contacto.getText().equals("Contacto...") ? "" : Contacto.getText();

		    // Llama al método de búsqueda
		    realizarBusqueda(texto, tel, contacto);
		});


		JPanel mensajes = new JPanel();
		mensajes.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		mensajes.setBackground(UIManager.getColor("List.dropCellBackground"));
		contentPane.add(mensajes);
		mensajes.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(210, 233, 255));
		mensajes.add(scrollPane);
		
	}
	
	private void realizarBusqueda(String texto, String telefono, String nombreContacto) {
	    // Llamar al controlador para realizar la búsqueda
	    List<Mensaje> resultados = controlador.buscarMensajes(texto, telefono, nombreContacto);

	    // Mostrar los resultados en el JList
	    mostrarResultados(resultados);
	}
	
	private void mostrarResultados(List<Mensaje> mensajes) {
	    // Crear el modelo para el JList
	    DefaultListModel<Mensaje> modelo = new DefaultListModel<>();
	    for (Mensaje mensaje : mensajes) {
	        modelo.addElement(mensaje); // Agrega cada mensaje al modelo
	    }

	    // Configurar el JList con el modelo
	    JList<Mensaje> listaMensajes = new JList<>(modelo);

	    // Personalizar el renderizado del JList
	    listaMensajes.setCellRenderer(new BusquedaCellRenderer());

	    // Mostrar los mensajes en el JScrollPane
	    JScrollPane scrollPane = new JScrollPane(listaMensajes);
	    JPanel mensajesPanel = (JPanel) contentPane.getComponent(contentPane.getComponentCount() - 1); // Obtén el panel de mensajes
	    mensajesPanel.removeAll(); // Limpia los resultados anteriores
	    mensajesPanel.add(scrollPane, BorderLayout.CENTER);
	    mensajesPanel.revalidate(); // Revalida para actualizar la vista
	    mensajesPanel.repaint();
	}


	//REVISAR ESTO
	private void addPlaceholderBehavior(JTextField textField, String placeholderText) {
		textField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textField.getText().equals(placeholderText)) {
					textField.setText("");
					textField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField.getText().isEmpty()) {
					textField.setText(placeholderText);
					textField.setForeground(Color.GRAY);
				}
			}
		});
	}

}
