package persistencia;
// COMENTAR
import excepciones.ExcepcionDAO;

public abstract class FactoriaDAO {
	
private static FactoriaDAO unicaInstancia;
	
	public static final String DAO_TDS = "persistencia.TDSFactoriaDAO";
		
	/** 
	 * Crea un tipo de factoria DAO.
	 * Solo existe el tipo TDSFactoriaDAO
	 */
	
	@SuppressWarnings("deprecation")
	public static FactoriaDAO getInstance(String tipo) throws ExcepcionDAO {
		if (unicaInstancia == null)
			try { unicaInstancia=(FactoriaDAO) Class.forName(tipo).newInstance();
			} catch (Exception e) {	
				throw new ExcepcionDAO(e.getMessage());
			} 
		return unicaInstancia;
	}
	
	/*
	 * Patron Singleton
	 */

	public static FactoriaDAO getInstance() throws ExcepcionDAO{
			if (unicaInstancia == null) return getInstance (FactoriaDAO.DAO_TDS);
					else return unicaInstancia;
		}

	/* Constructor */
	protected FactoriaDAO (){}
		
		
	// Metodos factoria que devuelven adaptadores que implementen estos interfaces
	public abstract AdaptadorContactoDAO getContactoDAO();
	public abstract AdaptadorUsuarioDAO getUsuarioDAO();
	public abstract AdaptadorMensajeDAO getMensajeDAO();
	public abstract AdaptadorGrupoDAO getGrupoDAO();

}
