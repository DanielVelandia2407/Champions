package model.structures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Implementación de una Lista Circular Doblemente Enlazada genérica.
 * Esta estructura permite navegar en ambas direcciones (siguiente y anterior)
 * y el último nodo se conecta con el primero formando un círculo.
 *
 * @param <T> El tipo de elementos que contendrá la lista
 */
public class CircularDoublyLinkedList<T> {

    /**
     * Clase interna que representa un nodo en la lista
     */
    private class Node {
        T data;
        Node next;
        Node prev;

        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private Node head;
    private int size;

    /**
     * Constructor que inicializa una lista vacía
     */
    public CircularDoublyLinkedList() {
        head = null;
        size = 0;
    }

    /**
     * Verifica si la lista está vacía
     * @return true si la lista está vacía, false en caso contrario
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Obtiene el tamaño actual de la lista
     * @return Número de elementos en la lista
     */
    public int size() {
        return size;
    }

    /**
     * Agrega un nuevo elemento al final de la lista
     * @param data Elemento a agregar
     */
    public void add(T data) {
        Node newNode = new Node(data);

        if (isEmpty()) {
            // Si la lista está vacía, el nuevo nodo se apunta a sí mismo
            head = newNode;
            head.next = head;
            head.prev = head;
        } else {
            // Si la lista no está vacía, insertar al final
            Node last = head.prev;

            newNode.next = head;
            newNode.prev = last;

            last.next = newNode;
            head.prev = newNode;
        }

        size++;
    }

    /**
     * Actualiza un elemento existente en la lista
     * @param data Elemento con los nuevos datos (debe implementar equals correctamente)
     * @return true si se actualizó con éxito, false si no se encontró el elemento
     */
    public boolean update(T data) {
        if (isEmpty()) {
            return false;
        }

        Node current = head;

        do {
            if (Objects.equals(current.data, data)) {
                current.data = data;
                return true;
            }
            current = current.next;
        } while (current != head);

        return false;
    }

    /**
     * Elimina un elemento de la lista
     * @param data Elemento a eliminar (debe implementar equals correctamente)
     * @return true si se eliminó con éxito, false si no se encontró el elemento
     */
    public boolean remove(T data) {
        if (isEmpty()) {
            return false;
        }

        Node current = head;

        do {
            if (Objects.equals(current.data, data)) {
                // Si es el único nodo de la lista
                if (size == 1) {
                    head = null;
                } else {
                    // Si es el nodo cabeza, actualizar la referencia
                    if (current == head) {
                        head = head.next;
                    }

                    // Ajustar las referencias de los nodos adyacentes
                    current.prev.next = current.next;
                    current.next.prev = current.prev;
                }

                size--;
                return true;
            }

            current = current.next;
        } while (current != head);

        return false;
    }

    /**
     * Busca un elemento en la lista
     * @param data Elemento a buscar
     * @return true si el elemento existe en la lista, false en caso contrario
     */
    public boolean contains(T data) {
        if (isEmpty()) {
            return false;
        }

        Node current = head;

        do {
            if (Objects.equals(current.data, data)) {
                return true;
            }
            current = current.next;
        } while (current != head);

        return false;
    }

    /**
     * Obtiene un elemento por su índice (0-based)
     * @param index Índice del elemento a obtener
     * @return El elemento en la posición especificada
     * @throws IndexOutOfBoundsException si el índice está fuera de rango
     */
    public T get(int index) {
        if (isEmpty() || index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice: " + index + ", Tamaño: " + size);
        }

        Node current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.data;
    }

    /**
     * Convierte la lista circular en una lista normal para facilitar su procesamiento
     * @return Lista con los elementos en el orden actual
     */
    public List<T> toList() {
        List<T> result = new ArrayList<>();

        if (isEmpty()) {
            return result;
        }

        Node current = head;

        do {
            result.add(current.data);
            current = current.next;
        } while (current != head);

        return result;
    }

    /**
     * Ordena la lista utilizando el algoritmo Merge Sort
     * @param comparator Comparador para definir el criterio de ordenamiento
     */
    public void sort(Comparator<T> comparator) {
        if (isEmpty() || size == 1) {
            return; // Lista vacía o con un solo elemento ya está ordenada
        }

        // Convertir a lista normal para ordenar
        List<T> listToSort = toList();

        // Aplicar Merge Sort a la lista
        List<T> sortedList = mergeSort(listToSort, comparator);

        // Reconstruir la lista circular con los elementos ordenados
        clear();
        for (T item : sortedList) {
            add(item);
        }
    }

    /**
     * Implementación del algoritmo Merge Sort para ordenar una lista
     * @param list Lista a ordenar
     * @param comparator Comparador para definir el criterio de ordenamiento
     * @return Lista ordenada
     */
    private List<T> mergeSort(List<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) {
            return list;
        }

        int middle = list.size() / 2;
        List<T> left = new ArrayList<>(list.subList(0, middle));
        List<T> right = new ArrayList<>(list.subList(middle, list.size()));

        left = mergeSort(left, comparator);
        right = mergeSort(right, comparator);

        return merge(left, right, comparator);
    }

    /**
     * Combina dos listas ordenadas en una sola lista ordenada
     * @param left Primera lista ordenada
     * @param right Segunda lista ordenada
     * @param comparator Comparador para definir el criterio de ordenamiento
     * @return Lista combinada ordenada
     */
    private List<T> merge(List<T> left, List<T> right, Comparator<T> comparator) {
        List<T> result = new ArrayList<>();
        int leftIndex = 0, rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (comparator.compare(left.get(leftIndex), right.get(rightIndex)) <= 0) {
                result.add(left.get(leftIndex));
                leftIndex++;
            } else {
                result.add(right.get(rightIndex));
                rightIndex++;
            }
        }

        // Agregar los elementos restantes de la lista izquierda
        result.addAll(left.subList(leftIndex, left.size()));
        result.addAll(right.subList(rightIndex, right.size()));

        return result;
    }

    /**
     * Limpia la lista, eliminando todos los elementos
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * Recorre la lista en sentido horario (de principio a fin)
     * @param callback Función a aplicar a cada elemento
     */
    public void traverseForward(java.util.function.Consumer<T> callback) {
        if (isEmpty()) {
            return;
        }

        Node current = head;

        do {
            callback.accept(current.data);
            current = current.next;
        } while (current != head);
    }

    /**
     * Recorre la lista en sentido antihorario (de fin a principio)
     * @param callback Función a aplicar a cada elemento
     */
    public void traverseBackward(java.util.function.Consumer<T> callback) {
        if (isEmpty()) {
            return;
        }

        Node current = head.prev; // Empezar por el último nodo

        do {
            callback.accept(current.data);
            current = current.prev;
        } while (current != head.prev);
    }

    /**
     * Representación en texto de la lista
     * @return String con los elementos de la lista
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        Node current = head;

        do {
            sb.append(current.data);
            current = current.next;

            if (current != head) {
                sb.append(", ");
            }
        } while (current != head);

        sb.append("]");
        return sb.toString();
    }
}