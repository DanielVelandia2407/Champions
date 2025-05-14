package model;

import model.data.ChampionsTeam;
import model.data.Title;
import model.structures.CircularDoublyLinkedList;
import utils.FileManager;

import java.util.ArrayList;
import java.util.List;

public class ChampionsTitlesModel {
    private CircularDoublyLinkedList<ChampionsTeam> teamsList;
    private FileManager fileManager;
    private static final String DEFAULT_FILE_PATH = "data/champions.json";
    private String currentFilePath;

    public ChampionsTitlesModel() {
        this.teamsList = new CircularDoublyLinkedList<>();
        this.fileManager = new FileManager();
        this.currentFilePath = DEFAULT_FILE_PATH;
    }

    /**
     * Carga los equipos desde el archivo JSON predeterminado
     * @throws Exception Si ocurre un error durante la carga
     */
    public void loadTeamsFromFile() throws Exception {
        List<ChampionsTeam> teams = fileManager.loadTeamsFromJSON(currentFilePath);
        teamsList.clear();

        // Agregar equipos a la lista circular doblemente enlazada
        for (ChampionsTeam team : teams) {
            teamsList.add(team);
        }

        // Ordenar por número de títulos (mayor a menor) por defecto
        sortTeams(0);
    }

    /**
     * Carga los equipos desde un archivo JSON específico
     * @param filePath Ruta del archivo JSON
     * @throws Exception Si ocurre un error durante la carga
     */
    public void loadTeamsFromFile(String filePath) throws Exception {
        List<ChampionsTeam> teams = fileManager.loadTeamsFromJSON(filePath);
        teamsList.clear();

        // Agregar equipos a la lista circular doblemente enlazada
        for (ChampionsTeam team : teams) {
            teamsList.add(team);
        }

        // Actualizar la ruta del archivo actual
        this.currentFilePath = filePath;

        // Ordenar por número de títulos (mayor a menor) por defecto
        sortTeams(0);
    }

    /**
     * Guarda los equipos en el archivo JSON actual
     * @throws Exception Si ocurre un error durante el guardado
     */
    public void saveTeamsToFile() throws Exception {
        List<ChampionsTeam> teams = getAllTeams();
        fileManager.saveTeamsToJSON(teams, currentFilePath);  // Usar currentFilePath en lugar de FILE_PATH
    }

    /**
     * Guarda los equipos en un archivo JSON específico
     * @param filePath Ruta del archivo JSON
     * @throws Exception Si ocurre un error durante el guardado
     */
    public void saveTeamsToFile(String filePath) throws Exception {
        List<ChampionsTeam> teams = getAllTeams();
        fileManager.saveTeamsToJSON(teams, filePath);
        this.currentFilePath = filePath; // Actualizar la ruta actual
    }

    /**
     * Obtiene la ruta del archivo actual
     * @return Ruta del archivo JSON actual
     */
    public String getCurrentFilePath() {
        return currentFilePath;
    }

    /**
     * Obtiene todos los equipos en la lista
     * @return Lista de equipos campeones
     */
    public List<ChampionsTeam> getAllTeams() {
        return teamsList.toList();
    }

    /**
     * Agrega un nuevo equipo a la lista
     * @param team Equipo a agregar
     */
    public void addTeam(ChampionsTeam team) {
        teamsList.add(team);
        sortTeams(0); // Reordenar por número de títulos
    }

    /**
     * Actualiza un equipo existente
     * @param team Equipo con los datos actualizados
     */
    public void updateTeam(ChampionsTeam team) {
        teamsList.update(team);
        sortTeams(0); // Reordenar por número de títulos
    }

    /**
     * Elimina un equipo de la lista
     * @param team Equipo a eliminar
     */
    public void removeTeam(ChampionsTeam team) {
        teamsList.remove(team);
    }

    /**
     * Busca equipos por nombre
     * @param searchTerm Término de búsqueda
     * @return Lista de equipos que coinciden con la búsqueda
     */
    public List<ChampionsTeam> searchTeams(String searchTerm) {
        List<ChampionsTeam> result = new ArrayList<>();
        List<ChampionsTeam> allTeams = getAllTeams();

        for (ChampionsTeam team : allTeams) {
            if (team.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                result.add(team);
            }
        }

        return result;
    }

    /**
     * Ordena los equipos según el criterio especificado
     * @param sortOption Opción de ordenamiento:
     *                  0 - Por número de títulos (mayor a menor)
     *                  1 - Por número de títulos (menor a mayor)
     *                  2 - Alfabético (A-Z)
     *                  3 - Alfabético (Z-A)
     */
    public void sortTeams(int sortOption) {
        switch (sortOption) {
            case 0: // Títulos (mayor a menor)
                teamsList.sort((team1, team2) ->
                        Integer.compare(team2.getTitles().size(), team1.getTitles().size()));
                break;
            case 1: // Títulos (menor a mayor)
                teamsList.sort((team1, team2) ->
                        Integer.compare(team1.getTitles().size(), team2.getTitles().size()));
                break;
            case 2: // Alfabético (A-Z)
                teamsList.sort((team1, team2) ->
                        team1.getName().compareToIgnoreCase(team2.getName()));
                break;
            case 3: // Alfabético (Z-A)
                teamsList.sort((team1, team2) ->
                        team2.getName().compareToIgnoreCase(team1.getName()));
                break;
        }
    }

    /**
     * Obtiene un equipo por su nombre
     * @param teamName Nombre del equipo
     * @return El equipo si existe, null en caso contrario
     */
    public ChampionsTeam getTeamByName(String teamName) {
        List<ChampionsTeam> allTeams = getAllTeams();

        for (ChampionsTeam team : allTeams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                return team;
            }
        }

        return null;
    }

    /**
     * Agrega un título a un equipo existente
     * @param teamName Nombre del equipo
     * @param title Título a agregar
     * @return true si se agregó correctamente, false si no se encontró el equipo
     */
    public boolean addTitleToTeam(String teamName, Title title) {
        ChampionsTeam team = getTeamByName(teamName);

        if (team != null) {
            team.addTitle(title);
            sortTeams(0); // Reordenar por número de títulos
            return true;
        }

        return false;
    }

    /**
     * Obtiene estadísticas sobre los equipos campeones
     * @return Un mapa con estadísticas como "equipoMasTitulos", "añoMasReciente", etc.
     */
    public String getStatistics() {
        List<ChampionsTeam> teams = getAllTeams();
        if (teams.isEmpty()) {
            return "No hay datos disponibles.";
        }

        // El equipo con más títulos será el primero si están ordenados por títulos (mayor a menor)
        ChampionsTeam mostTitlesTeam = teams.get(0);
        int totalTeams = teams.size();

        // Encontrar el año más reciente en todos los títulos
        int mostRecentYear = 0;
        String mostRecentTeam = "";

        for (ChampionsTeam team : teams) {
            List<Title> titles = team.getTitles();
            for (Title title : titles) {
                if (title.getYear() > mostRecentYear) {
                    mostRecentYear = title.getYear();
                    mostRecentTeam = team.getName();
                }
            }
        }

        // Construir el string de estadísticas
        StringBuilder stats = new StringBuilder();
        stats.append("Estadísticas de Champions League:\n\n");
        stats.append("• Equipo con más títulos: ").append(mostTitlesTeam.getName())
                .append(" (").append(mostTitlesTeam.getTitles().size()).append(" títulos)\n");
        stats.append("• Campeón más reciente: ").append(mostRecentTeam)
                .append(" (").append(mostRecentYear).append(")\n");
        stats.append("• Total de equipos campeones: ").append(totalTeams).append("\n");

        return stats.toString();
    }
}