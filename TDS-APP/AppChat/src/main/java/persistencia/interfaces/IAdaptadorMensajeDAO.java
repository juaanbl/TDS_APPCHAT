package persistencia.interfaces;

import java.util.List;
import dominio.Mensaje;
import excepciones.ExcepcionRegistroDuplicado;

public interface IAdaptadorMensajeDAO {
	
	public void registrarMensaje(Mensaje mensaje) throws ExcepcionRegistroDuplicado;
	public Mensaje recuperarMensaje(int codigo);
	public List<Mensaje> recuperarTodosMensajes();
		

}
