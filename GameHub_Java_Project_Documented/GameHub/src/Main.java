import java.util.ArrayList;
import java.util.Scanner;

/**
 * Console entry point that coordinates the application classes and interface. It owns
 * the menu and input validation; controller rules, player statistics and session logic remain
 * in their respective classes to separate responsibilities.
 */
public class Main {
    /**
     * Creates sample controllers (including the base class), manages players and completed
     * sessions, and repeats a menu using a while loop. A switch chooses operations; if/else
     * checks prerequisites; for loops display collections. Validation exceptions are caught so
     * the menu can continue.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // The scanner reads console input; collections store data only during this run.
        Scanner input = new Scanner(System.in);
        ControllerManager manager = new ControllerManager();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<GamingSession> sessions = new ArrayList<>();
        // A null selection means no player has registered or been chosen yet.
        Player currentPlayer = null;

        // Instantiate the original base class as well as both derived classes.
        GamingController standard = new GamingController();
        standard.setBrand("Generic");
        standard.setModel("Standard Controller");
        standard.setPrice(129.00);
        manager.addController(new PlayStationController("DualSense", 100, 50, 350.00,
                true, true, true));
        manager.addController(new XboxController("Xbox Controller", 75, 60, 299.00,
                60, false, true));
        manager.addController(standard);
        manager.selectController(1);

        boolean running = true;
        // While loop keeps the menu active until the user selects Exit.
        while (running) {
            printMenu(manager.getSelectedController(), currentPlayer);
            if (!input.hasNextLine()) break;
            String choice = input.nextLine().trim();
            try {
                GamingController controller = manager.getSelectedController();
                // Switch dispatches a different operation for each menu choice.
                switch (choice) {
                    case "1":
                        manager.displayAll();
                        break;
                    case "2":
                        // Sequential IDs distinguish profiles; select the newly registered player.
                        Player player = new Player("P" + (players.size() + 1),
                                readText(input, "Player name: "), readText(input, "Favourite game: "));
                        players.add(player);
                        currentPlayer = player;
                        System.out.println("Player registered and selected: " + player.getPlayerID());
                        break;
                    case "3":
                        manager.displayAll();
                        manager.selectController(readInt(input, "Controller number: ", 1, manager.getControllerCount()));
                        System.out.println("Selected: " + manager.getSelectedController().getModel());
                        break;
                    case "4":
                        // The if/else toggles power without accessing private fields.
                        if (controller.getIsOn()) controller.turnOff();
                        else controller.turnOn();
                        break;
                    case "5":
                        customise(input, controller);
                        break;
                    case "6":
                        // Require both a player and a powered controller before creating a session.
                        if (currentPlayer == null) {
                            System.out.println("Register or select a player first.");
                        } else if (!controller.getIsOn()) {
                            System.out.println("Turn on the selected controller first.");
                        } else {
                            String game = readText(input, "Game name: ");
                            int minutes = readInt(input, "Duration in minutes (1-480): ", 1, 480);
                            GamingSession session = new GamingSession("S" + (sessions.size() + 1),
                                    currentPlayer, controller, game, minutes);
                            session.startSession();
                            // Failed sessions are not added to history.
                            sessions.add(session);
                        }
                        break;
                    case "7":
                        System.out.println(controller.getModel() + ": " + controller.getBattery() + "%");
                        controller.checkBattery();
                        break;
                    case "8":
                        controller.recharge();
                        System.out.println(controller.getModel() + " recharged to " + controller.getBattery() + "%.");
                        break;
                    case "9":
                        System.out.println("--- Session History ---");
                        // Enhanced for loops display existing records without changing the lists.
                        if (sessions.isEmpty()) System.out.println("No completed sessions.");
                        for (GamingSession session : sessions) session.displaySummary();
                        System.out.println("--- Player Statistics ---");
                        if (players.isEmpty()) System.out.println("No registered players.");
                        for (Player registered : players) registered.displayProfile();
                        break;
                    case "10":
                        manager.compareControllers();
                        break;
                    case "11":
                        if (players.isEmpty()) {
                            System.out.println("No registered players.");
                        } else {
                            // An indexed loop supplies one-based numbers for menu selection.
                            for (int i = 0; i < players.size(); i++) {
                                System.out.print((i + 1) + ". ");
                                players.get(i).displayProfile();
                            }
                            currentPlayer = players.get(readInt(input, "Player number: ", 1, players.size()) - 1);
                            System.out.println("Selected player: " + currentPlayer.getPlayerName());
                        }
                        break;
                    case "0":
                        running = false;
                        System.out.println("Thank you for using GameHub!");
                        break;
                    default:
                        System.out.println("Invalid menu option. Please try again.");
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                // Validation errors should not crash the application.
                System.out.println("Unable to complete operation: " + ex.getMessage());
            }
        }
        input.close();
    }

    /**
     * Displays the current controller, selected player and all available menu commands.
     * Conditional expressions render power and missing-player states.
     *
     * @param controller currently selected controller
     * @param player selected player, or null if none
     */
    private static void printMenu(GamingController controller, Player player) {
        System.out.println("\n========================================");
        System.out.println("          GAMEHUB CONTROLLER SYSTEM");
        System.out.println("========================================");
        System.out.printf("Controller: %s | %d%% | %s%n", controller.getModel(),
                controller.getBattery(), controller.getIsOn() ? "ON" : "OFF");
        System.out.println("Player: " + (player == null ? "Not selected" : player.getPlayerName()));
        System.out.println("1. Display all gaming controllers");
        System.out.println("2. Register a player");
        System.out.println("3. Select gaming controller");
        System.out.println("4. Turn controller ON/OFF");
        System.out.println("5. Customise controller settings");
        System.out.println("6. Start gaming session");
        System.out.println("7. Check battery status");
        System.out.println("8. Recharge controller");
        System.out.println("9. View gaming session summary");
        System.out.println("10. Compare controllers");
        System.out.println("11. Select registered player");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Reads common volume and then uses instanceof to identify platform-specific settings. The
     * if/else branches use subclass setters only where appropriate; all controller types share
     * the inherited volume setter.
     *
     * @param input console scanner
     * @param controller currently selected controller
     */
    private static void customise(Scanner input, GamingController controller) {
        int volume = readInt(input, "Volume (0-100): ", 0, 100);
        // instanceof is used because these settings do not exist in the base class.
        // Runtime polymorphism itself is demonstrated in GamingSession and Manager.
        if (controller instanceof PlayStationController) {
            PlayStationController ps = (PlayStationController) controller;
            boolean triggers = readYesNo(input, "Adaptive triggers (y/n): ");
            boolean haptics = readYesNo(input, "Haptic feedback (y/n): ");
            boolean touchpad = readYesNo(input, "Touchpad (y/n): ");
            ps.setAdaptiveTriggers(triggers);
            ps.setHapticFeedback(haptics);
            ps.setTouchpadEnabled(touchpad);
        } else if (controller instanceof XboxController) {
            XboxController xbox = (XboxController) controller;
            int vibration = readInt(input, "Vibration intensity (0-100): ", 0, 100);
            boolean mapping = readYesNo(input, "Custom button mapping (y/n): ");
            boolean impulses = readYesNo(input, "Impulse triggers (y/n): ");
            xbox.setVibrationIntensity(vibration);
            xbox.setCustomButtonMapping(mapping);
            xbox.setImpulseTriggers(impulses);
        }
        controller.setVolume(volume);
        System.out.println("Settings updated. Current volume: " + controller.getVolume() + "%");
    }

    /**
     * Reads whole lines rather than mixing nextInt and nextLine. The while loop repeats for
     * blank input and exits on valid text; an end-of-input check avoids reading a closed
     * stream.
     *
     * @param input console scanner
     * @param prompt message shown before input
     * @return trimmed non-empty user input
     * @throws IllegalStateException if input has ended
     */
    private static String readText(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!input.hasNextLine()) throw new IllegalStateException("Input ended.");
            String value = input.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("Please enter a non-empty value.");
        }
    }
    /**
     * Repeats until a whole number within the inclusive range is entered. try/catch handles
     * non-numeric input and if checks the range.
     *
     * @param input console scanner
     * @param prompt message shown before input
     * @param min inclusive lower bound
     * @param max inclusive upper bound
     * @return validated whole number
     * @throws IllegalStateException if input has ended
     */
    private static int readInt(Scanner input, String prompt, int min, int max) {
        while (true) {
            String value = readText(input, prompt);
            try {
                int number = Integer.parseInt(value);
                if (number >= min && number <= max) return number;
            } catch (NumberFormatException ex) {
                // The same message handles malformed and out-of-range input.
            }
            System.out.printf("Enter a whole number from %d to %d.%n", min, max);
        }
    }
    /**
     * Repeats until a valid yes/no answer is provided, ignoring letter case.
     *
     * @param input console scanner
     * @param prompt question shown to the user
     * @return true for y/yes; false for n/no
     * @throws IllegalStateException if input has ended
     */
    private static boolean readYesNo(Scanner input, String prompt) {
        while (true) {
            String value = readText(input, prompt);
            if (value.equalsIgnoreCase("y") || value.equalsIgnoreCase("yes")) return true;
            if (value.equalsIgnoreCase("n") || value.equalsIgnoreCase("no")) return false;
            System.out.println("Please enter y or n.");
        }
    }
}
