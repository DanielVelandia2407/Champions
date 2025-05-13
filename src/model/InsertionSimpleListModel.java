package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InsertionSimpleListModel {

    private List<Politician> politicians;
    private Random random;

    // Lista de nombres de políticos corruptos ficticios
    private static final String[] FIRST_NAMES = {
            "Corrupto", "Ladino", "Sinvergüenza", "Ratero", "Saqueador",
            "Robaldo", "Malandro", "Estafador", "Timador", "Tramposo",
            "Ladrón", "Pillador", "Mangante", "Cleptómano", "Usurpador",
            "Defraudor", "Embustero", "Trapacero", "Chantajista", "Sobornador"
    };

    private static final String[] LAST_NAMES = {
            "Roba Todo", "Mata Fuego", "Saca Plata", "Mete Mano",
            "Come Mucho", "Pide Más", "Agarra Todo", "Lleva Lejos",
            "Hurta Bien", "Afana Legal", "Birla Fácil", "Sustrae Fino",
            "Manga Limpio", "Rapiña Libre", "Escamotea Rápido", "Desfalca Pro",
            "Sisea Experto", "Chorea Master", "Tranza VIP", "Tumba Elite"
    };

    public InsertionSimpleListModel() {
        politicians = new ArrayList<>();
        random = new Random();
    }

    // Clase interna para representar un político
    public static class Politician {
        private String name;
        private int moneyToSteal;

        public Politician(String name, int moneyToSteal) {
            this.name = name;
            this.moneyToSteal = moneyToSteal;
        }

        public String getName() {
            return name;
        }

        public int getMoneyToSteal() {
            return moneyToSteal;
        }

        @Override
        public String toString() {
            return name + " ($" + moneyToSteal + ")";
        }
    }

    // Generar políticos aleatorios
    public void generateRandomPoliticians(int count) {
        politicians.clear();

        for (int i = 0; i < count; i++) {
            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            String fullName = firstName + " " + lastName;

            // Generar cantidad aleatoria de dinero a robar (entre 10,000 y 1,000,000)
            int moneyToSteal = 10000 + random.nextInt(990001);

            politicians.add(new Politician(fullName, moneyToSteal));
        }
    }

    // Obtener el tamaño de la lista
    public int getSize() {
        return politicians.size();
    }

    // Obtener un político por índice
    public Politician getPolitician(int index) {
        if (index >= 0 && index < politicians.size()) {
            return politicians.get(index);
        }
        return null;
    }

    // Establecer un político en una posición específica
    public void setPolitician(int index, Politician politician) {
        if (index >= 0 && index < politicians.size()) {
            politicians.set(index, politician);
        }
    }

    public List<Politician> getAllPoliticians() {
        return new ArrayList<>(politicians);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < politicians.size(); i++) {
            sb.append("[").append(i).append("] ").append(politicians.get(i));
            if (i < politicians.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}