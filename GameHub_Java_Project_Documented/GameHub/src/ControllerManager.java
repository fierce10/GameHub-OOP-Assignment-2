import java.util.ArrayList;
import java.util.List;

/**
 * Manages controller objects in an ArrayList of parent-class references. This design supports
 * generic and specialized controllers with one collection and demonstrates runtime
 * polymorphism during display and comparison. The implicit no-argument constructor
 * creates an empty collection with no selected controller.
 */
public class ControllerManager {
    /**
     * Private collection of registered controller references; supports subclasses through the
     * parent type.
     */
    private final ArrayList<GamingController> controllers = new ArrayList<>();
    /**
     * Reference to the current controller, initially null.
     */
    private GamingController selectedController;

    /**
     * Registers a non-null controller in the managed collection.
     *
     * @param controller controller object to register
     * @throws IllegalArgumentException if controller is null
     */
    public void addController(GamingController controller) {
        if (controller == null) throw new IllegalArgumentException("Controller cannot be null.");
        controllers.add(controller);
    }
    /**
     * Reads the current collection size.
     *
     * @return number of registered controllers
     */
    public int getControllerCount() { return controllers.size(); }
    /**
     * Returns the selected controller for operations in Main.
     *
     * @return selected controller, or null before selection
     */
    public GamingController getSelectedController() { return selectedController; }
    /**
     * Returns a shallow defensive copy, preventing callers from adding or removing elements in
     * the original list. The controller objects themselves remain shared.
     *
     * @return separate list of registered controller references
     */
    public List<GamingController> getControllers() { return new ArrayList<>(controllers); }
    /**
     * Validates a one-based menu number before converting it to a zero-based list index.
     *
     * @param number controller number shown to the user
     * @throws IllegalArgumentException if number is outside registered range
     */
    public void selectController(int number) {
        if (number < 1 || number > controllers.size())
            throw new IllegalArgumentException("Invalid controller number.");
        selectedController = controllers.get(number - 1);
    }
    /**
     * Prints a message for an empty collection; otherwise an indexed for loop displays each
     * controller and marks the selected one. Calling displayInfo on parent references invokes
     * the appropriate overridden method.
     */
    public void displayAll() {
        if (controllers.isEmpty()) System.out.println("No controllers registered.");
        // Parent references invoke the actual child's overridden displayInfo().
        for (int i = 0; i < controllers.size(); i++) {
            GamingController controller = controllers.get(i);
            System.out.printf("%n--- Controller %d%s ---%n", i + 1,
                    controller == selectedController ? " (selected)" : "");
            controller.displayInfo();
        }
    }
    /**
     * Uses an enhanced for loop to show model, price, charge and one-hour usage for each
     * controller. The interface reference resolves the battery estimate polymorphically; an
     * early return handles an empty collection.
     */
    public void compareControllers() {
        if (controllers.isEmpty()) { System.out.println("No controllers to compare."); return; }
        System.out.printf("%-24s %10s %10s %15s%n", "Model", "Price (RM)", "Battery", "Use / 60 min");
        for (GamingController controller : controllers) {
            Playable playable = controller;
            System.out.printf("%-24s %10.2f %9d%% %14d%%%n", controller.getModel(),
                    controller.getPrice(), controller.getBattery(), playable.calculateBatteryUsage(60));
        }
        System.out.println("Battery estimates are simulated, not real hardware measurements.");
    }
}
