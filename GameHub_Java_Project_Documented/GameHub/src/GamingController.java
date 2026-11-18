/**
 * Concrete base class retained from Assignment 1. It centralises shared state and behaviour so
 * subclasses need not duplicate code. Private fields and validated setters enforce
 * encapsulation; implementing Playable supplies a common contract for runtime polymorphism.
 */
public class GamingController implements Playable {
    /**
     * Manufacturer of the controller.
     */
    private String brand;          // Manufacturer
    /**
     * Controller product name displayed in the menu.
     */
    private String model;          // Product name
    /**
     * Remaining battery percentage, in the range 0-100.
     */
    private int battery;           // Remaining battery, 0-100 percent
    /**
     * Speaker volume percentage, in the range 0-100.
     */
    private int volume;            // Volume, 0-100 percent
    /**
     * Non-negative, finite purchase price in Malaysian ringgit.
     */
    private double price;          // Purchase price in RM
    /**
     * Text describing how the controller connects.
     */
    private String connectionType; // Connection description
    /**
     * Power state; true means on and false means off.
     */
    private boolean isOn;         // Current power state

    /**
     * Creates the original default controller. Constructor chaining uses the parameterized
     * constructor so validation is not duplicated.
     */
    public GamingController() {
        this("Sony", "DualSense", 100, 50, 350.00, "Wireless", false);
    }

    /**
     * Initializes all seven original attributes through the final setters. Power is set last
     * because turning on depends on the validated battery level.
     *
     * @param brand manufacturer name
     * @param model product name
     * @param battery initial battery percentage (0-100)
     * @param volume initial speaker volume percentage (0-100)
     * @param price purchase price in RM
     * @param connectionType connection description
     * @param isOn initial power state
     * @throws IllegalArgumentException if text, percentage or price is invalid
     * @throws IllegalStateException if asked to start on an empty battery
     */
    public GamingController(String brand, String model, int battery, int volume,
                            double price, String connectionType, boolean isOn) {
        setBrand(brand);
        setModel(model);
        setBattery(battery);
        setVolume(volume);
        setPrice(price);
        setConnectionType(connectionType);
        setIsOn(isOn);
    }

    // Getters provide controlled read access to the original attributes.
    /**
     * Reads the manufacturer without exposing the private field for direct modification.
     *
     * @return manufacturer name
     */
    public String getBrand() { return brand; }
    /**
     * Reads the model without exposing the private field for direct modification.
     *
     * @return product name
     */
    public String getModel() { return model; }
    /**
     * Reads the charge level without exposing the private field for direct modification.
     *
     * @return remaining battery percentage
     */
    public int getBattery() { return battery; }
    /**
     * Reads the speaker volume without exposing the private field for direct modification.
     *
     * @return volume percentage
     */
    public int getVolume() { return volume; }
    /**
     * Reads the price without exposing the private field for direct modification.
     *
     * @return purchase price in RM
     */
    public double getPrice() { return price; }
    /**
     * Reads the connection type without exposing the private field for direct modification.
     *
     * @return connection description
     */
    public String getConnectionType() { return connectionType; }
    /**
     * Reads the power state without exposing the private field for direct modification.
     *
     * @return true if powered on; false otherwise
     */
    public boolean getIsOn() { return isOn; }

    // Setters validate changes before modifying the private state.
    /**
     * Changes the manufacturer after rejecting blank text.
     *
     * @param brand new manufacturer name
     * @throws IllegalArgumentException if text is blank or null
     */
    public final void setBrand(String brand) { this.brand = requireText(brand); }
    /**
     * Changes the product name after rejecting blank text.
     *
     * @param model new product name
     * @throws IllegalArgumentException if text is blank or null
     */
    public final void setModel(String model) { this.model = requireText(model); }
    /**
     * Updates remaining charge after range validation. The if statement turns power off at
     * zero to maintain a consistent state.
     *
     * @param battery new charge percentage
     * @throws IllegalArgumentException if outside 0-100
     */
    public final void setBattery(int battery) {
        requirePercent(battery);
        this.battery = battery;
        // A controller with no battery cannot remain switched on.
        if (battery == 0) isOn = false;
    }
    /**
     * Updates speaker volume after range validation.
     *
     * @param volume new volume percentage
     * @throws IllegalArgumentException if outside 0-100
     */
    public final void setVolume(int volume) {
        requirePercent(volume);
        this.volume = volume;
    }
    /**
     * Updates the RM price only when finite and non-negative. The if condition rejects invalid
     * numbers before assignment.
     *
     * @param price new price in RM
     * @throws IllegalArgumentException if price is negative or non-finite
     */
    public final void setPrice(double price) {
        if (!Double.isFinite(price) || price < 0)
            throw new IllegalArgumentException("Price must be a finite, non-negative number.");
        this.price = price;
    }
    /**
     * Updates the connection description with non-blank text.
     *
     * @param connectionType new connection description
     * @throws IllegalArgumentException if text is blank or null
     */
    public final void setConnectionType(String connectionType) {
        this.connectionType = requireText(connectionType);
    }
    /**
     * Changes power state only if sufficient charge exists to switch on.
     *
     * @param isOn new power state
     * @throws IllegalStateException if switching on with zero charge
     */
    public final void setIsOn(boolean isOn) {
        if (isOn && battery == 0)
            throw new IllegalStateException("Battery is empty. Recharge first.");
        this.isOn = isOn;
    }

