package persistencia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import beans.Entidad;
import beans.Propiedad;
import dominio.ContactoIndividual;
import dominio.Descuento;
import dominio.Usuario;
import excepciones.ExcepcionDAO;
import excepciones.ExcepcionRegistroDuplicado;
import persistencia.interfaces.IAdaptadorUsuarioDAO;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

/**
 * Clase AdaptadorUsuarioDAO
 * 
 * Esta clase actúa como un puente entre los objetos de dominio Usuario
 * y su representación en la base de datos, utilizando un servicio de persistencia.
 * Implementa los métodos necesarios para registrar, recuperar y gestionar usuarios
 * , asegurando la consistencia entre el modelo de datos y el sistema persistente.
 */

public class AdaptadorUsuarioDAO implements IAdaptadorUsuarioDAO {

    public static ServicioPersistencia servicioPersistencia;
    private static AdaptadorUsuarioDAO adaptadorUsuario;

    // Patrón Singleton
    public static AdaptadorUsuarioDAO getInstance() {
        if (adaptadorUsuario == null)
            adaptadorUsuario = new AdaptadorUsuarioDAO();
        return adaptadorUsuario;
    }

    private AdaptadorUsuarioDAO() {
        servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    // Método para convertir de Usuario a Entidad
    private Entidad usuarioToEntidad(Usuario usuario) {
        Entidad eUsuario = new Entidad();
        eUsuario.setNombre("Usuario");
        eUsuario.setPropiedades(new ArrayList<>(Arrays.asList(
                new Propiedad("nombreCompleto", usuario.getNombreCompleto()),
                new Propiedad("movil", usuario.getMovil()),
                new Propiedad("contrasena", usuario.getContrasena()),
                new Propiedad("email", usuario.getEmail()),
                new Propiedad("imagenPerfil", usuario.getPathImagen()),
                new Propiedad("premium", String.valueOf(usuario.isPremium())),
                new Propiedad("mensajeSaludo", usuario.getMensajeSaludo().orElse("")),
                new Propiedad("listaContacto", PersistenciaUtils.getCodigosContactos(usuario.getListaContactos())),
                new Propiedad("fechaNacimiento", usuario.getFechaNacimiento().map(date -> new SimpleDateFormat("dd/MM/yyyy").format(date)).orElse(null)),
                new Propiedad("grupos", PersistenciaUtils.obtenerCodigosGrupos(usuario.getGrupos())),
                new Propiedad("descuento", !usuario.getDescuento().isEmpty() ? usuario.getDescuento().get().getClass().getName() : ""),
                new Propiedad("fechaRegistro", usuario.getFechaRegistro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))                       
        )));
        return eUsuario;
    }

    // Método para convertir de Entidad a Usuario
    @SuppressWarnings("deprecation")
	private Usuario entidadToUsuario(Entidad eUsuario) {
        Usuario usuario = new Usuario();
        usuario.setCodigo(eUsuario.getId());
        usuario.setNombreCompleto(servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "nombreCompleto"));
        
        // Se añade el objeto al pool para evitar bucles a la hora de la recuperación de los objetos.
        PoolDAO.getInstance().addObjeto(usuario.getCodigo(), usuario);
        
