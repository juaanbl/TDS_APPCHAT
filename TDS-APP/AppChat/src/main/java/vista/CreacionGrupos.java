package vista;

import java.awt.Component;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import controlador.Controlador;
import dominio.ContactoIndividual;
import excepciones.ExcepcionCrearGrupo;
import java.awt.*;

public class CreacionGrupos extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Controlador controlador = Controlador.getInstance();

    // Componentes principales de la interfaz
    private JPanel center, top, izq, flechas, der, buttom;
    private JScrollPane scrollPaneIzq, scrollPaneDer;
    private JList<ContactoIndividual> listaContactos = new JList<>();
    private JList<ContactoIndividual> listaMiembrosGrupo = new JList<>();
    private JLabel lblNombreDelGrupo, lblFotoDePerfil, icono;
    private JTextField nombreGrupo;
    private JButton btnCrearGrupo, izqDer, derIzq, btnCancelar;
    private Component verticalGlue, horizontalGlue, horizontalGlue_3;
    private String pathImagen;
    private Component rigidArea;

    
    /**
     * Punto de entrada de la aplicación para pruebas independientes.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                CreacionGrupos frame = new CreacionGrupos(null);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Constructor que inicializa la ventana de creación de grupos.
     * @param principal Ventana principal para comunicación entre ventanas.
     */
    public CreacionGrupos(Principal principal) {
        // Configuración básica de la ventana
        setBackground(SystemColor.window);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 716, 499);
        contentPane = new JPanel();
        contentPane.setBackground(UIManager.getColor("List.dropCellBackground"));
        contentPane.setBorder(new TitledBorder(new LineBorder(new Color(25, 25, 112), 2), "Creación de grupos",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));

        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        // Panel superior para introducir el nombre del grupo y foto de perfil
        configurarPanelSuperior();

        // Panel central para mostrar las listas de contactos y miembros
        configurarPanelCentral();

        // Panel inferior para botones de acciones (crear grupo, cancelar)
        configurarPanelInferior(principal);

        // Inicializar lista de contactos disponibles
        actualizarListaContactos();
    }

    /**
     * Configura el panel superior con campos de nombre y foto de perfil.
     */
    private void configurarPanelSuperior() {
        top = new JPanel();
        top.setBackground(UIManager.getColor("List.dropCellBackground"));
        contentPane.add(top, BorderLayout.NORTH);

        lblNombreDelGrupo = new JLabel("Nombre del Grupo: ");
        lblNombreDelGrupo.setFont(new Font("Liberation Mono", Font.BOLD, 13));
        top.add(lblNombreDelGrupo);

        nombreGrupo = new JTextField();
        nombreGrupo.setColumns(10);
        top.add(nombreGrupo);

        horizontalGlue_3 = Box.createHorizontalGlue();
        horizontalGlue_3.setPreferredSize(new Dimension(180, 0));
        top.add(horizontalGlue_3);

        lblFotoDePerfil = new JLabel("Foto de perfil: ");
        lblFotoDePerfil.setFont(new Font("Liberation Mono", Font.BOLD, 13));
        top.add(lblFotoDePerfil);

        icono = new JLabel("");
        icono.setIcon(new ImageIcon(CreacionGrupos.class.getResource("/anadir-foto(1).png")));
        top.add(icono);

        horizontalGlue = Box.createHorizontalGlue();
        horizontalGlue.setPreferredSize(new Dimension(50, 0));
        top.add(horizontalGlue);

        // Cambiar imagen al hacer clic en el icono
        icono.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pathImagen = cambiarImagenPerfil();
            }
        });
    }

    /**
     * Configura el panel central con las listas de contactos y miembros del grupo.
     */
    private void configurarPanelCentral() {
        center = new JPanel();
        center.setBackground(UIManager.getColor("List.dropCellBackground"));
        contentPane.add(center, BorderLayout.CENTER);
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));

        configurarListaContactos();
        configurarPanelFlechas();
        configurarListaMiembrosGrupo();
    }

    /**
     * Configura el panel izquierdo con la lista de contactos disponibles.
     */
    private void configurarListaContactos() {
        izq = new JPanel();
        izq.setPreferredSize(new Dimension(250, 400));
        center.add(izq);
        izq.setLayout(new BorderLayout(0, 0));

        scrollPaneIzq = new JScrollPane();
        scrollPaneIzq.setPreferredSize(new Dimension(250, 400));
        izq.add(scrollPaneIzq, BorderLayout.CENTER);

        listaContactos.setCellRenderer(new ContactoCellRenderer());
        scrollPaneIzq.setViewportView(listaContactos);
    }

    /**
     * Configura el panel central con flechas para mover contactos entre listas.
     */
    /**
     * Configura el panel central con flechas para mover contactos entre listas.
     */
    private void configurarPanelFlechas() {
        flechas = new JPanel();
        flechas.setBackground(UIManager.getColor("List.dropCellBackground"));
        flechas.setLayout(new BoxLayout(flechas, BoxLayout.Y_AXIS));
        center.add(flechas);

        DefaultListModel<ContactoIndividual> miembrosGrupo = new DefaultListModel<>();
        listaMiembrosGrupo.setCellRenderer(new ContactoCellRenderer());

        izqDer = new JButton("---->");
        flechas.add(izqDer);
        izqDer.addActionListener(ev -> {
            ContactoIndividual seleccionado = listaContactos.getSelectedValue();
            if (seleccionado != null) {
                // Comprobar si ya existe en la lista de miembros
                boolean existe = false;
                for (int i = 0; i < miembrosGrupo.size(); i++) {
                    if (miembrosGrupo.get(i).equals(seleccionado)) {
                        existe = true;
                        break;
                    }
                }
                if (!existe) {
                    miembrosGrupo.addElement(seleccionado);
                    listaMiembrosGrupo.setModel(miembrosGrupo);
                } else {
                    JOptionPane.showMessageDialog(this, "El contacto ya está en la lista del grupo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        verticalGlue = Box.createVerticalGlue();
        verticalGlue.setMaximumSize(new Dimension(10, 10));
        flechas.add(verticalGlue);

        derIzq = new JButton("<----");
        flechas.add(derIzq);
        derIzq.addActionListener(ev -> {
            ContactoIndividual seleccionado = listaMiembrosGrupo.getSelectedValue();
            if (seleccionado != null) {
                miembrosGrupo.removeElement(seleccionado);
                listaMiembrosGrupo.setModel(miembrosGrupo);
            }
        });
    }

    /**
     * Configura el panel derecho con la lista de miembros seleccionados para el grupo.
     */
    private void configurarListaMiembrosGrupo() {
        der = new JPanel();
        der.setPreferredSize(new Dimension(250, 400));
        center.add(der);
        der.setLayout(new BorderLayout(0, 0));

        scrollPaneDer = new JScrollPane();
        scrollPaneDer.setPreferredSize(new Dimension(250, 400));
        der.add(scrollPaneDer, BorderLayout.CENTER);

        scrollPaneDer.setViewportView(listaMiembrosGrupo);
    }

    /**
     * Configura el panel inferior con los botones para crear el grupo o cancelar.
     * 
     * @param principal Ventana principal para comunicación entre ventanas.
     */
    private void configurarPanelInferior(Principal principal) {
        buttom = new JPanel();
        buttom.setBackground(UIManager.getColor("List.dropCellBackground"));
        contentPane.add(buttom, BorderLayout.SOUTH);
        buttom.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        btnCrearGrupo = new JButton("Crear grupo");
        btnCrearGrupo.setFont(new Font("Liberation Mono", Font.BOLD, 12));
        btnCrearGrupo.addActionListener(e -> {
            try {
                controlador.crearGrupo(nombreGrupo.getText(), pathImagen,
                        (DefaultListModel<ContactoIndividual>) listaMiembrosGrupo.getModel());
                MensajeAdvertencia.mostrarConfirmacion("El grupo se ha creado correctamente", contentPane);
                dispose();
            } catch (ExcepcionCrearGrupo e1) {
                e1.printStackTrace();
                MensajeAdvertencia.mostrarError(e1.getMessage(), contentPane);
            }
        });
        buttom.add(btnCrearGrupo);

        // Actualizar contactos en la ventana principal al cerrar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (principal != null) {
                    principal.actualizarListaContactos();
                }
            }
        });

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Liberation Mono", Font.BOLD, 12));
        btnCancelar.addActionListener(ev -> dispose());
        
        rigidArea = Box.createRigidArea(new Dimension(20, 20));
        rigidArea.setPreferredSize(new Dimension(300, 20));
        buttom.add(rigidArea);
        buttom.add(btnCancelar);
    }

    /**
     * Actualiza la lista de contactos disponibles para seleccionar.
     */
    private void actualizarListaContactos() {
        List<ContactoIndividual> contactos = controlador.obtenerContactos();
        DefaultListModel<ContactoIndividual> modelo = new DefaultListModel<>();
        for (ContactoIndividual contacto : contactos) {
            modelo.addElement(contacto);
        }
        listaContactos.setModel(modelo);
    }

    /**
     * Redimensiona y convierte una imagen en circular.
     * 
     * @param img Imagen original.
     * @return Icono de imagen circular.
     */
    private Image imagenCircular(Image img) {
        BufferedImage imgCirculo = new BufferedImage(50, 50, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = imgCirculo.createGraphics();
        Ellipse2D.Double forma = new Ellipse2D.Double(0, 0, 50, 50);
        graphics.setClip(forma);
        graphics.drawImage(img, 0, 0, 50, 50, null);
        graphics.dispose();
        return imgCirculo;
    }

    /**
     * Cambia la imagen de perfil del grupo seleccionando una nueva.
     * 
     * @return La ruta de la imagen seleccionada.
     */
    private String cambiarImagenPerfil() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona una nueva imagen de perfil");

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            try {
                // Leer la imagen seleccionada
                BufferedImage imagenOriginal = ImageIO.read(archivoSeleccionado);
                if (imagenOriginal == null) {
                    throw new Exception("El archivo seleccionado no es una imagen válida.");
                }

                // Redimensionar la imagen y convertirla en circular
                Image imagenRedimensionada = imagenOriginal.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                Image imagenCircular = imagenCircular(imagenRedimensionada);

                // Establecer la nueva imagen en el JLabel
                icono.setIcon(new ImageIcon(imagenCircular));
                icono.repaint();

                // Retornar la ruta de la imagen seleccionada
                return archivoSeleccionado.getAbsolutePath();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }

}
