package controller;

import model.EliminationsRMModel;
import model.data.Elimination;
import view.EliminationsRMView;
import view.MainView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.List;
import java.util.Stack;

/**
 * Controlador para la gestión de eliminaciones del Real Madrid.
 */
public class EliminationsRMController {
    private EliminationsRMView view;
    private EliminationsRMModel model;
    private MainView mainView;

    // Pila de navegación para recorrer las eliminaciones
    private Stack<Elimination> navigationStack;
    private int currentIndex = -1;
    private List<Elimination> currentEliminations;

    /**
     * Constructor del controlador.
     */
    public EliminationsRMController(EliminationsRMView view, EliminationsRMModel model) {
        this.view = view;
        this.model = model;
        this.navigationStack = new Stack<>();

        // Inicializar datos
        loadEliminationsData();

        // Configurar listeners
        setupEventListeners();
    }

    /**
     * Establece la referencia a la vista principal.
     */
    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    /**
     * Configura los listeners para los eventos de la interfaz.
     */
    private void setupEventListeners() {
        // Listener para el botón de volver al menú principal
        view.addBackButtonListener(e -> {
            view.closeView();
            if (mainView != null) {
                mainView.showView();
            }
        });

        // Listener para selección en la tabla
        view.addTableSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    displaySelectedEliminationDetails();
                }
            }
        });

        // Listener para búsqueda
        view.addSearchButtonListener(e -> searchEliminations());

        // Listener para cambio en ordenamiento
        view.addSortComboBoxListener(e -> sortEliminations());

        // Listener para agregar eliminación
        view.addAddButtonListener(e -> showAddEliminationDialog());

        // Listener para editar eliminación
        view.addEditButtonListener(e -> {
            int selectedIndex = view.getSelectedEliminationIndex();
            if (selectedIndex >= 0) {
                showEditEliminationDialog(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(view,
                        "Por favor, seleccione una eliminación para editar.",
                        "Selección Requerida", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Listener para eliminar eliminación
        view.addDeleteButtonListener(e -> {
            int selectedIndex = view.getSelectedEliminationIndex();
            if (selectedIndex >= 0) {
                confirmAndDeleteElimination(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(view,
                        "Por favor, seleccione una eliminación para eliminar.",
                        "Selección Requerida", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Listener para botón siguiente eliminación
        view.addNextEliminationButtonListener(e -> navigateToNextElimination());

        // Listener para botón anterior eliminación
        view.addPreviousEliminationButtonListener(e -> navigateToPreviousElimination());
    }

    /**
     * Carga los datos de eliminaciones.
     */
    private void loadEliminationsData() {
        try {
            model.loadEliminationsFromFile();
            updateEliminationsTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Error al cargar los datos: " + e.getMessage(),
                    "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza la tabla con los datos de eliminaciones.
     */
    private void updateEliminationsTable() {
        currentEliminations = model.getAllEliminations();
        view.updateEliminationsTable(currentEliminations);

        // Inicializar la pila de navegación
        navigationStack = new Stack<>();
        for (Elimination e : currentEliminations) {
            navigationStack.push(e);
        }

        // Inicializar el índice actual
        currentIndex = navigationStack.isEmpty() ? -1 : 0;
    }

    /**
     * Muestra los detalles de la eliminación seleccionada.
     */
    private void displaySelectedEliminationDetails() {
        int selectedIndex = view.getSelectedEliminationIndex();
        if (selectedIndex >= 0) {
            // Convertir índice visual a índice de modelo
            int modelIndex = view.convertRowIndexToModel(selectedIndex);

            if (modelIndex >= 0 && modelIndex < currentEliminations.size()) {
                Elimination elimination = currentEliminations.get(modelIndex);
                view.showEliminationDetails(elimination);

                // Actualizar índice actual para navegación
                currentIndex = modelIndex;
            }
        }
    }

    /**
     * Realiza búsqueda de eliminaciones según texto introducido.
     */
    private void searchEliminations() {
        String searchTerm = view.getSearchText().trim();
        currentEliminations = model.searchEliminations(searchTerm);

        if (currentEliminations.isEmpty() && !searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "No se encontraron eliminaciones que coincidan con '" + searchTerm + "'",
                    "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
        }

        view.updateEliminationsTable(currentEliminations);

        // Actualizar la pila de navegación con los resultados
        navigationStack = new Stack<>();
        for (Elimination e : currentEliminations) {
            navigationStack.push(e);
        }

        currentIndex = navigationStack.isEmpty() ? -1 : 0;
    }

    /**
     * Ordena las eliminaciones según el criterio seleccionado.
     */
    private void sortEliminations() {
        int sortOption = view.getSelectedSortOption();
        currentEliminations = model.getSortedEliminations(sortOption);
        view.updateEliminationsTable(currentEliminations);

        // Actualizar la pila de navegación con los resultados ordenados
        navigationStack = new Stack<>();
        for (Elimination e : currentEliminations) {
            navigationStack.push(e);
        }

        currentIndex = navigationStack.isEmpty() ? -1 : 0;
    }

    /**
     * Muestra el diálogo para agregar una nueva eliminación.
     */
    private void showAddEliminationDialog() {
        Elimination newElimination = view.showEliminationDialog(null, false);

        if (newElimination != null) {
            model.pushElimination(newElimination);

            try {
                model.saveEliminationsToFile();
                updateEliminationsTable();
                JOptionPane.showMessageDialog(view,
                        "Eliminación agregada exitosamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view,
                        "Error al guardar los datos: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Muestra el diálogo para editar una eliminación existente.
     */
    private void showEditEliminationDialog(int selectedIndex) {
        int modelIndex = view.convertRowIndexToModel(selectedIndex);

        if (modelIndex >= 0 && modelIndex < currentEliminations.size()) {
            Elimination elimination = currentEliminations.get(modelIndex);
            Elimination updatedElimination = view.showEliminationDialog(elimination, true);

            if (updatedElimination != null) {
                model.updateElimination(elimination, updatedElimination);

                try {
                    model.saveEliminationsToFile();
                    updateEliminationsTable();
                    JOptionPane.showMessageDialog(view,
                            "Eliminación actualizada exitosamente.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view,
                            "Error al guardar los datos: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Confirma y elimina una eliminación seleccionada.
     */
    private void confirmAndDeleteElimination(int selectedIndex) {
        int modelIndex = view.convertRowIndexToModel(selectedIndex);

        if (modelIndex >= 0 && modelIndex < currentEliminations.size()) {
            Elimination elimination = currentEliminations.get(modelIndex);

            int confirm = JOptionPane.showConfirmDialog(view,
                    "¿Está seguro de que desea eliminar esta eliminación?\n" +
                            "Temporada: " + elimination.getSeason() + "\n" +
                            "Fase: " + elimination.getPhase() + "\n" +
                            "Rival: " + elimination.getOpponent(),
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                model.removeElimination(elimination);

                try {
                    model.saveEliminationsToFile();
                    updateEliminationsTable();
                    JOptionPane.showMessageDialog(view,
                            "Eliminación eliminada exitosamente.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view,
                            "Error al guardar los datos: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Navega a la siguiente eliminación en la pila.
     */
    private void navigateToNextElimination() {
        if (currentEliminations == null || currentEliminations.isEmpty()) {
            return;
        }

        // Avanzar al siguiente índice
        currentIndex = (currentIndex + 1) % currentEliminations.size();

        // Seleccionar la eliminación actual y mostrar sus detalles
        Elimination elimination = currentEliminations.get(currentIndex);
        view.showEliminationDetails(elimination);

        // Seleccionar la fila correspondiente en la tabla
        view.selectEliminationRow(currentIndex);
    }

    /**
     * Navega a la eliminación anterior en la pila.
     */
    private void navigateToPreviousElimination() {
        if (currentEliminations == null || currentEliminations.isEmpty()) {
            return;
        }

        // Retroceder al índice anterior
        currentIndex = (currentIndex - 1 + currentEliminations.size()) % currentEliminations.size();

        // Seleccionar la eliminación actual y mostrar sus detalles
        Elimination elimination = currentEliminations.get(currentIndex);
        view.showEliminationDetails(elimination);

        // Seleccionar la fila correspondiente en la tabla
        view.selectEliminationRow(currentIndex);
    }
}
