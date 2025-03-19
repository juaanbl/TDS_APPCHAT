package controlador;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.DefaultListModel;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import persistencia.*;
import persistencia.interfaces.IAdaptadorContactoDAO;
import persistencia.interfaces.IAdaptadorGrupoDAO;
import persistencia.interfaces.IAdaptadorMensajeDAO;
import persistencia.interfaces.IAdaptadorUsuarioDAO;
import dominio.*;
import excepciones.*;
import controlador.filtros.*;

public class Controlador {
	
	private Usuario usuarioActual;
	
	private static Controlador controlador;
	
	// Gestores necesarios para la lógica de la aplicación.
	private RepositorioUsuarios repositorioUsuarios;
	private IAdaptadorUsuarioDAO adaptadorUsuario;
	private IAdaptadorContactoDAO adaptadorContacto;
	private IAdaptadorMensajeDAO adaptadorMensaje;
	private IAdaptadorGrupoDAO adaptadorGrupo;
	
	// Mapa con suppliers para crear los descuentos
	private static Map<String,Supplier<Descuento>> supplierDescuento = new HashMap<String, Supplier<Descuento>>();

	// Implementación del patron Singleton	
	public static Controlador getInstance() {
		if(controlador == null) {
			controlador = new Controlador();
		}
		return controlador;
	}
		
	private Controlador() {
		repositorioUsuarios  = RepositorioUsuarios.getInstance();
		inicializarAdaptadores();
		supplierDescuento.put(DescuentoMensajes.class.getName(), DescuentoMensajes::new);
		supplierDescuento.put(DescuentoFechaRegistro.class.getName(), DescuentoFechaRegistro::new);
		supplierDescuento.put(DescuentoCompuesto.class.getName(), DescuentoCompuesto::new);
	}
	
	private void inicializarAdaptadores() {
		FactoriaDAO factoria = null;
		try {
			factoria = FactoriaDAO.getInstance(FactoriaDAO.DAO_TDS);
		} catch (ExcepcionDAO e) {
			e.printStackTrace();
		}

		adaptadorGrupo = factoria.getGrupoDAO();
		adaptadorContacto = factoria.getContactoDAO();
		adaptadorMensaje = factoria.getMensajeDAO();
		adaptadorUsuario = factoria.getUsuarioDAO();
	}
	
	/**
	 * Registra al usuario en la aplicacion si los parametros cumplen los requisitos.
	 * 
	 * @param nombre
	 * @param apellidos
	 * @param movil
	 * @param contrasena
	 * @param contrasenaRepe
	 * @param email
	 * @param fechaNacimiento
	 * @param pathImagen
	 * @param mensajeSaludo
	 * @return
	 * @throws ExcepcionRegistro
	 * @throws ExcepcionDAO 
	 */

	public void registrarUsuario(String nombre, String apellidos, String movil, String contrasena,
            String contrasenaRepe, String email, Date fechaNacimiento,
            String pathImagen, String mensajeSaludo) throws ExcepcionRegistro {
		
		
		// Si el telefono ya está registrado se lanza una excepción
		if(repositorioUsuarios.getUsuarioPorTelefono(movil).isPresent()) {
			throw new ExcepcionRegistro("El teléfono ya está registrado");
		}
		
		// Validacion de requisitos
		validarCamposObligatorios(nombre, apellidos, movil, contrasena, contrasenaRepe, email);
		validarContrasenas(contrasena, contrasenaRepe);
		validarEmail(email);		

		Usuario usuario = new Usuario(nombre +" "+ apellidos, movil, contrasena, email);
		usuarioActual = usuario;
		
		configurarOpcionales(usuario, fechaNacimiento, pathImagen, mensajeSaludo);
		repositorioUsuarios.anadirUsuario(usuario);
		
	    try {
			adaptadorUsuario.registrarUsuario(usuario);
		} catch (ExcepcionRegistroDuplicado e) {
			e.printStackTrace();
			throw new ExcepcionRegistro("Usuario ya registrado");
		}
		
	}
	
