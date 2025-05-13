package model;

import java.util.Random;

public class InsertionSimpleCircularModel {

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

    // Clase interna para representar un nodo de la lista
    private static class Node {
        Politician data;
        Node next;

        Node(Politician data) {
            this.data = data;
            this.next = null;
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

    public InsertionSimpleCircularModel() {
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

    // Añadir un político al final de la lista circular
    private void add(Politician politician) {
        Node newNode = new Node(politician);

        if (head == null) {
            head = newNode;
            head.next = head; // Apunta a sí mismo
        } else {
            Node current = head;
            // Encontrar el último nodo (el que apunta a head)
            while (current.next != head) {
                current = current.next;
            }
            current.next = newNode;
            newNode.next = head; // El nuevo nodo apunta al head
        }
        size++;
    }

    // Limpiar la lista
    private void clear() {
        if (head != null) {
            Node current = head;
            Node last = head;

            // Encontrar el último nodo
            while (last.next != head) {
                last = last.next;
            }

            // Romper el círculo
            last.next = null;

            // Ahora podemos limpiar normalmente
            head = null;
        }
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

    // Implementación del insertion sort para lista circular
    public int insertionSort() {
        if (head == null || size <= 1) {
            return 0;
        }

        int iterations = 0;
        Node current = head.next; // Empezamos desde el segundo elemento

        for (int i = 1; i < size; i++) {
            Node nodeToInsert = current;
            Node nextNode = current.next;

            // Guardar el valor a insertar
            Politician key = nodeToInsert.data;

            // Encontrar la posición de inserción
            Node searchNode = head;
            Node prevSearch = null;
            int j = 0;

            while (j < i && searchNode.data.getMoneyToSteal() <= key.getMoneyToSteal()) {
                iterations++;
                prevSearch = searchNode;
                searchNode = searchNode.next;
                j++;
            }

            // Si necesitamos mover el nodo
            if (j < i) {
                // Remover el nodo de su posición actual
                Node temp = head;
                while (temp.next != nodeToInsert) {
                    temp = temp.next;
                }
                temp.next = nodeToInsert.next;

                // Insertar en la nueva posición
                if (prevSearch == null) {
                    // Insertar al principio
                    nodeToInsert.next = head;
                    head = nodeToInsert;

                    // Actualizar el último nodo para que apunte al nuevo head
                    Node last = head;
                    while (last.next != temp.next) {
                        last = last.next;
                    }
                    last.next = head;
                } else {
                    // Insertar en medio
                    nodeToInsert.next = prevSearch.next;
                    prevSearch.next = nodeToInsert;
                }
            }

            // Avanzar al siguiente nodo para la próxima iteración
            current = nextNode;
        }

        return iterations;
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