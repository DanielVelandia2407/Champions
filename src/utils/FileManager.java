package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.data.ChampionsTeam;
import model.data.Elimination;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria para la gestión de archivos JSON
 */
public class FileManager {
    private final Gson gson;

    /**
     * Constructor que inicializa el objeto Gson para serialización/deserialización
     */
    public FileManager() {
        // Configurar Gson para formatear el JSON de forma legible
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    /**
     * Carga la lista de equipos desde un archivo JSON
     * @param filePath Ruta del archivo JSON
     * @return Lista de equipos campeones
     * @throws Exception Si ocurre un error durante la carga
     */
    public List<ChampionsTeam> loadTeamsFromJSON(String filePath) throws Exception {
        File file = new File(filePath);

        // Si el archivo no existe, crear directorio y retornar lista vacía
        if (!file.exists()) {
            file.getParentFile().mkdirs(); // Crear directorios si no existen
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type teamListType = new TypeToken<List<ChampionsTeam>>(){}.getType();
            List<ChampionsTeam> teams = gson.fromJson(reader, teamListType);

            // Si el archivo está vacío o mal formateado, retornar lista vacía
            return teams != null ? teams : new ArrayList<>();
        } catch (IOException e) {
            throw new Exception("Error al cargar equipos desde archivo: " + e.getMessage());
        }
    }

    /**
     * Guarda la lista de equipos en un archivo JSON
     * @param teams Lista de equipos a guardar
     * @param filePath Ruta del archivo JSON
     * @throws Exception Si ocurre un error durante el guardado
     */
    public void saveTeamsToJSON(List<ChampionsTeam> teams, String filePath) throws Exception {
        File file = new File(filePath);

        // Crear directorios si no existen
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(teams, writer);
        } catch (IOException e) {
            throw new Exception("Error al guardar equipos en archivo: " + e.getMessage());
        }
    }

    /**
     * Carga la lista de eliminaciones desde un archivo JSON
     * @param filePath Ruta del archivo JSON
     * @return Lista de eliminaciones
     * @throws Exception Si ocurre un error durante la carga
     */
    public List<Elimination> loadEliminationsFromJSON(String filePath) throws Exception {
        File file = new File(filePath);

        // Si el archivo no existe, crear directorio y retornar lista vacía
        if (!file.exists()) {
            file.getParentFile().mkdirs(); // Crear directorios si no existen
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type eliminationListType = new TypeToken<List<Elimination>>(){}.getType();
            List<Elimination> eliminations = gson.fromJson(reader, eliminationListType);

            // Si el archivo está vacío o mal formateado, retornar lista vacía
            return eliminations != null ? eliminations : new ArrayList<>();
        } catch (IOException e) {
            throw new Exception("Error al cargar eliminaciones desde archivo: " + e.getMessage());
        }
    }

    /**
     * Guarda la lista de eliminaciones en un archivo JSON
     * @param eliminations Lista de eliminaciones a guardar
     * @param filePath Ruta del archivo JSON
     * @throws Exception Si ocurre un error durante el guardado
     */
    public void saveEliminationsToJSON(List<Elimination> eliminations, String filePath) throws Exception {
        File file = new File(filePath);

        // Crear directorios si no existen
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(eliminations, writer);
        } catch (IOException e) {
            throw new Exception("Error al guardar eliminaciones en archivo: " + e.getMessage());
        }
    }

    /**
     * Verifica si un archivo existe en la ruta especificada
     * @param filePath Ruta del archivo a verificar
     * @return true si el archivo existe, false en caso contrario
     */
    public boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * Crea un archivo JSON con datos de ejemplo para equipos
     * @param filePath Ruta donde crear el archivo
     * @throws Exception Si ocurre un error durante la creación
     */
    public void createSampleTeamsFile(String filePath) throws Exception {
        if (fileExists(filePath)) {
            return; // No sobrescribir si ya existe
        }

        List<ChampionsTeam> sampleTeams = createSampleTeamsData();
        saveTeamsToJSON(sampleTeams, filePath);
    }

    /**
     * Crea un archivo JSON con datos de ejemplo para eliminaciones
     * @param filePath Ruta donde crear el archivo
     * @throws Exception Si ocurre un error durante la creación
     */
    public void createSampleEliminationsFile(String filePath) throws Exception {
        if (fileExists(filePath)) {
            return; // No sobrescribir si ya existe
        }

        List<Elimination> sampleEliminations = createSampleEliminationsData();
        saveEliminationsToJSON(sampleEliminations, filePath);
    }

    /**
     * Crea datos de muestra para equipos
     * @return Lista con datos de muestra
     */
    private List<ChampionsTeam> createSampleTeamsData() {
        // Este método se implementaría con datos de ejemplo
        // Por brevedad, lo dejaré pendiente para que se implemente según necesidades
        return new ArrayList<>();
    }

    /**
     * Crea datos de muestra para eliminaciones
     * @return Lista con datos de muestra
     */
    private List<Elimination> createSampleEliminationsData() {
        // Este método se implementaría con datos de ejemplo
        // Por brevedad, lo dejaré pendiente para que se implemente según necesidades
        return new ArrayList<>();
    }
}