package controller;

import view.InsertionDoubleListView;
import view.InsertionMenu;
import model.InsertionDoubleListModel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertionDoubleListController {

    private InsertionDoubleListModel model;
    private InsertionDoubleListView view;
    private InsertionMenu insertionMenu;

    public InsertionDoubleListController(InsertionDoubleListModel model, InsertionDoubleListView view) {
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

                // Mostrar datos en la tabla con la estructura doble
                updateTableView();

                view.setSortButtonEnabled(true);
                view.setResultMessage("Políticos generados con éxito en lista doble", true);
                view.clearSteps();
                view.setIterationsMessage("");
                view.resetProgress();

                // Añadir explicación de la estructura doble
                view.addStep("Lista doble generada:");
                view.addStep("- Cada nodo tiene un puntero al siguiente");
                view.addStep("- Cada nodo tiene un puntero al anterior");
                view.addStep("- Permite navegación bidireccional\n");
                view.addStep(model.toString());

                // Verificar que la estructura doble esté correcta
                if (model.verifyDoubleLinks()) {
                    view.addStep("✓ Enlaces bidireccionales verificados correctamente");
                } else {
                    view.addStep("✗ Error en los enlaces bidireccionales");
                }

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
                view.addStep("=== INICIANDO INSERTION SORT DOBLE ===\n");
                view.addStep("Algoritmo: Insertion Sort para Lista Doble");
                view.addStep("Criterio: Ordenando por cantidad de dinero a robar (menor a mayor)");
                view.addStep("Ventaja: Navegación bidireccional para inserción eficiente\n");

                int n = model.getSize();

                view.addStep("Estado inicial de la lista doble:");
                view.addStep(model.toString());
                view.addStep("");

                // Deshabilitar botón durante ordenamiento
                view.setSortButtonEnabled(false);
                view.resetProgress();

                // Mostrar estado antes del ordenamiento
                for (int i = 0; i < Math.min(n, 10); i++) {
                    InsertionDoubleListModel.Politician politician = model.getPolitician(i);
                    view.addStep("  Nodo " + i + ": " + politician.getName() + " ($" + politician.getMoneyToSteal() + ")");
                }
                if (n > 10) {
                    view.addStep("  ... (" + (n - 10) + " nodos más)");
                }
                view.addStep("");

                // Ejecutar el algoritmo de ordenamiento y obtener iteraciones
                long startTime = System.currentTimeMillis();
                int iterations = model.insertionSort();
                long endTime = System.currentTimeMillis();

                // Simular pasos del algoritmo para visualización
                for (int i = 1; i < n; i++) {
                    view.addStep("--- Iteración " + i + " ---");

                    InsertionDoubleListModel.Politician current = model.getPolitician(i);
                    view.addStep("Procesando: " + current.getName() + " ($" + current.getMoneyToSteal() + ")");
                    view.addStep("Búsqueda bidireccional hacia atrás...");

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
                view.addStep("Lista doble final ordenada:");
                view.addStep(model.toString());
                view.addStep("");
                view.addStep("Ventajas de la lista doble:");
                view.addStep("- Inserción más eficiente con navegación bidireccional");
                view.addStep("- No es necesario mantener puntero al anterior");
                view.addStep("- Operaciones de eliminación más rápidas");
                view.addStep("");
                view.addStep("Estadísticas del ordenamiento:");
                view.addStep("- Tiempo de ejecución: " + (endTime - startTime) + " ms");

                // Verificar que la estructura doble sigue correcta
                if (model.verifyDoubleLinks()) {
                    view.addStep("✓ Enlaces bidireccionales verificados correctamente");
                } else {
                    view.addStep("✗ Error en los enlaces bidireccionales");
                }

                view.setProgressComplete();
                view.setResultMessage("Ordenamiento doble completado con éxito", true);
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

    // Helper method to update table view with double-linked structure visualization
    private void updateTableView() {
        int n = model.getSize();
        Object[][] tableData = new Object[n][5];

        for (int i = 0; i < n; i++) {
            InsertionDoubleListModel.Politician politician = model.getPolitician(i);
            tableData[i][0] = i + 1; // ID

            // Mostrar el nodo anterior
            if (i == 0) {
                tableData[i][1] = "← NULL"; // Primer nodo no tiene anterior
            } else {
                tableData[i][1] = "← " + i; // Apunta al nodo anterior
            }

            tableData[i][2] = politician.getName();
            tableData[i][3] = "$" + politician.getMoneyToSteal();

            // Mostrar el nodo siguiente
            if (i == n - 1) {
                tableData[i][4] = "NULL →"; // Último nodo no tiene siguiente
            } else {
                tableData[i][4] = (i + 2) + " →"; // Apunta al siguiente nodo
            }
        }

        view.setTableData(tableData);
    }

    // Method to start the application
    public void start() {
        view.showWindow();
    }
}