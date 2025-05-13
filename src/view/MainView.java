package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;


public class MainView extends JFrame {
    private JButton btnBubbleSort;
    private JButton btnQuickSort;
    private JButton btnMergeSort;
    private JButton btnInsertionSort;
    private JButton btnSelectionSort;
    private JButton btnExit;

    public MainView() {
        // Window configuration
        setTitle("Algoritmos de Ordenamiento");
        setSize(500, 500); // Aumentado la altura para acomodar 6 botones
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        // Background color
        getContentPane().setBackground(new Color(240, 248, 255));

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(70, 130, 180));
        titlePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("Algoritmos de Ordenamiento");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Seleccione un algoritmo para ejecutar");
        lblSubtitle.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblSubtitle.setForeground(new Color(240, 248, 255));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        titlePanel.add(lblSubtitle);

        add(titlePanel, BorderLayout.NORTH);

        // Center panel with buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel de botones con márgenes mejorados
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Styled buttons - diferentes colores para cada algoritmo
        btnBubbleSort = createStyledButton("Bubble Sort", new Color(41, 128, 185));
        btnQuickSort = createStyledButton("Quick Sort", new Color(46, 134, 193));
        btnMergeSort = createStyledButton("Merge Sort", new Color(52, 152, 219));
        btnInsertionSort = createStyledButton("Insertion Sort", new Color(39, 174, 96));
        btnSelectionSort = createStyledButton("Selection Sort", new Color(155, 89, 182));
        btnExit = createStyledButton("Salir", new Color(231, 76, 60));

        // Añadir botones con márgenes reducidos para que quepan todos
        buttonPanel.add(btnBubbleSort);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio vertical reducido
        buttonPanel.add(btnQuickSort);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio vertical reducido
        buttonPanel.add(btnMergeSort);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio vertical reducido
        buttonPanel.add(btnInsertionSort);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio vertical reducido
        buttonPanel.add(btnSelectionSort);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio vertical reducido
        buttonPanel.add(btnExit);

        centerPanel.add(buttonPanel);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(220, 220, 220));
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblInfo = new JLabel("© 2025 - Sorting Algorithms v1.0 Daniel Velandia 20191020140");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(100, 100, 100));

        bottomPanel.add(lblInfo);
        add(bottomPanel, BorderLayout.SOUTH);

        // Exit button action
        btnExit.addActionListener(e -> System.exit(0));
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 45)); // Altura ligeramente reducida
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Permite que se expanda horizontalmente
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra los botones

        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = backgroundColor;

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });

        return button;
    }

    // Métodos para añadir listeners a cada botón
    public void addBubbleSortListener(ActionListener listener) {
        btnBubbleSort.addActionListener(listener);
    }

    public void addQuickSortListener(ActionListener listener) {
        btnQuickSort.addActionListener(listener);
    }

    public void addMergeSortListener(ActionListener listener) {
        btnMergeSort.addActionListener(listener);
    }

    public void addInsertionSortListener(ActionListener listener) {
        btnInsertionSort.addActionListener(listener);
    }

    public void addSelectionSortListener(ActionListener listener) {
        btnSelectionSort.addActionListener(listener);
    }

    public void showView() {
        setVisible(true);
    }
}