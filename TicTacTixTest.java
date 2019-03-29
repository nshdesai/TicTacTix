import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * A terminal based implementation of the Tic-Tactix game
 *
 * @author Nishkrit Desai
 * @version 20th March 2019
 * @see TicTacTix
 *
 */
public class TicTacTixTest {
    private static final int GOING_FIRST_PROMPT = 0;
    private static final int GREETING_MESSAGE = 1;
    private static final int GET_PLAYER_NAME = 2;
    private static final int HALL_OF_FAME = 3;
    private static final String HALL_OF_FAME_FILE = "HallOfFame.txt";

    private static final Scanner input = new Scanner(System.in);
    private static BufferedReader buffer; // To read the file
    private static PrintWriter writer; // Write to the file
    private static TicTacTix board;

    public static void main(String[] args) {
        board = new TicTacTix();
        printPrompt(GREETING_MESSAGE);
        printHallofFame();

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
     * Prints prompt and information before any type of user input/event.
     * Uses a set of private constants to check which prompt is required.
     * @param promptType Which prompt is to be printed?
     */
    private static void printPrompt(int promptType) {
        switch (promptType) {
            case GOING_FIRST_PROMPT:
                System.out.print("Would you like to go first (y/n): ");
                break;
            case GREETING_MESSAGE:
                 String line1 = "*************************************\n";
                 String line2 = "* TicTacTix - A 3D Tic Tac Toe game *\n";
                 String line3 = "*         Design: Mr. Rao           *\n";
                 String line4 = "*         Author: ndesai            *\n";
                 String line5 = "*************************************\n";
                 System.out.println(line1 + line2 + line3 + line4 + line5);
                 break;
            case GET_PLAYER_NAME:
                System.out.print("What's your name? : ");
                break;
            case HALL_OF_FAME:
                 System.out.println("====================");
                 System.out.println("*   HALL OF FAME   *");
                 System.out.println("====================");
                 break;
            default:
                System.out.print("Good-bye!");
        }
    }

    /**
     * Method to print the players in the hall of fame file (if it exists).
     * If the file doesn't exist it prints the appropriate message.
     */
    private static void printHallofFame() {
        printPrompt(HALL_OF_FAME);
        try {
            buffer = new BufferedReader(new FileReader(HALL_OF_FAME_FILE));

            String line = "";
            while (line != null) {
                line = buffer.readLine().trim();
                System.out.println(line);
            }
        }
        catch (NullPointerException e) {
            // Don't do anything here
            // Handling EOF (by not raising a NullPointerException)
        }
        catch (IOException e) {
            System.out.println("Mwahaha...No human has ever beat me!");
        }
        System.out.println("\n********************");
    }

    /**
     * Method to handle the main game loop
     * @param goingFirst  character code of the player going first
     * @param goingSecond character code of the player going second
     */
    private static void gameLoop(char goingFirst, char goingSecond) {
        char gameState = board.PLAYABLE;

        while (gameState == board.PLAYABLE) {
            handlePlayer(goingFirst);
            handlePlayer(goingSecond);

            gameState = board.findGameState();
        }
        handleEnding(gameState);
    }

    /**
     * Handles a singular move made by a player. given the player's character code
     * @param player the code for the player whose move is to be handled (PLAYER / COMPUTER)
     */
    private static void handlePlayer(char player) {
        System.out.println(getPlayerGreeting(player));
        board.makeMove(player);
        System.out.println(board);
    }

    /**
     * Method to handle all possible ways the game could end
     * @param gameState character code (read from the TicTacTix class) for the state of the game (PLAYER/COMPUTER/STALEMATE)
     */
    private static void handleEnding(char gameState) {
        if (gameState == board.PLAYER) {
            System.out.println("Yay! You won!");
            System.out.println("This must be the fall of the machines ...");
            addHighScorer();
        }
        else if (gameState == board.COMPUTER) {
            System.out.println("I have won, come back with a real challenge.");
            System.out.println("Once again, computers have outsmart the puny humans");
        }
        else if (gameState == board.STALEMATE) {
            System.out.println("Stalemate! Funny isn't it - how the universe strives for balance");
            System.out.println("We could do this forever all it takes is one bored human and a smart computer.");
        }
        printPrompt(10); // Good-bye!
    }

    /**
     * Simple helper method to return a String containing a simple greeting prompt
     * before each player's turn.
     * @param  player character code for the player (the code for current player's turn)
     * @return        String containing 'turn prompt' for the current player
     */
    private static String getPlayerGreeting(char player) {
        if (player == board.PLAYER) {
            return "User's turn";
        }
        return "Computer's turn";
    }

    /**
     * Appends the high scorer's name to the Hall of Fame file.
     */
    private static void addHighScorer() {
        try {
            writer = new PrintWriter(new FileWriter(HALL_OF_FAME_FILE, true));
            String playerName = "";

            while (playerName.equals("")) {
                printPrompt(GET_PLAYER_NAME);
                playerName = input.nextLine();

                if (playerName.equals("")) {
                    System.out.println("Please enter a name with atleast one character.");
                }
            }
            writer.println(playerName);
            writer.close();
            System.out.println("You have been inducted into the TicTacTix Hall of Fame!");
        }
        // If the file could not be opened
        catch(IOException e) {
            System.out.println("The high score file is currently busy. Fatal. Exiting.");
            System.exit(0);
        }
    }
}
