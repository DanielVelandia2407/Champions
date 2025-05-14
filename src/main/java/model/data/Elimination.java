package model.data;

import java.util.Objects;

/**
 * Clase que representa una eliminación del Real Madrid en la Champions League.
 */
public class Elimination {
    private String season;         // Temporada (ej: "2022-2023")
    private String phase;          // Fase (ej: "Semifinal", "Octavos de final")
    private String opponent;       // Equipo rival
    private String resultHome;     // Resultado partido de ida
    private String resultAway;     // Resultado partido de vuelta
    private String description;    // Descripción del evento
    private String imagePath;      // Ruta a la imagen representativa

    // Constructor vacío para serialización JSON
    public Elimination() {
    }

    // Constructor completo
    public Elimination(String season, String phase, String opponent, String resultHome,
                       String resultAway, String description, String imagePath) {
        this.season = season;
        this.phase = phase;
        this.opponent = opponent;
        this.resultHome = resultHome;
        this.resultAway = resultAway;
        this.description = description;
        this.imagePath = imagePath;
    }

    // Getters y setters
    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getResultHome() {
        return resultHome;
    }

    public void setResultHome(String resultHome) {
        this.resultHome = resultHome;
    }

    public String getResultAway() {
        return resultAway;
    }

    public void setResultAway(String resultAway) {
        this.resultAway = resultAway;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // Método toString para facilitar la depuración
    @Override
    public String toString() {
        return "Elimination{" +
                "season='" + season + '\'' +
                ", phase='" + phase + '\'' +
                ", opponent='" + opponent + '\'' +
                ", resultHome='" + resultHome + '\'' +
                ", resultAway='" + resultAway + '\'' +
                ", description='" + description + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }

    // Métodos equals y hashCode para comparaciones
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
}