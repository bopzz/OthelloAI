import java.util.Random;
import java.util.Scanner;

public class main {

    public static void start() {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Choose your game:");
        System.out.println("1. Small 4x4 Reversi");
        System.out.println("2. Standard 8x8 Reversi");
        System.out.print("Your choice? ");
        String choice = scnr.nextLine();

        boolean isBadInput = true;
        int boardSize = 1;
        int gameType = 1;

        while (isBadInput) {
            switch (choice) {
                case "1":
                    boardSize = 4;
                    isBadInput = false;
                    break;
                case "2":
                    boardSize = 8;
                    isBadInput = false;
                    break;
                default:
                    System.out.print("Bad input, please choose from (1-2).");
                    choice = scnr.nextLine();

            }
        }

        System.out.println();

        System.out.println("Choose your opponent");
        System.out.println("1. A random agent");
        System.out.println("2. An agent using MINIMAX");
        System.out.println("3. An agent using MINIMAX with alpha-beta pruning");
        System.out.println("4. An agent using H-MINIMAX with a fixed depth cutoff and alpha-beta pruning");
        System.out.print("Your choice? ");
        choice = scnr.nextLine();

        isBadInput = true;
        while (isBadInput) {
            switch (choice) {
                case "1":
                case "2":
                case "3":
                case "4":
                    gameType = Integer.valueOf(choice);
                    isBadInput = false;
                    break;
                default:
                    System.out.print("Bad input, please choose from (1-4).");
                    choice = scnr.nextLine();
            }
        }

        System.out.println();

        while (gameHandler(boardSize, gameType)) {
            System.out.println();
        }

        System.out.println("Game ended.");
        scnr.close();
    }

    public static boolean gameHandler(int boardSize, int gameType) {
        Reversi test = new Reversi(boardSize, TURN.BLACK);
        Scanner scnr = new Scanner(System.in);
        test.printBoard();
        int depth = 5;

        if (gameType == 4) {
            System.out.print("Please insert depth (default is 5 [recommended])");
            while (scnr.hasNextLine()) {
                try {
                    depth = Integer.valueOf(scnr.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.print("Please enter a valid integer value for depth:");
                }
            }
        }

        System.out.println("Input moves in the form of {column}{row}. For example, c3");

        while (!test.isGameOver) {
            System.out.println("Current turn: " + test.turn);
            System.out.print("Please input your move:");
            String player = scnr.nextLine();
            while (player.length() <= 1 || player.length() >= 3) {
                System.out.println("Wrong format for move!");
                System.out.print("Please input your move:");
                player = scnr.nextLine();
            }
            int row = Integer.valueOf(String.valueOf(player.charAt(1))) - 1;
            int col = ((int) player.charAt(0)) - 97;

            System.out.println();

            int i = 0;
            Random rand = new Random();
            if (test.moveWithGraphics(row, col)) {
                boolean checkSkipForBlack = true;
                if (test.turn != TURN.BLACK) {
                    int[] botMove = new int[2];
                    int checkSkipForWhite = 0;
                    while (test.turn == TURN.WHITE && !test.isGameOver) {
                        System.out.println("Current turn: " + test.turn);
                        switch (gameType) {
                            case 1:
                                System.out.print("Choosing random moves");
                                for (int k = 0; k < 3; k++) {
                                    System.out.printf(".");
                                }
                                System.out.println();
                                botMove = test.nextLegalMove.get(rand.nextInt(test.nextLegalMove.size()))
                                        .getLegalMove();
                                break;
                            case 2:
                                System.out.println("Calculating optimal moves...");
                                botMove = test.optimalMove();
                                break;
                            case 3:
                                System.out.println("Calculating optimal moves...");
                                botMove = test.alphaBetaOptimalMove();
                                break;
                            case 4:
                                System.out.println("Calculating best moves...");
                                botMove = test.HeuristicAlphaBetaOptimalMove(depth);
                                break;
                        }
                        System.out.println(test.turn + " goes " + Reversi.getCharForNumber(botMove[1] + 1)
                                + (botMove[0] + 1) + ".");
                        System.out.println();
                        test.moveWithGraphics(botMove[0], botMove[1]);
                        if (checkSkipForWhite > 0 && !test.isGameOver) {
                            System.out.println("BLACK has no legal moves, passes to WHITE.");
                        }
                        checkSkipForWhite++;
                        checkSkipForBlack = false;
                    }
                }
                if (checkSkipForBlack) {
                    System.out.println("WHITE has no legal moves, passes to BLACK.");
                }
            }
        }

        System.out.println("Do you want to keep playing? (y/n)");
        String input;
        boolean isBadInput = true;
        while (isBadInput) {
            switch (scnr.nextLine()) {
                case "y":
                    System.out.println("Restarting...");
                    return true;
                case "n":
                    return false;
                default:
                    System.out.println("Bad input, please choose only y/n.");
                    input = scnr.nextLine();
            }
        }
        scnr.close();
        return true;
    }

    public static int convertCharToInt(char c) {
        return ((int) c) - 96;
    }

    public static void main(String[] args) {
        start();
    }
}
