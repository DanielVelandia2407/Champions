package model;

import java.util.Random;

public class InsertionDoubleListModel {

    private Node head;
    private Node tail;
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

    // Clase interna para representar un nodo de la lista doble
    private static class Node {
        Politician data;
        Node next;
        Node prev;

        Node(Politician data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
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

    public InsertionDoubleListModel() {
        head = null;
        tail = null;
        size = 0;
        random = new Random();
    }

    // Generar políticos aleatorios
    public void generateRandomPoliticians(int count) {
        clear();

        for (int i = 0; i < count; i++) {
            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            String fullName = firstName + " " + lastName;

            // Generar cantidad aleatoria de dinero a robar (entre 10,000 y 1,000,000)
            int moneyToSteal = 10000 + random.nextInt(990001);

            add(new Politician(fullName, moneyToSteal));
        }
    }

    // Añadir un político al final de la lista doble
    private void add(Politician politician) {
        Node newNode = new Node(politician);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    // Limpiar la lista
    private void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    // Obtener el tamaño de la lista
    public int getSize() {
        return size;
    }

    // Obtener un político por índice
    public Politician getPolitician(int index) {
        if (index < 0 || index >= size || head == null) {
            return null;
        }

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    // Obtener todos los políticos como array para visualización
    public Politician[] getAllPoliticians() {
        if (size == 0) {
            return new Politician[0];
        }

        Politician[] politicians = new Politician[size];
        Node current = head;

        for (int i = 0; i < size; i++) {
            politicians[i] = current.data;
            current = current.next;
        }

        return politicians;
    }

    // Obtener el nodo de un político por índice
    private Node getNode(int index) {
        if (index < 0 || index >= size || head == null) {
            return null;
        }

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    // Implementación del insertion sort para lista doble
    public int insertionSort() {
        if (head == null || size <= 1) {
            return 0;
        }

        int iterations = 0;
        Node current = head.next; // Empezamos desde el segundo elemento

        while (current != null) {
            Node nextNode = current.next;
            Politician key = current.data;

            // Buscar la posición de inserción
            Node search = head;
            while (search != current && search.data.getMoneyToSteal() <= key.getMoneyToSteal()) {
                iterations++;
                search = search.next;
            }

            // Si necesitamos mover el nodo
            if (search != current) {
                // Remover el nodo de su posición actual
                if (current.prev != null) {
                    current.prev.next = current.next;
                }
                if (current.next != null) {
                    current.next.prev = current.prev;
                }
                if (current == tail) {
                    tail = current.prev;
                }

                // Insertar en la nueva posición
                if (search == head) {
                    // Insertar al principio
                    current.prev = null;
                    current.next = head;
                    head.prev = current;
                    head = current;
                } else {
                    // Insertar en medio
                    current.prev = search.prev;
                    current.next = search;
                    if (search.prev != null) {
                        search.prev.next = current;
                    }
                    search.prev = current;
                }
            }

            current = nextNode;
        }

        return iterations;
    }

    // Verificar si la lista está correctamente enlazada (para debugging)
    public boolean verifyDoubleLinks() {
        if (head == null) return true;

        Node current = head;
        Node prev = null;

        while (current != null) {
            if (current.prev != prev) {
                return false;
            }
            prev = current;
            current = current.next;
        }

        return prev == tail;
    }

    // Método toString para mostrar el estado actual
    @Override
    public String toString() {
        if (size == 0) {
            return "Lista vacía";
        }

        StringBuilder sb = new StringBuilder();
        Node current = head;

        for (int i = 0; i < size; i++) {
            sb.append("[").append(i).append("] ").append(current.data);
            if (i < size - 1) {
                sb.append(", ");
            }
            current = current.next;
        }

        return sb.toString();
    }
}