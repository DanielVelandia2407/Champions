package model.data;

import java.util.Objects;

/**
 * Clase que representa una eliminación del Real Madrid en Champions League
 */
public class Elimination {
    private String season;
    private String phase;
    private String opponent;
    private String firstLegResult;
    private String secondLegResult;
    private String imagePath;
    private String description;

    /**
     * Constructor para crear una nueva eliminación
     * @param season Temporada (ej. "2012-2013")
     * @param phase Fase de la competición (ej. "Semifinal")
     * @param opponent Equipo rival
     * @param firstLegResult Resultado del partido de ida
     * @param secondLegResult Resultado del partido de vuelta
     * @param imagePath Ruta a la imagen de la eliminación
     * @param description Descripción de la eliminación
     */
    public Elimination(String season, String phase, String opponent,
                       String firstLegResult, String secondLegResult,
                       String imagePath, String description) {
        this.season = season;
        this.phase = phase;
        this.opponent = opponent;
        this.firstLegResult = firstLegResult;
        this.secondLegResult = secondLegResult;
        this.imagePath = imagePath;
        this.description = description;
    }

    /**
     * Obtiene la temporada
     * @return Temporada
     */
    public String getSeason() {
        return season;
    }

    /**
     * Establece la temporada
     * @param season Nueva temporada
     */
    public void setSeason(String season) {
        this.season = season;
    }

    /**
     * Obtiene la fase de la competición
     * @return Fase
     */
    public String getPhase() {
        return phase;
    }

    /**
     * Establece la fase de la competición
     * @param phase Nueva fase
     */
    public void setPhase(String phase) {
        this.phase = phase;
    }

    /**
     * Obtiene el equipo rival
     * @return Rival
     */
    public String getOpponent() {
        return opponent;
    }

    /**
     * Establece el equipo rival
     * @param opponent Nuevo rival
     */
    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    /**
     * Obtiene el resultado del partido de ida
     * @return Resultado del partido de ida
     */
    public String getFirstLegResult() {
        return firstLegResult;
    }

    /**
     * Establece el resultado del partido de ida
     * @param firstLegResult Nuevo resultado del partido de ida
     */
    public void setFirstLegResult(String firstLegResult) {
        this.firstLegResult = firstLegResult;
    }

    /**
     * Obtiene el resultado del partido de vuelta
     * @return Resultado del partido de vuelta
     */
    public String getSecondLegResult() {
        return secondLegResult;
    }

    /**
     * Establece el resultado del partido de vuelta
     * @param secondLegResult Nuevo resultado del partido de vuelta
     */
    public void setSecondLegResult(String secondLegResult) {
        this.secondLegResult = secondLegResult;
    }

    /**
     * Obtiene la ruta a la imagen
     * @return Ruta a la imagen
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Establece la ruta a la imagen
     * @param imagePath Nueva ruta a la imagen
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Obtiene la descripción de la eliminación
     * @return Descripción
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción de la eliminación
     * @param description Nueva descripción
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene el resultado global de la eliminatoria
     * @return Resultado global
     */
    public String getAggregateResult() {
        // Este método se podría implementar para calcular el resultado global
        // a partir de los resultados de ida y vuelta
        return "Implementar lógica para calcular resultado global";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elimination that = (Elimination) o;
        return Objects.equals(season, that.season) &&
                Objects.equals(phase, that.phase) &&
                Objects.equals(opponent, that.opponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(season, phase, opponent);
    }

    @Override
    public String toString() {
        return "Elimination{" +
                "season='" + season + '\'' +
                ", phase='" + phase + '\'' +
                ", opponent='" + opponent + '\'' +
                ", result='" + firstLegResult + " / " + secondLegResult + '\'' +
                '}';
    }
}