	/**
	 * Permite al usuario loguarse en la aplicación si sus credenciales son correctas.
	 * @param telefono
	 * @param contrasena
	 * @return
	 * @throws ExcepcionLogin
	 */
	
	public void loguearUsuario(String telefono, String contrasena) throws ExcepcionLogin {
	    
		repositorioUsuarios.getUsuarioPorTelefono(telefono)
	    .ifPresentOrElse(
	        u -> usuarioActual = u, // Si el usuario existe, asignarlo
	        () -> { throw new ExcepcionLogin("El teléfono no está registrado."); } // Si no, lanzar la excepción
	    );
	        	    
	
	    if (!usuarioActual.getContrasena().equals(contrasena)) {
	        throw new ExcepcionLogin("La contraseña es incorrecta.");
	    }
	    
	}

	
	/**
	 * Método para añadir un nuevo contacto al usuario cuyo teléfono se pasa como parámetro.
	 * @param tlf El número de teléfono del contacto
	 * @param nombreContacto El nombre personalizado para el contacto
	 * @return true si el contacto se agregó correctamente
	 * @throws ExcepcionContacto si el contacto ya existe o el usuario no está registrado
	 */
	public void agregarContacto(String tlf, String nombreContacto) throws ExcepcionAgregarContacto {
	    
	    // Verifica si el nombre del contacto ya está en uso
	    if(usuarioActual.getContactoPorNombre(nombreContacto).isPresent()) {
	        throw new ExcepcionAgregarContacto("El nombre del contacto ya está en uso");
	    }

	    // Verifica si el contacto ya está en la lista de contactos del usuario actual
	    if (usuarioActual.getContactoPorTelefono(tlf).isPresent()) {
	        throw new ExcepcionAgregarContacto("El contacto ya está agregado.");
	    }
	    
	    Optional<Usuario> usuarioContacto = repositorioUsuarios.getUsuarioPorTelefono(tlf);

	    if (usuarioContacto.isEmpty()) {
	        System.err.println("Número de teléfono no encontrado: " + tlf);
	        repositorioUsuarios.imprimirUsuarios(); // Verifica los usuarios cargados
	        throw new ExcepcionAgregarContacto("El número de télefono indicado no corresponde con ningún usuario");
	    }


	    // Si no está ya agregado y corresponde con un usuario de la explicación, crea y registra el nuevo contacto
	    ContactoIndividual nuevoContacto = new ContactoIndividual(nombreContacto, tlf, usuarioContacto.get());
	    usuarioActual.getListaContactos().add(nuevoContacto);

	    try {
	        // Registra el contacto en el adaptador de contactos
	        adaptadorContacto.registrarContacto(nuevoContacto);
	        // Actualiza el usuario con el nuevo contacto en el adaptador de usuarios
	        adaptadorUsuario.modificarUsuario(usuarioActual);
	    } catch (ExcepcionRegistroDuplicado e) {
	    	e.printStackTrace();
	        throw new ExcepcionAgregarContacto("Error al registrar el contacto: contacto duplicado.");
	    }
	}
	
