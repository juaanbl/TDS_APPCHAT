package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import dominio.Grupo;
import excepciones.ExcepcionDAO;
import excepciones.ExcepcionRegistroDuplicado;
import persistencia.interfaces.IAdaptadorGrupoDAO;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

/**
 * Adaptador para convertir entre el objeto de dominio Grupo y su 
 * representación en la base de datos utilizando un servicio de persistencia.
 */
public class AdaptadorGrupoDAO implements IAdaptadorGrupoDAO{

    private static AdaptadorGrupoDAO adaptadorGrupo;
    private ServicioPersistencia servicioPersistencia;
    private AdaptadorUsuarioDAO adaptadorUsuario;

    /**
     * Obtiene la instancia única del adaptador (patrón Singleton).
     */
    public static AdaptadorGrupoDAO getInstance() {
        if (adaptadorGrupo == null) {
            adaptadorGrupo = new AdaptadorGrupoDAO();
        }
        return adaptadorGrupo;
    }

    /**
     * Constructor que inicializa el servicio de persistencia y los adaptadores de usuario y contacto.
     */
    private AdaptadorGrupoDAO() {
        servicioPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
        try {
            adaptadorUsuario = TDSFactoriaDAO.getInstance().getUsuarioDAO();
        } catch (ExcepcionDAO e) {
            e.printStackTrace();
        }
    }
    

    /**
     * Convierte un Grupo a una Entidad.
     */
    private Entidad grupoToEntidad(Grupo grupo) {
        Entidad eGrupo = new Entidad();
        eGrupo.setNombre("Grupo");
        eGrupo.setPropiedades(new ArrayList<>(Arrays.asList(
            new Propiedad("nombre", grupo.getNombre()),
            new Propiedad("imagen", grupo.getImagen()),
            new Propiedad("propietario", String.valueOf(grupo.getPropietario().getCodigo())),
            new Propiedad("miembros", PersistenciaUtils.getCodigosContactos(grupo.getMiembros())),
            new Propiedad("mensajes", PersistenciaUtils.obtenerCodigosMensajes(grupo.getMensajes()))
        )));
        return eGrupo;
    }

    /**
     * Convierte una Entidad a un Grupo.
     */
    private Grupo entidadToGrupo(Entidad eGrupo) {
        Grupo grupo = new Grupo();
        grupo.setCodigo(eGrupo.getId());
        grupo.setNombre(servicioPersistencia.recuperarPropiedadEntidad(eGrupo, "nombre"));
        grupo.setImagen(servicioPersistencia.recuperarPropiedadEntidad(eGrupo, "imagen"));
        
        // Se añade el objeto al pool para evitar bucles a la hora de la recuperación de los objetos.
        PoolDAO.getInstance().addObjeto(grupo.getCodigo(), grupo);
        
        grupo.setPropietario(adaptadorUsuario.recuperarUsuario(Integer.parseInt(servicioPersistencia.recuperarPropiedadEntidad(eGrupo, "propietario"))));
        grupo.setMiembros(PersistenciaUtils.getListaMiembrosDesdeCodigos(servicioPersistencia.recuperarPropiedadEntidad(eGrupo, "miembros")));
        grupo.setMensajes(PersistenciaUtils.obtenerMensajesDesdeCódigos(servicioPersistencia.recuperarPropiedadEntidad(eGrupo, "mensajes")));
        return grupo;
    }

    /**
     * Registra un nuevo grupo en la base de datos.
     * 
     * @param nuevoGrupo Grupo a registrar.
     * @throws ExcepcionRegistroDuplicado Si el grupo ya está registrado.
     */
    public void registrarGrupo(Grupo nuevoGrupo) throws ExcepcionRegistroDuplicado {
        Entidad eGrupo = null;
        boolean noRegistrar = false;

        try {
            eGrupo = servicioPersistencia.recuperarEntidad(nuevoGrupo.getCodigo());
        } catch (NullPointerException e) {
            noRegistrar = true;
        }

        if (noRegistrar) throw new ExcepcionRegistroDuplicado("Entidad ya registrada");

        // Convertir el grupo a entidad y registrar
        eGrupo = grupoToEntidad(nuevoGrupo);
        eGrupo = servicioPersistencia.registrarEntidad(eGrupo);

        // Asignar el identificador único generado por el servicio de persistencia al grupo
        nuevoGrupo.setCodigo(eGrupo.getId());

        // Guardamos en el Pool
        PoolDAO.getInstance().addObjeto(nuevoGrupo.getCodigo(), nuevoGrupo);
    }

    /**
     * Recupera un grupo desde la base de datos dado su código.
     * 
     * @param codigo Código del grupo a recuperar.
     * @return El grupo correspondiente al código.
     */
    public Grupo recuperarGrupo(int codigo) {
        // Si la entidad está en el pool, la devolvemos directamente
        if (PoolDAO.getInstance().contiene(codigo)) return (Grupo) PoolDAO.getInstance().getObjeto(codigo);

        // Recuperar la entidad del grupo desde el servicio de persistencia
        Entidad eGrupo = servicioPersistencia.recuperarEntidad(codigo);

        // Convertir la entidad en un objeto Grupo
        return entidadToGrupo(eGrupo);
    }

    /**
     * Modifica las propiedades de un grupo guardado en la base de datos.
     * 
     * @param grupoModificar Grupo a modificar.
     * @throws ExcepcionRegistroDuplicado Si ocurre un error al modificar.
     */
    public void modificarGrupo(Grupo grupoModificar) {
        // Recuperar la entidad del grupo desde el servicio de persistencia
        Entidad eGrupo = servicioPersistencia.recuperarEntidad(grupoModificar.getCodigo());

        // Actualizar las propiedades del grupo
        for (Propiedad prop : eGrupo.getPropiedades()) {
            if (prop.getNombre().equals("imagen")) {
                prop.setValor(grupoModificar.getImagen());
            } else if (prop.getNombre().equals("miembros")) {
                prop.setValor(PersistenciaUtils.getCodigosContactos(grupoModificar.getMiembros()));
            } else if (prop.getNombre().equals("propietario")) {
                prop.setValor(String.valueOf(grupoModificar.getPropietario().getCodigo()));
            } else if (prop.getNombre().equals("nombre")) {
                prop.setValor(grupoModificar.getNombre());
            } else if (prop.getNombre().equals("mensajes")) {
                prop.setValor(PersistenciaUtils.obtenerCodigosMensajes(grupoModificar.getMensajes()));
            }

            // Modificar la propiedad en la base de datos
            servicioPersistencia.modificarPropiedad(prop);
        }
    }

    /**
     * Recupera todos los grupos de la base de datos.
     * 
     * @return Lista de todos los grupos.
     */
    public List<Grupo> recuperarTodosGrupos() {
        List<Grupo> grupos = new ArrayList<>();
        List<Entidad> eGrupos = servicioPersistencia.recuperarEntidades("Grupo");

        for (Entidad eGrupo : eGrupos) {
            grupos.add(recuperarGrupo(eGrupo.getId()));
        }

        return grupos;
    }
}
