    package classes;

    import interfaces.IGeneradorSudoku;

    public class GeneradorSudoku implements IGeneradorSudoku {

        //Generar el sudoku con distintas dificultades
        public static Sudoku generarFacil() { return crear("facil"); }
        public static Sudoku generarMedio() { return crear("medio"); }
        public static Sudoku generarDificil() { return crear("dificil"); }
        private static Sudoku crear(String dif) {
            Sudoku s = new Sudoku(); s.generarTablero(dif); return s;
        }
        public static Sudoku generar(String d) { return switch(d.toLowerCase()) {
            case "facil"-> generarFacil();
            case "medio"-> generarMedio();
            case "dificil"-> generarDificil();
            default-> throw new IllegalArgumentException();
        }; }
    }
