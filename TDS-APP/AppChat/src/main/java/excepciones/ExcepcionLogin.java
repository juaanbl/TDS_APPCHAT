package excepciones;

/**
 * Excepción para manejar errores de logueo.
 */

@SuppressWarnings("serial")
public class ExcepcionLogin extends RuntimeException {	// Extiende de una excepcion no verificada para poder emplearla dentro del .map que
														// verifica la contraseña.
	
		public ExcepcionLogin(String mensaje) {
			super(mensaje);
		}
}


