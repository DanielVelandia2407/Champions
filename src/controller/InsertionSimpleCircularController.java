package controller;

import view.InsertionSimpleCircularView;
import view.InsertionMenu;
import model.InsertionSimpleCircularModel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertionSimpleCircularController {

    private InsertionSimpleCircularModel model;
    private InsertionSimpleCircularView view;
    private InsertionMenu insertionMenu;

    public InsertionSimpleCircularController(InsertionSimpleCircularModel model, InsertionSimpleCircularView view) {
        this.model = model;
        this.view = view;

        // Configurar listeners
        this.view.addBackListener(new BackListener());
        this.view.addGenerateListener(new GenerateListener());
        this.view.addSortListener(new SortListener());
    }

    // Method to set the InsertionMenu for back navigation
    public void setInsertionMenu(InsertionMenu insertionMenu) {
        this.insertionMenu = insertionMenu;
    }

    // Listener for back button
    class BackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.dispose();
            // Regresar al menú de inserción si está disponible
            if (insertionMenu != null) {
                insertionMenu.setVisible(true);
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

                // Mostrar datos en la tabla con la estructura circular
                updateTableView();

                view.setSortButtonEnabled(true);
                view.setResultMessage("Políticos generados con éxito en lista circular", true);
                view.clearSteps();
                view.setIterationsMessage("");
                view.resetProgress();

                // Añadir explicación de la estructura circular
                view.addStep("Lista circular generada:");
                view.addStep("- Cada nodo apunta al siguiente");
                view.addStep("- El último nodo apunta al primero (HEAD)");
                view.addStep("- Esto crea un ciclo cerrado\n");
                view.addStep(model.toString());

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
                view.addStep("=== INICIANDO INSERTION SORT CIRCULAR ===\n");
                view.addStep("Algoritmo: Insertion Sort para Lista Simple Circular");
                view.addStep("Criterio: Ordenando por cantidad de dinero a robar (menor a mayor)");
                view.addStep("Nota: La lista mantiene su estructura circular\n");

                int n = model.getSize();

                view.addStep("Estado inicial de la lista circular:");
                view.addStep(model.toString());
                view.addStep("");

                // Deshabilitar botón durante ordenamiento
                view.setSortButtonEnabled(false);
                view.resetProgress();

                // Ejecutar el algoritmo de ordenamiento y obtener iteraciones
                int iterations = model.insertionSort();

                // Simular pasos del algoritmo para visualización
                for (int i = 1; i < n; i++) {
                    view.addStep("--- Iteración " + i + " ---");

                    InsertionSimpleCircularModel.Politician current = model.getPolitician(i);
                    view.addStep("Procesando: " + current.getName() + " ($" + current.getMoneyToSteal() + ")");
                    view.addStep("Comparando con nodos anteriores en la lista circular...");

                    // Actualizar vista
                    updateTableView();
                    view.highlightRow(i);

                    // Actualizar progreso
                    view.updateProgress(i, n);

                    try {
                        Thread.sleep(800); // Pausa para visualización
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    view.addStep("Estado después de la iteración " + i + ":");
                    view.addStep(model.toString());
                    view.addStep("");

                    // Actualizar vista final de la iteración
                    updateTableView();

                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                view.addStep("=== ORDENAMIENTO COMPLETADO ===");
                view.addStep("Lista circular final ordenada:");
                view.addStep(model.toString());
                view.addStep("\nLa estructura circular se mantiene intacta:");
                view.addStep("- Orden: de menor a mayor cantidad de dinero");
                view.addStep("- El último elemento aún apunta al primero");

                view.setProgressComplete();
                view.setResultMessage("Ordenamiento circular completado con éxito", true);
                view.setIterationsMessage("Número de comparaciones: " + iterations);

                // Actualizar vista final
                updateTableView();
                view.setSortButtonEnabled(true);

            } catch (Exception ex) {
                view.setResultMessage("Error durante el ordenamiento: " + ex.getMessage(), false);
                view.setSortButtonEnabled(true);
                ex.printStackTrace();
            }
        }
    }

    // Helper method to update table view with circular structure visualization
    private void updateTableView() {
        int n = model.getSize();
        Object[][] tableData = new Object[n][4];

        for (int i = 0; i < n; i++) {
            InsertionSimpleCircularModel.Politician politician = model.getPolitician(i);
            tableData[i][0] = i + 1; // ID
            tableData[i][1] = politician.getName();
            tableData[i][2] = "$" + politician.getMoneyToSteal();

            // Mostrar el siguiente nodo (circular)
            if (i == n - 1) {
                tableData[i][3] = "→ HEAD"; // Último nodo apunta al head
            } else {
                tableData[i][3] = "→ " + (i + 2); // Apunta al siguiente nodo
            }
        }

        view.setTableData(tableData);
    }

    // Method to start the application
    public void start() {
        view.showWindow();
    }
}