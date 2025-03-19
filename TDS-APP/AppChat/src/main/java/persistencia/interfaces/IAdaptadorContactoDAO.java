package persistencia.interfaces;

import java.util.List;

import dominio.ContactoIndividual;
import excepciones.ExcepcionRegistroDuplicado;

public interface IAdaptadorContactoDAO {
	
	public void registrarContacto(ContactoIndividual nuevoContacto) throws ExcepcionRegistroDuplicado;
	public ContactoIndividual recuperarContacto(int codigo);
	public List<ContactoIndividual> recuperarTodosContactos();
	public void modificarContacto(ContactoIndividual contacto);
}
