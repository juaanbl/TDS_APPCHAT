package dominio;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un descuento compuesto, combinando múltiples descuentos
 * utilizando el patrón Composite y Estrategia.
 */
public class DescuentoCompuesto implements Descuento {

    private List<Descuento> descuentos;

    public DescuentoCompuesto() {
        this.descuentos = new ArrayList<>();
    }

    /**
     * Añade un nuevo descuento al compuesto.
     *
     * @param descuento El descuento a añadir.
     */
    public void addDescuento(Descuento descuento) {
        descuentos.add(descuento);
    }

    /**
     * Elimina un descuento del compuesto.
     *
     * @param descuento El descuento a eliminar.
     */
    public void removeDescuento(Descuento descuento) {
        descuentos.remove(descuento);
    }

    /**
     * Aplica todos los descuentos en secuencia al precio inicial.
     *
     * @param precioInicial Precio sin descuento.
     * @return Precio tras aplicar todos los descuentos.
     */
    @Override
    public double getDescuento(Double precioInicial) {
        double precioFinal = precioInicial;
        for (Descuento descuento : descuentos) {
            precioFinal = descuento.getDescuento(precioFinal);
        }
        return precioFinal;
    }

    /**
     * Devuelve la lista de descuentos en el compuesto.
     *
     * @return Lista de descuentos.
     */
    public List<Descuento> getDescuentos() {
        return descuentos;
    }
}

