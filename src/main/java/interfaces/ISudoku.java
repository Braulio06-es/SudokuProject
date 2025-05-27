package interfaces;

public interface ISudoku {
    public int[][] getTablero();
    void generarTablero(String dificultad);
    private boolean fillBoard(){return false;};
    private boolean esValido(int fila, int col, int val) {return false;};
    private int[] shuffleArray() {return null;};
    boolean esMovimientoValido(int fila, int col, int val);
    boolean colocarNumero(int fila, int col, int val);
    boolean estaResuelto();
    void mostrarTablero();

}
