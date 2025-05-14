import controller.MainController;
import javax.swing.*;
import view.MainView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            MainController mainController = new MainController(mainView);
            mainView.showView();
        });
    }

}