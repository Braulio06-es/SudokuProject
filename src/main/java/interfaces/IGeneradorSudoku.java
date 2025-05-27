package interfaces;

import classes.Sudoku;

public interface IGeneradorSudoku {
    static Sudoku generarFacil(){return null;};
    static Sudoku generarMedio(){return null;};
    static Sudoku generarDificil(){return null;};
    private static Sudoku crear(String dif) {return null;};
    static Sudoku generar(String d) {return null;};
}
