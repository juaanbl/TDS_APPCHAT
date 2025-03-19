package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import dominio.ContactoIndividual;
import excepciones.ExcepcionDAO;
import excepciones.ExcepcionRegistroDuplicado;
import persistencia.interfaces.IAdaptadorContactoDAO;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

/**
 * Clase AdaptadorContactoDAO
 * 
 * Esta clase actúa como un puente entre los objetos de dominio ContactoIndividual
 * y su representación en la base de datos, utilizando un servicio de persistencia.
 * Implementa los métodos necesarios para registrar, recuperar y gestionar contactos
 * individuales, asegurando la consistencia entre el modelo de datos y el sistema persistente.
 */

public class AdaptadorContactoDAO implements IAdaptadorContactoDAO {

    /**
     * Instancia única de la clase (implementación Singleton).
     */
    public static AdaptadorContactoDAO adaptadorContacto;

    /**
     * Servicio de persistencia utilizado para interactuar con la base de datos.
     */
    private ServicioPersistencia servicioPersistencia;

    /**
     * Obtiene la instancia única de la clase.
     * 
     * @return Instancia de AdaptadorContactoDAO.
     */
    public static AdaptadorContactoDAO getInstance() {
        if (adaptadorContacto == null) {
            adaptadorContacto = new AdaptadorContactoDAO();
        }
        return adaptadorContacto;
    }

    /**
     * Constructor privado para implementar el patrón Singleton.
     * Inicializa el servicio de persistencia.
     */
    private AdaptadorContactoDAO() {
        servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
    }

    /**
     * Método para convertir de ContactoIndividual a Entidad
     */
    private Entidad contactoToEntidad(ContactoIndividual contacto) {
        Entidad eContacto = new Entidad();
        eContacto.setNombre("ContactoIndividual");
        eContacto.setPropiedades(new ArrayList<>(Arrays.asList(
                new Propiedad("nombre", contacto.getNombre()),
                new Propiedad("telefono", contacto.getTelefono()),
                new Propiedad("mensajes", PersistenciaUtils.obtenerCodigosMensajes(contacto.getMensajes())), 
                new Propiedad("usuario", String.valueOf(contacto.getUsuario().getCodigo()))
        )));
        return eContacto;
    }

    /**
     * Método para convertir de Entidad a ContactoIndividual
     */
    private ContactoIndividual entidadToContacto(Entidad eContacto) {
        ContactoIndividual contacto = new ContactoIndividual();
        contacto.setCodigo(eContacto.getId());
        contacto.setNombre(servicioPersistencia.recuperarPropiedadEntidad(eContacto, "nombre"));
        contacto.setTelefono(servicioPersistencia.recuperarPropiedadEntidad(eContacto, "telefono")); 
        // Se añade el objeto al pool para evitar bucles a la hora de la recuperación de los objetos.
        PoolDAO.getInstance().addObjeto(contacto.getCodigo(), contacto);
        contacto.setMensajes(PersistenciaUtils.obtenerMensajesDesdeCódigos(servicioPersistencia.recuperarPropiedadEntidad(eContacto, "mensajes")));              
        contacto.setUsuario(PersistenciaUtils.getUsuarioDesdeCodigo(servicioPersistencia.recuperarPropiedadEntidad(eContacto, "usuario")));
        return contacto;
    }

    /**
     * Registra un nuevo contacto individual en la base de datos.
     * 
     * @param nuevoContacto ContactoIndividual a registrar.
     * @throws ExcepcionRegistroDuplicado Si el contacto ya está registrado.
     */
    public void registrarContacto(ContactoIndividual nuevoContacto) throws ExcepcionRegistroDuplicado {
        Entidad eContacto = null;
        boolean noRegistrar = false;

        try {
            eContacto = servicioPersistencia.recuperarEntidad(nuevoContacto.getCodigo());
        } catch (NullPointerException e) {
            noRegistrar = true;
        }

        if (noRegistrar) throw new ExcepcionRegistroDuplicado("Entidad ya registrada");

        // Crear la entidad para el nuevo contacto
        eContacto = contactoToEntidad(nuevoContacto);

        // Registrar la entidad en el servicio de persistencia
        eContacto = servicioPersistencia.registrarEntidad(eContacto);

        // Asignar el identificador único generado por el servicio de persistencia al contacto
        nuevoContacto.setCodigo(eContacto.getId());

        // Guardamos en el Pool
        PoolDAO.getInstance().addObjeto(nuevoContacto.getCodigo(), nuevoContacto);
    }
    
    /**
     * 
     * 
     */
    
    public void modificarContacto(ContactoIndividual contactoModificar) {
    	
    	// Recuperar la entidad del usuario desde el servicio de persistencia
        Entidad eContacto = servicioPersistencia.recuperarEntidad(contactoModificar.getCodigo());
    	
        // Iterar sobre las propiedades del contacto
        for (Propiedad prop : eContacto.getPropiedades()) {
            if (prop.getNombre().equals("nombre")) {
                prop.setValor(contactoModificar.getNombre());
            } else if (prop.getNombre().equals("mensajes")) {
                prop.setValor(PersistenciaUtils.obtenerCodigosMensajes(contactoModificar.getMensajes()));
            } else if (prop.getNombre().equals("telefono")) {          
            	prop.setValor(contactoModificar.getTelefono());
            } else if (prop.getNombre().equals("usuario")) {
                prop.setValor(String.valueOf(contactoModificar.getUsuario().getCodigo())); 
            }                               

            // Modificar la propiedad en la base de datos
            servicioPersistencia.modificarPropiedad(prop);
        }
    }


    /**
     * Recupera un contacto individual desde la base de datos dado su código.
     *
     * @param codigo Código del contacto individual.
     * @return ContactoIndividual correspondiente al código.
     * @throws ExcepcionDAO
     */
    public ContactoIndividual recuperarContacto(int codigo) {
        // Si la entidad está en el pool, la devuelve directamente
        if (PoolDAO.getInstance().contiene(codigo)) return (ContactoIndividual) PoolDAO.getInstance().getObjeto(codigo);

        // Recuperar la entidad del contacto desde el servicio de persistencia
        Entidad eContacto = servicioPersistencia.recuperarEntidad(codigo);

        // Convertir la entidad en un objeto ContactoIndividual
        ContactoIndividual contacto = entidadToContacto(eContacto);
        contacto.setCodigo(codigo);
        return contacto;
    }

    /**
     * Recupera todos los contactos de la base de datos.
     *
     * @return Lista de todos los contactos.
     */
    public List<ContactoIndividual> recuperarTodosContactos() {
        List<ContactoIndividual> contactos = new ArrayList<>();
        List<Entidad> eContacts = servicioPersistencia.recuperarEntidades("ContactoIndividual");

        for (Entidad eContact : eContacts) {
            contactos.add(recuperarContacto(eContact.getId()));
        }

        return contactos;
    }
}
