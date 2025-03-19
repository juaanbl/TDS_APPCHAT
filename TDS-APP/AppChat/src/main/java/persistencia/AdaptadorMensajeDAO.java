package persistencia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import dominio.Contacto;
import dominio.Grupo;
import dominio.Mensaje;
import excepciones.ExcepcionDAO;
import excepciones.ExcepcionRegistroDuplicado;
import persistencia.interfaces.IAdaptadorMensajeDAO;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

/**
 * Clase AdaptadorMensajeDAO
 * 
 * Esta clase actúa como un puente entre los objetos de dominio Mensaje
 * y su representación en la base de datos, utilizando un servicio de persistencia.
 * Implementa los métodos necesarios para registrar, recuperar y gestionar mensajes.
 */

public class AdaptadorMensajeDAO implements IAdaptadorMensajeDAO{

    private static AdaptadorMensajeDAO adaptadorMensaje;
    private ServicioPersistencia servicioPersistencia;
    private AdaptadorContactoDAO adaptadorContacto;
    private AdaptadorUsuarioDAO adaptadorUsuario;
    private AdaptadorGrupoDAO adaptadorGrupo;

    /**
     * Obtiene la instancia única del adaptador (patrón Singleton).
     */
    public static AdaptadorMensajeDAO getInstance() {
        if (adaptadorMensaje == null) {
            adaptadorMensaje = new AdaptadorMensajeDAO();
        }
        return adaptadorMensaje;
    }

    /**
     * Constructor que inicializa el servicio de persistencia y los adaptadores de usuario, contacto y grupo.
     */
    private AdaptadorMensajeDAO() {
        servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
        try {
            adaptadorContacto = TDSFactoriaDAO.getInstance().getContactoDAO();
            adaptadorUsuario = TDSFactoriaDAO.getInstance().getUsuarioDAO();
            adaptadorGrupo = TDSFactoriaDAO.getInstance().getGrupoDAO();
        } catch (ExcepcionDAO e) {
            e.printStackTrace();
        }
    }
    

    /**
     * Convierte un Mensaje a una Entidad.
     */
    private Entidad mensajeToEntidad(Mensaje mensaje) {
        Entidad eMensaje = new Entidad();
        eMensaje.setNombre("Mensaje");

        // Determinamos si el receptor es un grupo
        boolean esGrupo = mensaje.getReceptor() instanceof Grupo;

        // Establecemos las propiedades de la entidad mensaje
        eMensaje.setPropiedades(new ArrayList<>(Arrays.asList(
            new Propiedad("texto", mensaje.getTexto()),
            new Propiedad("fechaYhora", mensaje.getFechaYhora().toString()),
            new Propiedad("emoticono", String.valueOf(mensaje.getEmoticono())),
            new Propiedad("receptor", String.valueOf(mensaje.getReceptor().getCodigo())),
            new Propiedad("toGrupo", String.valueOf(esGrupo)),
            new Propiedad("emisor", String.valueOf(mensaje.getEmisor().getCodigo())),
            new Propiedad("tipo", String.valueOf(mensaje.getTipo()))
        )));
        return eMensaje;
    }

    /**
     * Convierte una Entidad a un Mensaje.
     */
    private Mensaje entidadToMensaje(Entidad eMensaje) {
        Mensaje mensaje = new Mensaje();
        mensaje.setCodigo(eMensaje.getId());
        mensaje.setTexto(servicioPersistencia.recuperarPropiedadEntidad(eMensaje, "texto"));
        mensaje.setEmoticono(Integer.parseInt(servicioPersistencia.recuperarPropiedadEntidad(eMensaje, "emoticono")));
        mensaje.setFechaYhora(LocalDateTime.parse(servicioPersistencia.recuperarPropiedadEntidad(eMensaje, "fechaYhora")));

        // Recuperamos las propiedades que son objetos
        mensaje.setEmisor(adaptadorUsuario.recuperarUsuario(Integer.parseInt(servicioPersistencia.recuperarPropiedadEntidad(eMensaje, "emisor"))));

        boolean esGrupo = Boolean.parseBoolean(servicioPersistencia.recuperarPropiedadEntidad(eMensaje, "toGrupo"));
        int codigoReceptor = Integer.parseInt(servicioPersistencia.recuperarPropiedadEntidad(eMensaje, "receptor"));
        Contacto receptor;

        // Si el receptor es un grupo, lo recuperamos con el adaptador de grupos
        if (esGrupo) {
            receptor = adaptadorGrupo.recuperarGrupo(codigoReceptor);
        } else {
            // Si el receptor es un contacto individual, lo recuperamos con el adaptador de contactos
            receptor = adaptadorContacto.recuperarContacto(codigoReceptor);
        }

        mensaje.setReceptor(receptor);
        return mensaje;
    }

    /**
     * Registra un nuevo mensaje en la base de datos.
     * 
     * @param mensaje Mensaje a registrar.
     * @throws ExcepcionRegistroDuplicado Si el mensaje ya está registrado.
     */
    public void registrarMensaje(Mensaje mensaje) throws ExcepcionRegistroDuplicado {
        Entidad eMensaje = null;
        boolean noRegistrar = false;

        try {
            eMensaje = servicioPersistencia.recuperarEntidad(mensaje.getCodigo());
        } catch (NullPointerException e) {
            noRegistrar = true;
        }

        if (noRegistrar) throw new ExcepcionRegistroDuplicado("Entidad ya registrada");

        // Registrar al emisor (usuario) si no está registrado
        
        //adaptadorUsuario.registrarUsuario(mensaje.getEmisor());

        // Registrar al receptor si no está registrado
        //registrarReceptorNoAgregado(mensaje.getReceptor());

        // Convertir el mensaje a entidad y registrar
        eMensaje = mensajeToEntidad(mensaje);
        eMensaje = servicioPersistencia.registrarEntidad(eMensaje);

        // Asignar el identificador único generado por el servicio de persistencia al mensaje
        mensaje.setCodigo(eMensaje.getId());

        // Guardamos en el Pool
        PoolDAO.getInstance().addObjeto(mensaje.getCodigo(), mensaje);
    }

    /**
     * Recupera un mensaje desde la base de datos dado su código.
     * 
     * @param codigo Código del mensaje a recuperar.
     * @return El mensaje correspondiente al código.
     */
    public Mensaje recuperarMensaje(int codigo) {
        // Si la entidad está en el pool, la devolvemos directamente
        if (PoolDAO.getInstance().contiene(codigo)) return (Mensaje) PoolDAO.getInstance().getObjeto(codigo);
        
        // Recuperar la entidad del mensaje desde el servicio de persistencia
        Entidad eMensaje = servicioPersistencia.recuperarEntidad(codigo);

        // Convertir la entidad en un objeto Mensaje
        return entidadToMensaje(eMensaje);
    }

    /**
     * Recupera todos los mensajes de la base de datos.
     * 
     * @return Lista de todos los mensajes.
     * @throws ExcepcionRegistroDuplicado
     */
    public List<Mensaje> recuperarTodosMensajes()  {
        List<Mensaje> mensajes = new LinkedList<>();
        List<Entidad> eMensajes = servicioPersistencia.recuperarEntidades("Mensaje");

        for (Entidad eMensaje : eMensajes) {
            mensajes.add(recuperarMensaje(eMensaje.getId()));
        }
        return mensajes;
    }

}
