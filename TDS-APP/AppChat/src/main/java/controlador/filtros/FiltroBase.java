package controlador.filtros;

import java.util.List;

import dominio.Mensaje;

/**
 * Clase que representa un filtro base a partir del cual se crean el resto de filtros.
 * 
 */

public class FiltroBase implements FiltroBusqueda {
    @Override
    public List<Mensaje> filtrar(List<Mensaje> mensajes) {
        return mensajes; 
    }
}
