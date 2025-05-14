package model.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase que representa un equipo campeón de la Champions League
 */
public class ChampionsTeam {
    private String name;
    private List<Title> titles;

    /**
     * Constructor para crear un nuevo equipo
     * @param name Nombre del equipo
     */
    public ChampionsTeam(String name) {
        this.name = name;
        this.titles = new ArrayList<>();
    }

    /**
     * Constructor completo para crear un equipo con títulos
     * @param name Nombre del equipo
     * @param titles Lista de títulos ganados
     */
    public ChampionsTeam(String name, List<Title> titles) {
        this.name = name;
        this.titles = titles != null ? titles : new ArrayList<>();
    }

    /**
     * Obtiene el nombre del equipo
     * @return Nombre del equipo
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del equipo
     * @param name Nuevo nombre del equipo
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene la lista de títulos del equipo
     * @return Lista de títulos
     */
    public List<Title> getTitles() {
        return titles;
    }

    /**
     * Establece la lista completa de títulos
     * @param titles Nueva lista de títulos
     */
    public void setTitles(List<Title> titles) {
        this.titles = titles != null ? titles : new ArrayList<>();
    }

    /**
     * Agrega un nuevo título a la lista de títulos del equipo
     * @param title Título a agregar
     */
    public void addTitle(Title title) {
        if (title != null) {
            titles.add(title);
        }
    }

    /**
     * Elimina un título específico de la lista
     * @param title Título a eliminar
     * @return true si se eliminó con éxito, false en caso contrario
     */
    public boolean removeTitle(Title title) {
        return titles.remove(title);
    }

    /**
     * Obtiene el número total de títulos del equipo
     * @return Cantidad de títulos
     */
    public int getTitleCount() {
        return titles.size();
    }

    /**
     * Obtiene el año del título más reciente
     * @return Año del título más reciente, o 0 si no tiene títulos
     */
    public int getLatestTitleYear() {
        int latestYear = 0;

        for (Title title : titles) {
            if (title.getYear() > latestYear) {
                latestYear = title.getYear();
            }
        }

        return latestYear;
    }

    /**
     * Obtiene el título más reciente
     * @return El título más reciente, o null si no tiene títulos
     */
    public Title getLatestTitle() {
        if (titles.isEmpty()) {
            return null;
        }

        Title latest = titles.get(0);

        for (Title title : titles) {
            if (title.getYear() > latest.getYear()) {
                latest = title;
            }
        }

        return latest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChampionsTeam team = (ChampionsTeam) o;
        return Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "ChampionsTeam{" +
                "name='" + name + '\'' +
                ", titles=" + titles.size() +
                '}';
    }
}