	  /**
     * Actualiza un contacto individual en la base de datos.
     *
     * @param contacto El contacto a actualizar.
     * @throws ExcepcionDAO Si ocurre un error en la persistencia.
     */
    public void actualizarContacto(ContactoIndividual contacto) throws ExcepcionDAO {
        adaptadorContacto.modificarContacto(contacto); // Guardar en la base de datos
    }

	
    /**
     * Envía un mensaje con contenido textual a un contacto o grupo.
     *
     * @param receptor El contacto o grupo receptor del mensaje.
     * @param contenidoMensaje El contenido textual del mensaje.
     */
    public void enviarMensaje(Contacto receptor, String contenidoMensaje) {
        Mensaje mensajeEnviado = new Mensaje(contenidoMensaje, usuarioActual, receptor, Mensaje.ENVIADO);
        receptor.enviarMensaje(mensajeEnviado);

        try {
            adaptadorMensaje.registrarMensaje(mensajeEnviado);
        } catch (ExcepcionRegistroDuplicado e) {
            e.printStackTrace();
        }

        if (receptor instanceof ContactoIndividual) {
            adaptadorContacto.modificarContacto((ContactoIndividual) receptor);

            Usuario receptorUsuario = ((ContactoIndividual) receptor).getUsuario();
            if (receptorUsuario != null && !receptorUsuario.tieneContacto(usuarioActual)) {
                ContactoIndividual nuevoContacto = new ContactoIndividual("", usuarioActual.getMovil(), usuarioActual);
                receptorUsuario.getListaContactos().add(nuevoContacto);

                try {
                    adaptadorContacto.registrarContacto(nuevoContacto);
                } catch (ExcepcionRegistroDuplicado e) {
                    e.printStackTrace();
                }
                adaptadorUsuario.modificarUsuario(receptorUsuario);
            }
        } else if (receptor instanceof Grupo) {
        			Grupo grupo = (Grupo) receptor;
        	
        	for(ContactoIndividual miembro : grupo.getMiembros()) {
        		miembro.enviarMensaje(mensajeEnviado);
        		adaptadorContacto.modificarContacto(miembro);
        	}
            adaptadorGrupo.modificarGrupo(grupo);
        }
    }

    /**
     * Envía un mensaje con emoticono a un contacto o grupo.
     *
     * @param receptor El contacto o grupo receptor del mensaje.
     * @param emoji El código del emoticono a enviar.
     */
    public void enviarMensaje(Contacto receptor, int emoji) {
        Mensaje mensajeEnviado = new Mensaje(emoji, usuarioActual, receptor, Mensaje.ENVIADO);
        receptor.enviarMensaje(mensajeEnviado);

        try {
            adaptadorMensaje.registrarMensaje(mensajeEnviado);
        } catch (ExcepcionRegistroDuplicado e) {
            e.printStackTrace();
        }

        if (receptor instanceof ContactoIndividual) {
            adaptadorContacto.modificarContacto((ContactoIndividual) receptor);
        } else if (receptor instanceof Grupo) {
        	Grupo grupo = (Grupo) receptor;
        	
        	for(ContactoIndividual miembro : grupo.getMiembros()) {
        		miembro.enviarMensaje(mensajeEnviado);
        		adaptadorContacto.modificarContacto(miembro);
        	}
            adaptadorGrupo.modificarGrupo(grupo);
        }
    }

	 /**
     * Obtiene todos los mensajes asociados a un contacto.
     *
     * @param contacto El contacto del que se desea obtener los mensajes.
     * @return Lista de mensajes asociados al contacto, ordenados por fecha.
     */
    public List<Mensaje> getMensajes(Contacto contacto) {
        if (contacto instanceof ContactoIndividual && !((ContactoIndividual) contacto).isUsuario(usuarioActual)) {
            return Stream
                    .concat(contacto.getMensajes().stream(),
                            contacto.getMensajesRecibidos(Optional.of(usuarioActual)).stream())
                    .sorted().collect(Collectors.toList());
        } else {
            return contacto.getMensajes().stream().sorted().collect(Collectors.toList());
        }
    }
	
	
	 /**
     * Busca mensajes según los filtros especificados.
     *
     * @param texto Fragmento de texto a buscar en los mensajes.
     * @param telefono Teléfono asociado al emisor o receptor del mensaje.
     * @param nombreContacto Nombre del contacto asociado al mensaje.
     * @return Lista de mensajes que cumplen con los filtros aplicados.
     */
    public List<Mensaje> buscarMensajes(String texto, String telefono, String nombreContacto) {
        List<Mensaje> mensajes = obtenerMensajesDeTodosLosContactos();

        FiltroBusqueda filtro = new FiltroBase();
        if (texto != null && !texto.isEmpty()) {
            filtro = new FiltroPorTexto(filtro, texto);
        }
        if (telefono != null && !telefono.isEmpty()) {
            filtro = new FiltroPorTelefono(filtro, telefono);
        }
        if (nombreContacto != null && !nombreContacto.isEmpty()) {
            filtro = new FiltroPorNombre(filtro, nombreContacto);
        }

        return filtro.filtrar(mensajes);
    }


