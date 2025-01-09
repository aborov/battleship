package battleship;

import java.util.Scanner;

public class Main {

    private static final int SIZE = 10;
    private static final char EMPTY_CELL = '~';
    private static final char SHIP_CELL = 'O';
    private static final char HIT_CELL = 'X';
    private static final char MISS_CELL = 'M';
    private static final char[] ROWS = "ABCDEFGHIJ".toCharArray();
    private static final String[] SHIP_NAMES = {
            "Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"
    };
    private static final int[] SHIP_SIZES = {5, 4, 3, 3, 2};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize fields
        char[][] field1 = initializeField();
        char[][] field2 = initializeField();
        char[][] fogField1 = initializeField();
        char[][] fogField2 = initializeField();

        // Player 1 place ships
        System.out.println("Player 1, place your ships on the game field\n");
        placeShips(field1, scanner);
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
        clearScreen();

        // Player 2 place ships
        System.out.println("Player 2, place your ships on the game field\n");
        placeShips(field2, scanner);
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
        clearScreen();

        boolean player1Turn = true;
        boolean gameOver = false;

        while (!gameOver) {
            if (player1Turn) {
                printBattleFields(fogField2, field1);
                System.out.println("\nPlayer 1, it's your turn:\n");
                String shot = scanner.nextLine();
                processShot(field2, fogField2, shot);
            } else {
                printBattleFields(fogField1, field2);
                System.out.println("\nPlayer 2, it's your turn:\n");
                String shot = scanner.nextLine();
                processShot(field1, fogField1, shot);
            }

            System.out.println("Press Enter and pass the move to another player");
            scanner.nextLine();
            clearScreen();
            player1Turn = !player1Turn;
        }
    }

    private static char[][] initializeField() {
        char[][] field = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                field[i][j] = EMPTY_CELL;
            }
        }
        return field;
    }

    private static void placeShips(char[][] field, Scanner scanner) {
        printField(field);  // Print initial empty field

        for (int i = 0; i < SHIP_NAMES.length; i++) {
            System.out.println();  // Add newline before each ship placement
            placeShip(scanner, field, SHIP_NAMES[i], SHIP_SIZES[i]);
        }
    }

    private static void placeShip(Scanner scanner, char[][] field, String shipName, int shipSize) {
        while (true) {
            System.out.printf("Enter the coordinates of the %s (%d cells):%n", shipName, shipSize);
            String[] coordinates = scanner.nextLine().split(" ");
            if (coordinates.length != 2 || !isValidCoordinate(coordinates[0]) || !isValidCoordinate(coordinates[1])) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                continue;
            }

            int[] start = parseCoordinate(coordinates[0]);
            int[] end = parseCoordinate(coordinates[1]);

            // Check alignment (vertical or horizontal)
            if (start[0] != end[0] && start[1] != end[1]) {
                System.out.println("Error! Wrong ship location! Try again:");
                continue;
            }

            // Check ship length
            int length = Math.abs(start[0] - end[0]) + Math.abs(start[1] - end[1]) + 1;
            if (length != shipSize) {
                System.out.printf("Error! Wrong length of the %s! Try again:%n", shipName);
                continue;
            }

            // Check for overlapping ships and adjacent ships
            if (!isValidPlacement(field, start, end, shipSize)) {
                // The error message is handled inside isValidPlacement
                continue;
            }

            // Place the ship
            placeShipOnField(field, start, end);
            // Print the field after successful placement
            printField(field);
            break;
        }
    }

    private static boolean isValidCoordinate(String coordinate) {
        if (coordinate.length() < 2 || coordinate.length() > 3) {
            return false;
        }
        char row = coordinate.charAt(0);
        String colStr = coordinate.substring(1);
        if (row < 'A' || row > 'J') {
            return false;
        }
        try {
            int col = Integer.parseInt(colStr);
            return col >= 1 && col <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int[] parseCoordinate(String coordinate) {
        int row = coordinate.charAt(0) - 'A';
        int col = Integer.parseInt(coordinate.substring(1)) - 1;
        return new int[]{row, col};
    }

    private static boolean isValidPlacement(char[][] field, int[] start, int[] end, int shipSize) {
        // Check alignment (vertical or horizontal)
        if (start[0] != end[0] && start[1] != end[1]) {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        }
        // Check ship length
        int length = Math.abs(start[0] - end[0]) + Math.abs(start[1] - end[1]) + 1;
        if (length != shipSize) {
            System.out.println("Error! Wrong length of the ship! Try again:");
            return false;
        }
        // Determine the range for rows and columns
        int rowStart = Math.min(start[0], end[0]);
        int rowEnd = Math.max(start[0], end[0]);
        int colStart = Math.min(start[1], end[1]);
        int colEnd = Math.max(start[1], end[1]);
        // Check for overlapping ships and ships too close (adjacent cells)
        for (int i = rowStart - 1; i <= rowEnd + 1; i++) {
            for (int j = colStart - 1; j <= colEnd + 1; j++) {
                if (i >= 0 && i < SIZE && j >= 0 && j < SIZE) {
                    if (field[i][j] == SHIP_CELL) {
                        System.out.println("Error! You placed it too close to another one. Try again:");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static void placeShipOnField(char[][] field, int[] start, int[] end) {
        int rowStart = Math.min(start[0], end[0]);
        int rowEnd = Math.max(start[0], end[0]);
        int colStart = Math.min(start[1], end[1]);
        int colEnd = Math.max(start[1], end[1]);
        for (int i = rowStart; i <= rowEnd; i++) {
            for (int j = colStart; j <= colEnd; j++) {
                field[i][j] = SHIP_CELL;
            }
        }
    }

    private static void printField(char[][] field) {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < SIZE; i++) {
            System.out.print((char)('A' + i) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void printFieldWithFog(char[][] field) {
        System.out.print("  ");
        for (int i = 1; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print(ROWS[i] + " ");
            for (int j = 0; j < SIZE; j++) {
                if (field[i][j] == SHIP_CELL) {
                    System.out.print(EMPTY_CELL + " ");
                } else {
                    System.out.print(field[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    private static boolean isShipSunk(char[][] field, int row, int col) {
        // Check horizontally to the left
        for (int j = col; j >= 0; j--) {
            if (field[row][j] == SHIP_CELL) {
                return false;
            } else if (field[row][j] == EMPTY_CELL) {
                break;
            }
        }
        // Check horizontally to the right
        for (int j = col; j < SIZE; j++) {
            if (field[row][j] == SHIP_CELL) {
                return false;
            } else if (field[row][j] == EMPTY_CELL) {
                break;
            }
        }
        // Check vertically upwards
        for (int i = row; i >= 0; i--) {
            if (field[i][col] == SHIP_CELL) {
                return false;
            } else if (field[i][col] == EMPTY_CELL) {
                break;
            }
        }
        // Check vertically downwards
        for (int i = row; i < SIZE; i++) {
            if (field[i][col] == SHIP_CELL) {
                return false;
            } else if (field[i][col] == EMPTY_CELL) {
                break;
            }
        }
        return true;
    }

    private static boolean areAllShipsSunk(char[][] field) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (field[i][j] == SHIP_CELL) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void printBattleFields(char[][] fogField, char[][] playerField) {
        printFieldWithFog(fogField);  // Show opponent's field with fog of war
        System.out.println("---------------------");
        printField(playerField);      // Show player's own field
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static boolean processShot(char[][] field, char[][] fogField, String shot) {
        if (!isValidCoordinate(shot)) {
            System.out.println("\nError! You entered the wrong coordinates! Try again:");
            return false;
        }

        int[] coordinates = parseCoordinate(shot);
        int row = coordinates[0];
        int col = coordinates[1];

        if (field[row][col] == SHIP_CELL) {
            field[row][col] = HIT_CELL;
            fogField[row][col] = HIT_CELL;
            printFieldWithFog(fogField);

            if (areAllShipsSunk(field)) {
                System.out.println("\nYou sank the last ship. You won. Congratulations!");
                return true;
            } else if (isShipSunk(field, row, col)) {
                System.out.println("\nYou sank a ship!");
            } else {
                System.out.println("\nYou hit a ship!");
            }
        } else {
            field[row][col] = MISS_CELL;
            fogField[row][col] = MISS_CELL;
            printFieldWithFog(fogField);
            System.out.println("\nYou missed!");
        }

        return false;
    }
}