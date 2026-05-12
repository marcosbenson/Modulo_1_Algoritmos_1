package Home;

import java.util.ArrayList;
import java.util.List;

/**
 * Centraliza la logica de estadisticas.
 * Usa un RepositorioEstadisticas para guardar y cargar datos.
 */
public class GestorEstadisticas {

    private RepositorioEstadisticas repositorio;

    public GestorEstadisticas(RepositorioEstadisticas repositorio) {
        this.repositorio = repositorio;
    }

    public void guardarEstadisticas(EstadisticasGenerales stats) {
        try {
            repositorio.guardar(stats.getNombreModulo(), stats);
        } catch (PersistenciaException e) {
            System.err.println("Error al guardar estadisticas: " + e.getMessage());
        }
    }

    public EstadisticasGenerales obtenerPorModulo(String nombreModulo) {
        try {
            return repositorio.cargar(nombreModulo);
        } catch (PersistenciaException e) {
            System.err.println("Error al cargar estadisticas: " + e.getMessage());
            return null;
        }
    }

    public List<EstadisticasGenerales> obtenerTodas() {
        try {
            return repositorio.cargarTodas();
        } catch (PersistenciaException e) {
            System.err.println("Error al cargar estadisticas: " + e.getMessage());
            return new ArrayList<EstadisticasGenerales>();
        }
    }

    public int calcularPuntajeTotalCurso() {
        int total = 0;
        List<EstadisticasGenerales> todas = obtenerTodas();
        for (EstadisticasGenerales stats : todas) {
            total = total + stats.getPuntajeTotal();
        }
        return total;
    }
}