    /**
     * Obtiene todos los mensajes de los contactos asociados al usuario actual.
     *
     * @return Lista de mensajes de todos los contactos.
     */
    private List<Mensaje> obtenerMensajesDeTodosLosContactos() {
        return obtenerContactos().stream()
                .flatMap(c -> getMensajes(c).stream())
                .collect(Collectors.toList());
    }


	/**
	 * Crea un nuevo grupo con un nombre dado y una lista de miembros.
	 *
	 * @param nombreGrupo     Nombre del grupo a crear.
	 * @param listaMiembros   Lista de miembros que formarán parte del grupo.
	 * @return true si el grupo se crea correctamente.
	 * @throws ExcepcionCrearGrupo si el grupo ya está registrado o se produce algún error durante su creación.
	 */
	public void crearGrupo(String nombreGrupo, String imagenGrupo, DefaultListModel<ContactoIndividual> listaMiembros) throws ExcepcionCrearGrupo {
	    
		if(nombreGrupo.equals("")) {
			throw new ExcepcionCrearGrupo("Introduzca un nombre para grupo");
		}
		
		if(listaMiembros.isEmpty() || listaMiembros.size() < 2) {
			throw new ExcepcionCrearGrupo("Seleccione un número válido de participantes");
		}
		
	    // Convertir la lista de miembros del modelo en una lista de ContactoIndividual
	    Set<ContactoIndividual> miembrosGrupo = new HashSet<>();
	    
	    for (int i = 0; i < listaMiembros.getSize(); i++) {
	        if(!miembrosGrupo.add(listaMiembros.getElementAt(i))) {
	        	throw new ExcepcionCrearGrupo("No puede haber miembros repetidos en el grupo");
	        }
	    }
	    
	    Grupo grupoNuevo;
	    
	    // Crear un nuevo grupo con los datos proporcionados
	    if(imagenGrupo != null) {
	    	grupoNuevo = new Grupo(nombreGrupo, imagenGrupo, usuarioActual, miembrosGrupo);
	    }else {
	    	 grupoNuevo = new Grupo(nombreGrupo, usuarioActual, miembrosGrupo);
	    }
	    
	    // Añadimos el grupo al usuarioActual
	    usuarioActual.addGrupo(grupoNuevo);	    	   

	    try {
	        adaptadorGrupo.registrarGrupo(grupoNuevo); // Registrar el grupo en la base de datos
	        adaptadorUsuario.modificarUsuario(usuarioActual); // Actualizar los datos del usuario
	    } catch (ExcepcionRegistroDuplicado e) {
	        e.printStackTrace();
	        throw new ExcepcionCrearGrupo("El grupo ya ha sido registrado");
	    }

	}

