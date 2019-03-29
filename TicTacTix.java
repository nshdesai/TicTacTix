import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

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
    // Only moves and input within the game (not the interface)
    private static final Scanner input = new Scanner(System.in);

    public TicTacTix () {
        board = new char[3][3][3];
        initBoard();
        board[1][1][1] = 'N'; // The center cell is inaccessible
    }

    /**
     * \u0000 (null character) is the default char value in Java
     * Some terminals will handle printing this character differently
     * This method was written to avoid variation in how the board is printed
     * by filling the array with spaces.
     */
    private void initBoard() {
        for (char[][] layer: board) {
            for (char[] row: layer)
                Arrays.fill(row, ' ');
        }
    }

    /**
     * Method to handle all the moves in the game. This method is used by the Test program
     * to makes moves for either player.
     * @param player the character constant for a player (PLAYER / COMPUTER)
     */
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

    /**
     * A helper method to simply pick a set of coordinates to make the next move on.
     * Does not check if the coordinates produce a valid move. Simply returns a valid
     * set of coordinates
     * @param  player Character constanst for the move-making player
     * @return        an integer array of length 3 containing the coordinates of the move made (formatted as [layer, row, column])
     */
    private int[] pickCoords(char player) {
        // Coords array: [LAYER, ROW, COLUMN]
        int[] coords = {0, 0, 0};

        if (player == PLAYER) {
            // Grid in game is numbered 1 -> 3 NOT 0 -> 2
            coords[LAYER] = getCoordInput(LAYER) - 1;
            coords[ROW] = getCoordInput(ROW) - 1;
            coords[COLUMN] = getCoordInput(COLUMN) - 1;
        }

        // Computer randomly picks a coordinate
        else if (player == COMPUTER) {
            coords[LAYER] = randInt(0, 3);
            coords[ROW] = randInt(0, 3);
            coords[COLUMN] = randInt(0, 3);
        }
        // Just in case an attempt is made to pass in character other than X/O
        else {
            System.out.println("ERROR: Invalid player. Fatal. Exiting.");
            System.exit(0);
        }

        return coords;
    }

    /**
     * Handles the coordinate input given by the user and ensures that it is an integer within the range
     * 1<= coord <= 3. Does so using input validation and exception handling.
     * @param  inputType integer constant for which prompt needs to be printed (prompt to enter row/col/layer)
     * @return           the integer value for the coordinate value entered by the user
     */
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

    /**
     * Checks if a move made to a particular cell is valid. i.e. Is the cell already taken or not?
     * @param  layer  layer coord of move being made
     * @param  row    row coord of move
     * @param  col    column coord of move
     * @param  player character code for player
     * @return        is the move valid?
     */
    private boolean validateMove(int layer, int row, int col, char player) {
        if (board[layer][row][col] != ' ') {
            // Only be verbose when the user picks a cell that's taken
            if (player == PLAYER) {
                System.out.println("This cell is already taken. Please pick another one");
            }
            return false;
        }
        return true;
    }

    /**
     * Simple helper method used to unclutter the logic in getCoordInput().
     * Checks if the coordinate entered by the user is in the range 1 <= coord <= 3
     * @param  coord integer value for the coordinate
     * @return       is the coord within a valid range?
     */
    private boolean validateCoord(int coord) {
        if ((coord >= 1) && (coord <= 3)) {
            return true;
        }
        else {
            System.out.println("Coordinate is out of bounds. Please enter a value between 1 and 3");
            return false;
        }
    }

    /**
     * Helper method to handle printing the user prompt for each form of user input (within the game)
     * Does not handle printing input prompts for the game _interface_ just the game alone
     * @param inputType integer constant for which prompt is to be printed (ROW/COLUMN/LAYER)
     */
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

    /**
     * This method finds what state the game is in after a round of moves. i.e.
     * Did a player win? Is the board now in a stalemate state? or is it still playable?
     * @return Returns the character code (PLAYER / COMPUTER) for the player that won (if the game is in a 'winner' state)
     *         Otherwise returns the character constant for the current state of the game (STALEMATE/PLAYABLE)
     */
    public char findGameState() {
        char[] symbols = {PLAYER, COMPUTER};
        boolean diagonals, streak;

        if (!checkStalemate()) {
            for (char symbol: symbols) {
                diagonals = checkDiagonals(symbol);
                streak = checkAllStreaks(symbol);

                if (diagonals || streak)
                    return symbol; // Return winner
            }
            return PLAYABLE;
        }
        return STALEMATE;
    }

    /**
     * Checks of any diagonal streak on the board. Any streak that isn't a straight line
     * in any plane (x/y/z) is considered a diagonal streak.
     *
     * @param  symbol the character code for the player
     * @return        did the specified player hit any diagonal streak (boolean) ?
     */
    private boolean checkDiagonals(char symbol) {
        // Note: This is a very ugly way to solve this sub-problem,
        // but optimizing it (while keeping it readable) would involve significantly more code.
        // Don't check the middle layer because not vertical diagonals can pass through it's center
        int[] diag_cells = {0, 2}; // Possible end coords for a diagonal

        // Check all vertical diagonals
        for (int col: diag_cells) {
            // a == b && b == c and c == symbol => a == b == c == symbol
            if ((board[0][0][col] == board[1][1][col]) && (board[1][1][col] == board[2][2][col]) && (board[2][2][col] == symbol))
                return true;
            else if ((board[0][2][col] == board[1][1][col]) && (board[1][1][col] == board[2][0][col]) && (board[2][0][col] == symbol))
                return true;
        }
        for (int row: diag_cells) {
            if((board[0][row][2] == board[1][row][1]) && (board[1][row][1] == board[2][row][0]) && (board[0][row][2] == symbol))
                return true;
            else if((board[0][row][0] == board[1][row][1]) && (board[1][row][1] == board[2][row][2]) && (board[0][row][0] == symbol))
                return true;
        }

        // Check horizontal diagonals (at the top and bottom face of the game "cube")
        for (int layer: diag_cells) {
            if ((board[layer][0][0] == board[layer][1][1]) && (board[layer][1][1] == board[layer][2][2]) && (board[layer][2][2] == symbol))
                return true;
            if ((board[layer][0][2] == board[layer][1][1]) && (board[layer][1][1] == board[layer][2][0]) && (board[layer][2][0] == symbol))
                return true;
        }

        return false;
    }

    /**
     * Checks for all streaks that are not considered diagonals.
     * The check can be a little confusing because it has been optimised.
     * But the idea is that, instead of iterating over plane (in either the x, y or z-axis)
     * seperately to find a streak in the board. We could assign generic variables x and y to simulataneously
     * check over all 3 dimensions at the same time.
     * @param  symbol character code for the player specified
     * @return        Is there a straight streak anywhere (for the given player) on the board?
     */
    private boolean checkAllStreaks(char symbol) {
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

    /**
     * Checks for an individual streak on the board given a set of coordinates and a direction to search in.
     * For example checkStreak(0, 2, LAYER) would search of a streak starting on row: 0, col: 2 and would check
     * in the layer direction. Note: checkStreak(0, 2, ROW), however, would check for streaks starting at
     * layer: 0, col: 2 and would look for a streak in the ROW direction
     * @param  x      A generic variable for the first component of a coordinate in a plane
     * @param  y      A generic variable for the second component of a coordinate in a plane
     * @param  symbol the character code for the player whose streak is being checked
     * @param  type   Which direction should I search for the streak in?
     * @return        did I find a streak at the given coordinates (boolean) ?
     */
    private boolean checkStreak(int x, int y, char symbol, int type) {
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

    /**
     * Checks if a board is in the state of stalemate. A helper method to clarify
     * the logic in findGameState()
     * @return boolean (is the board in the state of a stalemate?)
     */
    private boolean checkStalemate() {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    if (board[x][y][z] == ' ')
                        return false; // if a single cell is empty, the board is not in stalemate
                }
            }
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

    /**
     * Returns a neatly formatted grid of ASCII characters to display the state
     * of thae game after every turn.
     * @return String Decorated ASCII characters for the board
     */
    public String toString() {
        String out = " =============================\n";

        // Layer labels
        out += "  Layer 1   Layer 2   Layer 3 \n   1 2 3     1 2 3     1 2 3 \n";
        out += " -----------------------------\n";

        for (int line = 0; line < 5; line++) {
            if (line % 2 == 0) {
                for (int layer = 0; layer < 3; layer++) {
                    String row = line/2 + 1 + ": "; // Row label

                    for (int col = 0; col < 5; col++) {
                        // Even column values contain cells
                        if (col % 2 == 0)
                            row += board[layer][line/2][col/2];
                        else
                            row += "|";
                    }
                    row += "  ";
                    out += row;
                }
                out += "\n";
            }
            else {
                out += "   =+=+=     =+=+=     =+=+=\n";
            }
        }
        out += "\n *****************************\n";

        return out;
    }
}
