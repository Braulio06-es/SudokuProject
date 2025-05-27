package classes;

import javax.swing.*;

public class Main {
    // Método main para iniciar la aplicación con diálogo de dificultad
    public static void main(String[] args) {
        String[] opciones = {"facil", "medio", "dificil"};
        String seleccion = (String) JOptionPane.showInputDialog(
                null,
                "Selecciona dificultad:",
                "Clase Sudoku",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                "medio"
        );
        String dificultad = (seleccion == null) ? "medio" : seleccion;

        // Ejecuta la GUI en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> new SudokuGUI(dificultad));

    }
}
