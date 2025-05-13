package controller;

import view.InsertionSimpleListView;
import view.InsertionMenu;
import model.InsertionSimpleListModel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertionSimpleListController {

    private InsertionSimpleListModel model;
    private InsertionSimpleListView view;
    private InsertionMenu insertionMenu;

    public InsertionSimpleListController(InsertionSimpleListModel model, InsertionSimpleListView view) {
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

                // Mostrar datos en la tabla
                Object[][] tableData = new Object[numElements][3];
                for (int i = 0; i < numElements; i++) {
                    tableData[i][0] = i + 1; // ID
                    tableData[i][1] = model.getPolitician(i).getName();
                    tableData[i][2] = "$" + model.getPolitician(i).getMoneyToSteal();
                }

                view.setTableData(tableData);
                view.setSortButtonEnabled(true);
                view.setResultMessage("Políticos generados con éxito", true);
                view.clearSteps();
                view.setIterationsMessage("");
                view.resetProgress();

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
                view.addStep("=== INICIANDO INSERTION SORT ===\n");
                view.addStep("Algoritmo: Insertion Sort para Lista Simple");
                view.addStep("Criterio: Ordenando por cantidad de dinero a robar (menor a mayor)\n");

                int iterations = 0;
                int n = model.getSize();

                view.addStep("Array inicial:");
                view.addStep(model.toString());
                view.addStep("");

                // Deshabilitar botón durante ordenamiento
                view.setSortButtonEnabled(false);
                view.resetProgress();

                // Implementar Insertion Sort
                for (int i = 1; i < n; i++) {
                    InsertionSimpleListModel.Politician key = model.getPolitician(i);
                    int j = i - 1;

                    view.addStep("--- Iteración " + (i) + " ---");
                    view.addStep("Elemento a insertar: " + key.getName() + " ($" + key.getMoneyToSteal() + ")");
                    view.addStep("Posición actual: " + i);

                    // Highlight current element
                    view.highlightRow(i);

                    // Update progress
                    view.updateProgress(i, n);

                    // Move elements that are greater than key
                    while (j >= 0 && model.getPolitician(j).getMoneyToSteal() > key.getMoneyToSteal()) {
                        iterations++;
                        view.addStep("Comparando con: " + model.getPolitician(j).getName() +
                                " ($" + model.getPolitician(j).getMoneyToSteal() + ")");
                        view.addStep("$" + model.getPolitician(j).getMoneyToSteal() + " > $" +
                                key.getMoneyToSteal() + " → Mover a la derecha");

                        model.setPolitician(j + 1, model.getPolitician(j));
                        j = j - 1;

                        // Actualizar vista
                        updateTableView();

                        try {
                            Thread.sleep(500); // Pausa para visualización
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (j + 1 != i) {
                        model.setPolitician(j + 1, key);
                        view.addStep("Insertando en posición: " + (j + 1));
                    } else {
                        view.addStep("El elemento ya está en su posición correcta");
                    }

                    view.addStep("Estado después de la iteración:");
                    view.addStep(model.toString());
                    view.addStep("");

                    // Actualizar vista final de la iteración
                    updateTableView();

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                view.addStep("=== ORDENAMIENTO COMPLETADO ===");
                view.addStep("Array final ordenado:");
                view.addStep(model.toString());

                view.setProgressComplete();
                view.setResultMessage("Ordenamiento completado con éxito", true);
                view.setIterationsMessage("Número de comparaciones: " + iterations);

                // Highlight all data
                updateTableView();
                view.setSortButtonEnabled(true);

            } catch (Exception ex) {
                view.setResultMessage("Error durante el ordenamiento: " + ex.getMessage(), false);
                view.setSortButtonEnabled(true);
                ex.printStackTrace();
            }
        }
    }

    // Helper method to update table view
    private void updateTableView() {
        int n = model.getSize();
        Object[][] tableData = new Object[n][3];

        for (int i = 0; i < n; i++) {
            tableData[i][0] = i + 1; // ID
            tableData[i][1] = model.getPolitician(i).getName();
            tableData[i][2] = "$" + model.getPolitician(i).getMoneyToSteal();
        }

        view.setTableData(tableData);
    }

    // Method to start the application
    public void start() {
        view.showWindow();
    }
}