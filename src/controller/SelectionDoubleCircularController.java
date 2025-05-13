package controller;

import view.SelectionDoubleCircularView;
import view.SelectionMenu;
import model.SelectionDoubleCircularModel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectionDoubleCircularController {

    private SelectionDoubleCircularModel model;
    private SelectionDoubleCircularView view;
    private SelectionMenu selectionMenu;

    public SelectionDoubleCircularController(SelectionDoubleCircularModel model, SelectionDoubleCircularView view) {
        this.model = model;
        this.view = view;

        // Configurar listeners
        this.view.addBackListener(new BackListener());
        this.view.addGenerateListener(new GenerateListener());
        this.view.addSortListener(new SortListener());
    }

    // Method to set the SelectionMenu for back navigation
    public void setSelectionMenu(SelectionMenu selectionMenu) {
        this.selectionMenu = selectionMenu;
    }

    // Listener for back button
    class BackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.dispose();
            // Regresar al menú de selección si está disponible
            if (selectionMenu != null) {
                selectionMenu.setVisible(true);
            }
        }
    }

    // Listener for generate button
    class GenerateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String numElementsText = view.getNumElements();

                if (numElementsText.isEmpty()) {
                    view.setResultMessage("Por favor ingrese el número de políticos", false);
                    return;
                }

                int numElements = Integer.parseInt(numElementsText);

                if (numElements <= 0) {
                    view.setResultMessage("El número debe ser mayor que 0", false);
                    return;
                }

                if (numElements > 100) {
                    view.setResultMessage("El número no puede exceder 100", false);
                    return;
                }

                // Generar datos aleatorios
                model.generateRandomPoliticians(numElements);

                // Mostrar datos en la tabla con la estructura doble circular
                updateTableView();

                view.setSortButtonEnabled(true);
                view.setResultMessage("Políticos generados con éxito en lista doble circular", true);
                view.clearSteps();
                view.setIterationsMessage("");
                view.resetProgress();

                // Añadir explicación de la estructura doble circular
                view.addStep("Lista doble circular generada:");
                view.addStep("- Cada nodo tiene punteros next y prev");
                view.addStep("- El último nodo apunta al primero (next)");
                view.addStep("- El primer nodo apunta al último (prev)");
                view.addStep("- Navegación bidireccional con estructura circular\n");
                view.addStep("Dirección hacia adelante:");
                view.addStep(model.toString());
                view.addStep("\nDirección hacia atrás:");
                view.addStep(model.toStringReverse());

            } catch (NumberFormatException ex) {
                view.setResultMessage("Por favor ingrese un número válido", false);
            } catch (Exception ex) {
                view.setResultMessage("Error: " + ex.getMessage(), false);
            }
        }
    }

    // Listener for sort button
    class SortListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                view.clearSteps();
                view.addStep("=== INICIANDO SELECTION SORT DOBLE CIRCULAR ===\n");
                view.addStep("Algoritmo: Selection Sort para Lista Doble Circular");
                view.addStep("Criterio: Ordenando por cantidad de dinero a robar (menor a mayor)");
                view.addStep("Estructura: Navegación bidireccional circular optimizada\n");

                int n = model.getSize();

                view.addStep("Estado inicial de la lista doble circular:");
                view.addStep(model.toString());
                view.addStep("");

                // Deshabilitar botón durante ordenamiento
                view.setSortButtonEnabled(false);
                view.resetProgress();

                // Ejecutar el algoritmo de ordenamiento paso a paso
                executeSortingSteps(n);

                view.addStep("=== ORDENAMIENTO COMPLETADO ===");
                view.addStep("Lista doble circular final ordenada:");
                view.addStep("Dirección hacia adelante: " + model.toString());
                view.addStep("Dirección hacia atrás: " + model.toStringReverse());
                view.addStep("\nLa estructura doble circular se mantiene perfectamente");
                view.addStep("con todos los punteros bidireccionales correctos");

                view.setProgressComplete();
                view.setResultMessage("Ordenamiento doble circular completado con éxito", true);

                // Actualizar vista final
                updateTableView();
                view.setSortButtonEnabled(true);

            } catch (Exception ex) {
                view.setResultMessage("Error durante el ordenamiento: " + ex.getMessage(), false);
                view.setSortButtonEnabled(true);
                ex.printStackTrace();
            }
        }

        // Método para ejecutar el ordenamiento paso a paso con visualización
        private void executeSortingSteps(int n) {
            int comparisons = 0;

            for (int i = 0; i < n - 1; i++) {
                view.addStep("--- Iteración " + (i + 1) + " ---");
                view.addStep("Posición actual: " + i);
                view.addStep("Buscando el mínimo valor desde la posición " + i + " hasta el final...");

                int minIndex = i;

                // Actualizar progreso
                view.updateProgress(i, n - 1);

                // Resaltar la posición actual
                view.highlightRow(i);

                try {
                    Thread.sleep(500); // Pausa para visualización
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                // Buscar el mínimo en la parte no ordenada de la lista doble circular
                for (int j = i + 1; j < n; j++) {
                    comparisons++;

                    SelectionDoubleCircularModel.Politician currentPolitician = model.getPolitician(j);
                    SelectionDoubleCircularModel.Politician minPolitician = model.getPolitician(minIndex);

                    view.addStep("Comparando: " + currentPolitician.getName() +
                            " ($" + currentPolitician.getMoneyToSteal() + ")" +
                            " con " + minPolitician.getName() +
                            " ($" + minPolitician.getMoneyToSteal() + ")");

                    // Resaltar los elementos siendo comparados
                    view.highlightRows(j, minIndex);

                    try {
                        Thread.sleep(300); // Pausa para visualización
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    if (currentPolitician.getMoneyToSteal() < minPolitician.getMoneyToSteal()) {
                        minIndex = j;
                        view.addStep("Nuevo mínimo encontrado: " + currentPolitician.getName() +
                                " ($" + currentPolitician.getMoneyToSteal() + ")");
                    }
                }

                // Realizar el intercambio si es necesario
                if (minIndex != i) {
                    view.addStep("\nIntercambiando en la lista doble circular:");
                    view.addStep("- Posición " + i + ": " + model.getPolitician(i).getName() +
                            " ($" + model.getPolitician(i).getMoneyToSteal() + ")");
                    view.addStep("- Posición " + minIndex + ": " + model.getPolitician(minIndex).getName() +
                            " ($" + model.getPolitician(minIndex).getMoneyToSteal() + ")");

                    // Resaltar los elementos a intercambiar
                    view.highlightRows(i, minIndex);

                    try {
                        Thread.sleep(500); // Pausa para visualización
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    model.swap(i, minIndex);

                    view.addStep("Intercambio realizado (todos los punteros circulares mantenidos)");
                } else {
                    view.addStep("No se requiere intercambio");
                }

                view.addStep("\nEstado después de la iteración " + (i + 1) + ":");
                view.addStep(model.toString());
                view.addStep("Elementos ordenados hasta la posición " + i + "\n");

                // Actualizar vista
                updateTableView();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            // Mostrar el total de comparaciones
            view.setIterationsMessage("Número de comparaciones: " + comparisons);
        }
    }

    // Helper method to update table view with double circular structure visualization
    private void updateTableView() {
        int n = model.getSize();
        Object[][] tableData = new Object[n][5];

        for (int i = 0; i < n; i++) {
            SelectionDoubleCircularModel.Politician politician = model.getPolitician(i);
            tableData[i][0] = i + 1; // ID
            tableData[i][1] = politician.getName();
            tableData[i][2] = "$" + politician.getMoneyToSteal();

            // Mostrar el nodo anterior
            if (i == 0) {
                tableData[i][3] = "← " + n; // Primer nodo apunta al último
            } else {
                tableData[i][3] = "← " + i; // Apunta al anterior
            }

            // Mostrar el nodo siguiente
            if (i == n - 1) {
                tableData[i][4] = "→ 1"; // Último nodo apunta al primero
            } else {
                tableData[i][4] = "→ " + (i + 2); // Apunta al siguiente
            }
        }

        view.setTableData(tableData);
    }

    // Method to start the application
    public void start() {
        view.showWindow();
    }
}