/**
 * Specialized Sony controller demonstrating inheritance (extends GamingController). Common
 * fields and operations remain in the parent; this class adds private platform-specific
 * settings and overrides gameplay, display and battery estimation.
 */
public class PlayStationController extends GamingController {
    /**
     * Whether adaptive trigger simulation is enabled. Adds to estimated usage.
     */
    private boolean adaptiveTriggers; // Adaptive trigger simulation
    /**
     * Whether haptic feedback simulation is enabled. Adds to estimated usage.
     */
    private boolean hapticFeedback;   // Haptic feedback simulation
    /**
     * Whether the touchpad is enabled in the simulated settings.
     */
    private boolean touchpadEnabled;  // Touchpad setting

    /**
     * Calls super to initialize inherited attributes with Sony, Wireless and power off, then
     * stores the specialized settings. This reuses parent validation.
     *
     * @param model product name
     * @param battery initial charge percentage
     * @param volume initial speaker volume percentage
     * @param price purchase price in RM
     * @param adaptiveTriggers whether adaptive triggers are enabled
     * @param hapticFeedback whether haptics are enabled
     * @param touchpadEnabled whether touchpad is enabled
     * @throws IllegalArgumentException if an inherited or percentage setting is invalid
     */
    public PlayStationController(String model, int battery, int volume, double price,
                                 boolean adaptiveTriggers, boolean hapticFeedback,
                                 boolean touchpadEnabled) {
        super("Sony", model, battery, volume, price, "Wireless", false);
        this.adaptiveTriggers = adaptiveTriggers;
        this.hapticFeedback = hapticFeedback;
        this.touchpadEnabled = touchpadEnabled;
    }
    /**
     * Reads the private adaptive triggers setting.
     *
     * @return true if adaptive triggers is enabled; false otherwise
     */
    public boolean getAdaptiveTriggers() { return adaptiveTriggers; }
    /**
     * Reads the private haptic feedback setting.
     *
     * @return true if haptic feedback is enabled; false otherwise
     */
    public boolean getHapticFeedback() { return hapticFeedback; }
    /**
     * Reads the private touchpad setting.
     *
     * @return true if touchpad is enabled; false otherwise
     */
    public boolean getTouchpadEnabled() { return touchpadEnabled; }
    /**
     * Updates adaptive triggers through a public method, without exposing its private field.
     *
     * @param enabled new enabled/disabled state
     */
    public void setAdaptiveTriggers(boolean enabled) { adaptiveTriggers = enabled; }
    /**
     * Updates haptic feedback through a public method, without exposing its private field.
     *
     * @param enabled new enabled/disabled state
     */
    public void setHapticFeedback(boolean enabled) { hapticFeedback = enabled; }
    /**
     * Updates touchpad through a public method, without exposing its private field.
     *
     * @param enabled new enabled/disabled state
     */
    public void setTouchpadEnabled(boolean enabled) { touchpadEnabled = enabled; }

    /**
     * Overrides the common Playable operation to print Sony-specific gameplay settings.
     * Runtime dispatch selects this implementation when the actual object is of this class.
     */
    @Override
    public void playGame() {
        System.out.printf("PlayStation mode | Adaptive triggers: %s | Haptics: %s | Touchpad: %s%n",
                adaptiveTriggers, hapticFeedback, touchpadEnabled);
    }
    /**
     * Uses eight points per hour plus three for adaptive triggers and four for haptics. The
     * two independent if statements allow both increases to apply; touchpad is display-only in
     * the simulation. Delegates rounding and duration validation to the inherited helper.
     *
     * @param minutes simulated duration (1-480 minutes)
     * @return estimated whole percentage points consumed
     * @throws IllegalArgumentException if duration is outside 1-480
     */
    @Override
    public int calculateBatteryUsage(int minutes) {
        int rate = 8;
        if (adaptiveTriggers) rate += 3;
        if (hapticFeedback) rate += 4;
        return estimateUsage(minutes, rate);
    }
    /**
     * Overrides the parent display method, calls super to reuse the common output, then prints
     * platform-specific settings.
     */
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf("Adaptive triggers: %s | Haptic feedback: %s | Touchpad: %s%n",
                adaptiveTriggers, hapticFeedback, touchpadEnabled);
    }
}
