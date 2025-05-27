package classes;// Clase classe.Sudoku: Lógica de generación y validación de tableros classe.Sudoku
import classes.exceptions.CeldasVaciasIncompatiblesException;
import interfaces.ISudoku;

import java.util.Random;

public class Sudoku implements ISudoku {
    private int[][] tablero;         // Matriz 9x9 que almacena valores del tablero
    private boolean[][] celdasFijas; // Indica qué casillas no pueden modificarse
    private static final int SIZE = 9; // Tamaño del tablero
    private Random random = new Random(); // Generador de aleatorios

    // Constructor: inicializa tablero y matriz de celdas fijas
    public Sudoku() {
        tablero = new int[SIZE][SIZE];
        celdasFijas = new boolean[SIZE][SIZE];
    }

    // Devuelve la referencia al tablero para lecturas (no modificar directamente)
    public int[][] getTablero() {
        return tablero;
    }

    /**
     * Genera un tablero completo (llenado por backtracking)
     * y elimina casillas según dificultad: "facil", "medio", "dificil".
     */
    public void generarTablero(String dificultad) {
        fillBoard(); // Paso 1: llenar todo el tablero de forma válida

        // Determina cuántas casillas vaciar según la dificultad
        int vaciar = switch (dificultad.toLowerCase()) {
            case "facil"  -> 30;
            case "medio"  -> 40;
            case "dificil"-> 50;
            default        -> 40;
        };

        // Marca todas las casillas como fijas inicialmente
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                celdasFijas[i][j] = true;
            }
        }

        // Paso 2: eliminar aleatoriamente 'vaciar' celdas
        int eliminadas = 0;
        while (eliminadas < vaciar) {
            int fila = random.nextInt(SIZE);
            int col  = random.nextInt(SIZE);
            if (tablero[fila][col] != 0) {
                tablero[fila][col] = 0;         // Vacía la casilla
                celdasFijas[fila][col] = false; // Ya no es fija
                eliminadas++;
            }
        }
        // Verificar número de casillas vacías
        int contadorVacias = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (tablero[i][j] == 0) contadorVacias++;
            }
        }
        if (contadorVacias != vaciar) {
            try {
                throw new CeldasVaciasIncompatiblesException(
                        "Se esperaban " + vaciar + " celdas vacías para dificultad '" +
                                dificultad + "', pero se encontraron " + contadorVacias + "."
                 );
            } catch (CeldasVaciasIncompatiblesException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Rellena recursivamente el tablero usando backtracking.
     * Devuelve true al completarse con éxito.
     */
    private boolean fillBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (tablero[i][j] == 0) {
                    // Prueba números del 1 al 9 en orden aleatorio
                    for (int num : shuffleArray()) {
                        if (esValido(i, j, num)) {
                            tablero[i][j] = num;
                            if (fillBoard()) return true; // Continúa rellenando
                            tablero[i][j] = 0;          // Deshace si falla
                        }
                    }
                    return false; // Ningún número válido en esta casilla
                }
            }
        }
        return true; // No hay casillas vacías, tablero completo
    }

    /**
     * Comprueba sin mensajes si 'val' cabe en (fila,col) según reglas classe.Sudoku.
     */
    private boolean esValido(int fila, int col, int val) {
        // Chequea fila y columna
        for (int k = 0; k < SIZE; k++) {
            if (tablero[fila][k] == val || tablero[k][col] == val) {
                return false;
            }
        }
        // Chequea subcuadrícula 3x3
        int startRow = (fila / 3) * 3;
        int startCol = (col  / 3) * 3;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (tablero[startRow + r][startCol + c] == val) {
                    return false;
                }
            }
        }
        return true; // Cumple todas las comprobaciones
    }

    /**
     * Genera un array 1..9 en orden aleatorio (Fisher–Yates shuffle).
     */
    private int[] shuffleArray() {
        int[] nums = new int[SIZE];
        for (int i = 0; i < SIZE; i++) nums[i] = i + 1;
        for (int i = SIZE - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int tmp = nums[i]; nums[i] = nums[j]; nums[j] = tmp;
        }
        return nums;
    }

    /**
     * Valida un movimiento de usuario mostrando un mensaje en caso de error.
     */
    public boolean esMovimientoValido(int fila, int col, int val) {
        if (!esValido(fila, col, val)) {
            System.out.println("Error: Movimiento invalido en fila, columna o subcuadricula.");
            return false;
        }
        return true;
    }

    /**
     * Intenta colocar 'val' en (fila,col) si no es celda fija.
     * Devuelve true y muestra confirmación en consola.
     */
    public boolean colocarNumero(int fila, int col, int val) {
        if (fila < 0 || fila >= SIZE || col < 0 || col >= SIZE) {
            System.out.println("Error: fila/columna fuera de rango.");
            return false;
        }
        if (celdasFijas[fila][col]) {
            System.out.println("Error: celda fija.");
            return false;
        }
        if (!esMovimientoValido(fila, col, val)) return false;
        tablero[fila][col] = val;
        System.out.println("Numero colocado correctamente.");
        return true;
    }

    /**
     * Comprueba si el classe.Sudoku está completo y válido.
     */
    public boolean estaResuelto() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (tablero[i][j] == 0 || !esValido(i, j, tablero[i][j])) {
                    return false;
                }
            }
        }
        System.out.println("Se ha resuelto el classe.Sudoku.");
        return true;
    }

    /**
     * Muestra el tablero en consola con divisiones 3x3.
     */
    public void mostrarTablero() {
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0) System.out.println(
                    "+-------+-------+-------+"
            ); // Límite horizontal de bloques
            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0) System.out.print("| "); // Límite vertical de bloques
                int v = tablero[i][j];
                System.out.print(v == 0 ? ". " : v + " "); // Punto para vacíos
            }
            System.out.println("|");
        }
        System.out.println("+-------+-------+-------+");
    }
}