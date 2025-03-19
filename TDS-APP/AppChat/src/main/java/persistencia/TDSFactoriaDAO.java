package persistencia;

/**
 * Clase `TDSFactoriaDAO` que extiende de `FactoriaDAO`.
 * Implementa el patrón Factoría abstracta para proporcionar instancias de los adaptadores DAO
 * correspondientes a la persistencia de la aplicación.
 */
public class TDSFactoriaDAO extends FactoriaDAO {

	TDSFactoriaDAO() {
        // Constructor vacío
    }

    /**
     * Devuelve una instancia única del adaptador de usuario (`AdaptadorUsuarioDAO`).
     * 
     * @return Instancia de `AdaptadorUsuarioDAO`.
     */
    @Override
    public AdaptadorUsuarioDAO getUsuarioDAO() {
        return AdaptadorUsuarioDAO.getInstance();
    }

    /**
     * Devuelve una instancia única del adaptador de mensaje (`AdaptadorMensajeDAO`).
     * 
     * @return Instancia de `AdaptadorMensajeDAO`.
     */
    @Override
    public AdaptadorMensajeDAO getMensajeDAO() {
        return AdaptadorMensajeDAO.getInstance();
    }

    /**
     * Devuelve una instancia única del adaptador de contacto (`AdaptadorContactoDAO`).
     * 
     * @return Instancia de `AdaptadorContactoDAO`.
     */
    @Override
    public AdaptadorContactoDAO getContactoDAO() {
        return AdaptadorContactoDAO.getInstance();
    }

    /**
     * Devuelve una instancia única del adaptador de grupo (`AdaptadorGrupoDAO`).
     * 
     * @return Instancia de `AdaptadorGrupoDAO`.
     */
    @Override
    public AdaptadorGrupoDAO getGrupoDAO() {
        return AdaptadorGrupoDAO.getInstance();
    }
}
