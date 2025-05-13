package model.data;

import java.util.Objects;

/**
 * Clase que representa un título ganado por un equipo en la Champions League
 */
public class Title {
    private int year;
    private int topScorerGoals;

    /**
     * Constructor para crear un nuevo título
     * @param year Año en que se ganó el título
     * @param topScorerGoals Número de goles del máximo goleador
     */
    public Title(int year, int topScorerGoals) {
        this.year = year;
        this.topScorerGoals = topScorerGoals;
    }

    /**
     * Obtiene el año del título
     * @return Año del título
     */
    public int getYear() {
        return year;
    }

    /**
     * Establece el año del título
     * @param year Año del título
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Obtiene el número de goles del máximo goleador
     * @return Goles del máximo goleador
     */
    public int getTopScorerGoals() {
        return topScorerGoals;
    }

    /**
     * Establece el número de goles del máximo goleador
     * @param topScorerGoals Goles del máximo goleador
     */
    public void setTopScorerGoals(int topScorerGoals) {
        this.topScorerGoals = topScorerGoals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title = (Title) o;
        return year == title.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year);
    }

    @Override
    public String toString() {
        return "Title{" +
                "year=" + year +
                ", goals=" + topScorerGoals +
                '}';
    }
}