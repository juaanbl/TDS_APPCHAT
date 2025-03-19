package persistencia.interfaces;

import java.util.List;
import dominio.Grupo;
import excepciones.ExcepcionRegistroDuplicado;

public interface IAdaptadorGrupoDAO {
	
	public void registrarGrupo(Grupo nuevoGrupo) throws ExcepcionRegistroDuplicado;
	public Grupo recuperarGrupo(int codigo) ;
	public void modificarGrupo(Grupo grupoModificar);
	public List<Grupo> recuperarTodosGrupos() ;

}
