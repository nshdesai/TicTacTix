import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.Arrays;


/**
 * class for the Tic-Tactix game. This class will instantiate the
 * game board and will handle all moves made (both by the user and the computer).
 * It also retains the state of the game board, determines if a player won the
 * game and in general, handles all actions within a game of TicTacTix.
 *
 * @author Nishkrit Desai
 * @version 20th March 2019
 *
 */
public class TicTacTix {
   private char[][][] board;

    // A few constants used to make the logic more transparent
    public static final char PLAYER = 'X';
    public static final char COMPUTER = 'O';
    // Game states
    public static final char STALEMATE = 'S';
    public static final char PLAYABLE = 'P';

    private static final int LAYER = 0;
    private static final int ROW = 1;
    private static final int COLUMN = 2;

    // Scanner to get input from user
    public static final Scanner input = new Scanner(System.in);

    public TicTacTix () {
        board = new char[3][3][3];
        board[1][1][1] = 'N'; // The center cell is inaccessible
    }

    public String toString() {
        String out = "";

        for (char[][] layer: board) {
            out += "LAYER: \n";

            for (char[] row: layer) {
                out += Arrays.toString(row) + "\n";
            }

            out += "***************** \n\n";
        }

        return out;
    }

    public void makeMove(char player) {
        int layer = 0;
        int row = 0;
        int column = 0;
        boolean validMove;

        // Ensure that the coordinates for move made (either by player or computer) is valid
        do {
            // Get the coordinates from player
            int[] coords = pickCoords(player);
            // "Disassemble coords from array"
            layer = coords[LAYER];
            row = coords[ROW];
            column = coords[COLUMN];
            // Check if the move is valid
            validMove = validateMove(layer, row, column, player);
        } while (!validMove);

        // Fill the grid with the appropriate character
        board[layer][row][column] = player;
    }

    private int[] pickCoords(char player) {
        // Coords array: [LAYER, ROW, COLUMN]
        int[] coords = {0, 0, 0};

        if (player == PLAYER) {
            coords[LAYER] = getCoordInput(LAYER) - 1;
            coords[ROW] = getCoordInput(ROW) - 1;
            coords[COLUMN] = getCoordInput(COLUMN) - 1;
        }

        else if (player == COMPUTER) {
            coords[LAYER] = randInt(0, 2);
            coords[ROW] = randInt(0, 2);
            coords[COLUMN] = randInt(0, 2);
        }
        // Just in case an attempt is made to pass in character other than X/O
        else {
            System.out.println("ERROR: Invalid player. Fatal. Exiting.");
            System.exit(0);
        }

        return coords;
    }


    private boolean validateMove(int layer, int row, int col, char player) {
        // \u0000 (null character) is the default char value in Java
        if (board[layer][row][col] != '\u0000') {
            // Only be verbose when the user picks a cell that's taken
            if (player == PLAYER) {
                System.out.println("This cell is already taken. Please pick another one");
            }
            return false;
        }
        return true;
    }

    /**
     * Helper function to get a random number within a given range
     * Not inclusive of the max limit i.e. x ϵ [min, max)
     * @param  min Minimum value of the random number
     * @param  max Maximum value of the random number (not inclusive)
     * @return     A random integer in the specified limit
     */
    private static int randInt(int min, int max){
        return ((int) (Math.random() * (max - min))) + min; // random() generates a number between 0 and 1
    }

    private int getCoordInput(int inputType) {
        int coord = -1;
        boolean validCoord = false;

        while (!validCoord) {
            try {
                printPrompt(inputType);
                coord = input.nextInt();
                validCoord = validateCoord(coord);
            }
            catch(InputMismatchException exp) {
                System.out.println("Please enter an integer.");
            }
            // Clear the input stream everytime (otherwise "<int> <int> <int>" is read as valid input)
            input.nextLine();
        }

        return coord;
    }

    private boolean validateCoord(int coord) {
        if ((coord >= 1) && (coord <= 3)) {
            return true;
        }
        else {
            System.out.println("Coordinate is out of bounds. Please enter a value between 1 and 3");
            return false;
        }
    }

    private void printPrompt(int inputType) {
        String prompt = "Make move on ";

        switch (inputType) {
            case LAYER:
                prompt += "layer #: ";
                break;
            case ROW:
                prompt += "row #: ";
                break;
            case COLUMN:
                prompt += "column #: ";
                break;
        }
        System.out.print(prompt);
    }

    public char findGameState() {
        char[] symbols = {'X', 'O'};
        boolean diagonals, streak;

        if (!checkStalemate()) {
            for (char symbol: symbols) {
                diagonals = checkDiagonals(symbol);
                streak = checkAllStreaks(symbol);

                if (diagonals || streak) {
                    return symbol; // Return winner
                }
            }
            return PLAYABLE;
        }
        return STALEMATE;
    }

    private boolean checkDiagonals(char symbol) {
        
    }

    private boolean checkAllStreaks(char symbol) {
        int streak = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (checkStreak(x, y, symbol, LAYER))
                    return true;
                if (checkStreak(x, y, symbol, ROW))
                    return true;
                if (checkStreak(x, y, symbol, COLUMN))
                    return true;
            }
        }
        return false;
    }

    public boolean checkStreak(int x, int y, char symbol, int type) {
        int streak = 0;

        for (int count = 0; count < 3; count++) {
            if (type == LAYER) {
                if (board[count][x][y] == symbol)
                    streak++;
            }
            else if (type == ROW) {
                if (board[x][count][y] == symbol)
                    streak++;
            }
            else if (type == COLUMN) {
                if (board[x][y][count] == symbol)
                    streak++;
            }
        }

        if (streak == 3)
            return true;
        return false;
    }

    private boolean checkStalemate() {
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    if (board[x][y][z] == '\u0000')
                        return false;
                }
            }
        }
        return true;
    }

}
