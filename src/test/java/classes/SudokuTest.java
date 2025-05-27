package classes;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class SudokuTest {

    @Test
    void getTablero() {
        // Given
        Sudoku sudoku = new Sudoku();

        // When
        int[][] tablero = sudoku.getTablero();

        // Then
        assertNotNull(tablero, "El tablero no debe ser null");
        assertEquals(9, tablero.length, "El tablero debe tener 9 filas");
        for (int i = 0; i < 9; i++) {
            assertEquals(9, tablero[i].length, "Cada fila del tablero debe tener 9 columnas");
            for (int j = 0; j < 9; j++) {
                assertEquals(0, tablero[i][j],
                        String.format("La posición (%d,%d) debe inicializarse a 0", i, j));
            }
        }
    }

    @Test
    void generarTablero() {
        // Given
        Sudoku sudoku = new Sudoku();

        // When

        // Then
        assertThrows(NullPointerException.class,
                () -> sudoku.generarTablero(null),
                "Pasar null como dificultad debe lanzar NullPointerException");
    }

    @Test
    void esMovimientoValido() {
        // Given
        Sudoku sudoku = new Sudoku(); // tablero vacío, todo 0

        // When
        boolean resultado = sudoku.esMovimientoValido(0, 0, 5);

        // Then
        assertTrue(resultado, "En un tablero vacío, colocar un 5 en (0,0) debe ser válido");
    }

    @Test
    void colocarNumero() {
        // Given
        Sudoku sudoku = new Sudoku();  // tablero vacío, sin celdas fijas
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream respaldo = System.out;
        System.setOut(new PrintStream(salida));

        // When
        boolean resultado = sudoku.colocarNumero(0, 0, 5);

        // Then
        assertTrue(resultado, "Debe devolver true al colocar un número válido en una celda vacía");
        assertTrue(salida.toString().contains("Numero colocado correctamente."),
                "Debe imprimir 'Numero colocado correctamente.'");

        // restaurar salida
        System.setOut(respaldo);
    }

    @Test
    void estaResuelto() {
        // Given
        Sudoku sudoku = new Sudoku(); // tablero recién creado, todo a 0

        // When
        boolean resultado = sudoku.estaResuelto();

        // Then
        assertFalse(resultado, "Un tablero vacío no debe considerarse resuelto");
    }

    @Test
    void mostrarTablero() {
        // Given
        Sudoku sudoku = new Sudoku();  // tablero vacío
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream respaldo = System.out;
        System.setOut(new PrintStream(salida));

        // When
        sudoku.mostrarTablero();

        // Then
        String console = salida.toString();
        // Debe contener la línea horizontal de bloques
        assertTrue(console.contains("+-------+-------+-------+"),
                "Debe imprimir separadores horizontales '+-------+-------+-------+'");
        // Debe contener al menos una línea de celdas con puntos para vacíos
        assertTrue(console.contains("| . . . "),
                "Debe imprimir filas con '| ' seguido de '. ' para celdas vacías");

    }
}