package dominio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Clase abstracta que representa un contacto en el sistema.
 * Es la base para los contactos individuales y los grupos.
 */

public abstract class Contacto {
	
	private String nombre;
	private List<Mensaje> mensajes;
	private int codigo;

	
	public Contacto(String nombre) {
		this.nombre = nombre;
		 this.mensajes = new ArrayList<Mensaje>();
	}

	public Contacto(String nombre, List<Mensaje> mensajes) {
		this.nombre = nombre;
		this.mensajes = mensajes;
	}

    public Contacto() {
        this.mensajes = new ArrayList<>();
    }
    
    
    public String getNombre() {
        return nombre;
    }

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


    public List<Mensaje> getMensajes() {			
        return new ArrayList<Mensaje>(mensajes);
    }
    
	public abstract List<Mensaje> getMensajesRecibidos(Optional<Usuario> usuario);
    
	public void añadirMensajes(List<Mensaje> mensajes) {
		this.mensajes.addAll(mensajes);
	}
	
	public void enviarMensaje(Mensaje mensaje) {
	    mensajes.add(mensaje);

	    // Si el receptor es un usuario registrado, agregar el mensaje a su lista
	    if (mensaje.getReceptor() instanceof ContactoIndividual) {
	        ((ContactoIndividual) mensaje.getReceptor()).recibirMensaje(mensaje);
	    }
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	
	

}
