package excepciones;

@SuppressWarnings("serial")
public class ExcepcionRegistroDuplicado extends Exception {

	public ExcepcionRegistroDuplicado(String mensaje) {
		super(mensaje);
	}
}
