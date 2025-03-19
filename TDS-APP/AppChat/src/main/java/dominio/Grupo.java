 package dominio;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Clase que representa un grupo de contactos.
 */
public class Grupo extends Contacto {

    /**
     * Ruta de la imagen predeterminada para un grupo.
     */
    public static final String IMAGEN_POR_DEFECTO_GRUPO = "/personas(2)(1)(1).png";

    private String imagen; // Imagen asociada al grupo.
    private Set<ContactoIndividual> miembros; // Miembros que forman parte del grupo.
    private Usuario propietario; // Usuario propietario del grupo.

    /**
     * Constructor de la clase Grupo.
     *
     * @param nombre        el nombre del grupo.
     * @param propietario   el propietario del grupo.
     * @param listaMiembros los miembros iniciales del grupo.
     */
    public Grupo(String nombre, Usuario propietario, Set<ContactoIndividual> listaMiembros) {
        super(nombre);
        this.propietario = propietario;
        this.miembros = listaMiembros;
        this.imagen = IMAGEN_POR_DEFECTO_GRUPO;
    }

    /**
     * Constructor de la clase Grupo con una imagen personalizada.
     *
     * @param nombre        el nombre del grupo.
     * @param pathImagen    la ruta de la imagen del grupo.
     * @param propietario   el propietario del grupo.
     * @param listaMiembros los miembros iniciales del grupo.
     */
    public Grupo(String nombre, String pathImagen, Usuario propietario, Set<ContactoIndividual> listaMiembros) {
        super(nombre);
        this.propietario = propietario;
        this.miembros = listaMiembros;
        this.imagen = pathImagen;
    }

    /**
     * Constructor por defecto. Crea un grupo vacío con una imagen predeterminada.
     */
    public Grupo() {
        this.miembros = new HashSet<>();
        this.imagen = IMAGEN_POR_DEFECTO_GRUPO;
    }

    /**
     * Obtiene los miembros del grupo.
     *
     * @return una lista de contactos individuales que son miembros del grupo.
     */
    public List<ContactoIndividual> getMiembros() {
        return new LinkedList<>(miembros);
    }

    /**
     * Elimina un contacto del grupo.
     *
     * @param contacto el contacto que se desea eliminar del grupo.
     */
    public void eliminarContacto(Contacto contacto) {
        miembros.remove(contacto);
    }

    /**
     * Comprueba si un usuario pertenece al grupo.
     *
     * @param usuario el usuario que se desea verificar.
     * @return {@code true} si el usuario pertenece al grupo, de lo contrario {@code false}.
     */
    public boolean poseeMiembro(Usuario usuario) {
        return miembros.stream().anyMatch(c -> c.getUsuario().equals(usuario));
    }

    /**
     * Obtiene el propietario del grupo.
     *
     * @return el propietario del grupo.
     */
    public Usuario getPropietario() {
        return propietario;
    }

    /**
     * Establece el propietario del grupo.
     *
     * @param propietario el nuevo propietario del grupo.
     */
    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }

    /**
     * Obtiene la imagen del grupo.
     *
     * @return la ruta de la imagen asociada al grupo.
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Establece una nueva imagen para el grupo.
     *
     * @param imagen la nueva ruta de la imagen del grupo.
     * 
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     * Establece los miembros del grupo.
     *
     * @param miembros el conjunto de miembros que conformarán el grupo.
     */
    public void setMiembros(Set<ContactoIndividual> miembros) {
        this.miembros = miembros;
    }
    
    @Override
	public List<Mensaje> getMensajesRecibidos(Optional<Usuario> emptyOpt) {
		return this.miembros.stream().flatMap(c -> c.getUsuario().getGrupos().stream())
				.filter(c -> c instanceof Grupo).map(c -> (Grupo) c).filter(g -> this.equals(g))
				.flatMap(g -> g.getMensajes().stream()).collect(Collectors.toList());
	}
    


}
