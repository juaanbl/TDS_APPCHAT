package persistencia;

import java.util.HashMap;

/**
 * Clase `PoolDAO` que implementa un pool de objetos para adaptadores que lo requieran.
 * Utiliza el patrón Singleton para garantizar que solo exista una instancia única.
 */
public class PoolDAO {

    private static PoolDAO pool;

    private HashMap<Integer, Object> poolDAO;

    private PoolDAO() {
        poolDAO = new HashMap<Integer, Object>();
    }

    /**
     * Método estático que devuelve la instancia única del pool (Singleton).
     * Si no existe, se crea.
     * 
     * @return Instancia única de `PoolDAO`.
     */
    public static PoolDAO getInstance() {
        if (pool == null) {
            pool = new PoolDAO();
        }
        return pool;
    }

    /**
     * Obtiene un objeto del pool dado su identificador.
     * Devuelve `null` si no se encuentra el objeto.
     * 
     * @param id Identificador único del objeto.
     * @return El objeto asociado al identificador o `null` si no existe.
     */
    public Object getObjeto(int id) {
        return poolDAO.get(id);
    }

    /**
     * Agrega un nuevo objeto al pool, asociándolo con un identificador único.
     * Si el identificador ya existe, el objeto se sobrescribe.
     * 
     * @param id     Identificador único del objeto.
     * @param objeto Objeto a almacenar en el pool.
     */
    public void addObjeto(int id, Object objeto) {
        poolDAO.put(id, objeto);
    }

    /**
     * Comprueba si un objeto con el identificador especificado existe en el pool.
     * 
     * @param id Identificador único del objeto.
     * @return `true` si el objeto está en el pool, `false` en caso contrario.
     */
    public boolean contiene(int id) {
        return poolDAO.containsKey(id);
    }
}
