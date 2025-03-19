package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;

public class SeleccionarGrupo extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JButton cancelButton;
    private JButton okButton;
    private JComboBox<String> comboBox;
    private String grupoSeleccionado;

    /**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SeleccionarGrupo dialog = new SeleccionarGrupo(null,null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}

    /**
     * Crea el diálogo de selección de grupo.
     * 
     * @param owner        La ventana principal (puede ser null).
     * @param nombresGrupos Lista con los nombres de los grupos a mostrar.
     */
    public SeleccionarGrupo(Frame owner, List<String> nombresGrupos) {
        super(owner, true); 
        setBounds(100, 100, 413, 205);
        getContentPane().setBackground(UIManager.getColor("List.dropCellBackground"));
        getContentPane().setLayout(new BorderLayout());
        
        // Establecer un borde para el diálogo
        getRootPane().setBorder(new LineBorder(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"), 4));

        // Panel superior con el título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(UIManager.getColor("List.dropCellBackground"));
        getContentPane().add(panelTitulo, BorderLayout.NORTH);
        panelTitulo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel lblSeleccioneElGrupo = new JLabel("Seleccione el grupo a modificar");
        lblSeleccioneElGrupo.setFont(new Font("Liberation Mono", Font.BOLD, 14));
        panelTitulo.add(lblSeleccioneElGrupo);

        // Panel central con el ComboBox
        JPanel panelComboBox = new JPanel();
        panelComboBox.setBackground(UIManager.getColor("List.dropCellBackground"));
        contentPanel.add(panelComboBox);
        comboBox = new JComboBox<>(nombresGrupos.toArray(new String[0]));
        comboBox.setPreferredSize(new Dimension(250, 26));
        panelComboBox.add(comboBox);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

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
                grupoSeleccionado = (String) comboBox.getSelectedItem(); // Guarda el grupo seleccionado
                dispose(); // Cierra el diálogo
            }
        });
        buttonPane.add(okButton);

        // Espaciadores horizontales
        Component horizontalGlue1 = Box.createHorizontalGlue();
        horizontalGlue1.setPreferredSize(new Dimension(80, 0));
        buttonPane.add(horizontalGlue1);

        JLabel labelIcono = new JLabel("");
        labelIcono.setIcon(new ImageIcon(SeleccionarGrupo.class.getResource("/discusionPequeño.png")));
        buttonPane.add(labelIcono);

        Component horizontalGlue2 = Box.createHorizontalGlue();
        horizontalGlue2.setPreferredSize(new Dimension(80, 0));
        buttonPane.add(horizontalGlue2);

        // Botón Cancelar
        cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Liberation Mono", Font.BOLD, 12));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                grupoSeleccionado = null; // No guarda ningún grupo seleccionado
                dispose(); // Cierra el diálogo
            }
        });
        buttonPane.add(cancelButton);
    }

    /**
     * Obtiene el nombre del grupo seleccionado.
     * 
     * @return El nombre del grupo seleccionado o null si se canceló.
     */
    public String getGrupoSeleccionado() {
        return grupoSeleccionado;
    }

    /**
     * Muestra el diálogo y devuelve el grupo seleccionado.
     * 
     * @param owner        La ventana principal desde la que se llama.
     * @param nombresGrupos Lista de nombres de los grupos para seleccionar.
     * @return El grupo seleccionado o null si no se seleccionó ninguno.
     */
    public static String mostrarDialogo(java.awt.Frame owner, List<String> nombresGrupos) {
        SeleccionarGrupo dialog = new SeleccionarGrupo(owner, nombresGrupos);
        dialog.setVisible(true);
        return dialog.getGrupoSeleccionado();
    }
}
