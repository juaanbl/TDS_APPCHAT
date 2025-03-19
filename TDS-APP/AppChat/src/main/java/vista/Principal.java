package vista;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import controlador.Controlador;
import dominio.Contacto;
import dominio.ContactoIndividual;
import dominio.Grupo;
import dominio.Mensaje;
import excepciones.ExcepcionDAO;
import tds.BubbleText;

/**
 * Clase Principal representa la ventana principal de la aplicación.
 * Permite gestionar contactos, grupos y funcionalidades premium, además de enviar mensajes.
 */
public class Principal extends JFrame {

    private static final long serialVersionUID = 1L;

    // Componentes principales de la ventana
    private JPanel contentPane;
    private JLabel lblUsuario; // Contiene la imagen de perfil
    private JList<Contacto> listaContactos; 
    private Controlador controlador = Controlador.getInstance(); 
    private double precioPremium = -1; 
    private Principal principal = this; 
    private JTextField textField; 
    private JPanel chat;
    private JPopupMenu popupMenu;

    /**
     * Método principal para ejecutar la ventana.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Principal frame = new Principal();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Constructor de la clase.
     * Configura la ventana principal y sus componentes.
     */
    public Principal() {
    	
        // Configuración de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 934, 512);

        // Configuración del panel principal
        contentPane = new JPanel();
        contentPane.setBackground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
        contentPane.setPreferredSize(new Dimension(20, 20));
        contentPane.setSize(new Dimension(800, 800));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        
        // Configuración del panel superior con botones y opciones de usuario
        configurarPanelSuperior();

        // Configuración del área central (contactos y chat)
        configurarPanelCentral();
        crearPopupMenu(); // Crear el popup para añadir contactos sin agregar
        configurarPopupParaListaContactos(); // Configurar el mouse listener para la lista de contactos
    }