    /**
     * Switches on through the validated power setter, then prints confirmation.
     *
     * @throws IllegalStateException if battery is empty
     */
    public void turnOn() {
        setIsOn(true);
        System.out.println(model + " is now ON.");
    }
    /**
     * Switches off through the power setter and prints confirmation.
     */
    public void turnOff() {
        setIsOn(false);
        System.out.println(model + " is now OFF.");
    }
    /**
     * Reports charge status using a conditional expression: above 20% is sufficient; 20% or
     * below triggers a warning.
     */
    public void checkBattery() {
        System.out.println(battery > 20 ? "Battery is sufficient." : "Low battery. Please charge.");
    }
    /**
     * Prints the seven common attributes. A conditional expression converts the boolean power
     * state to ON or OFF. Subclasses extend this method using super.
     */
    public void displayInfo() {
        System.out.printf("Brand: %s%nModel: %s%nBattery: %d%%%nVolume: %d%%%nPrice: RM%.2f%nConnection: %s%nPower: %s%n",
                brand, model, battery, volume, price, connectionType, isOn ? "ON" : "OFF");
    }
    /**
     * Simulates restoring charge to 100% without switching the controller on or changing its
     * other settings.
     */
    public void recharge() { setBattery(100); }

    /**
     * Deducts simulated usage only when the controller is powered on and the requested charge
     * is available. The setter handles automatic shutdown.
     *
     * @param amount percentage points to deduct
     * @throws IllegalStateException if controller is off
     * @throws IllegalArgumentException if amount is negative or exceeds remaining battery
     */
    public void consumeBattery(int amount) {
        if (!isOn) throw new IllegalStateException("Turn on the controller first.");
        if (amount < 0 || amount > battery)
            throw new IllegalArgumentException("Insufficient battery or invalid usage.");
        setBattery(battery - amount);
    }

    /**
     * Prints the generic gameplay mode. Subclasses override the same method signature to
     * provide their own output.
     */
    @Override
    public void playGame() {
        System.out.println("Standard controller gameplay activated.");
    }
    /**
     * Uses the common estimate helper with a default hourly rate of eight percentage points.
     *
     * @param minutes simulated duration (1-480 minutes)
     * @return estimated battery usage in whole percentage points
     * @throws IllegalArgumentException if duration is outside 1-480
     */
    @Override
    public int calculateBatteryUsage(int minutes) { return estimateUsage(minutes, 8); }

    /**
     * Centralizes the shared battery formula: duration x hourly rate / 60. Rounding up avoids
     * understating consumption. The range check protects all subclasses.
     *
     * @param minutes simulated duration (1-480 minutes)
     * @param ratePerHour simulated percentage points used per hour
     * @return rounded-up estimated percentage points (not capped at remaining charge)
     * @throws IllegalArgumentException if duration is outside 1-480
     */
    protected final int estimateUsage(int minutes, int ratePerHour) {
        if (minutes < 1 || minutes > 480)
            throw new IllegalArgumentException("Duration must be 1-480 minutes.");
        return (int) Math.ceil(minutes * ratePerHour / 60.0);
    }
    /**
     * Reusable validation for parent and subclass percentage settings; rejects invalid values
     * before they are stored.
     *
     * @param value percentage to check
     * @throws IllegalArgumentException if outside 0-100
     */
    protected static void requirePercent(int value) {
        if (value < 0 || value > 100)
            throw new IllegalArgumentException("Value must be between 0 and 100.");
    }
    /**
     * Rejects null or whitespace-only text and removes surrounding spaces before storage.
     *
     * @param value text to check
     * @return trimmed non-empty text
     * @throws IllegalArgumentException if null or blank
     */
    private static String requireText(String value) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException("Text cannot be empty.");
        return value.trim();
    }
}
