import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * A menu driven, terminal based implementation of the Tic-Tactix game
 *
 * @author Nishkrit Desai
 * @version 20th March 2019
 * @see TicTacTix
 *
 */
public class TicTacTixTest {
    private static final Scanner input = new Scanner(System.in);
    private static final int GOING_FIRST_PROMPT = 0;
    private static TicTacTix board;

    public static void main(String[] args) {
        board = new TicTacTix();
        char goingFirst = getGoingFirst();
        char goingSecond = getGoingSecond(goingFirst);
        gameLoop(goingFirst, goingSecond);
    }

    /**
     * Returns a char (X/O) that is the assigned symbol for the player going first.
     * i.e. if the user is going first the method returns 'X' else returns 'O'
     * Uses input validation to ensure that the user enters one of (y/Y/n/N)
     * @return char the character code for a player (user = 'X', computer = 'O')?
     */
    private static char getGoingFirst() {
        while (true) {
            printPrompt(GOING_FIRST_PROMPT);
            String response = input.nextLine();
            System.out.println("Your response was: " + response);

            if (response.toLowerCase().equals("y")) {
                return board.PLAYER;
            }
            else if (response.toLowerCase().equals("n")) {
                return board.COMPUTER;
            }
        }
    }

    /**
     * Returns the character code for the player going second (X/O). This is a
     * simple helper method used to simplify/clarify the logic in the gameLoop()
     * method.
     * @param  goingFirst character code for the player going first
     * @return            character code for the player going second
     */
    private static char getGoingSecond(char goingFirst) {
        if (goingFirst == board.PLAYER) {
            return board.COMPUTER;
        }
        return board.PLAYER;
    }

    /**
     * Prints the prompt for user input for each instance where the user input is
     * required. Uses a set of private constants to check which prompt is required.
     * @param promptType Which prompt is to be printed?
     */
    private static void printPrompt(int promptType) {
        switch (promptType) {
            case GOING_FIRST_PROMPT:
                System.out.print("Would you like to go first (Y/N): ");
                break;
            default:
                System.out.print("Good-bye!");
        }
    }

    private static void gameLoop(char goingFirst, char goingSecond) {
        boolean winner = false;

        while (!winner) {
            board.makeMove(goingFirst);
            System.out.println("Computer playing!");
            board.makeMove(goingSecond);
            System.out.println(board);
            winner = board.checkWinner();
        }
    }
}
