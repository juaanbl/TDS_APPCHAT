package vista;

import javax.swing.*;
import javax.swing.border.Border;

import dominio.Contacto;
import dominio.ContactoIndividual;
import dominio.Grupo;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

@SuppressWarnings("serial")
public class ContactoCellRenderer extends JPanel implements ListCellRenderer<Contacto> {
    private static final Border SELECCIONADO = BorderFactory.createLineBorder(Color.BLUE, 2);
    private static final Border NO_SELECCIONADO = BorderFactory.createEmptyBorder(2, 2, 2, 2);

    private JLabel lblImagen;
    private JLabel lblNombre;
    private JLabel lblTelefono;
    private JLabel lblSaludo;

    public ContactoCellRenderer() {
        setLayout(new BorderLayout(10, 10)); // Espaciado entre imagen y texto

        lblImagen = new JLabel();
        lblNombre = new JLabel();
        lblTelefono = new JLabel();
        lblSaludo = new JLabel();

        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        lblTelefono.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSaludo.setFont(new Font("Arial", Font.ITALIC, 12));

        JPanel panelTexto = new JPanel(new GridLayout(3, 1)); // Para organizar los textos verticalmente
        panelTexto.setOpaque(false); // Hace transparente el panel para usar el fondo de la celda
        panelTexto.add(lblNombre);
        panelTexto.add(lblTelefono);
        panelTexto.add(lblSaludo);

        add(lblImagen, BorderLayout.WEST);  // Imagen a la izquierda
        add(panelTexto, BorderLayout.CENTER);  // Texto a la derecha
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Contacto> listacontactos, Contacto contacto, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        if (contacto instanceof ContactoIndividual) {
            ContactoIndividual ci = (ContactoIndividual) contacto;

            if (ci.getNombre().isEmpty()) {
                lblNombre.setText("Desconocido");
                lblTelefono.setText("Tel: " + ci.getTelefono());
                lblSaludo.setText(ci.getUsuario().getMensajeSaludo().orElse(""));
                configurarImagen(ci.getUsuario().getPathImagen()); // Imagen predeterminada para contactos desconocidos

            } else {
                // Configuración para contactos agregados
                lblNombre.setText(ci.getNombre());
                lblTelefono.setText("Tel: " + ci.getUsuario().getMovil());
                lblSaludo.setText(ci.getUsuario().getMensajeSaludo().orElse(""));
                configurarImagen(ci.getUsuario().getPathImagen());
            }
        } else if (contacto instanceof Grupo) {
            Grupo grupo = (Grupo) contacto;

            lblNombre.setText(grupo.getNombre());
            configurarImagen(grupo.getImagen());

            // Construir cadena de nombres de miembros
            StringBuilder cadenaMiembros = new StringBuilder();
            for (ContactoIndividual miembro : grupo.getMiembros()) {
                cadenaMiembros.append(miembro.getNombre()).append(", ");
            }
            if (cadenaMiembros.length() > 2) {
                cadenaMiembros.setLength(cadenaMiembros.length() - 2); // Quitar la última coma y espacio
            }

            lblTelefono.setText("Miembros: " + cadenaMiembros);
            lblSaludo.setText(""); // No hay saludo para grupos
        }

        // Configuración de selección
        if (isSelected) {
            setBorder(SELECCIONADO);
            setBackground(listacontactos.getSelectionBackground());
            setForeground(listacontactos.getSelectionForeground());
        } else {
            setBorder(NO_SELECCIONADO);
            setBackground(Color.WHITE); // Fondo blanco
            setForeground(listacontactos.getForeground());
        }

        // Asegurarse de que todos los componentes internos tengan el mismo fondo
        lblImagen.setOpaque(true);
        lblImagen.setBackground(getBackground());
        lblNombre.setOpaque(true);
        lblNombre.setBackground(getBackground());
        lblTelefono.setOpaque(true);
        lblTelefono.setBackground(getBackground());
        lblSaludo.setOpaque(true);
        lblSaludo.setBackground(getBackground());

        setOpaque(true); // Fondo visible
        return this;
    }

    // Método auxiliar para configurar imágenes utilizando comprobarPathImagen e imagenCircular
    private void configurarImagen(String rutaImagen) {
        Image imagen = comprobarPathImagen(rutaImagen);
        if (imagen != null) {
            lblImagen.setIcon(new ImageIcon(imagenCircular(imagen)));
        } else {
            lblImagen.setIcon(null); // Imagen no disponible
        }
    }

    // Método para comprobar y cargar la imagen
    private Image comprobarPathImagen(String rutaImagen) {
        Image imagen = null;

        if (rutaImagen != null) {
            File archivoImagen = new File(rutaImagen);
            if (archivoImagen.exists()) {
                imagen = new ImageIcon(archivoImagen.getAbsolutePath()).getImage();
            } else {
                URL resource = getClass().getResource(rutaImagen);
                if (resource != null) {
                    imagen = new ImageIcon(resource).getImage();
                } else {
                    System.err.println("Ruta de imagen inválida: " + rutaImagen);
                    resource = getClass().getResource("/usuario(1).png");
                    imagen = new ImageIcon(resource).getImage();
                }
            }
        } else {
            URL resource = getClass().getResource("/usuario(1).png");
            imagen = new ImageIcon(resource).getImage();
        }

        return imagen;
    }

    // Método para redimensionar y crear imágenes circulares
    private Image imagenCircular(Image img) {
        BufferedImage imgCirculo = new BufferedImage(50, 50, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = imgCirculo.createGraphics();
        Ellipse2D.Double forma = new Ellipse2D.Double(0, 0, 50, 50);
        graphics.setClip(forma);
        graphics.drawImage(img, 0, 0, 50, 50, null);
        graphics.dispose();
        return imgCirculo;
    }
}