        usuario.setMovil(servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "movil"));
        usuario.setContrasena(servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "contrasena"));
        usuario.setEmail(servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "email"));
        usuario.setPathImagen(servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "imagenPerfil"));
        usuario.setPremium(Boolean.parseBoolean(servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "premium")));
        usuario.setGrupos(PersistenciaUtils.obtenerGruposDesdeCodigos(servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "grupos")));
        
        // Se añade el objeto al pool para evitar bucles a la hora de la recuperación de los objetos.
        PoolDAO.getInstance().addObjeto(usuario.getCodigo(), usuario);        
        
        // Recuperamos la fecha de registro
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaRegistro = LocalDate.parse(servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "fechaRegistro"), formatter);
        usuario.setFechaRegistro(fechaRegistro);
        
        // Recuperar el descuento
        String descuento = servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "descuento");
		Descuento descuentoObj = null;
		if (!descuento.isEmpty()) {
			try {
				descuentoObj = (Descuento) Class.forName(descuento).newInstance();
				usuario.setDescuento(descuentoObj);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
        
        // Recuperar y asignar la fecha de nacimiento
        String fechaNacimientoStr = servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "fechaNacimiento");
        if (fechaNacimientoStr != null && !fechaNacimientoStr.isEmpty()) {
            try {
            	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date fechaNacimiento = dateFormat.parse(fechaNacimientoStr);
                usuario.setFechaNacimiento(fechaNacimiento);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Recuperar lista de contactos
        String listaContacto = servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "listaContacto");
        if (listaContacto != null && !listaContacto.isEmpty()) {
            List<ContactoIndividual> contactos = PersistenciaUtils.getListaContactosDesdeCodigos(listaContacto);
            usuario.setListaContactos(contactos);
        }

        // Recuperar mensaje saludo (opcional)
        Optional.ofNullable(servicioPersistencia.recuperarPropiedadEntidad(eUsuario, "mensajeSaludo"))
                .ifPresent(usuario::setMensajeSaludo);

        return usuario;
    }

    @Override
    public void registrarUsuario(Usuario nuevoUsuario) throws ExcepcionRegistroDuplicado{
        Entidad eUsuario = null;
        boolean noRegistrar = false;

        try {
            eUsuario = servicioPersistencia.recuperarEntidad(nuevoUsuario.getCodigo());
        } catch (NullPointerException e) {
            noRegistrar = true;
        }

        if (noRegistrar) throw new ExcepcionRegistroDuplicado("Entidad ya registrada");

        // Registrar los atributos que son objetos
        AdaptadorContactoDAO adaptadorContacto = null;
		try {
			adaptadorContacto = TDSFactoriaDAO.getInstance().getContactoDAO();
		} catch (ExcepcionDAO e) {
			e.printStackTrace();
		}
		
        for (ContactoIndividual contacto : nuevoUsuario.getListaContactos()) {
            adaptadorContacto.registrarContacto(contacto);
        }

        // Crear y registrar la entidad Usuario
        eUsuario = usuarioToEntidad(nuevoUsuario);
        eUsuario = servicioPersistencia.registrarEntidad(eUsuario);

        // Asignar el ID generado por el servicio de persistencia al Usuario
        nuevoUsuario.setCodigo(eUsuario.getId());

        // Guardamos en el Pool
        PoolDAO.getInstance().addObjeto(nuevoUsuario.getCodigo(), nuevoUsuario);
    }

    @Override
    public void modificarUsuario(Usuario usuarioModificar) {
    	
        // Recuperar la entidad del usuario desde el servicio de persistencia
        Entidad eUsuario = servicioPersistencia.recuperarEntidad(usuarioModificar.getCodigo());

        // Iterar sobre las propiedades de la entidad (actualización de datos)
        for (Propiedad prop : eUsuario.getPropiedades()) {
            if (prop.getNombre().equals("nombreCompleto")) {
                prop.setValor(usuarioModificar.getNombreCompleto());
            } else if (prop.getNombre().equals("movil")) {
                prop.setValor(usuarioModificar.getMovil());
            } else if (prop.getNombre().equals("contrasena")) {
                prop.setValor(usuarioModificar.getContrasena());
            } else if (prop.getNombre().equals("email")) {
                prop.setValor(usuarioModificar.getEmail());
            } else if (prop.getNombre().equals("fechaNacimiento")) {
                prop.setValor(usuarioModificar.getFechaNacimiento().isEmpty() ? null
                        : new SimpleDateFormat("dd/MM/yyyy").format(usuarioModificar.getFechaNacimiento().get()));
            } else if (prop.getNombre().equals("imagenPerfil")) {
                prop.setValor(
                        usuarioModificar.getPathImagen().equals(Usuario.IMAGEN_POR_DEFECTO) ? Usuario.IMAGEN_POR_DEFECTO
                                : usuarioModificar.getPathImagen());
            } else if (prop.getNombre().equals("premium")) {
                prop.setValor(String.valueOf(usuarioModificar.isPremium()));
            } else if (prop.getNombre().equals("mensajeSaludo")) {
                prop.setValor(usuarioModificar.getMensajeSaludo().orElse(null));
            } else if (prop.getNombre().equals("listaContacto")) {
                prop.setValor(PersistenciaUtils.getCodigosContactos(usuarioModificar.getListaContactos()));
            } else if(prop.getNombre().equals("grupos")) {
            	prop.setValor(PersistenciaUtils.obtenerCodigosGrupos(usuarioModificar.getGrupos()));
            } else if(prop.getNombre().equals("descuento")) {
            	prop.setValor(usuarioModificar.getDescuento().isPresent() ? usuarioModificar.getDescuento().get().getClass().getName() : "");
            }

            // Modificar la propiedad en la base de datos
            servicioPersistencia.modificarPropiedad(prop);
        }
    }

    @Override
    public Usuario recuperarUsuario(int codigo) {
    	
        // Si la entidad está en el Pool, la devolvemos directamente
        if (PoolDAO.getInstance().contiene(codigo)) return (Usuario) PoolDAO.getInstance().getObjeto(codigo);

        // Recuperar la entidad del usuario desde el servicio de persistencia
        Entidad eUsuario = servicioPersistencia.recuperarEntidad(codigo);
        
        // Convertir la entidad en Usuario
        return entidadToUsuario(eUsuario);
    }

    @Override
    public List<Usuario> recuperarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        List<Entidad> eUsuarios = servicioPersistencia.recuperarEntidades("Usuario");

        for (Entidad eUsuario : eUsuarios) {
            usuarios.add(recuperarUsuario(eUsuario.getId()));
        }
        return usuarios;
    }
}
