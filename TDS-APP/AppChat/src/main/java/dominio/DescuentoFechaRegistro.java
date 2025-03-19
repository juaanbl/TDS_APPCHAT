package dominio;

public class DescuentoFechaRegistro implements Descuento{
	
	/** Aplica un 20% de descuento al precio pasado como parámetro.
	 * @param precioInicial
	 * @return precio final aplicando el descuento.
	 */	
	
	@Override
	public double getDescuento(Double precioInicial) {
		return 0.2 * precioInicial;
	}

}
