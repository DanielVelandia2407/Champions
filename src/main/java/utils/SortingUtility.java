package utils;

import model.data.Elimination;
import java.util.Comparator;
import java.util.List;

/**
 * Utilidad para ordenar colecciones relacionadas con las eliminaciones.
 */
public class SortingUtility {

    /**
     * Ordena las eliminaciones por temporada (más reciente primero).
     */
    public static Comparator<Elimination> bySeasonDescending = (e1, e2) -> {
        // Asumiendo que la temporada tiene formato "YYYY-YYYY"
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
        return getPhaseWeight(e2.getPhase()) - getPhaseWeight(e1.getPhase());
    };

    /**
     * Asigna un peso numérico a cada fase para ordenarlas por importancia.
     */
    private static int getPhaseWeight(String phase) {
        phase = phase.toLowerCase();
        if (phase.contains("final")) return 10;
        if (phase.contains("semi")) return 9;
        if (phase.contains("cuartos")) return 8;
        if (phase.contains("octavos")) return 7;
        if (phase.contains("dieciseisavos")) return 6;
        if (phase.contains("grupo")) return 5;
        return 0; // Fase desconocida
    }

    /**
     * Aplica múltiples criterios de ordenación a una lista de eliminaciones.
     */
    public static void sortByMultipleCriteria(List<Elimination> eliminations, int criteriaOption) {
        switch (criteriaOption) {
            case 1: // Más reciente primero
                eliminations.sort(bySeasonDescending);
                break;
            case 2: // Más antigua primero
                eliminations.sort(bySeasonAscending);
                break;
            case 3: // Por fase más importante
                eliminations.sort(byPhaseImportance);
                break;
            case 4: // Por fase y luego temporada reciente
                eliminations.sort(byPhaseImportance.thenComparing(bySeasonDescending));
                break;
            default:
                eliminations.sort(bySeasonDescending); // Ordenamiento por defecto
        }
    }
}
