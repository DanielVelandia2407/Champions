package model;

import java.util.Random;

public class InsertionDoubleCircularModel {

    private Node head;
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

    // Clase interna para representar un nodo de la lista doble circular
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

    public InsertionDoubleCircularModel() {
        head = null;
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

    // Añadir un político al final de la lista doble circular
    private void add(Politician politician) {
        Node newNode = new Node(politician);

        if (head == null) {
            head = newNode;
            head.next = head;
            head.prev = head;
        } else {
            Node tail = head.prev;

            // Insertar al final
            newNode.prev = tail;
            newNode.next = head;

            tail.next = newNode;
            head.prev = newNode;
        }
        size++;
    }

    // Limpiar la lista
    private void clear() {
        if (head != null) {
            // Romper el círculo para evitar memory leaks
            head.prev.next = null;
            head.prev = null;
        }
        head = null;
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

    // Implementación del insertion sort para lista doble circular
    public int insertionSort() {
        if (head == null || size <= 1) {
            return 0;
        }

        int iterations = 0;
        Node current = head.next; // Empezamos desde el segundo elemento

        for (int i = 1; i < size; i++) {
            Node nodeToMove = current;
            Node nextNode = current.next;
            Politician key = nodeToMove.data;

            // Buscar la posición de inserción
            Node searchNode = head;
            int position = 0;
            boolean shouldMove = false;

            // Búsqueda en la parte ordenada
            for (int j = 0; j < i; j++) {
                iterations++;
                if (searchNode.data.getMoneyToSteal() > key.getMoneyToSteal()) {
                    shouldMove = true;
                    break;
                }
                searchNode = searchNode.next;
                position++;
            }

            // Si necesitamos mover el nodo
            if (shouldMove) {
                // Remover el nodo de su posición actual
                nodeToMove.prev.next = nodeToMove.next;
                nodeToMove.next.prev = nodeToMove.prev;

                // Insertar en la nueva posición
                if (position == 0) {
                    // Insertar al principio (nuevo head)
                    nodeToMove.prev = head.prev;
                    nodeToMove.next = head;
                    head.prev.next = nodeToMove;
                    head.prev = nodeToMove;
                    head = nodeToMove;
                } else {
                    // Insertar en medio
                    Node insertAfter = searchNode.prev;
                    nodeToMove.prev = insertAfter;
                    nodeToMove.next = searchNode;
                    insertAfter.next = nodeToMove;
                    searchNode.prev = nodeToMove;
                }
            }

            current = nextNode;
        }

        return iterations;
    }

    // Verificar si la lista está correctamente enlazada como doble circular
    public boolean verifyDoubleCircularLinks() {
        if (head == null) return true;

        Node current = head;
        Node prev = null;
        int count = 0;

        // Verificar hacia adelante
        do {
            if (current.prev != prev) {
                return false;
            }
            prev = current;
            current = current.next;
            count++;

            // Evitar bucle infinito si hay un error
            if (count > size) {
                return false;
            }
        } while (current != head);

        // Verificar que el último nodo apunte al head
        if (prev.next != head || head.prev != prev) {
            return false;
        }

        return count == size;
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