package controller;

import view.InsertionMenu;
import view.InsertionSimpleListView;
import view.InsertionSimpleCircularView;
import view.InsertionDoubleListView;
import view.InsertionDoubleCircularView;
import view.MainView;
import model.InsertionSimpleListModel;
import model.InsertionSimpleCircularModel;
import model.InsertionDoubleListModel;
import model.InsertionDoubleCircularModel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InsertionMenuController {

    private InsertionMenu view;
    private MainView mainView; // Añadido para poder regresar al menú principal

    public InsertionMenuController(InsertionMenu view) {
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
            // Don't close current window, just hide it
            view.setVisible(false);

            // Create and start Simple List insertion sort
            InsertionSimpleListModel model = new InsertionSimpleListModel();
            InsertionSimpleListView simpleListView = new InsertionSimpleListView();
            InsertionSimpleListController controller = new InsertionSimpleListController(model, simpleListView);

            // Pass reference to this view so we can return to it
            controller.setInsertionMenu(view);

            controller.start();
        }
    }

    // Listener for Simple Circular List option
    class SimpleCircularListListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Don't close current window, just hide it
            view.setVisible(false);

            // Create and start Simple Circular List insertion sort
            InsertionSimpleCircularModel model = new InsertionSimpleCircularModel();
            InsertionSimpleCircularView simpleCircularView = new InsertionSimpleCircularView();
            InsertionSimpleCircularController controller = new InsertionSimpleCircularController(model, simpleCircularView);

            // Pass reference to this view so we can return to it
            controller.setInsertionMenu(view);

            controller.start();
        }
    }

    // Listener for Double List option
    class DoubleListListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Don't close current window, just hide it
            view.setVisible(false);

            // Create and start Double List insertion sort
            InsertionDoubleListModel model = new InsertionDoubleListModel();
            InsertionDoubleListView doubleListView = new InsertionDoubleListView();
            InsertionDoubleListController controller = new InsertionDoubleListController(model, doubleListView);

            // Pass reference to this view so we can return to it
            controller.setInsertionMenu(view);

            controller.start();
        }
    }

    // Listener for Double Circular List option
    class DoubleCircularListListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Don't close current window, just hide it
            view.setVisible(false);

            // Create and start Double Circular List insertion sort
            InsertionDoubleCircularModel model = new InsertionDoubleCircularModel();
            InsertionDoubleCircularView doubleCircularView = new InsertionDoubleCircularView();
            InsertionDoubleCircularController controller = new InsertionDoubleCircularController(model, doubleCircularView);

            // Pass reference to this view so we can return to it
            controller.setInsertionMenu(view);

            controller.start();
        }
    }

    // Listener for back button - CORREGIDO
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