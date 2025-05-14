package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.data.ChampionsTeam;
import model.data.Title;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        List<ChampionsTeam> teams = new ArrayList<>();

        try {
            String content;

            // Primero, intentar cargar como recurso del classpath
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);

            if (inputStream != null) {
                // Es un recurso en el classpath
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    content = sb.toString();
                }
                System.out.println("Archivo cargado desde el classpath: " + filePath);
            } else {
                // Intentar cargar como archivo del sistema
                File file = new File(filePath);
                if (!file.exists()) {
                    throw new FileNotFoundException("El archivo no existe ni como recurso ni como archivo: " + filePath);
                }
                content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                System.out.println("Archivo cargado desde el sistema de archivos: " + file.getAbsolutePath());
            }

            System.out.println("Contenido del archivo leído: " + (content.length() > 100 ? content.substring(0, 100) + "..." : content));

            // Usar Gson para parsear el JSON
            Type listType = new TypeToken<ArrayList<ChampionsTeam>>(){}.getType();
            teams = gson.fromJson(content, listType);

            System.out.println("Equipos parseados del JSON: " + teams.size());
            for (ChampionsTeam team : teams) {
                System.out.println("Equipo: " + team.getName() + ", Títulos: " + team.getTitles().size());
            }
        } catch (Exception e) {
            System.err.println("Error en loadTeamsFromJSON: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return teams;
    }

    /**
     * Guarda la lista de equipos en un archivo JSON
     * @param teams Lista de equipos a guardar
     * @param filePath Ruta del archivo JSON
     * @throws Exception Si ocurre un error durante el guardado
     */
    public void saveTeamsToJSON(List<ChampionsTeam> teams, String filePath) throws Exception {
        try {
            // Asegurarse de que los directorios existan
            File file = new File(filePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // Escribir el archivo JSON
            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(teams, writer);
                System.out.println("Datos guardados en: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new Exception("Error al guardar equipos en archivo: " + e.getMessage(), e);
        }
    }

    /* Eliminar o modificar los métodos relacionados con Elimination si no existe esta clase
    public List<Elimination> loadEliminationsFromJSON(String resourcePath) throws Exception {
        // ...
    }

    public void saveEliminationsToJSON(List<Elimination> eliminations, String resourcePath) throws Exception {
        // ...
    }

    public void createSampleEliminationsFile(String resourcePath) throws Exception {
        // ...
    }

    private List<Elimination> createSampleEliminationsData() {
        // ...
    }
    */

    /**
     * Verifica si un archivo existe
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
     * Crea datos de muestra para equipos
     * @return Lista con datos de muestra
     */
    private List<ChampionsTeam> createSampleTeamsData() {
        // Implementar datos de ejemplo si es necesario
        return new ArrayList<>();
    }
}