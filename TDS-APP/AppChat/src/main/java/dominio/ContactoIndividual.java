package dominio;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.swing.ImageIcon;

/**
 * Clase que represena un contacto individual
 */

public class ContactoIndividual extends Contacto {
	
    
    private String telefono;
    private Usuario usuario; // Usuario al que corresponde el contacto

    public ContactoIndividual(String nombre, String telefono, Usuario usuario) {
        super(nombre);
        this.telefono = telefono;
        this.usuario = usuario;
    }
    
    public ContactoIndividual(String nombre, LinkedList<Mensaje> mensajes, String telefono, Usuario usuario) {
		super(nombre, mensajes);
		this.telefono= telefono;
		this.usuario = usuario;
	}
    
    public ContactoIndividual() {
        super();
    }
    
    public String getTelefono() {
        return telefono;
    }
    
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public ImageIcon getFoto() {
		return usuario.getIconoDesdePath();
	}

	// Añade al contacto al grupo en cuestion
	public void addGrupo(Grupo grupo) {
		usuario.addGrupo(grupo);;
	}

	/**
	 * Modifica el grupo del contacto
	 * 
	 * @param g Grupo ya modificado
	 */
	public void modificarGrupo(Grupo g) {
		List<Grupo> grupos = usuario.getGrupos();
		grupos.remove(g);
		grupos.add(g);
	}
	
	  public ContactoIndividual getContacto(Usuario usuario) {
	        return this.usuario.getListaContactos().stream()
	                .filter(c -> c instanceof ContactoIndividual)
	                .map(c -> (ContactoIndividual) c)
	                .filter(c -> c.getUsuario().equals(usuario))
	                .findAny()
	                .orElse(null);
	    }
	@Override
	public List<Mensaje> getMensajesRecibidos(Optional<Usuario> usuario) {
		ContactoIndividual contacto = getContacto(usuario.orElse(null));
		if (contacto != null) {
			return contacto.getMensajes();
		} else
			return new LinkedList<>();
	}
	
	//NO SE SI ESTO FUNCIONARÁ
	public void recibirMensaje(Mensaje mensaje) {
        getMensajes().add(mensaje);
    }

	/**
	 * Comprueba si se corresponde con el usuario pasado como parámetro
	 * 
	 * @param otherUser Usuario con el que realizar la comprobación
	 * @return Devuelve si el usuario asociado al contacto es el mismo que el pasado
	 *         como parámetro
	 */
	public boolean isUsuario(Usuario otro) {
		return usuario.equals(otro);
	}

	
	@Override
	public int hashCode() {
	    return Objects.hash(telefono); // Genera un hash basado solo en el campo 'telefono'.
	}


	/**
	 * Dos contactos son iguales si tienen el mismo número de teléfono
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContactoIndividual other = (ContactoIndividual) obj;
		if (!telefono.equals(other.telefono))
			return false;
		return true;
	}

}
