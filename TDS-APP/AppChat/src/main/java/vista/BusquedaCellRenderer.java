package vista;

import java.awt.*;
import javax.swing.*;
import dominio.Mensaje;

public class BusquedaCellRenderer extends JPanel implements ListCellRenderer<Mensaje> {
    private static final long serialVersionUID = 1L;
    private JLabel emisor;
    private JLabel receptor;
    private JTextArea mensajeTexto;

    public BusquedaCellRenderer() {
        // Componentes
        emisor = new JLabel();
        receptor = new JLabel();
        mensajeTexto = new JTextArea();
        mensajeTexto.setWrapStyleWord(true);
        mensajeTexto.setLineWrap(true);
        mensajeTexto.setOpaque(false); // Fondo transparente
        mensajeTexto.setEditable(false);
        mensajeTexto.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // Configuración del layout
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel para emisor y receptor
        JPanel header = new JPanel(new GridLayout(1, 2, 10, 0));
        header.setOpaque(false); // Fondo transparente
        emisor.setFont(new Font("SansSerif", Font.BOLD, 14));
        receptor.setFont(new Font("SansSerif", Font.ITALIC, 12));
        emisor.setForeground(new Color(34, 139, 34)); // Verde oscuro
        receptor.setForeground(new Color(70, 130, 180)); // Azul

        header.add(emisor);
        header.add(receptor);

        // Añadir componentes al panel principal
        add(header, BorderLayout.NORTH);
        add(mensajeTexto, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Mensaje> list, Mensaje mensaje, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        // Configurar emisor y receptor
        emisor.setText("De: " + mensaje.getEmisor().getNombreCompleto());
        receptor.setText("Para: " + mensaje.getReceptor().getNombre());

        // Configurar el texto del mensaje
        mensajeTexto.setText(mensaje.getTexto());

        // Ajustar colores según selección
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }
}
