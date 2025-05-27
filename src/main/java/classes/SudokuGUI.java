package classes;

import interfaces.ISudokuGUI;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SudokuGUI extends JFrame implements ISudokuGUI {
    private Sudoku sudoku;
    private JTextField[][] celdas;
    private JLabel timerLabel;
    private JLabel errorsLabel;
    private JLabel hintsLabel;
    private int errorsCount = 0;
    private int hintsLeft = 3;
    private Timer timer;
    private int secondsElapsed = 0;
    private Random random = new Random();

    public SudokuGUI(String dif) {
        sudoku = GeneradorSudoku.generar(dif);
        celdas = new JTextField[9][9];
        initUI(dif);
        startTimer();
    }

    private void initUI(String dificultadInicial) {
        setTitle("classe.Sudoku");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        timerLabel = new JLabel("Tiempo: 00:00");
        errorsLabel = new JLabel("Errores: 0");
        hintsLabel = new JLabel("Pistas: " + hintsLeft);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        // Selector de dificultad
        String[] opciones = {"facil", "medio", "dificil"};
        JComboBox<String> difficultyCombo = new JComboBox<>(opciones);
        difficultyCombo.setSelectedItem(dificultadInicial);
        difficultyCombo.addActionListener(e -> {
            String dif = (String) difficultyCombo.getSelectedItem();
            SwingUtilities.invokeLater(() -> {
                dispose();
                new SudokuGUI(dif);
            });
        });
        topPanel.add(new JLabel("Dificultad:"));
        topPanel.add(difficultyCombo);

        topPanel.add(timerLabel);
        topPanel.add(errorsLabel);
        topPanel.add(hintsLabel);

        JButton nuevoTableroButton = new JButton("Nuevo Tablero");
        nuevoTableroButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                dispose();
                new SudokuGUI(dificultadInicial);
            });
        });
        topPanel.add(nuevoTableroButton);

        JButton hintButton = new JButton("Pista");
        hintButton.addActionListener(e -> {
            if (hintsLeft <= 0) return;
            List<int[]> empties = new ArrayList<>();
            int[][] board = sudoku.getTablero();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (board[i][j] == 0) empties.add(new int[]{i, j});
                }
            }
            if (empties.isEmpty()) return;
            int[] cell = empties.get(random.nextInt(empties.size()));
            int r = cell[0], c = cell[1];
            try {
                Sudoku sol = new Sudoku();
                Method fill = Sudoku.class.getDeclaredMethod("fillBoard");
                fill.setAccessible(true);
                fill.invoke(sol);
                int val = sol.getTablero()[r][c];
                sudoku.colocarNumero(r, c, val);
                celdas[r][c].setText(String.valueOf(val));
                celdas[r][c].setEditable(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            hintsLeft--;
            hintsLabel.setText("Pistas: " + hintsLeft);
            if (hintsLeft == 0) hintButton.setEnabled(false);
        });
        topPanel.add(hintButton);

        add(topPanel, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(9, 9));
        Font font = new Font("SansSerif", Font.BOLD, 20);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField tf = new JTextField();
                tf.setFont(font);
                tf.setHorizontalAlignment(JTextField.CENTER);
                int val = sudoku.getTablero()[i][j];
                tf.setText(val != 0 ? String.valueOf(val) : "");
                tf.setEditable(val == 0);
                ((AbstractDocument) tf.getDocument()).setDocumentFilter(new DocumentFilter() {
                    @Override
                    public void replace(FilterBypass fb, int offs, int len, String str, AttributeSet a) throws BadLocationException {
                        if (str != null && str.matches("[1-9]") && fb.getDocument().getLength() + str.length() - len <= 1) {
                            super.replace(fb, offs, len, str, a);
                        }
                    }
                });
                tf.setBackground(((i + j) % 2 == 0) ? Color.WHITE : new Color(245, 245, 245));
                int top = (i % 3 == 0) ? 4 : 1;
                int left = (j % 3 == 0) ? 4 : 1;
                int bottom = (i == 8) ? 4 : 1;
                int right = (j == 8) ? 4 : 1;
                tf.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.DARK_GRAY));

                final int row = i, col = j;
                tf.addFocusListener(new FocusAdapter() {
                    Set<JTextField> highlighted = new HashSet<>();
                    @Override
                    public void focusGained(FocusEvent e) {
                        highlight(row, col);
                    }
                    @Override
                    public void focusLost(FocusEvent e) {
                        resetHighlights();
                    }
                    private void highlight(int r, int c) {
                        int blockRow = (r / 3) * 3;
                        int blockCol = (c / 3) * 3;
                        for (int k = 0; k < 9; k++) {
                            celdas[r][k].setBackground(Color.LIGHT_GRAY);
                            highlighted.add(celdas[r][k]);
                            celdas[k][c].setBackground(Color.LIGHT_GRAY);
                            highlighted.add(celdas[k][c]);
                        }
                        for (int bi = 0; bi < 3; bi++) {
                            for (int bj = 0; bj < 3; bj++) {
                                JTextField cell = celdas[blockRow + bi][blockCol + bj];
                                cell.setBackground(Color.LIGHT_GRAY);
                                highlighted.add(cell);
                            }
                        }
                    }
                    private void resetHighlights() {
                        for (JTextField cell : highlighted) {
                            int r = -1, c = -1;
                            outer: for (int ii = 0; ii < 9; ii++) {
                                for (int jj = 0; jj < 9; jj++) {
                                    if (celdas[ii][jj] == cell) { r = ii; c = jj; break outer; }
                                }
                            }
                            boolean editable = sudoku.getTablero()[r][c] == 0;
                            cell.setBackground((r + c) % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                        }
                        highlighted.clear();
                    }
                });
                tf.addActionListener(e -> {
                    try {
                        int v = Integer.parseInt(tf.getText());
                        if (!sudoku.colocarNumero(row, col, v)) {
                            errorsCount++;
                            errorsLabel.setText("Errores: " + errorsCount);
                            JOptionPane.showMessageDialog(this,
                                    "Movimiento inválido",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            tf.setText("");
                        } else if (sudoku.estaResuelto()) {
                            timer.stop();
                            JOptionPane.showMessageDialog(this,
                                    "¡Completado!",
                                    "Fin",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        errorsCount++;
                        errorsLabel.setText("Errores: " + errorsCount);
                        JOptionPane.showMessageDialog(this,
                                "Introduce un número entre 1 y 9",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        tf.setText("");
                    }
                });
                celdas[i][j] = tf;
                grid.add(tf);
            }
        }
        add(grid, BorderLayout.CENTER);
        pack();
        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startTimer() {
        timer = new Timer(1000, e -> {
            secondsElapsed++;
            int mins = secondsElapsed / 60;
            int secs = secondsElapsed % 60;
            timerLabel.setText(String.format("Tiempo: %02d:%02d", mins, secs));
        });
        timer.start();
    }

    private void resetBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int val = sudoku.getTablero()[i][j];
                celdas[i][j].setText(val != 0 ? String.valueOf(val) : "");
                celdas[i][j].setEditable(val == 0);
            }
        }
    }

    private void resetStats() {
        secondsElapsed = 0;
        timerLabel.setText("Tiempo: 00:00");
        errorsCount = 0;
        hintsLeft = 3;
        hintsLabel.setText("Pistas: " + hintsLeft);
        errorsLabel.setText("Errores: 0");
        if (timer != null) timer.restart();
    }
}
