package controller;

import model.ChampionsTitlesModel;
import model.data.ChampionsTeam;
import model.data.Title;
import view.ChampionsTitlesView;
import view.MainView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ChampionsTitlesController {
    private ChampionsTitlesView view;
    private ChampionsTitlesModel model;
    private MainView mainView;

    public ChampionsTitlesController(ChampionsTitlesView view, ChampionsTitlesModel model) {
        this.view = view;
        this.model = model;

        // Inicializar datos
        loadTeamsData();

        // Configurar listeners
        setupEventListeners();
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    private void setupEventListeners() {
        // Listener para el botón de volver al menú principal
        view.addBackButtonListener(e -> {
            view.closeView();
            if (mainView != null) {
                mainView.showView();
            }
        });

        // Listener para selección en la tabla
        view.addTableSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    displaySelectedTeamDetails();
                }
            }
        });

        // Listener para búsqueda
        view.addSearchButtonListener(e -> searchTeams());

        // Listener para cambio en el ordenamiento
        view.addSortComboBoxListener(e -> sortTeams());

        // Listener para agregar equipo
        view.addAddTeamButtonListener(e -> showAddTeamDialog());

        // Listener para editar equipo
        view.addEditTeamButtonListener(e -> {
            int selectedRow = view.getSelectedTeamRow();
            if (selectedRow >= 0) {
                showEditTeamDialog(selectedRow);
            } else {
                JOptionPane.showMessageDialog(view,
                        "Por favor, seleccione un equipo para editar.",
                        "Selección Requerida", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Listener para eliminar equipo
        view.addDeleteTeamButtonListener(e -> {
            int selectedRow = view.getSelectedTeamRow();
            if (selectedRow >= 0) {
                confirmAndDeleteTeam(selectedRow);
            } else {
                JOptionPane.showMessageDialog(view,
                        "Por favor, seleccione un equipo para eliminar.",
                        "Selección Requerida", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void loadTeamsData() {
        // Cargar datos del modelo y mostrarlos en la vista
        try {
            model.loadTeamsFromFile();
            updateTeamsTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Error al cargar los datos: " + e.getMessage(),
                    "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTeamsTable() {
        List<ChampionsTeam> teams = model.getAllTeams();

        // Convertir la lista de equipos a un formato para la tabla
        Object[][] tableData = new Object[teams.size()][4];
        for (int i = 0; i < teams.size(); i++) {
            ChampionsTeam team = teams.get(i);
            List<Title> titles = team.getTitles();

            // Obtener el último título (asumiendo que están ordenados cronológicamente)
            Title lastTitle = titles.isEmpty() ? null : titles.get(titles.size() - 1);

            tableData[i][0] = team.getName();
            tableData[i][1] = titles.size(); // Número de títulos
            tableData[i][2] = lastTitle != null ? lastTitle.getYear() : "N/A";
            tableData[i][3] = lastTitle != null ? lastTitle.getTopScorerGoals() + " goles" : "N/A";
        }

        view.setTeamsData(tableData);
    }

    private void displaySelectedTeamDetails() {
        int selectedRow = view.getSelectedTeamRow();
        if (selectedRow >= 0) {
            // Convertir el índice de la fila visual al índice del modelo (por si está ordenada)
            int modelRow = view.getTeamsTable().convertRowIndexToModel(selectedRow);

            ChampionsTeam team = model.getAllTeams().get(modelRow);
            StringBuilder details = new StringBuilder();

            details.append("Equipo: ").append(team.getName()).append("\n\n");
            details.append("Total de títulos: ").append(team.getTitles().size()).append("\n\n");
            details.append("Títulos ganados:\n");

            List<Title> titles = team.getTitles();
            for (Title title : titles) {
                details.append("• ").append(title.getYear())
                        .append(" - Goleador: ").append(title.getTopScorerGoals())
                        .append(" goles\n");
            }

            view.showTeamDetails(details.toString());
        }
    }

    private void searchTeams() {
        String searchTerm = view.getSearchText().trim();
        if (!searchTerm.isEmpty()) {
            List<ChampionsTeam> filteredTeams = model.searchTeams(searchTerm);

            if (filteredTeams.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "No se encontraron equipos que coincidan con '" + searchTerm + "'",
                        "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Actualizar la tabla con los resultados filtrados
                Object[][] tableData = new Object[filteredTeams.size()][4];
                for (int i = 0; i < filteredTeams.size(); i++) {
                    ChampionsTeam team = filteredTeams.get(i);
                    List<Title> titles = team.getTitles();
                    Title lastTitle = titles.isEmpty() ? null : titles.get(titles.size() - 1);

                    tableData[i][0] = team.getName();
                    tableData[i][1] = titles.size();
                    tableData[i][2] = lastTitle != null ? lastTitle.getYear() : "N/A";
                    tableData[i][3] = lastTitle != null ? lastTitle.getTopScorerGoals() + " goles" : "N/A";
                }

                view.setTeamsData(tableData);
            }
        } else {
            // Si la búsqueda está vacía, mostrar todos los equipos
            updateTeamsTable();
        }
    }

    private void sortTeams() {
        int sortOption = view.getSelectedSortOption();
        model.sortTeams(sortOption);
        updateTeamsTable();
    }

    private void showAddTeamDialog() {
        // Aquí implementaríamos un diálogo para agregar un nuevo equipo
        // Por simplicidad, se crea un JOptionPane básico

        String teamName = JOptionPane.showInputDialog(view,
                "Nombre del equipo:",
                "Agregar Nuevo Equipo", JOptionPane.PLAIN_MESSAGE);

        if (teamName != null && !teamName.trim().isEmpty()) {
            // Crear nuevo equipo
            ChampionsTeam newTeam = new ChampionsTeam(teamName.trim());

            // Solicitar información del primer título
            boolean addTitle = (JOptionPane.showConfirmDialog(view,
                    "¿Desea agregar un título para este equipo?",
                    "Agregar Título",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);

            if (addTitle) {
                addTitleToTeam(newTeam);
            }

            // Añadir equipo al modelo
            model.addTeam(newTeam);

            // Guardar cambios y actualizar vista
            try {
                model.saveTeamsToFile();
                updateTeamsTable();
                JOptionPane.showMessageDialog(view,
                        "Equipo agregado exitosamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view,
                        "Error al guardar los datos: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addTitleToTeam(ChampionsTeam team) {
        // Diálogo para agregar un título
        JTextField yearField = new JTextField();
        JTextField goalsField = new JTextField();

        Object[] message = {
                "Año del título:", yearField,
                "Goles del máximo goleador:", goalsField
        };

        int option = JOptionPane.showConfirmDialog(view, message, "Agregar Título",
                JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int year = Integer.parseInt(yearField.getText().trim());
                int goals = Integer.parseInt(goalsField.getText().trim());

                // Validar datos
                if (year < 1955 || year > 2025) {
                    throw new IllegalArgumentException("El año debe estar entre 1955 y 2025.");
                }
                if (goals < 0) {
                    throw new IllegalArgumentException("La cantidad de goles no puede ser negativa.");
                }

                // Agregar título al equipo
                Title title = new Title(year, goals);
                team.addTitle(title);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view,
                        "Por favor, introduzca valores numéricos válidos.",
                        "Error de Formato", JOptionPane.ERROR_MESSAGE);

                // Intentar nuevamente
                addTitleToTeam(team);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(view,
                        e.getMessage(),
                        "Error de Validación", JOptionPane.ERROR_MESSAGE);

                // Intentar nuevamente
                addTitleToTeam(team);
            }
        }
    }

    private void showEditTeamDialog(int selectedRow) {
        // Convertir el índice de la fila visual al índice del modelo
        int modelRow = view.getTeamsTable().convertRowIndexToModel(selectedRow);
        ChampionsTeam team = model.getAllTeams().get(modelRow);

        // Aquí implementaríamos un diálogo completo para editar el equipo
        // Por simplicidad, solo permitimos cambiar el nombre del equipo

        String newName = JOptionPane.showInputDialog(view,
                "Nombre del equipo:",
                "Editar Equipo", JOptionPane.PLAIN_MESSAGE,
                null, null, team.getName()).toString();

        if (newName != null && !newName.trim().isEmpty() && !newName.equals(team.getName())) {
            team.setName(newName.trim());

            // Guardar cambios y actualizar vista
            try {
                model.saveTeamsToFile();
                updateTeamsTable();
                JOptionPane.showMessageDialog(view,
                        "Equipo actualizado exitosamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view,
                        "Error al guardar los datos: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void confirmAndDeleteTeam(int selectedRow) {
        // Convertir el índice de la fila visual al índice del modelo
        int modelRow = view.getTeamsTable().convertRowIndexToModel(selectedRow);
        ChampionsTeam team = model.getAllTeams().get(modelRow);

        int confirm = JOptionPane.showConfirmDialog(view,
                "¿Está seguro de que desea eliminar el equipo '" + team.getName() + "'?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            model.removeTeam(team);

            // Guardar cambios y actualizar vista
            try {
                model.saveTeamsToFile();
                updateTeamsTable();
                JOptionPane.showMessageDialog(view,
                        "Equipo eliminado exitosamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view,
                        "Error al guardar los datos: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
