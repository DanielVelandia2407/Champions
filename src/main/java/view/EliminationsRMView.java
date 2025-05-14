package view;

import model.data.Elimination;
import utils.ImageLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * Vista para mostrar y gestionar las eliminaciones del Real Madrid.
 */
public class EliminationsRMView extends JFrame {
    private JTable eliminationsTable;
    private DefaultTableModel tableModel;
    private JTextArea descriptionArea;
    private JLabel imageLabel;
    private JPanel detailsPanel;
    private JTextField searchField;
    private JComboBox<String> sortComboBox;
    private JButton searchButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;
    private JButton nextEliminationButton;
    private JButton previousEliminationButton;

    /**
     * Constructor de la vista.
     */
    public EliminationsRMView() {
        setupUI();
    }

    /**
     * Configura los componentes de la interfaz de usuario.
     */
    private void setupUI() {
        setTitle("Eliminaciones del Real Madrid en Champions League");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Panel principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);

        // Panel superior con controles
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchButton = new JButton("Buscar");
        searchPanel.add(new JLabel("Buscar:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sortComboBox = new JComboBox<>(new String[]{
                "Más recientes primero",
                "Más antiguas primero",
                "Por fase",
                "Por fase y temporada"
        });
        sortPanel.add(new JLabel("Ordenar:"));
        sortPanel.add(sortComboBox);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(sortPanel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Panel central con tabla y detalles
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Tabla de eliminaciones
        String[] columnNames = {"Temporada", "Fase", "Rival"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        eliminationsTable = new JTable(tableModel);
        eliminationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eliminationsTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane tableScrollPane = new JScrollPane(eliminationsTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Eliminaciones"));
        centerPanel.add(tableScrollPane);

        // Panel de detalles con imagen y descripción
        detailsPanel = new JPanel(new BorderLayout(0, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Detalles"));

        // Panel para la imagen con botones de navegación
        JPanel imagePanel = new JPanel(new BorderLayout());
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(400, 200));

        // Botones de navegación para la pila
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        previousEliminationButton = new JButton("« Anterior");
        nextEliminationButton = new JButton("Siguiente »");
        navigationPanel.add(previousEliminationButton);
        navigationPanel.add(nextEliminationButton);

        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.add(navigationPanel, BorderLayout.SOUTH);

        // Área de texto para la descripción
        descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setPreferredSize(new Dimension(400, 150));

        detailsPanel.add(imagePanel, BorderLayout.CENTER);
        detailsPanel.add(descScrollPane, BorderLayout.SOUTH);

        centerPanel.add(detailsPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backButton = new JButton("Volver al Menú Principal");
        addButton = new JButton("Agregar Eliminación");
        editButton = new JButton("Editar Eliminación");
        deleteButton = new JButton("Eliminar");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Muestra la vista.
     */
    public void showView() {
        setVisible(true);
    }

    /**
     * Cierra la vista.
     */
    public void closeView() {
        setVisible(false);
    }

    /**
     * Actualiza la tabla con los datos de eliminaciones.
     */
    public void updateEliminationsTable(List<Elimination> eliminations) {
        tableModel.setRowCount(0); // Limpiar tabla

        for (Elimination e : eliminations) {
            Object[] row = {e.getSeason(), e.getPhase(), e.getOpponent()};
            tableModel.addRow(row);
        }

        // Si hay eliminaciones, seleccionar la primera
        if (!eliminations.isEmpty()) {
            eliminationsTable.setRowSelectionInterval(0, 0);
        }
    }

    /**
     * Muestra los detalles de una eliminación seleccionada.
     */
    public void showEliminationDetails(Elimination elimination) {
        if (elimination != null) {
            // Cargar imagen
            ImageIcon icon = ImageLoader.loadAndResizeImage(elimination.getImagePath(), 400, 200);
            imageLabel.setIcon(icon);

            // Mostrar detalles en el área de texto
            StringBuilder details = new StringBuilder();
            details.append("Temporada: ").append(elimination.getSeason()).append("\n");
            details.append("Fase: ").append(elimination.getPhase()).append("\n");
            details.append("Rival: ").append(elimination.getOpponent()).append("\n");
            details.append("Resultado ida: ").append(elimination.getResultHome()).append("\n");
            details.append("Resultado vuelta: ").append(elimination.getResultAway()).append("\n\n");
            details.append(elimination.getDescription());

            descriptionArea.setText(details.toString());
            descriptionArea.setCaretPosition(0); // Scroll al inicio
        } else {
            // No hay eliminación seleccionada
            imageLabel.setIcon(null);
            descriptionArea.setText("");
        }
    }

    /**
     * Muestra un diálogo para agregar o editar una eliminación.
     */
    public Elimination showEliminationDialog(Elimination elimination, boolean isEditing) {
        JTextField seasonField = new JTextField(10);
        JTextField phaseField = new JTextField(15);
        JTextField opponentField = new JTextField(15);
        JTextField resultHomeField = new JTextField(10);
        JTextField resultAwayField = new JTextField(10);
        JTextArea descriptionField = new JTextArea(5, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionField);

        JTextField imagePathField = new JTextField(20);
        JButton browseButton = new JButton("Examinar...");

        // Si estamos editando, llenar los campos con los datos existentes
        if (isEditing && elimination != null) {
            seasonField.setText(elimination.getSeason());
            phaseField.setText(elimination.getPhase());
            opponentField.setText(elimination.getOpponent());
            resultHomeField.setText(elimination.getResultHome());
            resultAwayField.setText(elimination.getResultAway());
            descriptionField.setText(elimination.getDescription());
            imagePathField.setText(elimination.getImagePath());
        }

        // Panel para selección de imagen
        JPanel imagePanel = new JPanel(new BorderLayout(5, 0));
        imagePanel.add(imagePathField, BorderLayout.CENTER);
        imagePanel.add(browseButton, BorderLayout.EAST);

        // Manejar evento del botón examinar
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png", "gif"));

            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                imagePathField.setText(selectedFile.getAbsolutePath());
            }
        });

        // Organizar componentes en el panel
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Temporada (YYYY-YYYY):"), gbc);
        gbc.gridx = 1;
        panel.add(seasonField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Fase:"), gbc);
        gbc.gridx = 1;
        panel.add(phaseField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Rival:"), gbc);
        gbc.gridx = 1;
        panel.add(opponentField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Resultado ida:"), gbc);
        gbc.gridx = 1;
        panel.add(resultHomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Resultado vuelta:"), gbc);
        gbc.gridx = 1;
        panel.add(resultAwayField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Imagen:"), gbc);
        gbc.gridx = 1;
        panel.add(imagePanel, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(descScrollPane, gbc);

        // Mostrar diálogo
        String title = isEditing ? "Editar Eliminación" : "Agregar Nueva Eliminación";
        int result = JOptionPane.showConfirmDialog(this, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Validar campos obligatorios
            if (seasonField.getText().trim().isEmpty() ||
                    phaseField.getText().trim().isEmpty() ||
                    opponentField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, complete los campos obligatorios.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            // Crear o actualizar objeto Elimination
            Elimination newElimination = isEditing ? elimination : new Elimination();
            newElimination.setSeason(seasonField.getText().trim());
            newElimination.setPhase(phaseField.getText().trim());
            newElimination.setOpponent(opponentField.getText().trim());
            newElimination.setResultHome(resultHomeField.getText().trim());
            newElimination.setResultAway(resultAwayField.getText().trim());
            newElimination.setDescription(descriptionField.getText().trim());
            newElimination.setImagePath(imagePathField.getText().trim());

            return newElimination;
        }

        return null;
    }

    /**
     * Obtiene el índice de la fila seleccionada en la tabla.
     */
    public int getSelectedEliminationIndex() {
        return eliminationsTable.getSelectedRow();
    }

    /**
     * Convierte el índice de fila visual al índice de modelo en caso de ordenación.
     */
    public int convertRowIndexToModel(int viewIndex) {
        if (viewIndex >= 0) {
            return eliminationsTable.convertRowIndexToModel(viewIndex);
        }
        return -1;
    }

    /**
     * Obtiene el texto de búsqueda.
     */
    public String getSearchText() {
        return searchField.getText();
    }

    /**
     * Obtiene la opción de ordenación seleccionada.
     */
    public int getSelectedSortOption() {
        return sortComboBox.getSelectedIndex();
    }

    /**
     * Selecciona una fila específica en la tabla.
     * @param rowIndex índice de la fila a seleccionar
     */
    public void selectEliminationRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < eliminationsTable.getRowCount()) {
            eliminationsTable.setRowSelectionInterval(rowIndex, rowIndex);
            // Hacer scroll a la fila seleccionada
            eliminationsTable.scrollRectToVisible(
                    eliminationsTable.getCellRect(rowIndex, 0, true));
        }
    }

    // Métodos para agregar listeners a los componentes

    public void addTableSelectionListener(javax.swing.event.ListSelectionListener listener) {
        eliminationsTable.getSelectionModel().addListSelectionListener(listener);
    }

    public void addSearchButtonListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    public void addSortComboBoxListener(ActionListener listener) {
        sortComboBox.addActionListener(listener);
    }

    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addEditButtonListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }

    public void addDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void addNextEliminationButtonListener(ActionListener listener) {
        nextEliminationButton.addActionListener(listener);
    }

    public void addPreviousEliminationButtonListener(ActionListener listener) {
        previousEliminationButton.addActionListener(listener);
    }
}