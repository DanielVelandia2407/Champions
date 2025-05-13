package model;

import java.util.Random;

public class SelectionSimpleCircularModel {

    private Politician head;
    private int size;
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

    public SelectionSimpleCircularModel() {
        head = null;
        size = 0;
        random = new Random();
    }

    // Clase interna para representar un nodo en la lista circular
    public static class Politician {
        private String name;
        private int moneyToSteal;
        private Politician next;

        public Politician(String name, int moneyToSteal) {
            this.name = name;
            this.moneyToSteal = moneyToSteal;
            this.next = null;
        }

        public String getName() {
            return name;
        }

        public int getMoneyToSteal() {
            return moneyToSteal;
        }

        public Politician getNext() {
            return next;
        }

        public void setNext(Politician next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return name + " ($" + moneyToSteal + ")";
        }
    }

    // Generar políticos aleatorios en una lista circular
    public void generateRandomPoliticians(int count) {
        head = null;
        size = 0;

        if (count <= 0) return;

        for (int i = 0; i < count; i++) {
            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            String fullName = firstName + " " + lastName;

            // Generar cantidad aleatoria de dinero a robar (entre 10,000 y 1,000,000)
            int moneyToSteal = 10000 + random.nextInt(990001);

            Politician newPolitician = new Politician(fullName, moneyToSteal);

            if (head == null) {
                head = newPolitician;
                head.setNext(head); // Apunta a sí mismo para crear el círculo
            } else {
                // Insertar al final y mantener la estructura circular
                Politician current = head;
                while (current.getNext() != head) {
                    current = current.getNext();
                }
                current.setNext(newPolitician);
                newPolitician.setNext(head);
            }
            size++;
        }
    }

    // Obtener el tamaño de la lista
    public int getSize() {
        return size;
    }

    // Obtener un político por índice
    public Politician getPolitician(int index) {
        if (head == null || index < 0 || index >= size) {
            return null;
        }

        Politician current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current;
    }

    // Intercambiar dos políticos en la lista circular
    public void swap(int i, int j) {
        if (head == null || i < 0 || i >= size || j < 0 || j >= size || i == j) {
            return;
        }

        Politician politicianI = getPolitician(i);
        Politician politicianJ = getPolitician(j);

        // Intercambiar solo los datos, no los nodos
        String tempName = politicianI.name;
        int tempMoney = politicianI.moneyToSteal;

        politicianI.name = politicianJ.name;
        politicianI.moneyToSteal = politicianJ.moneyToSteal;

        politicianJ.name = tempName;
        politicianJ.moneyToSteal = tempMoney;
    }

    // Implementación del algoritmo Selection Sort para lista circular
    public int selectionSort() {
        if (head == null || size <= 1) {
            return 0;
        }

        int comparisons = 0;

        for (int i = 0; i < size - 1; i++) {
            int minIndex = i;

            // Encontrar el mínimo elemento en la parte no ordenada
            for (int j = i + 1; j < size; j++) {
                comparisons++;

                Politician politicianJ = getPolitician(j);
                Politician minPolitician = getPolitician(minIndex);

                if (politicianJ.getMoneyToSteal() < minPolitician.getMoneyToSteal()) {
                    minIndex = j;
                }
            }

            // Intercambiar el elemento mínimo encontrado con el primer elemento
            swap(i, minIndex);
        }

        return comparisons;
    }

    // Convertir a string mostrando la estructura circular
    @Override
    public String toString() {
        if (head == null) {
            return "Lista vacía";
        }

        StringBuilder sb = new StringBuilder();
        Politician current = head;
        int count = 0;

        do {
            sb.append("[").append(count).append("] ").append(current);
            current = current.getNext();
            count++;
            if (count < size) {
                sb.append(" → ");
            }
        } while (current != head && count < size);

        sb.append(" →← (circular)");
        return sb.toString();
    }
}