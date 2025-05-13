package controller;

import view.SelectionMenu;
import view.MainView;
import view.SelectionSimpleListView;
import view.SelectionSimpleCircularView;
import view.SelectionDoubleListView;
import view.SelectionDoubleCircularView;
import model.SelectionSimpleListModel;
import model.SelectionSimpleCircularModel;
import model.SelectionDoubleListModel;
import model.SelectionDoubleCircularModel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectionMenuController {

    private SelectionMenu view;
    private MainView mainView; // Para poder regresar al menú principal

    public SelectionMenuController(SelectionMenu view) {
        this.view = view;

        // Add action listeners
        this.view.addSimpleListListener(new SimpleListListener());
        this.view.addSimpleCircularListListener(new SimpleCircularListListener());
        this.view.addDoubleListListener(new DoubleListListener());
        this.view.addDoubleCircularListListener(new DoubleCircularListListener());
        this.view.addBackListener(new BackListener());
    }

    // Method to set the MainView for back navigation
    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    // Listener for Simple List option
    class SimpleListListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Crear el modelo y la vista
            SelectionSimpleListModel model = new SelectionSimpleListModel();
            SelectionSimpleListView simpleView = new SelectionSimpleListView();

            // Crear el controlador
            SelectionSimpleListController controller = new SelectionSimpleListController(model, simpleView);
            controller.setSelectionMenu(view);

            // Ocultar el menú actual y mostrar la vista de lista simple
            view.setVisible(false);
            controller.start();
        }
    }

    // Listener for Simple Circular List option
    class SimpleCircularListListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Crear el modelo y la vista circular
            SelectionSimpleCircularModel model = new SelectionSimpleCircularModel();
            SelectionSimpleCircularView circularView = new SelectionSimpleCircularView();

            // Crear el controlador
            SelectionSimpleCircularController controller = new SelectionSimpleCircularController(model, circularView);
            controller.setSelectionMenu(view);

            // Ocultar el menú actual y mostrar la vista de lista circular
            view.setVisible(false);
            controller.start();
        }
    }

    // Listener for Double List option
    class DoubleListListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Crear el modelo y la vista doble
            SelectionDoubleListModel model = new SelectionDoubleListModel();
            SelectionDoubleListView doubleView = new SelectionDoubleListView();

            // Crear el controlador
            SelectionDoubleListController controller = new SelectionDoubleListController(model, doubleView);
            controller.setSelectionMenu(view);

            // Ocultar el menú actual y mostrar la vista de lista doble
            view.setVisible(false);
            controller.start();
        }
    }

    // Listener for Double Circular List option
    class DoubleCircularListListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Crear el modelo y la vista doble circular
            SelectionDoubleCircularModel model = new SelectionDoubleCircularModel();
            SelectionDoubleCircularView doubleCircularView = new SelectionDoubleCircularView();

            // Crear el controlador
            SelectionDoubleCircularController controller = new SelectionDoubleCircularController(model, doubleCircularView);
            controller.setSelectionMenu(view);

            // Ocultar el menú actual y mostrar la vista de lista doble circular
            view.setVisible(false);
            controller.start();
        }
    }

    // Listener for back button
    class BackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.dispose();
            // Regresar al mainView si está disponible
            if (mainView != null) {
                mainView.setVisible(true);
            }
        }
    }

    // Method to set the back navigation to main menu
    public void setBackToMainNavigation(ActionListener listener) {
        view.addBackListener(listener);
    }

    // Method to start the application
    public void start() {
        view.showWindow();
    }
}