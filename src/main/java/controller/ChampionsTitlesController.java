package controller;

import model.ChampionsTitlesModel;
import model.data.ChampionsTeam;
import model.data.Title;
import view.ChampionsTitlesView;
import view.MainView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import javax.swing.DefaultListModel;
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

        // Listener para cargar archivo
        view.addLoadFileButtonListener(e -> loadCustomJsonFile());
    }

    private void loadCustomJsonFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Archivo JSON");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos JSON (*.json)", "json"));

        // Opcional: Establecer directorio inicial a Descargas
        String userHome = System.getProperty("user.home");
        File downloadsDir = new File(userHome + "/Downloads");
        if (downloadsDir.exists()) {
            fileChooser.setCurrentDirectory(downloadsDir);
        }

        int result = fileChooser.showOpenDialog(view);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Depuración
                System.out.println("Archivo seleccionado: " + selectedFile.getAbsolutePath());
                System.out.println("¿El archivo existe? " + selectedFile.exists());
                System.out.println("Tamaño del archivo: " + selectedFile.length() + " bytes");

                // Si el archivo no existe, lanzar una excepción
                if (!selectedFile.exists()) {
                    throw new FileNotFoundException("El archivo seleccionado no existe: " + selectedFile.getAbsolutePath());
                }

                // Cargar equipos desde el archivo seleccionado
                model.loadTeamsFromFile(selectedFile.getAbsolutePath());

                // Verificar si se cargaron equipos
                List<ChampionsTeam> teams = model.getAllTeams();
                System.out.println("Equipos cargados: " + teams.size());

                // Imprimir los equipos cargados para depuración
                for (ChampionsTeam team : teams) {
                    System.out.println("Equipo: " + team.getName() + ", Títulos: " + team.getTitles().size());
                }

                // Actualizar la tabla y la etiqueta con la ruta del archivo
                updateTeamsTable();
                view.updateFilePathLabel(selectedFile.getAbsolutePath());

                JOptionPane.showMessageDialog(view,
                        "Archivo cargado exitosamente. " + teams.size() + " equipos encontrados.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException e) {
                System.err.println("Error de archivo no encontrado: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(view,
                        "Error: Archivo no encontrado: " + e.getMessage(),
                        "Error de Carga", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                System.err.println("Error al cargar el archivo: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(view,
                        "Error al cargar el archivo: " + e.getMessage(),
                        "Error de Carga", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadTeamsData() {
        // Cargar datos del modelo y mostrarlos en la vista
        try {
            model.loadTeamsFromFile();
            updateTeamsTable();

            // Actualizar la etiqueta con la ruta del archivo predeterminado
            view.updateFilePathLabel(model.getCurrentFilePath());
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

        // Crear un panel con pestañas para organizar la edición
        JTabbedPane tabbedPane = new JTabbedPane();

        // Pestaña para editar información básica del equipo
        JPanel basicInfoPanel = new JPanel(new BorderLayout(10, 10));
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField nameField = new JTextField(team.getName(), 20);
        namePanel.add(new JLabel("Nombre del equipo:"));
        namePanel.add(nameField);
        basicInfoPanel.add(namePanel, BorderLayout.NORTH);

        // Pestaña para gestionar títulos
        JPanel titlesPanel = new JPanel(new BorderLayout(10, 10));
        DefaultListModel<String> titlesListModel = new DefaultListModel<>();

        // Llenar la lista de títulos
        List<Title> titles = team.getTitles();
        for (int i = 0; i < titles.size(); i++) {
            Title title = titles.get(i);
            titlesListModel.addElement("Año: " + title.getYear() + ", Goles: " + title.getTopScorerGoals());
        }

        JList<String> titlesList = new JList<>(titlesListModel);
        titlesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane titlesScrollPane = new JScrollPane(titlesList);

        // Panel de botones para gestionar títulos
        JPanel titlesButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addTitleButton = new JButton("Agregar Título");
        JButton editTitleButton = new JButton("Editar Título");
        JButton deleteTitleButton = new JButton("Eliminar Título");

        titlesButtonPanel.add(addTitleButton);
        titlesButtonPanel.add(editTitleButton);
        titlesButtonPanel.add(deleteTitleButton);

        titlesPanel.add(titlesScrollPane, BorderLayout.CENTER);
        titlesPanel.add(titlesButtonPanel, BorderLayout.SOUTH);

        // Agregar pestañas
        tabbedPane.addTab("Información Básica", basicInfoPanel);
        tabbedPane.addTab("Títulos", titlesPanel);

        // Configurar diálogo
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(view), "Editar Equipo", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(tabbedPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Guardar");
        JButton cancelButton = new JButton("Cancelar");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Evento para agregar un nuevo título
        addTitleButton.addActionListener(e -> {
            addTitleToTeam(team);
            // Actualizar la lista de títulos
            updateTitlesList(titlesListModel, team.getTitles());
        });

        // Evento para editar un título seleccionado
        editTitleButton.addActionListener(e -> {
            int selectedIndex = titlesList.getSelectedIndex();
            if (selectedIndex >= 0) {
                editTitle(team, selectedIndex);
                // Actualizar la lista después de editar
                updateTitlesList(titlesListModel, team.getTitles());
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor, seleccione un título para editar.",
                        "Selección Requerida", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Evento para eliminar un título seleccionado
        deleteTitleButton.addActionListener(e -> {
            int selectedIndex = titlesList.getSelectedIndex();
            if (selectedIndex >= 0) {
                int confirm = JOptionPane.showConfirmDialog(dialog,
                        "¿Está seguro de que desea eliminar este título?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Eliminar el título
                    team.getTitles().remove(selectedIndex);
                    // Actualizar la lista
                    updateTitlesList(titlesListModel, team.getTitles());
                }
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor, seleccione un título para eliminar.",
                        "Selección Requerida", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Evento para guardar cambios
        saveButton.addActionListener(e -> {
            String newName = nameField.getText().trim();
            if (!newName.isEmpty()) {
                team.setName(newName);

                // Guardar cambios y actualizar vista
                try {
                    model.saveTeamsToFile();
                    updateTeamsTable();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(view,
                            "Equipo actualizado exitosamente.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view,
                            "Error al guardar los datos: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "El nombre del equipo no puede estar vacío.",
                        "Error de Validación", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Evento para cancelar
        cancelButton.addActionListener(e -> dialog.dispose());

        // Configurar tamaño y mostrar diálogo
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(view);
        dialog.setVisible(true);
    }

    // Método para actualizar la lista de títulos
    private void updateTitlesList(DefaultListModel<String> model, List<Title> titles) {
        model.clear();
        for (Title title : titles) {
            model.addElement("Año: " + title.getYear() + ", Goles: " + title.getTopScorerGoals());
        }
    }

    // Método para editar un título existente
    private void editTitle(ChampionsTeam team, int titleIndex) {
        Title title = team.getTitles().get(titleIndex);

        JTextField yearField = new JTextField(String.valueOf(title.getYear()));
        JTextField goalsField = new JTextField(String.valueOf(title.getTopScorerGoals()));

        Object[] message = {
                "Año del título:", yearField,
                "Goles del máximo goleador:", goalsField
        };

        int option = JOptionPane.showConfirmDialog(view, message, "Editar Título",
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

                // Actualizar título
                title.setYear(year);
                title.setTopScorerGoals(goals);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view,
                        "Por favor, introduzca valores numéricos válidos.",
                        "Error de Formato", JOptionPane.ERROR_MESSAGE);

                // Intentar nuevamente
                editTitle(team, titleIndex);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(view,
                        e.getMessage(),
                        "Error de Validación", JOptionPane.ERROR_MESSAGE);

                // Intentar nuevamente
                editTitle(team, titleIndex);
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