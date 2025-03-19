package controlador.filtros;

import java.util.List;
import java.util.stream.Collectors;

import dominio.Mensaje;

/**
 * Decorador que añade la funcionalidad al filtro base para filtrar por el texto de un mensaje.
 */


public class FiltroPorTexto implements FiltroBusqueda {
    private String texto;
    private FiltroBusqueda filtroDecorado;

    public FiltroPorTexto(FiltroBusqueda filtroDecorado, String texto) {
        this.filtroDecorado = filtroDecorado;
        this.texto = texto;
    }

    @Override
    public List<Mensaje> filtrar(List<Mensaje> mensajes) {
        List<Mensaje> mensajesFiltrados = filtroDecorado.filtrar(mensajes);
        if (texto == null || texto.isEmpty()) {
            return mensajesFiltrados; // Si no hay texto, no filtra
        }
        return mensajesFiltrados.stream()
                .filter(m -> m.getTexto().contains(texto))
                .collect(Collectors.toList());
    }
}