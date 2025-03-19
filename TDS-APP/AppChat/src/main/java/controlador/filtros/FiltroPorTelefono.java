package controlador.filtros;

import java.util.List;
import java.util.stream.Collectors;

import dominio.ContactoIndividual;
import dominio.Mensaje;

/**
 * Decorador que añade la funcionalidad al filtro base para filtrar por teléfono del contacto.
 */

public class FiltroPorTelefono implements FiltroBusqueda {
    private String telefono;
    private FiltroBusqueda filtroDecorado;

    public FiltroPorTelefono(FiltroBusqueda filtroDecorado, String telefono) {
        this.filtroDecorado = filtroDecorado;
        this.telefono = telefono;
    }

    @Override
    public List<Mensaje> filtrar(List<Mensaje> mensajes) {
        List<Mensaje> mensajesFiltrados = filtroDecorado.filtrar(mensajes);

        if (telefono == null || telefono.isEmpty()) {
            return mensajesFiltrados; // Si no hay teléfono, no filtra
        }

        return mensajesFiltrados.stream()
                .filter(m -> {
                    // Comparar con el teléfono del emisor
                    if (telefono.equals(m.getEmisor().getMovil())) {
                        return true;
                    }

                    // Comparar con el teléfono del receptor (si es ContactoIndividual)
                    if (m.getReceptor() instanceof ContactoIndividual) {
                        ContactoIndividual receptor = (ContactoIndividual) m.getReceptor();
                        return telefono.equals(receptor.getTelefono());
                    }

                    return false;
                })
                .collect(Collectors.toList());
    }
}