	/**
	 * Modifica un grupo existente actualizando su lista de miembros.
	 *
	 * @param nombreGrupo     Nombre del grupo a modificar.
	 * @param listaMiembros   Nueva lista de miembros para el grupo.
	 * @return true si el grupo se modifica correctamente.
	 * @throws ExcepcionModificarGrupo 
	 */
	public void modificarGrupo(String nombreGrupo, DefaultListModel<ContactoIndividual> listaMiembros) throws ExcepcionModificarGrupo {
	    
		if(listaMiembros.size() < 2 || listaMiembros.isEmpty()) {
			throw new ExcepcionModificarGrupo("Seleccione un número válido de miembros");
		}
		
	    // Convertir la lista de miembros del modelo en una lista de ContactoIndividual
	    Set<ContactoIndividual> miembrosGrupo = new HashSet<>();
	    for (int i = 0; i < listaMiembros.getSize(); i++) {
	        if(!miembrosGrupo.add(listaMiembros.getElementAt(i))) {
	        	throw new ExcepcionModificarGrupo("No puede haber miembros repetidos en el grupo");
	        }
	    }

	    // Obtener el grupo existente y actualizar su lista de miembros
	    Grupo grupoAmodificar = usuarioActual.getGrupoPorNombre(nombreGrupo);
	    grupoAmodificar.setMiembros(miembrosGrupo);

	    // Guardar los cambios en la base de datos 
	    adaptadorGrupo.modificarGrupo(grupoAmodificar);
	    adaptadorUsuario.modificarUsuario(usuarioActual);
	}
	
	
	/**
	 * Activa la suscripción premium para el usuario actual.
	 * 
	 * Aplica un descuento basado en el número de mensajes enviados 
	 * o en la fecha de registro, si corresponde. Finalmente, actualiza
	 * los datos del usuario en la persistencia.
	 * 
	 * @return El precio final de la suscripción premium, teniendo en cuenta el descuento aplicado.
	 */
	public double setPremium() {
		if(usuarioActual.getNumMensajesUltimoMes() >= 5 && usuarioActual.getFechaRegistro().isAfter(LocalDate.of(2025, 1, 1))){
			Descuento descuentoCompuesto = supplierDescuento.get(DescuentoCompuesto.class.getName()).get();
			 ((DescuentoCompuesto) descuentoCompuesto).addDescuento(supplierDescuento.get(DescuentoMensajes.class.getName()).get());
			 ((DescuentoCompuesto) descuentoCompuesto).addDescuento(supplierDescuento.get(DescuentoFechaRegistro.class.getName()).get());
			 usuarioActual.setPremium(true);
			 usuarioActual.setDescuento(descuentoCompuesto);
			adaptadorUsuario.modificarUsuario(usuarioActual);

		}else if(usuarioActual.getNumMensajesUltimoMes() >= 5) {				
			usuarioActual.setPremium(true);
			usuarioActual.setDescuento(supplierDescuento.get(DescuentoMensajes.class.getName()).get());
			adaptadorUsuario.modificarUsuario(usuarioActual);
		}else if(usuarioActual.getFechaRegistro().isAfter(LocalDate.of(2025, 1, 1))) { 
			usuarioActual.setPremium(true);
			usuarioActual.setDescuento(supplierDescuento.get(DescuentoFechaRegistro.class.getName()).get());
			adaptadorUsuario.modificarUsuario(usuarioActual);
		}
				
		return usuarioActual.getPrecio();
	}
	
	/**
	 * Cancela la suscripción premium del usuario actual.
	 * 
	 * Desactiva el estado premium, elimina el descuento asociado 
	 * y actualiza los datos del usuario en la persistencia.
	 */
	public void cancelarPremium() {
		
		usuarioActual.setPremium(false);
		usuarioActual.setDescuento(null);
		adaptadorUsuario.modificarUsuario(usuarioActual);
	}
	
