package excepciones;

/**
 * Excepción para manejar errores de registro.
 */

@SuppressWarnings("serial")
public class ExcepcionRegistro extends Exception {

	public ExcepcionRegistro(String mensaje) {
		super(mensaje);
	}

}


