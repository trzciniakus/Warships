import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Battleships {

    private int gridSize = 10;
    private int fleetSize = 5;
    private int playerShips = 0;
    private int computerShips = 0;
    private Map<String, Field> grid;
    private Map<Integer, String> playerMarkers
            = new HashMap<>(Map.of(0, "X",
            1, "@",
            2, "$"));

    public Battleships() {
        this.grid = new HashMap<>();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid.put(i + Integer.toString(j), new Field());
            }
        }
    }

    public void runGame() {
        System.out.println("**** Welcome to Battle Ships game ****\n");
        System.out.println("Right now, the sea is empty\n");
        printGrid();
        System.out.println("Deploy your ships:");
        while (playerShips < fleetSize) {
            deployPlayerShip();
        }
        while (computerShips < fleetSize) {
            deployComputerShip();
        }
        while (playerShips != 0 && computerShips != 0) {
            playTurn();
        }
    }

    private void playTurn() {
        System.out.println("YOUR TURN");
        playerGuess();
        System.out.println("COMPUTER'S TURN");
        computerGuess();
        printGrid();
    }

    private void playerGuess() {
        String inputMessage = "Enter X coordinate:";
        int x = getUserInputCoordinate(inputMessage);
        inputMessage = "Enter Y coordinate:";
        int y = getUserInputCoordinate(inputMessage);

        Field guessedField = grid.get(x + Integer.toString(y));
        if (guessedField.revealed) {
            System.out.println("The field has been already revealed. You need to choose another one.");
            playerGuess();
        } else {
            guessedField.revealed = true;
            switch (guessedField.shipOwner) {
                case 0:
                    System.out.println("You missed.");
                    break;
                case 1:
                    System.out.println("You hit your own ship.");
                    playerShips--;
                    break;
                case 2:
                    System.out.println("BOOM! You sunk the ship!");
                    computerShips--;
                    break;
            }
        }
    }

    private int getUserInputCoordinate(String displayedMessage) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(displayedMessage);
            if (scanner.hasNextInt()) {
                int userEntry = scanner.nextInt();
                if (userEntry >= 0 && userEntry < gridSize) {
                    return userEntry;
                }
            }
            scanner.nextLine();
            System.out.println("Invalid coordinate");
        }
    }

    private void computerGuess() {
        Random random = new Random();
        int x = random.nextInt(10);
        int y = random.nextInt(10);

        Field guessedField = grid.get(x + Integer.toString(y));
        if (guessedField.revealed || guessedField.shipOwner == 2) {
            computerGuess();
        } else if (guessedField.shipOwner == 1) {
            guessedField.revealed = true;
            playerShips--;
            System.out.println("BOOM! Computer sunk the ship!");
        } else {
            guessedField.revealed = true;
            System.out.println("Computer missed.");
        }
    }

    private void deployPlayerShip() {

        String inputMessage = "Enter X coordinate for your " + (playerShips + 1) + ". ship:";
        int x = getUserInputCoordinate(inputMessage);
        inputMessage = "Enter Y coordinate for your " + (playerShips + 1) + ". ship:";
        int y = getUserInputCoordinate(inputMessage);

        if (deployShip(x, y, 1)) {
            playerShips++;
            System.out.println("Player ship " + playerShips + " has been deployed");
        } else {
            System.out.println("The field is already occupied");
            deployPlayerShip();
        }
    }

    private void deployComputerShip() {
        Random random = new Random();
        int x = random.nextInt(10);
        int y = random.nextInt(10);

        if (deployShip(x, y, 2)) {
            computerShips++;
        } else {
            deployComputerShip();
        }
    }

    private void printGrid() {

        Runnable printXAxe = () -> {
            System.out.print("   ");
            for (int i = 0; i < gridSize; i++) {
                System.out.print(i);
            }
            System.out.println();
        };

        printXAxe.run();

        for (int i = 0; i < gridSize; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < gridSize; j++) {
                Field currentField = grid.get(j + Integer.toString(i));
                System.out.print(currentField.revealed || (currentField.shipOwner == 1)
                        ? playerMarkers.get(currentField.shipOwner) : " ");
            }
            System.out.println("| " + i);
        }

        printXAxe.run();

        System.out.println("\nYour ships: " + playerShips + " | Computer ships: " + computerShips + "\n");
    }

    private boolean deployShip(int x, int y, int player) {
        Field currentField = grid.get(x + Integer.toString(y));
        if (currentField.shipOwner == 0) {
            currentField.shipOwner = player;
            return true;
        }
        return false;
    }

    class Field {
        private int shipOwner;
        private boolean revealed;

        public Field() {
            this.shipOwner = 0;
            this.revealed = false;
        }
    }
}