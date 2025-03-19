package vista;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * Clase utilitaria para mostrar mensajes de advertencia o confirmación al usuario.
 * Proporciona métodos estáticos para simplificar la creación de cuadros de diálogo.
 */
public class MensajeAdvertencia {

    /**
     * Muestra un mensaje de error en un cuadro de diálogo.
     * 
     * @param mensaje          El texto del mensaje de error a mostrar.
     * @param parentComponent  El componente padre para centrar el cuadro de diálogo.
     */
    public static void mostrarError(String mensaje, Component parentComponent) {
        JOptionPane.showMessageDialog(parentComponent, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un mensaje de confirmación en un cuadro de diálogo.
     * 
     * @param mensaje          El texto del mensaje de confirmación a mostrar.
     * @param parentComponent  El componente padre para centrar el cuadro de diálogo.
     */
    public static void mostrarConfirmacion(String mensaje, Component parentComponent) {
        JOptionPane.showMessageDialog(parentComponent, mensaje, "Confirmación", JOptionPane.INFORMATION_MESSAGE);
    }
}
