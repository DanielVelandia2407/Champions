package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;

public class InsertionDoubleCircularView extends JFrame {

    private final JButton btnGenerate;
    private final JButton btnSort;
    private final JButton btnBack;
    private final JTable dataTable;
    private final DefaultTableModel tableModel;
    private final JTextField txtNumElements;
    private final JLabel lblResult;
    private final JLabel lblIterations;
    private final JTextArea txtSteps;
    private final JProgressBar progressBar;
    private final JLabel lblStructureInfo;

    public InsertionDoubleCircularView() {
        // Basic window configuration
        setTitle("Insertion Sort - Lista Doble Circular");
        setSize(950, 980);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        // Set soft background color for the entire window
        getContentPane().setBackground(new Color(240, 248, 255));

        // Top panel with title and subtitle
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(70, 130, 180)); // Steel Blue
        titlePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Insertion Sort - Lista Doble Circular");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Navegación bidireccional con estructura circular");
        lblSubtitle.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblSubtitle.setForeground(new Color(240, 248, 255));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Info sobre estructura doble circular
        lblStructureInfo = new JLabel("Nota: Lista circular con enlaces bidireccionales");
        lblStructureInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStructureInfo.setForeground(new Color(255, 255, 255, 180));
        lblStructureInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        titlePanel.add(lblSubtitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(lblStructureInfo);

        add(titlePanel, BorderLayout.NORTH);

        // Center panel with table and control components
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(240, 248, 255));
        centerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(240, 248, 255));

        // Create table model with columns for the politicians
        tableModel = new DefaultTableModel(new Object[]{"ID", "Anterior", "Nombre", "Dinero a Robar ($)", "Siguiente"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        dataTable = new JTable(tableModel);
        dataTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dataTable.setRowHeight(25);
        dataTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        dataTable.getTableHeader().setBackground(new Color(41, 128, 185));
        dataTable.getTableHeader().setForeground(Color.WHITE);
        dataTable.setSelectionBackground(new Color(187, 222, 251));
        dataTable.setSelectionForeground(Color.BLACK);
        dataTable.setGridColor(new Color(189, 195, 199));

        // Center align money values
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        dataTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);

        // Center align ID, Previous and Next
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        dataTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        dataTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        dataTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        // Set column widths
        dataTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        dataTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        dataTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        dataTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        dataTable.getColumnModel().getColumn(4).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 1));

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel(new BorderLayout(10, 10));
        controlPanel.setBackground(new Color(240, 248, 255));
        controlPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Vertical control panel
        JPanel verticalControlPanel = new JPanel();
        verticalControlPanel.setLayout(new BoxLayout(verticalControlPanel, BoxLayout.Y_AXIS));
        verticalControlPanel.setBackground(new Color(240, 248, 255));

        // Panel for generating data
        JPanel generatePanel = createControlPanel();
        JLabel lblNumElements = new JLabel("Número de políticos:");
        lblNumElements.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txtNumElements = new JTextField(10);
        txtNumElements.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNumElements.setText("8"); // Default value

        btnGenerate = createStyledButton("Generar Políticos", new Color(46, 204, 113));

        generatePanel.add(lblNumElements);
        generatePanel.add(txtNumElements);
        generatePanel.add(Box.createHorizontalStrut(10));
        generatePanel.add(btnGenerate);

        verticalControlPanel.add(generatePanel);
        verticalControlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Progress bar panel
        JPanel progressPanel = createControlPanel();
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(46, 204, 113));
        progressBar.setBackground(Color.WHITE);
        progressBar.setString("Listo para ordenar");
        progressBar.setPreferredSize(new Dimension(400, 25));

        progressPanel.add(progressBar);
        verticalControlPanel.add(progressPanel);
        verticalControlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Panel for sorting
        JPanel sortPanel = createControlPanel();
        btnSort = createStyledButton("Ordenar (Insertion Sort Doble Circular)", new Color(41, 128, 185));
        btnSort.setEnabled(false); // Disable until data is generated

        sortPanel.add(btnSort);

        verticalControlPanel.add(sortPanel);
        verticalControlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Results panel
        JPanel resultsPanel = createControlPanel();
        lblResult = new JLabel("");
        lblResult.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblResult.setHorizontalAlignment(SwingConstants.CENTER);

        lblIterations = new JLabel("");
        lblIterations.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblIterations.setHorizontalAlignment(SwingConstants.CENTER);

        resultsPanel.add(lblResult);
        resultsPanel.add(Box.createHorizontalStrut(20));
        resultsPanel.add(lblIterations);

        verticalControlPanel.add(resultsPanel);
        verticalControlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Steps panel
        JPanel stepsPanel = new JPanel(new BorderLayout());
        stepsPanel.setBackground(new Color(240, 248, 255));

        JLabel lblSteps = new JLabel("Pasos del algoritmo:");
        lblSteps.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txtSteps = new JTextArea(5, 0);
        txtSteps.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtSteps.setEditable(false);
        txtSteps.setBackground(Color.WHITE);
        txtSteps.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 1));

        JScrollPane stepsScrollPane = new JScrollPane(txtSteps);
        stepsScrollPane.setPreferredSize(new Dimension(0, 150));

        stepsPanel.add(lblSteps, BorderLayout.NORTH);
        stepsPanel.add(stepsScrollPane, BorderLayout.CENTER);

        verticalControlPanel.add(stepsPanel);
        verticalControlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Button panel for back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 248, 255));

        btnBack = createStyledButton("Volver", new Color(231, 76, 60));
        buttonPanel.add(btnBack);

        verticalControlPanel.add(buttonPanel);

        controlPanel.add(verticalControlPanel, BorderLayout.CENTER);
        centerPanel.add(controlPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with information
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(220, 220, 220));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblInfo = new JLabel("© 2025 - APOCO Sorting Algorithms v1.0 - Lista Doble Circular");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(100, 100, 100));

        bottomPanel.add(lblInfo);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panel.setBackground(new Color(240, 248, 255));
        return panel;
    }

    // Method to create a styled button
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 35));

        return button;
    }

    // Methods to assign external actions to buttons
    public void addGenerateListener(ActionListener listener) {
        btnGenerate.addActionListener(listener);
    }

    public void addSortListener(ActionListener listener) {
        btnSort.addActionListener(listener);
    }

    public void addBackListener(ActionListener listener) {
        btnBack.addActionListener(listener);
    }

    // Method to get number of elements
    public String getNumElements() {
        return txtNumElements.getText().trim();
    }

    // Method to enable/disable sort button
    public void setSortButtonEnabled(boolean enabled) {
        btnSort.setEnabled(enabled);
    }

    // Method to display result message
    public void setResultMessage(String message, boolean isSuccess) {
        lblResult.setText(message);
        lblResult.setForeground(isSuccess ? new Color(46, 125, 50) : new Color(198, 40, 40));
    }

    // Method to display iterations count
    public void setIterationsMessage(String iterations) {
        lblIterations.setText(iterations);
        lblIterations.setForeground(new Color(41, 128, 185));
    }

    // Method to populate the table with values
    public void setTableData(Object[][] data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    // Method to add step to the steps area
    public void addStep(String step) {
        txtSteps.append(step + "\n");
    }

    // Method to clear steps
    public void clearSteps() {
        txtSteps.setText("");
    }

    // Method to highlight a specific row in the table
    public void highlightRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < dataTable.getRowCount()) {
            dataTable.setRowSelectionInterval(rowIndex, rowIndex);
            dataTable.scrollRectToVisible(dataTable.getCellRect(rowIndex, 0, true));
        }
    }

    // Method to update progress bar
    public void updateProgress(int current, int total) {
        int percentage = (int) ((current * 100.0) / total);
        progressBar.setValue(percentage);
        progressBar.setString("Progreso: " + percentage + "%");
    }

    // Method to reset progress bar
    public void resetProgress() {
        progressBar.setValue(0);
        progressBar.setString("Listo para ordenar");
    }

    // Method to set final progress
    public void setProgressComplete() {
        progressBar.setValue(100);
        progressBar.setString("¡Ordenamiento completado!");
    }

    // Method to show the window
    public void showWindow() {
        setVisible(true);
    }
}