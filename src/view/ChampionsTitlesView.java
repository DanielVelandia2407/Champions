package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ChampionsTitlesView extends JFrame {
    private JTable teamsTable;
    private DefaultTableModel tableModel;
    private JButton btnAddTeam;
    private JButton btnEditTeam;
    private JButton btnDeleteTeam;
    private JButton btnBack;
    private JTextField searchField;
    private JButton btnSearch;
    private JTextArea detailsArea;
    private JComboBox<String> sortOptionComboBox;

    public ChampionsTitlesView() {
        // Configuración de la ventana
        setTitle("Champions League - Equipos Campeones");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 248, 255));

        // Panel de título
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        // Panel central con tabla y detalles
        JPanel centralPanel = new JPanel(new BorderLayout(10, 10));
        centralPanel.setBackground(new Color(240, 248, 255));
        centralPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Panel de búsqueda y opciones
        JPanel searchPanel = createSearchPanel();
        centralPanel.add(searchPanel, BorderLayout.NORTH);

        // Panel con la tabla de equipos
        JPanel tablePanel = createTablePanel();
        centralPanel.add(tablePanel, BorderLayout.CENTER);

        // Panel de detalles del equipo seleccionado
        JPanel detailsPanel = createDetailsPanel();
        centralPanel.add(detailsPanel, BorderLayout.EAST);

        add(centralPanel, BorderLayout.CENTER);

        // Panel de botones de acción
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(70, 130, 180));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("Listado de Equipos Campeones de Champions League");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Ordenados por número de títulos");
        lblSubtitle.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblSubtitle.setForeground(new Color(240, 248, 255));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblSubtitle);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(240, 248, 255));

        JLabel lblSearch = new JLabel("Buscar equipo:");
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        searchField = new JTextField(20);
        btnSearch = new JButton("Buscar");
        btnSearch.setBackground(new Color(70, 130, 180));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);

        JLabel lblSort = new JLabel("Ordenar por:");
        lblSort.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        String[] sortOptions = {"Títulos (mayor a menor)", "Títulos (menor a mayor)", "Alfabético (A-Z)", "Alfabético (Z-A)"};
        sortOptionComboBox = new JComboBox<>(sortOptions);
        sortOptionComboBox.setPreferredSize(new Dimension(180, 25));

        panel.add(lblSearch);
        panel.add(searchField);
        panel.add(btnSearch);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(lblSort);
        panel.add(sortOptionComboBox);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));

        // Crear modelo de tabla
        String[] columnNames = {"Equipo", "Títulos", "Último Título", "Goleador (Último título)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };

        teamsTable = new JTable(tableModel);
        teamsTable.setRowHeight(25);
        teamsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        teamsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        teamsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teamsTable.setAutoCreateRowSorter(true);

        // Hacer que la tabla ocupe la mayor parte del espacio
        teamsTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(teamsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                new EmptyBorder(10, 10, 10, 10)));
        panel.setPreferredSize(new Dimension(300, 0));

        JLabel lblDetails = new JLabel("Detalles del Equipo");
        lblDetails.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDetails.setHorizontalAlignment(SwingConstants.CENTER);

        detailsArea = new JTextArea();
        detailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        JScrollPane detailsScroll = new JScrollPane(detailsArea);

        panel.add(lblDetails, BorderLayout.NORTH);
        panel.add(detailsScroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(new Color(220, 220, 220));

        btnAddTeam = new JButton("Agregar Equipo");
        styleButton(btnAddTeam, new Color(46, 139, 87));

        btnEditTeam = new JButton("Editar Equipo");
        styleButton(btnEditTeam, new Color(70, 130, 180));

        btnDeleteTeam = new JButton("Eliminar Equipo");
        styleButton(btnDeleteTeam, new Color(178, 34, 34));

        btnBack = new JButton("Volver al Menú");
        styleButton(btnBack, new Color(100, 100, 100));

        panel.add(btnAddTeam);
        panel.add(btnEditTeam);
        panel.add(btnDeleteTeam);
        panel.add(btnBack);

        return panel;
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));

        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = color;

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
    }

    // Métodos para añadir datos a la tabla
    public void setTeamsData(Object[][] data) {
        tableModel.setRowCount(0); // Limpiar tabla
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    // Método para mostrar detalles de un equipo
    public void showTeamDetails(String details) {
        detailsArea.setText(details);
    }

    // Métodos para agregar listeners
    public void addBackButtonListener(ActionListener listener) {
        btnBack.addActionListener(listener);
    }

    public void addAddTeamButtonListener(ActionListener listener) {
        btnAddTeam.addActionListener(listener);
    }

    public void addEditTeamButtonListener(ActionListener listener) {
        btnEditTeam.addActionListener(listener);
    }

    public void addDeleteTeamButtonListener(ActionListener listener) {
        btnDeleteTeam.addActionListener(listener);
    }

    public void addSearchButtonListener(ActionListener listener) {
        btnSearch.addActionListener(listener);
    }

    public void addSortComboBoxListener(ActionListener listener) {
        sortOptionComboBox.addActionListener(listener);
    }

    public void addTableSelectionListener(ListSelectionListener listener) {
        teamsTable.getSelectionModel().addListSelectionListener(listener);
    }

    // Métodos para obtener valores
    public String getSearchText() {
        return searchField.getText();
    }

    public int getSelectedSortOption() {
        return sortOptionComboBox.getSelectedIndex();
    }

    public int getSelectedTeamRow() {
        return teamsTable.getSelectedRow();
    }

    public JTable getTeamsTable() {
        return teamsTable;
    }

    public void showView() {
        setVisible(true);
    }

    public void closeView() {
        setVisible(false);
        dispose();
    }
}