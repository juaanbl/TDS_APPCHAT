package persistencia;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import dominio.ContactoIndividual;
import dominio.Grupo;
import dominio.Mensaje;
import dominio.Usuario;
import excepciones.ExcepcionDAO;
import excepciones.ExcepcionRegistroDuplicado;

public class PersistenciaUtils {

	private static AdaptadorMensajeDAO adaptadorMensaje;
	private static AdaptadorContactoDAO adaptadorContacto;
	private static AdaptadorGrupoDAO adaptadorGrupo;
	private static AdaptadorUsuarioDAO adaptadorUsuario;

	static {
		try {
			adaptadorMensaje = TDSFactoriaDAO.getInstance().getMensajeDAO();
			adaptadorContacto = TDSFactoriaDAO.getInstance().getContactoDAO();
			adaptadorGrupo = TDSFactoriaDAO.getInstance().getGrupoDAO();
			adaptadorUsuario = TDSFactoriaDAO.getInstance().getUsuarioDAO();
		} catch (ExcepcionDAO e) {
			e.printStackTrace();
		}
	}
		
	/**
     * Convierte una cadena de códigos de grupos en una lista de objetos Grupo.
     * 
     * @param codigosGrupos Cadena de códigos separados por espacios.
     * @return Lista de grupos.
     */
	
	public static List<Grupo> obtenerGruposDesdeCodigos(String codigosGrupos) {
		List<Grupo> listaGrupos = new ArrayList<Grupo>();

		if (codigosGrupos == null || codigosGrupos.trim().isEmpty()) {
			return listaGrupos; 
		}

		StringTokenizer srtTok = new StringTokenizer(codigosGrupos, " ");

		while (srtTok.hasMoreTokens()) {
			int codigo = Integer.parseInt(srtTok.nextToken());
			Grupo grupo = adaptadorGrupo.recuperarGrupo(codigo); 
			listaGrupos.add(grupo);
		}

		return listaGrupos;
	}

	/**
	 * Convierte una lista de grupos en una cadena de códigos separados por
	 * espacios.
	 * 
	 * @param grupos Lista de grupos.
	 * @return Cadena de códigos de los grupos.
	 */
	public static String obtenerCodigosGrupos(List<Grupo> grupos) {
		StringBuilder codigos = new StringBuilder();

		for (Grupo grupo : grupos) {
			codigos.append(grupo.getCodigo()).append(" ");
		}

		return codigos.toString().trim(); 
	}


	/**
	 * Convierte una cadena de códigos de mensajes en una lista de mensajes.
	 * 
	 * @param codigosMensajes Cadena de códigos de mensajes.
	 * @return Lista de mensajes.
	 */

	static List<Mensaje> obtenerMensajesDesdeCódigos(String codigosMensajes) {
		List<Mensaje> listaMensajes = new ArrayList<>();

		// Validar si la cadena es nula o vacía
		if (codigosMensajes == null || codigosMensajes.trim().isEmpty()) {
			return listaMensajes; // Retornar lista vacía
		}

		StringTokenizer srtTok = new StringTokenizer(codigosMensajes, " ");

		while (srtTok.hasMoreTokens()) {
			int codigo = Integer.parseInt(srtTok.nextToken());
			Mensaje mensaje = adaptadorMensaje.recuperarMensaje(codigo);
			listaMensajes.add(mensaje);
		}

		return listaMensajes;
	}

	/**
	 * Convierte una lista de mensajes en una cadena de códigos separados por
	 * espacios.
	 * 
	 * @param mensajes Lista de mensajes.
	 * @return Cadena de códigos de los mensajes.
	 */
	static String obtenerCodigosMensajes(List<Mensaje> mensajes) {
		String idMensajes = "";

		for (Mensaje mensaje : mensajes) {
			idMensajes += mensaje.getCodigo() + " ";
			
		}		
		return idMensajes.trim();
	}

	/**
	 * Convierte la lista de contactos pasada como parámetro a una lista con sus códigos correspondientes.
	 * @param listaContactos
	 * @return
	 */

	static String getCodigosContactos(List<ContactoIndividual> listaContactos) {

		String codigosContacto = "";

		for (ContactoIndividual contacto : listaContactos) {
			codigosContacto += contacto.getCodigo() + " ";
		}

		return codigosContacto.trim();
	}

	/**
	 * Convierte una cadena que contiene codigos de contactos a su contacto respectivo.
	 * @param codigos
	 * @return
	 * @throws ExcepcionRegistroDuplicado
	 */

	static List<ContactoIndividual> getListaContactosDesdeCodigos(String codigos) {
		List<ContactoIndividual> listaContactos = new ArrayList<>();

		// Validar si la cadena es nula o vacía
		if (codigos == null || codigos.trim().isEmpty()) {
			return listaContactos; // Retornar lista vacía
		}

		StringTokenizer strTok = new StringTokenizer(codigos, " ");

		while (strTok.hasMoreTokens()) {
			int codigo = Integer.parseInt((String) strTok.nextElement());
			ContactoIndividual contacto = adaptadorContacto.recuperarContacto(codigo);
			listaContactos.add(contacto);
		}

		return listaContactos;
	}
	
	/**
	 * Convierte una cadena que contiene codigos de contactos a su contacto respectivo, esta vez devolviendo un se.
	 * @param codigos
	 * @return
	 * @throws ExcepcionRegistroDuplicado
	 */
	
	static Set<ContactoIndividual> getListaMiembrosDesdeCodigos(String codigos) {
		Set<ContactoIndividual> listaContactos = new HashSet<>();

		// Validar si la cadena es nula o vacía
		if (codigos == null || codigos.trim().isEmpty()) {
			return listaContactos; // Retornar lista vacía
		}

		StringTokenizer strTok = new StringTokenizer(codigos, " ");

		while (strTok.hasMoreTokens()) {
			int codigo = Integer.parseInt((String) strTok.nextElement());
			ContactoIndividual contacto = adaptadorContacto.recuperarContacto(codigo);
			listaContactos.add(contacto);
		}

		return listaContactos;
	}
	
	static Usuario getUsuarioDesdeCodigo(String codigo) {
		return adaptadorUsuario.recuperarUsuario(Integer.parseInt(codigo));
	}

}
