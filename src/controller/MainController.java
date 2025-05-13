package controller;

import view.MainView;
import view.InsertionMenu;
import view.SelectionMenu;

public class MainController {
    private MainView mainView;

    public MainController(MainView view) {
        this.mainView = view;


        // Listeners para Sorting
        this.mainView.addInsertionSortListener(e -> openInsertionView());
        this.mainView.addSelectionSortListener(e -> openSelectionView());




    }

    // Method to show Insertion view
    private void openInsertionView() {
        mainView.setVisible(false);
        InsertionMenu insertionMenu = new InsertionMenu();
        InsertionMenuController controller = new InsertionMenuController(insertionMenu);
        controller.setMainView(mainView);

        insertionMenu.showWindow();
    }

    // Method to show Selection view
    private void openSelectionView() {
        mainView.setVisible(false);
        SelectionMenu selectionMenu = new SelectionMenu();
        SelectionMenuController controller = new SelectionMenuController(selectionMenu);
        controller.setMainView(mainView);

        selectionMenu.showWindow();
    }

    // Main method for demonstration
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            MainController controller = new MainController(mainView);
            mainView.setVisible(true);
        });
    }
}