    /**
     * Configura el panel superior con botones y opciones de usuario.
     */
    private void configurarPanelSuperior() {
        JPanel arriba = new JPanel();
        arriba.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        contentPane.add(arriba, BorderLayout.NORTH);
        arriba.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        // Botón para volver a la ventana de login
        JButton atras = new JButton("");
        atras.setIcon(new ImageIcon(Principal.class.getResource("/volver(1).png")));
        arriba.add(atras);
        atras.addActionListener(ev -> {
        	dispose();
        	Login log = new Login();
        	log.setVisible(true);
        });

        // Botón para gestionar grupos
        JButton btnGestionGrupos = new JButton("Gestionar Grupos");
        btnGestionGrupos.setIcon(new ImageIcon(Principal.class.getResource("/citizen_8382930(1).png")));
        arriba.add(btnGestionGrupos);

        // Menú contextual para gestionar grupos
        JPopupMenu menuGrupos = new JPopupMenu();
        JMenuItem crearGrupo = new JMenuItem("Crear grupo");
        crearGrupo.addActionListener(ev -> {
            CreacionGrupos creador = new CreacionGrupos(principal);
            creador.setVisible(true);
        });
        menuGrupos.add(crearGrupo);

        JMenuItem modificarGrupo = new JMenuItem("Modificar grupo");
        modificarGrupo.addActionListener(ev -> {
            String grupoSeleccionado = SeleccionarGrupo.mostrarDialogo(null, controlador.obtenerNombresGruposUsuario());
            if (grupoSeleccionado != null) {
                ModificarGrupo modificador = new ModificarGrupo(principal, grupoSeleccionado);
                modificador.setVisible(true);
                actualizarListaContactos();
            }
        });
        menuGrupos.add(modificarGrupo);

        btnGestionGrupos.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    menuGrupos.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    menuGrupos.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

     // Botón para exportar contactos o grupos en formato PDF
        JButton exportar = new JButton("");
        exportar.setIcon(new ImageIcon(Principal.class.getResource("/document_9890148(1).png")));
        arriba.add(exportar);

        exportar.addActionListener(ev -> {
            if (controlador.isUsuarioPremium()) {
                // Combinar nombres de contactos y grupos para el diálogo
                List<String> opciones = new ArrayList<>();
                opciones.addAll(controlador.obtenerNombresContactos());
                opciones.addAll(controlador.obtenerNombresGruposUsuario());

                // Mostrar diálogo de selección
                String[] seleccionadoYruta = SeleccionarContacto.mostrarDialogo(principal, opciones);
                if (seleccionadoYruta != null) {
                    if (controlador.exportarPDF(seleccionadoYruta[1], seleccionadoYruta[0])) {
                        MensajeAdvertencia.mostrarConfirmacion("Se ha exportado correctamente el documento", principal);
                    } else {
                        MensajeAdvertencia.mostrarError("No se ha podido exportar el documento", principal);
                    }
                }
                
            } else {
                MensajeAdvertencia.mostrarError("Esta funcionalidad solo está disponible para usuarios premium", principal);
            }
        });


        // Botón para buscar contactos
        JButton buscar = new JButton("");
        buscar.setIcon(new ImageIcon(Principal.class.getResource("/buscar(1).png")));
        arriba.add(buscar);
        buscar.addActionListener(ev -> {
            Buscar buscador = new Buscar();
            buscador.setVisible(true);
        });

        // Botón para agregar contactos
        JButton btnContactos = new JButton("Agregar contacto");
        btnContactos.setIcon(new ImageIcon(Principal.class.getResource("/libreta-de-contactos.png")));
        arriba.add(btnContactos);
        btnContactos.addActionListener(ev -> {
            AñadirContacto contactos = new AñadirContacto(this);
            contactos.setVisible(true);
            actualizarListaContactos();
        });
        


        // Botón para gestionar la versión premium
        JButton btnPremium = new JButton("PREMIUM");
        btnPremium.setIcon(new ImageIcon(Principal.class.getResource("/dolar(2)(1).png")));
        arriba.add(btnPremium);
        configurarBotonPremium(btnPremium);

      
        //etiqueta del usuario
        lblUsuario = new JLabel(controlador.getNombreUsuario());
        lblUsuario.setIconTextGap(10);
        comprobarPathImagen();

        lblUsuario.setHorizontalTextPosition(SwingConstants.RIGHT);
        lblUsuario.setVerticalTextPosition(SwingConstants.CENTER);
        
        lblUsuario.setOpaque(true);
        arriba.add(lblUsuario);
        lblUsuario.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cambiarImagenPerfil();
            }
        });
    }

    /**
     * Configura el botón de la versión premium con acciones específicas.
     */
    private void configurarBotonPremium(JButton btnPremium) {
        JMenuItem cancelarPremiumItem = new JMenuItem("Cancelar Premium"); // Crear cancelarPremiumItem

        btnPremium.addActionListener(ev -> {
            if (controlador.isUsuarioPremium()) {
                MensajeAdvertencia.mostrarConfirmacion("El usuario ya ha obtenido la versión premium.", contentPane);
            } else {
                precioPremium = controlador.setPremium();
                String precioAtexto = String.format("%.2f", precioPremium);
                if (precioPremium > 0) {
                    MensajeAdvertencia.mostrarConfirmacion(
                            "Enhorabuena, has obtenido la versión premium con un precio de: " + precioAtexto,
                            contentPane);
                    btnPremium.setIcon(new ImageIcon(Principal.class.getResource("/cheque(1).png")));
                    cancelarPremiumItem.setEnabled(true); // Habilitar opción al hacerse premium
                } else {
                    MensajeAdvertencia.mostrarError("No se ha podido obtener la versión premium", contentPane);
                }
            }
        });

        // Crear el menú contextual
        JPopupMenu popupMenu = new JPopupMenu();
        cancelarPremiumItem.addActionListener(ev -> {
            controlador.cancelarPremium();
            MensajeAdvertencia.mostrarConfirmacion("Se ha cancelado la suscripción premium", contentPane);
            btnPremium.setIcon(new ImageIcon(Principal.class.getResource("/dolar(2)(1).png")));
            cancelarPremiumItem.setEnabled(false); // Deshabilitar opción al cancelar premium
        });

        // Habilitar o deshabilitar la opción según el estado del usuario
        cancelarPremiumItem.setEnabled(controlador.isUsuarioPremium());
        popupMenu.add(cancelarPremiumItem);

        // Añadir el menú contextual al botón
        btnPremium.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(btnPremium, e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(btnPremium, e.getX(), e.getY());
                }
            }
        });

        if (controlador.isUsuarioPremium()) {
            btnPremium.setIcon(new ImageIcon(Principal.class.getResource("/cheque(1).png")));
        }
    }

    /**
     * Configura el panel central que incluye la lista de contactos y el área de chat.
     */
    private void configurarPanelCentral() {
        JPanel centro = new JPanel();
        contentPane.add(centro, BorderLayout.CENTER);
        centro.setLayout(new BorderLayout(0, 0));

        // Panel izquierdo con la lista de contactos
        JPanel izq = new JPanel();
        Dimension panelSize = new Dimension(250, 0);
        izq.setPreferredSize(panelSize);
        izq.setBorder(new TitledBorder(new LineBorder(new Color(99, 130, 191), 2), "Mensajes", TitledBorder.LEADING,TitledBorder.TOP, null, new Color(51, 51, 51)));
        izq.setBackground(UIManager.getColor("List.dropCellBackground"));
        centro.add(izq, BorderLayout.WEST);
        izq.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        izq.add(scrollPane, BorderLayout.CENTER);

        listaContactos = new JList<>();
        listaContactos.setCellRenderer(new ContactoCellRenderer());
        scrollPane.setViewportView(listaContactos);

        actualizarListaContactos();

        // Panel derecho con el área de chat
        JPanel der = new JPanel();
        der.setPreferredSize(panelSize);
        der.setBorder(new TitledBorder(new LineBorder(new Color(99, 130, 191), 2), "Chat", TitledBorder.LEADING,
                TitledBorder.TOP, null, new Color(51, 51, 51)));
        der.setBackground(UIManager.getColor("Tree.dropCellBackground"));
        centro.add(der, BorderLayout.CENTER);
        der.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane_1 = new JScrollPane();
        der.add(scrollPane_1, BorderLayout.CENTER);

        chat = new JPanel();
        scrollPane_1.setViewportView(chat);
        chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
        chat.setBackground(UIManager.getColor("Tree.dropCellBackground"));

        configurarPanelEnviarMensaje(chat, der);
        
        listaContactos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Contacto contactoSeleccionado = listaContactos.getSelectedValue();
                if (contactoSeleccionado != null) {
                    cargarChat(chat, contactoSeleccionado); // Carga los mensajes del contacto seleccionado
                }
            }
        });
    }
    
    /**
     * Encargada de la creación de los bubbletext de los chats
     * 
     * @param chat Panel de chat.
     * @param contactoSeleccionado Al que se le va a enviar el mensaje
     */
    private void cargarChat(JPanel chat, Contacto contactoSeleccionado) {
    	 SwingUtilities.invokeLater(() -> {
    	        chat.removeAll();
    	        List<Mensaje> mensajes = controlador.getMensajes(contactoSeleccionado);
        for (Mensaje mensaje : mensajes) {            
            String emisor;
            Color colorBurbuja;
            int direccion;

            if (mensaje.getEmisor().equals(controlador.getUsuarioActual())) {
                colorBurbuja = Color.GREEN; // Mensajes enviados
                emisor = "Tú";
                direccion = BubbleText.SENT;
            } else {
                colorBurbuja = Color.LIGHT_GRAY; // Mensajes recibidos
                if (contactoSeleccionado instanceof ContactoIndividual && 
                        ((ContactoIndividual) contactoSeleccionado).getNombre().isEmpty()) {
                        emisor = "Desconocido"; // Muestra "Desconocido" si el nombre está vacío
                    } else {
                        emisor = contactoSeleccionado.getNombre();
                    }
                    direccion = BubbleText.RECEIVED;
            }
            BubbleText burbuja;
            if (mensaje.getTexto().isEmpty()) {
				burbuja = new BubbleText(chat, mensaje.getEmoticono(), colorBurbuja, emisor, direccion, 12);
			}else {
				burbuja = new BubbleText(chat, mensaje.getTexto(), colorBurbuja,emisor, direccion);
			}
            
            chat.add(burbuja);
        }
        
        //Para que el ScrollBar siempre vaya hasta abajo
        JScrollBar verticalBar = ((JScrollPane) chat.getParent().getParent()).getVerticalScrollBar();
        verticalBar.setValue(verticalBar.getMaximum());
        actualizarChat(chat, contactoSeleccionado);
        });
   }
    
    /**
     * Llama al controlador para enviar un mensaje
     * 
     * @param chat Panel de chat.
     * @param texto del mensaje
     * @param contacto Al que se le va a enviar el mensaje
     */
    
    private void enviarMensaje(JPanel chat, String texto, Contacto contacto) {
        if (contacto == null) {
            MensajeAdvertencia.mostrarError("Selecciona un contacto o grupo para enviar un mensaje.", this);
            return;
        }

        if (contacto instanceof Grupo) {
            controlador.enviarMensaje((Grupo) contacto, texto); 
        } else {
            controlador.enviarMensaje(contacto, texto);
        }

        actualizarListaContactos();
        listaContactos.setSelectedValue(contacto, true);
        cargarChat(chat, contacto);

        textField.setText(""); // Limpia el campo de texto
    }

    /**
     * Llama al controlador para enviar un mensaje
     * 
     * @param chat Panel de chat.
     * @param emoji 
     * @param contacto Al que se le va a enviar el mensaje
     */
    
    private void enviarEmoticono(JPanel chat, int emoji, Contacto contacto) {
        if (contacto == null) {
            MensajeAdvertencia.mostrarError("Selecciona un contacto o grupo para enviar un mensaje.", this);
            return;
        }

        if (contacto instanceof Grupo) {
            controlador.enviarMensaje((Grupo) contacto, emoji);
        } else {
            controlador.enviarMensaje(contacto, emoji);
        }

        actualizarListaContactos();
        listaContactos.setSelectedValue(contacto, true);
        cargarChat(chat, contacto);
    }


    /**
     * Configura el panel para enviar mensajes en el área de chat.
     * 
     * @param chat Panel de chat.
     * @param der  Panel derecho que contiene el chat.
     */
    private void configurarPanelEnviarMensaje(JPanel chat, JPanel der) {
        JPanel enviarMensaje = new JPanel(new BorderLayout());
        der.add(enviarMensaje, BorderLayout.SOUTH);

        JPopupMenu menuEmoticonos = new JPopupMenu();
        for (int i = 0; i < BubbleText.MAXICONO; i++) {
            JLabel emoticonoLabel = new JLabel(BubbleText.getEmoji(i));
            emoticonoLabel.setName(Integer.toString(i));
            menuEmoticonos.add(emoticonoLabel);

            emoticonoLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                   enviarEmoticono(chat, Integer.valueOf(emoticonoLabel.getName()), listaContactos.getSelectedValue());
                }
            });
        }

        JButton btnEmoticono = new JButton("");
        btnEmoticono.setIcon(new ImageIcon(Principal.class.getResource("/contento(1).png")));
        enviarMensaje.add(btnEmoticono, BorderLayout.WEST);

        btnEmoticono.addActionListener(e -> menuEmoticonos.show(btnEmoticono, btnEmoticono.getWidth() / 2,
                btnEmoticono.getHeight() / 2));

        textField = new JTextField();
        enviarMensaje.add(textField, BorderLayout.CENTER);

        JButton btnEnviar = new JButton("");
        btnEnviar.setIcon(new ImageIcon(Principal.class.getResource("/enviar-mensaje(1).png")));
        enviarMensaje.add(btnEnviar, BorderLayout.EAST);

        btnEnviar.addActionListener(ev -> {
            String mensajeTexto = textField.getText();
            if (!mensajeTexto.isEmpty()) {
               enviarMensaje(chat, mensajeTexto, listaContactos.getSelectedValue());
            }
        });

    }
    

    /**
     * Redimensiona y convierte una imagen en circular.
     * 
     * @param img imagen
     * @return Icono de imagen circular.
     */
    private Image imagenCircular(Image img) {
       BufferedImage imgCirculo=new BufferedImage(50,50,BufferedImage.TYPE_4BYTE_ABGR);
       Graphics2D graphics=imgCirculo.createGraphics();
       Ellipse2D.Double forma = new Ellipse2D.Double(0,0,50,50);
       graphics.setClip(forma);
       graphics.drawImage(img,0,0,50,50,null);
       graphics.dispose();
       return imgCirculo;
    }

    /**
     * Cambia la imagen de perfil del usuario.
     */
    private void cambiarImagenPerfil() {
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

                // Actualizar la ruta de la imagen en el controlador
                String pathImagen = archivoSeleccionado.getAbsolutePath();
                controlador.setImagenPerfilUsuario(pathImagen);

                // Establecer la nueva imagen en el JLabel
                lblUsuario.setIcon(new ImageIcon(imagenCircular));
                lblUsuario.repaint();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /**
     * Comprueba el path de la imagen y en caso de error coloca una imagen por defecto
     */
    private void comprobarPathImagen() {
    	String pathImagen = controlador.getImagenUsuario();
    	Image imagen;

    	if (pathImagen != null) {
    	    File archivoImagen = new File(pathImagen);

    	    if (archivoImagen.exists()) {
    	        // Cargar imagen desde el sistema de archivos
    	        imagen = new ImageIcon(archivoImagen.getAbsolutePath()).getImage();
    	    } else {
    	        // Intentar cargar desde el classpath
    	        URL resource = getClass().getResource(pathImagen);
    	        if (resource != null) {
    	            imagen = new ImageIcon(resource).getImage();
    	        } else {
    	            System.err.println("Ruta de imagen inválida: " + pathImagen);
    	            // Usar imagen predeterminada si no se encuentra
    	            resource = getClass().getResource("/usuario(1).png");
    	            imagen = new ImageIcon(resource).getImage();
    	        }
    	    }
    	} else {
    	    // Usar imagen predeterminada si no hay ruta proporcionada
    	    URL resource = getClass().getResource("/usuario(1).png");
    	    imagen = new ImageIcon(resource).getImage();
    	}

    	// Asignar la imagen al JLabel
    	lblUsuario.setIcon(new ImageIcon(imagenCircular(imagen)));

    }


    /**
     * Actualiza la lista de contactos mostrada en la ventana.
     */
    public void actualizarListaContactos() {
        List<Contacto> contactos = controlador.obtenerContactosYgrupos();

        DefaultListModel<Contacto> modelo = new DefaultListModel<>();
        for (Contacto contacto : contactos) {
            modelo.addElement(contacto);
        }
        listaContactos.setModel(modelo);
    }
    
    /**
     * Actualiza el chat donde se encuentran los mensajes con los contactos
     */
    public void actualizarChat(JPanel chat, Contacto contactoSeleccionado) {
        if (listaContactos.getSelectedValue() != null && listaContactos.getSelectedValue().equals(contactoSeleccionado)) {
            chat.revalidate();
            chat.repaint(); 
        }
    }
    
    /**
     * Creación del popupMenu para la asignación de nombres a los contactos desconocidos
     */
    private void crearPopupMenu() {
        popupMenu = new JPopupMenu();

        JMenuItem asignarNombre = new JMenuItem("Asignar nombre");
        asignarNombre.addActionListener(e -> {
            ContactoIndividual contactoSeleccionado = (ContactoIndividual) listaContactos.getSelectedValue();
            if (contactoSeleccionado != null && contactoSeleccionado.getNombre().isEmpty()) {
                String nuevoNombre = JOptionPane.showInputDialog(this,"Introduce un nombre para el contacto:","Asignar Nombre",JOptionPane.PLAIN_MESSAGE);

                if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                    contactoSeleccionado.setNombre(nuevoNombre);
                    try {
						controlador.actualizarContacto(contactoSeleccionado);
					} catch (ExcepcionDAO e1) {
						e1.printStackTrace();
					} // Actualiza en la base de datos
                    
                    JOptionPane.showMessageDialog(this, "Nombre asignado con éxito.","Confirmación",JOptionPane.INFORMATION_MESSAGE);
                    actualizarListaContactos(); 
                }
            }
        });

        popupMenu.add(asignarNombre);
    }

    /**
     * acciones con el ratón asignadas al popup 
     */
    private void configurarPopupParaListaContactos() {
        listaContactos.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mostrarPopupSiEsNecesario(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mostrarPopupSiEsNecesario(e);
            }

            private void mostrarPopupSiEsNecesario(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int index = listaContactos.locationToIndex(e.getPoint());
                    listaContactos.setSelectedIndex(index); // Seleccionar el contacto en la posición del clic

                    Contacto contactoSeleccionado = listaContactos.getSelectedValue();
                    if (contactoSeleccionado instanceof ContactoIndividual &&
                        ((ContactoIndividual) contactoSeleccionado).getNombre().isEmpty()) {
                        popupMenu.show(listaContactos, e.getX(), e.getY());
                    }
                }
            }
        });
    }


}