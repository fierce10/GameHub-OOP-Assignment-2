/**
 * Specialized Microsoft controller demonstrating inheritance (extends GamingController).
 * Common fields and operations remain in the parent; this class adds private platform-specific
 * settings and overrides gameplay, display and battery estimation.
 */
public class XboxController extends GamingController {
    /**
     * Vibration strength (0-100%); affects estimated usage.
     */
    private int vibrationIntensity;      // Vibration level in percent
    /**
     * Whether custom button mapping is enabled in the simulation.
     */
    private boolean customButtonMapping; // Custom mapping setting
    /**
     * Whether impulse-trigger simulation is enabled; affects estimated usage.
     */
    private boolean impulseTriggers;     // Impulse trigger simulation

    /**
     * Calls super to initialize inherited attributes with Microsoft, Wireless and power off,
     * then stores the specialized settings. This reuses parent validation.
     *
     * @param model product name
     * @param battery initial charge percentage
     * @param volume initial speaker volume percentage
     * @param price purchase price in RM
     * @param vibrationIntensity initial vibration percentage (0-100)
     * @param customButtonMapping whether custom mapping is enabled
     * @param impulseTriggers whether impulse triggers are enabled
     * @throws IllegalArgumentException if an inherited or percentage setting is invalid
     */
    public XboxController(String model, int battery, int volume, double price,
                          int vibrationIntensity, boolean customButtonMapping,
                          boolean impulseTriggers) {
        super("Microsoft", model, battery, volume, price, "Wireless", false);
        setVibrationIntensity(vibrationIntensity);
        this.customButtonMapping = customButtonMapping;
        this.impulseTriggers = impulseTriggers;
    }
    /**
     * Reads the private vibration intensity setting.
     *
     * @return vibration percentage (0-100)
     */
    public int getVibrationIntensity() { return vibrationIntensity; }
    /**
     * Reads the private custom button mapping setting.
     *
     * @return true if custom button mapping is enabled; false otherwise
     */
    public boolean getCustomButtonMapping() { return customButtonMapping; }
    /**
     * Reads the private impulse triggers setting.
     *
     * @return true if impulse triggers is enabled; false otherwise
     */
    public boolean getImpulseTriggers() { return impulseTriggers; }
    /**
     * Updates vibration intensity through a public method after reusing the parent percentage
     * validator.
     *
     * @param intensity new vibration percentage (0-100)
     * @throws IllegalArgumentException if outside 0-100
     */
    public final void setVibrationIntensity(int intensity) {
        requirePercent(intensity);
        vibrationIntensity = intensity;
    }
    /**
     * Updates custom button mapping through a public method, without exposing its private
     * field.
     *
     * @param enabled new enabled/disabled state
     */
    public void setCustomButtonMapping(boolean enabled) { customButtonMapping = enabled; }
    /**
     * Updates impulse triggers through a public method, without exposing its private field.
     *
     * @param enabled new enabled/disabled state
     */
    public void setImpulseTriggers(boolean enabled) { impulseTriggers = enabled; }

    /**
     * Overrides the common Playable operation to print Microsoft-specific gameplay settings.
     * Runtime dispatch selects this implementation when the actual object is of this class.
     */
    @Override
    public void playGame() {
        System.out.printf("Xbox mode | Vibration: %d%% | Custom mapping: %s | Impulse triggers: %s%n",
                vibrationIntensity, customButtonMapping, impulseTriggers);
    }
    /**
     * Uses six points per hour plus rounded-up vibration intensity divided by 20, and two
     * extra points when impulse triggers are enabled. Custom mapping does not affect usage in
     * this simulation. Delegates rounding and duration validation to the inherited helper.
     *
     * @param minutes simulated duration (1-480 minutes)
     * @return estimated whole percentage points consumed
     * @throws IllegalArgumentException if duration is outside 1-480
     */
    @Override
    public int calculateBatteryUsage(int minutes) {
        int rate = 6 + (int) Math.ceil(vibrationIntensity / 20.0);
        if (impulseTriggers) rate += 2;
        return estimateUsage(minutes, rate);
    }
    /**
     * Overrides the parent display method, calls super to reuse the common output, then prints
     * platform-specific settings.
     */
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf("Vibration: %d%% | Custom mapping: %s | Impulse triggers: %s%n",
                vibrationIntensity, customButtonMapping, impulseTriggers);
    }
}
