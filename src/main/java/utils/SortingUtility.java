package utils;

import model.data.Elimination;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilidad para ordenar colecciones relacionadas con las eliminaciones.
 */
public class SortingUtility {

    /**
     * Mapa para asignar pesos a cada fase para ordenar por importancia.
     */
    private static final Map<String, Integer> PHASE_WEIGHTS = new HashMap<>();

    // Inicializar el mapa de pesos de fases
    static {
        PHASE_WEIGHTS.put("final", 10);
        PHASE_WEIGHTS.put("semifinal", 9);
        PHASE_WEIGHTS.put("cuartos de final", 8);
        PHASE_WEIGHTS.put("octavos de final", 7);
        PHASE_WEIGHTS.put("dieciseisavos de final", 6);
        PHASE_WEIGHTS.put("fase de grupos", 5);
        PHASE_WEIGHTS.put("ronda preliminar", 4);
        PHASE_WEIGHTS.put("clasificación", 3);
    }

    /**
     * Ordena las eliminaciones por temporada (más reciente primero).
     */
    public static Comparator<Elimination> bySeasonDescending = (e1, e2) -> {
        try {
            int season1 = Integer.parseInt(e1.getSeason().split("-")[0]);
            int season2 = Integer.parseInt(e2.getSeason().split("-")[0]);
            return season2 - season1; // Orden descendente
        } catch (Exception e) {
            // En caso de error, comparar strings
            return e2.getSeason().compareTo(e1.getSeason());
        }
    };

    /**
     * Ordena las eliminaciones por temporada (más antigua primero).
     */
    public static Comparator<Elimination> bySeasonAscending = (e1, e2) -> {
        // Asumiendo que la temporada tiene formato "YYYY-YYYY"
        try {
            int season1 = Integer.parseInt(e1.getSeason().split("-")[0]);
            int season2 = Integer.parseInt(e2.getSeason().split("-")[0]);
            return season1 - season2; // Orden ascendente
        } catch (Exception e) {
            // En caso de error, comparar strings
            return e1.getSeason().compareTo(e2.getSeason());
        }
    };

    /**
     * Ordena las eliminaciones por fase (más avanzada primero: Final, Semifinal, etc.).
     */
    public static Comparator<Elimination> byPhaseImportance = (e1, e2) -> {
        int weight1 = getPhaseWeight(e1.getPhase());
        int weight2 = getPhaseWeight(e2.getPhase());

        if (weight1 == weight2) {
            // Si los pesos son iguales, ordenar alfabéticamente por nombre de fase
            return e1.getPhase().compareToIgnoreCase(e2.getPhase());
        }

        return weight2 - weight1; // Orden descendente por importancia
    };

    /**
     * Asigna un peso numérico a cada fase para ordenarlas por importancia.
     * Este método mejorado busca coincidencias parciales en los nombres de fase.
     */
    private static int getPhaseWeight(String phase) {
        if (phase == null) return 0;

        String phaseLower = phase.toLowerCase();

        // Buscar coincidencias exactas primero
        if (PHASE_WEIGHTS.containsKey(phaseLower)) {
            return PHASE_WEIGHTS.get(phaseLower);
        }

        // Buscar coincidencias parciales
        for (Map.Entry<String, Integer> entry : PHASE_WEIGHTS.entrySet()) {
            if (phaseLower.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        // Para depuración: imprimir fases no reconocidas
        System.out.println("Fase no reconocida: " + phase);

        return 0; // Fase desconocida
    }

    /**
     * Aplica múltiples criterios de ordenación a una lista de eliminaciones.
     */
    public static void sortByMultipleCriteria(List<Elimination> eliminations, int criteriaOption) {
        switch (criteriaOption) {
            case 0: // Más recientes primero
                eliminations.sort(bySeasonDescending);
                break;
            case 1: // Más antiguas primero
                eliminations.sort(bySeasonAscending);
                break;
            case 2: // Por fase
                eliminations.sort(byPhaseImportance);
                break;
            case 3: // Por fase y temporada
                eliminations.sort(byPhaseImportance.thenComparing(bySeasonDescending));
                break;
            default:
                eliminations.sort(bySeasonDescending); // Ordenamiento por defecto
        }

        // Imprimir la lista ordenada para depuración
        System.out.println("Lista ordenada (criterio " + criteriaOption + "):");
        for (Elimination e : eliminations) {
            System.out.println("Fase: " + e.getPhase() + ", Temporada: " + e.getSeason());
        }
    }
}