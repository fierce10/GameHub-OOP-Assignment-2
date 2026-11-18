/**
 * Abstraction for playable controllers. Declares what a controller can do, not how it does it.
 * A reference of this type can invoke different implementations at runtime.
 */
public interface Playable {
    /**
     * Displays the gameplay mode and relevant controller settings; implementations choose
     * their own behavior.
     */
    void playGame();
    /**
     * Estimates battery consumption using the actual controller implementation.
     *
     * @param minutes simulated gaming duration (1-480 minutes)
     * @return estimated whole battery percentage points consumed
     * @throws IllegalArgumentException if duration is outside 1-480
     */
    int calculateBatteryUsage(int minutes);
}
