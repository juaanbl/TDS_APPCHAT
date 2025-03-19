package controlador.filtros;

import java.util.List;
import java.util.stream.Collectors;

import dominio.Mensaje;

/**
 * Decorador que añade la funcionalidad al filtro base para filtrar por nombre de contacto.
 */

public class FiltroPorNombre implements FiltroBusqueda {
    private String nombre;
    private FiltroBusqueda filtroDecorado;

    public FiltroPorNombre(FiltroBusqueda filtroDecorado, String nombre) {
        this.filtroDecorado = filtroDecorado;
        this.nombre = nombre;
    }

    @Override
    public List<Mensaje> filtrar(List<Mensaje> mensajes) {
        List<Mensaje> mensajesFiltrados = filtroDecorado.filtrar(mensajes);
        if (nombre == null || nombre.isEmpty()) {
            return mensajesFiltrados; // Si no hay nombre, no filtra
        }
        return mensajesFiltrados.stream()
                .filter(m -> m.getEmisor().getNombreCompleto().equals(nombre) ||
                             m.getReceptor().getNombre().equals(nombre))
                .collect(Collectors.toList());
    }
}