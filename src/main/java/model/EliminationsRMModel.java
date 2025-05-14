package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.data.Elimination;
import utils.SortingUtility;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Modelo para gestionar las eliminaciones del Real Madrid.
 * Implementa una estructura de pila (Stack) para mantener las eliminaciones.
 */
public class EliminationsRMModel {
    private Stack<Elimination> eliminationsStack;
    private String dataFilePath;
    private final Gson gson;

    /**
     * Constructor del modelo.
     */
    public EliminationsRMModel() {
        this.eliminationsStack = new Stack<>();
        this.dataFilePath = "src/main/resources/data/eliminaciones.json";
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Establece la ruta del archivo de datos.
     */
    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    /**
     * Carga las eliminaciones desde el archivo JSON.
     */
    public void loadEliminationsFromFile() throws IOException {
        try (Reader reader = new FileReader(dataFilePath)) {
            Type listType = new TypeToken<ArrayList<Elimination>>(){}.getType();
            List<Elimination> eliminationsList = gson.fromJson(reader, listType);

            // Si el archivo está vacío o no existe, inicializar con lista vacía
            if (eliminationsList == null) {
                eliminationsList = new ArrayList<>();
            }

            // Convertir lista a pila
            eliminationsStack = new Stack<>();
            for (Elimination e : eliminationsList) {
                eliminationsStack.push(e);
            }
        } catch (FileNotFoundException e) {
            // Si el archivo no existe, crear uno nuevo con datos de ejemplo
            createDefaultEliminationsFile();
            loadEliminationsFromFile(); // Intentar cargar nuevamente
        }
    }

    /**
     * Guarda las eliminaciones en el archivo JSON.
     */
    public void saveEliminationsToFile() throws IOException {
        // Convertir la pila a una lista para facilitar la serialización
        List<Elimination> eliminationsList = new ArrayList<>(eliminationsStack);

        try (Writer writer = new FileWriter(dataFilePath)) {
            gson.toJson(eliminationsList, writer);
        }
    }

    /**
     * Crea un archivo JSON con datos de ejemplo si no existe.
     */
    private void createDefaultEliminationsFile() throws IOException {
        File file = new File(dataFilePath);
        file.getParentFile().mkdirs(); // Crear directorios si no existen

        List<Elimination> defaultEliminations = Arrays.asList(
                new Elimination(
                        "2022-2023",
                        "Semifinal",
                        "Manchester City",
                        "1-1",
                        "0-4",
                        "El Real Madrid cayó eliminado en semifinales ante el Manchester City tras un contundente 4-0 en el Etihad Stadium.",
                        "images/eliminations/city_2023.jpg"
                ),
                new Elimination(
                        "2019-2020",
                        "Octavos de final",
                        "Manchester City",
                        "1-2",
                        "1-2",
                        "El equipo blanco fue eliminado en octavos por el Manchester City de Guardiola, con un global de 2-4.",
                        "images/eliminations/city_2020.jpg"
                ),
                new Elimination(
                        "2018-2019",
                        "Octavos de final",
                        "Ajax",
                        "2-1",
                        "1-4",
                        "Histórica remontada del Ajax en el Bernabéu, ganando 1-4 y eliminando al tricampeón de Europa.",
                        "images/eliminations/ajax_2019.jpg"
                )
        );

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(defaultEliminations, writer);
        }
    }

    /**
     * Obtiene todas las eliminaciones como una lista.
     */
    public List<Elimination> getAllEliminations() {
        return new ArrayList<>(eliminationsStack);
    }

    /**
     * Agrega una nueva eliminación a la pila.
     */
    public void pushElimination(Elimination elimination) {
        eliminationsStack.push(elimination);
    }

    /**
     * Elimina y devuelve la eliminación más reciente de la pila.
     */
    public Elimination popElimination() {
        if (!eliminationsStack.isEmpty()) {
            return eliminationsStack.pop();
        }
        return null;
    }

    /**
     * Consulta la eliminación más reciente sin eliminarla.
     */
    public Elimination peekElimination() {
        if (!eliminationsStack.isEmpty()) {
            return eliminationsStack.peek();
        }
        return null;
    }

    /**
     * Devuelve el tamaño de la pila de eliminaciones.
     */
    public int getEliminationsCount() {
        return eliminationsStack.size();
    }

    /**
     * Busca eliminaciones por temporada, fase o rival.
     */
    public List<Elimination> searchEliminations(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return new ArrayList<>(eliminationsStack);
        }

        searchTerm = searchTerm.toLowerCase().trim();
        List<Elimination> results = new ArrayList<>();

        for (Elimination e : eliminationsStack) {
            if (e.getSeason().toLowerCase().contains(searchTerm) ||
                    e.getPhase().toLowerCase().contains(searchTerm) ||
                    e.getOpponent().toLowerCase().contains(searchTerm) ||
                    e.getDescription().toLowerCase().contains(searchTerm)) {
                results.add(e);
            }
        }

        return results;
    }

    /**
     * Ordena las eliminaciones según el criterio especificado.
     */
    public List<Elimination> getSortedEliminations(int sortCriteria) {
        List<Elimination> sortedList = new ArrayList<>(eliminationsStack);
        SortingUtility.sortByMultipleCriteria(sortedList, sortCriteria);
        return sortedList;
    }

    /**
     * Elimina una eliminación específica.
     */
    public boolean removeElimination(Elimination elimination) {
        return eliminationsStack.remove(elimination);
    }

    /**
     * Actualiza una eliminación existente.
     */
    public boolean updateElimination(Elimination oldElimination, Elimination newElimination) {
        // Creamos una lista temporal para poder modificar la pila
        List<Elimination> tempList = new ArrayList<>(eliminationsStack);

        int index = tempList.indexOf(oldElimination);
        if (index >= 0) {
            tempList.set(index, newElimination);
            // Reconstruir la pila con la lista actualizada
            eliminationsStack.clear();
            for (Elimination e : tempList) {
                eliminationsStack.push(e);
            }
            return true;
        }

        return false;
    }
}
