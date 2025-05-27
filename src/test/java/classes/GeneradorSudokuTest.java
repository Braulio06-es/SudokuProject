package classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneradorSudokuTest {

    @Test
    void generarFacil() {
        //Given
        //No hay precondiciones para este test

        //When
        Sudoku sudoku = GeneradorSudoku.generarFacil();
        //Then
        assertNotNull(sudoku, "El método generarFacil no debe retornar null");

        //Comprobar que el nivel fácil cumple los requisitos

        int vacias = cuentaVacias(sudoku.getTablero());
        // Fácil permite hasta 30 celdas vacías
        assertTrue(vacias <= 30, "El tablero fácil no debe tener más de 30 celdas vacías (encontró " + vacias + ")");
    }

    @Test
    void generarMedio() {
        //Given
        //No hay precondiciones para este test

        //When
        Sudoku sudoku = GeneradorSudoku.generarMedio();
        //Then
        assertNotNull(sudoku, "El método generarFacil no debe retornar null");

        //Comprobar que el nivel medio cumple los requisitos
        int vacias = cuentaVacias(sudoku.getTablero());
        // Medio permite hasta 40 celdas vacías
        assertTrue(vacias <= 40, "El tablero fácil no debe tener más de 30 celdas vacías (encontró " + vacias + ")");
    }

    @Test
    void generarDificil() {
        //Given
        //No hay precondiciones para este test

        //When
        Sudoku sudoku = GeneradorSudoku.generarDificil();
        //Then
        assertNotNull(sudoku, "El método generarFacil no debe retornar null");

        //Comprobar que el nivel difícil cumple los requisitos

        int vacias = cuentaVacias(sudoku.getTablero());
        // Difícil permite hasta 50 celdas vacías
        assertTrue(vacias <= 50, "El tablero fácil no debe tener más de 30 celdas vacías (encontró " + vacias + ")");
    }

    @Test
    void generar() {
        //Dificultad fácil
        //Given
        String dificultad = "fAcil";
        // When
        Sudoku resultado = GeneradorSudoku.generar(dificultad);
        // Then
        assertNotNull(resultado, "La conversión a minúsculas debería tratar 'fAcil' como 'facil'");

        //Dificultad media
        // Given
        dificultad = "MeDiO";
        // When
        resultado = GeneradorSudoku.generar(dificultad);
        // Then
        assertNotNull(resultado, "La conversión a minúsculas debería tratar 'MeDiO' como 'medio'");

        //Dificultad difícil
        //Given
        dificultad = "DiFicil";
        // When
        resultado = GeneradorSudoku.generar(dificultad);
        // Then
        assertNotNull(resultado, "La conversión a minúsculas debería tratar 'DiFicil' como 'dificil'");
    }

    // Método para no repetir este procedimiento en cada test
    private int cuentaVacias(int[][] tablero) {
        int cont = 0;
        for (int[] fila : tablero) {
            for (int v : fila) {
                if (v == 0) cont++;
            }
        }
        return cont;
    }
}