	/**
	 * Exporta los contactos del usuario actual a un archivo PDF en la ruta especificada.
	 * 
	 * @param ruta la ubicación donde se guardará el archivo PDF.
	 * @return Devuelve true si la exportación fue exitosa y false en caso de error.
	 */	
	public boolean exportarPDF(String ruta, String nombreContacto) {
	    try {
	        Optional<Contacto> contactoOGrupo = usuarioActual.getContactoOGrupoPorNombre(nombreContacto);
	        if (contactoOGrupo.isEmpty()) {
	            throw new IllegalArgumentException("El contacto o grupo indicado no existe.");
	        }
	        Contacto contactoSeleccionado = contactoOGrupo.get();

	        File rutaFile = new File(ruta);
	        if (rutaFile.isDirectory() || ruta.endsWith(File.separator)) {
	            ruta = new File(ruta, "Conversacion_" + contactoSeleccionado.getNombre().replaceAll("\\s+", "_") + ".pdf").getAbsolutePath();
	        }

	        FileOutputStream archivo = new FileOutputStream(ruta);
	        Document documento = new Document();
	        PdfWriter.getInstance(documento, archivo);
	        documento.open();

	        documento.add(new Paragraph("Conversación con: " + contactoSeleccionado.getNombre()));
	        documento.add(Chunk.NEWLINE);

	        if (contactoSeleccionado instanceof Grupo) {
	            Grupo grupo = (Grupo) contactoSeleccionado;

	            documento.add(new Paragraph("Miembros del grupo:"));
	            for (ContactoIndividual miembro : grupo.getMiembros()) {
	                documento.add(new Paragraph(" - " + miembro.getNombre() + " (" + miembro.getTelefono() + ")"));
	            }
	            documento.add(Chunk.NEWLINE);

	            documento.add(new Paragraph("Mensajes del grupo:"));
	            List<Mensaje> mensajesGrupo = grupo.getMensajes();
	            mensajesGrupo.sort(Comparator.comparing(Mensaje::getFechaYhora));

	            for (Mensaje mensaje : mensajesGrupo) {
	                String emisor = mensaje.getEmisor().getNombreCompleto();
	                String texto = mensaje.getTexto();
	                String fecha = mensaje.getFechaYhora().toString();

	                documento.add(new Paragraph(emisor + " (" + fecha + "):"));
	                documento.add(new Paragraph(texto));
	                documento.add(Chunk.NEWLINE);
	            }
	        } else {
	            List<Mensaje> mensajes = getMensajes(contactoSeleccionado);
	            mensajes.sort(Comparator.comparing(Mensaje::getFechaYhora));

	            for (Mensaje mensaje : mensajes) {
	                String emisor = mensaje.getEmisor().getNombreCompleto();
	                String texto = mensaje.getTexto();
	                String fecha = mensaje.getFechaYhora().toString();

	                documento.add(new Paragraph(emisor + " (" + fecha + "):"));
	                documento.add(new Paragraph(texto));
	                documento.add(Chunk.NEWLINE);
	            }
	        }

	        documento.close();
	        return true;
	    } catch (DocumentException | IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	
	
	/**
	 * Obtiene un grupo del usuario actual por su nombre.
	 *
	 * @param nombreGrupo El nombre del grupo a buscar.
	 * @return El grupo correspondiente al nombre proporcionado, o un grupo vacío si no se encuentra.
	 */	
	public Grupo getGrupoPorNombre(String nombreGrupo){
		return usuarioActual.getGrupoPorNombre(nombreGrupo);
	}

	/**
	 * Obtiene todos los contactos y grupos asociados al usuario actual.
	 *
	 * @return Lista de contactos y grupos del usuario actual.
	 */
	public List<Contacto> obtenerContactosYgrupos() {	    
	    return usuarioActual.obtenerContactosYgrupos();
	}

	/**
	 * Obtiene los nombres de los grupos asociados al usuario actual.
	 *
	 * @return Lista de nombres de grupos del usuario actual.
	 */
	public List<String> obtenerNombresGruposUsuario() {
	    return usuarioActual.getNombresGrupos();
	}
	
	/**
	 * 
	 * @return Lista de nombres del contacto
	 */
	
	public List<String> obtenerNombresContactos() {
	    if (usuarioActual != null) {
	        return usuarioActual.getListaContactos().stream()
	                .map(Contacto::getNombre)
	                .collect(Collectors.toList());
	    }
	    return new ArrayList<>(); // Devuelve una lista vacía si el usuario no está definido.
	}

	/**
	 * Obtiene la lista de contactos individuales asociados al usuario actual.
	 *
	 * @return Lista de ContactoIndividual del usuario actual.
	 */
	public List<ContactoIndividual> obtenerContactos() {
	    
	    if (usuarioActual != null) {
	        return usuarioActual.getListaContactos();
	    }

	    return new ArrayList<>(); // Retornar lista vacía si no hay usuario actual
	}

	/**
	 * Obtiene la ruta de la imagen del usuario actual.
	 *
	 * @return Ruta de la imagen del usuario actual.
	 */
	public String getImagenUsuario() {
	    if (usuarioActual.getPathImagen() == null || usuarioActual.getPathImagen().isEmpty()) {
	        return Usuario.IMAGEN_POR_DEFECTO; // Imagen predeterminada
	    }
	    return usuarioActual.getPathImagen();
	}


	/**
	 * Obtiene el nombre completo del usuario logueado.
	 *
	 * @return Nombre completo del usuario actual.
	 */
	public String getNombreUsuario() {
	    return usuarioActual.getNombreCompleto();
	}
	
	/**
	 * Actualiza la imagen de perfil del usuario actual.
	 *
	 * @param pathImagen La ruta de la nueva imagen de perfil.
	 */
	public void setImagenPerfilUsuario(String pathImagen) {
	    usuarioActual.setPathImagen(pathImagen);
	    adaptadorUsuario.modificarUsuario(usuarioActual);
	}
	
	/**
	 * Verifica si el usuario actual tiene una suscripción premium.
	 *
	 * @return {@code true} si el usuario es premium, {@code false} en caso contrario.
	 */
	public boolean isUsuarioPremium() {
	    return usuarioActual.isPremium();
	}
	
	public Usuario getUsuarioActual() {
		return usuarioActual;
	}


	// -------- Funciones auxiliares ----------
	
	/**
	 * Encargado de validar que todos los campos obligatorios no esten vacíos.
	 * @param nombre
	 * @param apellidos
	 * @param movil
	 * @param contrasena
	 * @param contrasenaRepe
	 * @param email
	 * @throws ExcepcionRegistro
	 */

	private void validarCamposObligatorios(String nombre, String apellidos, String movil,
                   String contrasena, String contrasenaRepe, String email) throws ExcepcionRegistro {
		if (nombre.isEmpty() || apellidos.isEmpty() || movil.isEmpty() || contrasena.isEmpty()
				|| contrasenaRepe.isEmpty() || email.isEmpty()) {
			
			throw new ExcepcionRegistro("Rellene los campos obligatorios");
			
		}
	}
	
	/**
	 * Valida que las passwords introducidas por el usuario coincidan. 
	 * @param contrasena
	 * @param contrasenaRepe
	 * @throws ExcepcionRegistro
	 */

	private void validarContrasenas(String contrasena, String contrasenaRepe) throws ExcepcionRegistro {
		if (!contrasena.equals(contrasenaRepe)) {
			throw new ExcepcionRegistro("Las contraseñas no son iguales");
		}
	}
	
	/**
	 * Verifica que el email introducido por el usuario sigue un formato adecuado.
	 * @param email
	 * @throws ExcepcionRegistro
	 */
	private void validarEmail(String email) throws ExcepcionRegistro {
	    if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
	        throw new ExcepcionRegistro("El email no es válido.");
	    }
	}
	
	/**
	 * Establece el valor de los campos opcionales del registro al usuario pasado como parámetro.
	 * @param usuario
	 * @param fechaNacimiento
	 * @param pathImagen
	 * @param mensajeSaludo
	 */
	
	private void configurarOpcionales(Usuario usuario, Date fechaNacimiento, String pathImagen, String mensajeSaludo) {
	    // Uso de Optional para manejar valores opcionales de manera segura
	    Optional.ofNullable(fechaNacimiento).ifPresent(usuario::setFechaNacimiento);
	    Optional.ofNullable(mensajeSaludo).filter(s -> !s.isEmpty()).ifPresent(usuario::setMensajeSaludo);
	    Optional.ofNullable(pathImagen)
	            .filter(icon -> !icon.equals(Usuario.IMAGEN_POR_DEFECTO))
	            .ifPresent(icon -> usuario.setPathImagen(icon));
	}

	
	

}
