/**
 * Connects a Player and GamingController through object references (association). One
 * simulated session validates power and battery, dispatches gameplay polymorphically, updates
 * charge and player statistics, and records a snapshot for history.
 */
public class GamingSession {
    /**
     * Immutable session identifier.
     */
    private final String sessionID;
    /**
     * Reference to the participating player.
     */
    private final Player player;
    /**
     * Reference to the controller used in this session.
     */
    private final GamingController controller;
    /**
     * Immutable name of the game played.
     */
    private final String gameName;
    /**
     * Requested gaming time in minutes (1-480).
     */
    private final int duration; // Minutes requested
    /**
     * Snapshot of battery percentage points deducted after successful play.
     */
    private int batteryConsumed;
    /**
     * Snapshot of remaining charge after successful play.
     */
    private int batteryRemaining;
    /**
     * Snapshot of model name, retained even if the controller is renamed later.
     */
    private String controllerModel; // Snapshot, so history survives later setting changes
    /**
     * Tracks whether the session ran successfully and prevents duplicate execution.
     */
    private boolean completed;      // Prevents running the same session twice

    /**
     * Creates a pending session. Compound conditions reject missing objects and blank text; a
     * separate range check rejects invalid durations before any gameplay occurs.
     *
     * @param sessionID session identifier
     * @param player participating player
     * @param controller selected controller
     * @param gameName game to simulate
     * @param duration requested minutes (1-480)
     * @throws IllegalArgumentException if required data or duration is invalid
     */
    public GamingSession(String sessionID, Player player, GamingController controller,
                         String gameName, int duration) {
        if (sessionID == null || sessionID.trim().isEmpty() || player == null || controller == null
                || gameName == null || gameName.trim().isEmpty())
            throw new IllegalArgumentException("Session requires an ID, player, controller and game.");
        if (duration < 1 || duration > 480)
            throw new IllegalArgumentException("Duration must be 1-480 minutes.");
        this.sessionID = sessionID;
        this.player = player;
        this.controller = controller;
        this.gameName = gameName.trim();
        this.duration = duration;
    }

    /**
     * Reads the session record without allowing external code to overwrite it.
     *
     * @return session identifier
     */
    public String getSessionID() { return sessionID; }
    /**
     * Reads the session record without allowing external code to overwrite it.
     *
     * @return requested duration in minutes
     */
    public int getDuration() { return duration; }
    /**
     * Reads the session record without allowing external code to overwrite it.
     *
     * @return recorded charge consumed in percentage points (zero before completion)
     */
    public int getBatteryConsumed() { return batteryConsumed; }
    /**
     * Reads the session record without allowing external code to overwrite it.
     *
     * @return recorded charge after play (zero before completion)
     */
    public int getBatteryRemaining() { return batteryRemaining; }
    /**
     * Reads the session record without allowing external code to overwrite it.
     *
     * @return true if the session has completed; false otherwise
     */
    public boolean isCompleted() { return completed; }

    /**
     * Rejects duplicate, powered-off or undercharged sessions before updating state. The
     * Playable reference invokes the actual controller methods at runtime. After successful
     * gameplay, charge and player time are updated and a historical snapshot is recorded. The
     * final if reports automatic shutdown.
     *
     * @throws IllegalStateException if already completed, powered off or insufficiently charged
     */
    public void startSession() {
        if (completed) throw new IllegalStateException("This session is already completed.");
        if (!controller.getIsOn()) throw new IllegalStateException("Turn on the controller first.");

        // Runtime polymorphism: the interface reference selects the subclass methods.
        Playable playable = controller;
        int required = playable.calculateBatteryUsage(duration);
        if (required > controller.getBattery())
            throw new IllegalStateException("Not enough battery. Need " + required
                    + "%, available " + controller.getBattery() + "%. Recharge or shorten the session.");

        // Only after all prerequisite checks pass do we change charge and player time.
        playable.playGame();
        controller.consumeBattery(required);
        player.addGamingMinutes(duration);
        // Capture historical values so subsequent battery/model changes do not alter this record.
        batteryConsumed = required;
        batteryRemaining = controller.getBattery();
        controllerModel = controller.getModel();
        completed = true;
        System.out.printf("Session complete. Battery used: %d%% | Remaining: %d%%%n",
                batteryConsumed, batteryRemaining);
        controller.checkBattery();
        if (!controller.getIsOn()) System.out.println("Automatic shutdown: battery is empty.");
    }
    /**
     * Prints completed session details using recorded charge and model values. An early return
     * avoids showing an unfinished session as completed.
     */
    public void displaySummary() {
        if (!completed) {
            System.out.println(sessionID + " is not completed.");
            return;
        }
        System.out.printf("%s | %s | %s | %s | %d min | Used: %d%% | Remaining: %d%%%n",
                sessionID, player.getPlayerName(), controllerModel, gameName,
                duration, batteryConsumed, batteryRemaining);
    }
}
