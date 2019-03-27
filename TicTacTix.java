import java.util.Random;
import java.util.Scanner;

/**
 * class for the Tic-Tactix game. This class will instantiate the
 * game board and will handle all moves made (both by the user and the computer).
 * It also retains the state of the game board and determines if a player had won the
 * game
 *
 * @author Nishkrit Desai
 * @version 20th March 2019
 *
 */
public class TicTacTix {
    /**
     * Default constructor for the TicTacTix game board
     */
    private char[][][] board;

    public TicTacTix () {
        board = new char[3][3][3];
    }

    public String toString() {
        String out = "";
        String colNums = "  1 0 2   1 0 2   1 0 2 \n";

        return out;
    }

    public void playerMove() {
        makeMove(PLAYER);
    }

    public void computerMove() {
        makeMove(COMPUTER);
    }

    public void makeMove(int player) {
        int layer, column, row;

        // Ensure that the move made (either by player or computer) is valid
        do {
            if (player == PLAYER) {
                layer = getInput(LAYER);
                row = getInput(ROW);
                column = getInput(COLUMN);
            }
            else if (player == COMPUTER) {
                layer = randInt(0, 2);
                row = randInt(0, 2);
                column = randInt(0, 2);
            }
            boolean validMove = validateMove(layer, row, column);
        } while (!validMove);

        // Fill the grid with the appropriate character
        fillGrid(layer, row, column, player);
    }



}
