package controller;

import view.MainView;
import view.ChampionsTitlesView;
import view.EliminationsRMView;

import model.ChampionsTitlesModel;

public class MainController {
    private MainView mainView;

    public MainController(MainView view) {
        this.mainView = view;

        // Listeners para nuestros nuevos botones
        this.mainView.addListaChampionsListener(e -> openChampionsListView());
        //this.mainView.addEliminacionesRealMadridListener(e -> openRealMadridEliminacionesView());
    }

    // Método para mostrar la vista de Listado de Champions
    private void openChampionsListView() {
        mainView.setVisible(false);
        ChampionsTitlesView championsListView = new ChampionsTitlesView();
        ChampionsTitlesModel championsModel = new ChampionsTitlesModel();
        ChampionsTitlesController controller = new ChampionsTitlesController(championsListView, championsModel);

        controller.setMainView(mainView);

        championsListView.showView();
    }

//    // Método para mostrar la vista de Eliminaciones del Real Madrid
//    private void openRealMadridEliminacionesView() {
//        mainView.setVisible(false);
//        EliminationsRMView realMadridView = new EliminationsRMView();
//        EliminationsRMController controller = new EliminationsRMController(realMadridView);
//        controller.setMainView(mainView);
//
//        realMadridView.showView();
//    }

    // Método main para demostración
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            MainController controller = new MainController(mainView);
            mainView.showView();
        });
    }
}