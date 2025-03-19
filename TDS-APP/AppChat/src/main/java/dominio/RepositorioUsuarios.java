package dominio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import excepciones.ExcepcionDAO;
import persistencia.FactoriaDAO;

public class RepositorioUsuarios {
	
	private Map<String, Usuario> usuariosRegistrados;	// Mapa que relaciona el télefono con su usuario correspondiente.
	private static RepositorioUsuarios repositorioUsuarios;
	private FactoriaDAO factoriaTDS;
		
	public RepositorioUsuarios() {
		usuariosRegistrados = new HashMap<String, Usuario>();
		try {
			factoriaTDS = FactoriaDAO.getInstance(FactoriaDAO.DAO_TDS);
		} catch (ExcepcionDAO e) {
			e.printStackTrace();
		}
		this.cargarRepositorio();
	}
	
	// Patrón Singleton 
	
	public static RepositorioUsuarios getInstance() {
		  if (repositorioUsuarios == null) { 
	            repositorioUsuarios = new RepositorioUsuarios();
	        }
	        return repositorioUsuarios; 
	}
	
	
	/**
	 * Añade a la lista de usuarios registrados el usuario pasado como párametro.
	 * @param nuevoUsuario
	 */
	
	public void anadirUsuario(Usuario nuevoUsuario) {
		usuariosRegistrados.put(nuevoUsuario.getMovil(), nuevoUsuario);
	}
	
	/**
	 * Elimina de la lista de usuarios registrados al usuario pasado como parámetro.
	 * @param usuarioEliminar
	 */
	
	public void eliminarUsuario(Usuario usuarioEliminar) {
		usuariosRegistrados.remove(usuarioEliminar.getMovil());
	}
	
	/**
	 * Devuelve un Optional que contiene, si se encuentra en la lista de usuarios, el usuario cuyo teléfono se pasa como parámetro.
	 * @param tlf
	 * @return
	 */
	
	public Optional<Usuario> getUsuarioPorTelefono(String tlf) {
	    String telefonoNormalizado = tlf.replaceAll("\\s+", "").replaceAll("-", "");
	    return Optional.ofNullable(usuariosRegistrados.get(telefonoNormalizado));
	}

	
	// ------- Funcion Auxiliar----------
	
	public void cargarRepositorio() {
	    List<Usuario> usuarios = factoriaTDS.getUsuarioDAO().recuperarUsuarios();
	    
	    for (Usuario usuario : usuarios) {
	        try {
	            if (usuario.getMovil() != null && !usuario.getMovil().isEmpty()) {
	            	String telefonoNormalizado = usuario.getMovil().replaceAll("\\s+", "").replaceAll("-", "");
	            	usuariosRegistrados.put(telefonoNormalizado, usuario);

	            } else {
	                System.err.println("El usuario con código " + usuario.getCodigo() + " no tiene un teléfono válido.");
	            }
	        } catch (Exception e) {
	            System.err.println("Error cargando usuario con código " + usuario.getCodigo() + ": " + e.getMessage());
	        }
	    }
	}

	public void imprimirUsuarios() {
	    System.out.println("Usuarios registrados:");
	    for (Map.Entry<String, Usuario> entry : usuariosRegistrados.entrySet()) {
	        System.out.println("Teléfono: " + entry.getKey() + " - Nombre: " + entry.getValue().getNombreCompleto());
	    }
	}

	
	

}
