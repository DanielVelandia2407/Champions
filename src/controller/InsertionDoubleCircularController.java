package controller;

import view.InsertionDoubleCircularView;
import view.InsertionMenu;
import model.InsertionDoubleCircularModel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertionDoubleCircularController {

    private InsertionDoubleCircularModel model;
    private InsertionDoubleCircularView view;
    private InsertionMenu insertionMenu;

    public InsertionDoubleCircularController(InsertionDoubleCircularModel model, InsertionDoubleCircularView view) {
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

                // Mostrar datos en la tabla con la estructura doble circular
                updateTableView();

                view.setSortButtonEnabled(true);
                view.setResultMessage("Políticos generados con éxito en lista doble circular", true);
                view.clearSteps();
                view.setIterationsMessage("");
                view.resetProgress();

                // Añadir explicación de la estructura doble circular
                view.addStep("Lista doble circular generada:");
                view.addStep("- Cada nodo tiene puntero al siguiente y al anterior");
                view.addStep("- El primer nodo apunta al último como anterior");
                view.addStep("- El último nodo apunta al primero como siguiente");
                view.addStep("- Permite navegación bidireccional circular\n");
                view.addStep(model.toString());

                // Verificar que la estructura doble circular esté correcta
                if (model.verifyDoubleCircularLinks()) {
                    view.addStep("✓ Enlaces bidireccionales circulares verificados correctamente");
                } else {
                    view.addStep("✗ Error en los enlaces bidireccionales circulares");
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
                view.addStep("=== INICIANDO INSERTION SORT DOBLE CIRCULAR ===\n");
                view.addStep("Algoritmo: Insertion Sort para Lista Doble Circular");
                view.addStep("Criterio: Ordenando por cantidad de dinero a robar (menor a mayor)");
                view.addStep("Ventaja: Navegación bidireccional y circular\n");

                int n = model.getSize();

                view.addStep("Estado inicial de la lista doble circular:");
                view.addStep(model.toString());
                view.addStep("");

                // Deshabilitar botón durante ordenamiento
                view.setSortButtonEnabled(false);
                view.resetProgress();

                // Mostrar estructura circular antes del ordenamiento
                view.addStep("Verificación de estructura circular:");
                for (int i = 0; i < Math.min(n, 5); i++) {
                    InsertionDoubleCircularModel.Politician politician = model.getPolitician(i);
                    view.addStep("  Nodo " + i + ": " + politician.getName() + " ($" + politician.getMoneyToSteal() + ")");
                }
                view.addStep("  ... (estructura circular completa)");
                view.addStep("");

                // Ejecutar el algoritmo de ordenamiento y obtener iteraciones
                long startTime = System.currentTimeMillis();
                int iterations = model.insertionSort();
                long endTime = System.currentTimeMillis();

                // Simular pasos del algoritmo para visualización
                for (int i = 1; i < n; i++) {
                    view.addStep("--- Iteración " + i + " ---");

                    InsertionDoubleCircularModel.Politician current = model.getPolitician(i);
                    view.addStep("Procesando: " + current.getName() + " ($" + current.getMoneyToSteal() + ")");
                    view.addStep("Búsqueda circular bidireccional...");

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
                view.addStep("Lista doble circular final ordenada:");
                view.addStep(model.toString());
                view.addStep("");
                view.addStep("Ventajas de la lista doble circular:");
                view.addStep("- Navegación bidireccional completa");
                view.addStep("- Acceso circular en ambas direcciones");
                view.addStep("- No hay extremos (principio/fin)");
                view.addStep("- Búsqueda optimizada con dos direcciones");
                view.addStep("");
                view.addStep("Estadísticas del ordenamiento:");
                view.addStep("- Tiempo de ejecución: " + (endTime - startTime) + " ms");

                // Verificar que la estructura doble circular sigue correcta
                if (model.verifyDoubleCircularLinks()) {
                    view.addStep("✓ Enlaces bidireccionales circulares verificados correctamente");
                } else {
                    view.addStep("✗ Error en los enlaces bidireccionales circulares");
                }

                view.setProgressComplete();
                view.setResultMessage("Ordenamiento doble circular completado con éxito", true);
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

    // Helper method to update table view with double-circular structure visualization
    private void updateTableView() {
        int n = model.getSize();
        Object[][] tableData = new Object[n][5];

        for (int i = 0; i < n; i++) {
            InsertionDoubleCircularModel.Politician politician = model.getPolitician(i);
            tableData[i][0] = i + 1; // ID

            // Mostrar el nodo anterior (circular)
            if (i == 0) {
                tableData[i][1] = "← " + n; // Primer nodo apunta al último
            } else {
                tableData[i][1] = "← " + i; // Apunta al nodo anterior
            }

            tableData[i][2] = politician.getName();
            tableData[i][3] = "$" + politician.getMoneyToSteal();

            // Mostrar el nodo siguiente (circular)
            if (i == n - 1) {
                tableData[i][4] = "1 →"; // Último nodo apunta al primero
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