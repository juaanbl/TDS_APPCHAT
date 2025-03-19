package excepciones;

@SuppressWarnings("serial")
public class ExcepcionAgregarContacto extends Exception {
	public ExcepcionAgregarContacto(String mensaje) {
		super(mensaje);
	}

}
