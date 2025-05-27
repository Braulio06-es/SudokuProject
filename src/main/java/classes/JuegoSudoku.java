package classes;

import classes.exceptions.DificultadDefaultException;

import javax.swing.*;
import java.util.Scanner;

public class JuegoSudoku {
    //classe.Sudoku en la terminal
    public void iniciar() {
        Scanner sc = new Scanner(System.in);
        // Saludo inicial al usuario
        System.out.println("Bienvenido al classe.Sudoku");
        // Pide nivel de dificultad
        System.out.print("Dificultad (facil, medio, dificil): ");
        String dif = sc.nextLine().trim().toLowerCase();

        Sudoku s;
        try {
            // Genera tablero según dificultad
            s = GeneradorSudoku.generar(dif);
        } catch (DificultadDefaultException e) {
            // Caída a dificultad media si la entrada es inválida
            s = GeneradorSudoku.generarMedio();
        }


        // Bucle de juego: hasta que el classe.Sudoku esté completo
        while (true) {
            s.mostrarTablero();  // Imprime tablero en consola

            if (s.estaResuelto()) {
                // Mensaje Swing cuando se resuelve correctamente
                JOptionPane.showMessageDialog(
                        null,
                        "¡Has completado el sudoku!",
                        "Fin",
                        JOptionPane.INFORMATION_MESSAGE
                );
                break;
            }

            // Solicita jugada: fila, columna y valor
            System.out.print("fila col valor: ");
            String[] p = sc.nextLine().split("\\s+");
            if (p.length != 3) {
                // Validación de formato (3 números separados)
                System.out.println("Formato inválido. Introduce tres números.");
                continue;
            }
            try {
                // Ajuste de índices (usuario 1-9 a 0-8)
                int fila = Integer.parseInt(p[0]) - 1;
                int col  = Integer.parseInt(p[1]) - 1;
                int val  = Integer.parseInt(p[2]);
                // Intenta colocar el número y guarda el resultado
                boolean exito = s.colocarNumero(fila, col, val);
                if (exito) {
                    // Muestra de nuevo el tablero después de un movimiento válido
                    s.mostrarTablero();
                }
            } catch (NumberFormatException ex) {
                // Manejo de entradas no numéricas
                System.out.println("Error de entrada. Usa números del 1 al 9.");
            }
        }

        // Cierra scanner para evitar fugas
        sc.close();

    }

    public static void main(String[] args) {
        new JuegoSudoku().iniciar();

    }
}
