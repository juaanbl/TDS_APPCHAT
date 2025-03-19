package excepciones;

@SuppressWarnings("serial")
public class ExcepcionDAO extends  Exception {
	
	public ExcepcionDAO(String mensaje) {
		super(mensaje);
	}

}
