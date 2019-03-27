import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * A menu driven, terminal based implemnentation of the Tic-Tactix game
 *
 * @@author Nishkrit Desai
 * @@version 20th March 2019
 * @@see TicTacTix
 *
 */
public class TicTacTixTest {
    private static final Scanner input = new Scanner(System.in);
    private static final int GOING_FIRST_PROMPT = 0;
    public static final TicTacTix board;

    public static void main(String[] args) {
        board = new TicTacTix();
        boolean goingFirst = goingFirst();
        gameLoop();
    }

    /**
     * Returns a boolean that says whether the user wants to go first or not.
     * Uses input validation to ensure that the user enters one of (y/Y/n/N)
     * @return boolean Is the user going first?
     */
    private static boolean goingFirst() {
        String response = "";

        while (true) {
            printPrompt(GOING_FIRST_PROMPT);
            response = input.nextLine();
            System.out.println("Your response was: " + response);

            if (response.toLowerCase().equals("y")) {
                return true;
            }
            else if (response.toLowerCase().equals("n")) {
                return false;
            }
        }
    }

    /**
     * Prints the prompt for user input for each instance where the user input is
     * required. Uses a set of private constants to check which prompt is required.
     * @param promptType Which prompt is to be printed?
     */
    private static void printPrompt(int promptType) {
        switch (promptType) {
            case GOING_FIRST_PROMPT:
                System.out.print("Would you like to go first (y/Y/n/N): ");
                break;
            default:
                System.out.print("Good-bye!");
        }
    }

    private void gameLoop(boolean goingFirst) {
        boolean winner = false;

        while (!winner) {
            switch (goingFirst) {
                case true:
                    board.playerMove();
                    board.computerMove();
                    break;
                case false:
                    board.computerMove();
                    board.playerMove();
                    break;
            }
            winner = board.checkWinner();
        }
    }

}
