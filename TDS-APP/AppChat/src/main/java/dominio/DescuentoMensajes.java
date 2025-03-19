package dominio;

public class DescuentoMensajes implements Descuento {
	
	/** Aplica un 10% de descuento al precio pasado como parámetro.
	 * @param precioInicial
	 * @return precio final aplicando el descuento.
	 */	
	
	@Override
	public double getDescuento(Double precioInicial) {
		return 0.1 * precioInicial;

	}
	
	

}
