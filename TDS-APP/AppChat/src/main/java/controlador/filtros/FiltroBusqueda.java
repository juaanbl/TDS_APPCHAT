package controlador.filtros;

import java.util.List;

import dominio.Mensaje;

/**
 * Interfaz que deben implementan los filtros.
 */

public interface FiltroBusqueda {
    List<Mensaje> filtrar(List<Mensaje> mensajes);
}
