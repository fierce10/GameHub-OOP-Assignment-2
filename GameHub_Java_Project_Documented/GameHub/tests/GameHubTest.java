/** Standalone tests: no external libraries. Throws AssertionError on failure. */
public class GameHubTest {
    private static int passed;
    public static void main(String[] args) {
        GamingController base = new GamingController();
        check(base.getBattery() == 100 && base.getVolume() == 50 && !base.getIsOn(), "Original default constructor");
        base.setVolume(70);
        check(base.getVolume() == 70, "Setter and getter");
        expect(IllegalArgumentException.class, () -> base.setVolume(101), "Reject volume above 100");
        check(base.getVolume() == 70, "Invalid setter leaves previous value");
        expect(IllegalArgumentException.class, () -> base.setBattery(-1), "Reject negative battery");
        expect(IllegalArgumentException.class, () -> base.setPrice(-1), "Reject negative price");
        expect(IllegalArgumentException.class, () -> new GamingController("A", "B", 101, 50, 1, "Wireless", false), "Constructor validation");
        PlayStationController ps = new PlayStationController("DualSense", 100, 50, 350, true, true, true);
        XboxController xbox = new XboxController("Xbox", 75, 60, 299, 60, false, true);
        check(ps.getBrand().equals("Sony") && xbox.getBrand().equals("Microsoft"), "Inherited parent attributes");
        Playable[] devices = {base, ps, xbox};
        check(devices[0].calculateBatteryUsage(60) == 8 && devices[1].calculateBatteryUsage(60) == 15 && devices[2].calculateBatteryUsage(60) == 11, "Runtime polymorphism: 8, 15, 11 percent");
        ps.setHapticFeedback(false);
        check(ps.calculateBatteryUsage(60) == 11, "Specialized PlayStation setting");
        ps.setHapticFeedback(true);
        xbox.setVibrationIntensity(0);
        check(xbox.calculateBatteryUsage(60) == 8, "Specialized Xbox setting");
        xbox.setVibrationIntensity(60);
        expect(IllegalArgumentException.class, () -> xbox.setVibrationIntensity(110), "Reject invalid vibration");
        expect(IllegalArgumentException.class, () -> ps.calculateBatteryUsage(0), "Reject invalid duration");
        Player player = new Player("P1", "Test Player", "Racing");
        GamingSession session = new GamingSession("S1", player, ps, "Racing", 60);
        expect(IllegalStateException.class, session::startSession, "Reject powered-off session");
        check(player.getTotalGamingMinutes() == 0 && ps.getBattery() == 100, "Failed session changes no statistics");
        ps.turnOn();
        session.startSession();
        check(session.isCompleted() && ps.getBattery() == 85 && player.getTotalGamingMinutes() == 60, "Successful integrated session");
        check(session.getBatteryConsumed() == 15 && session.getBatteryRemaining() == 85, "Session snapshot");
        expect(IllegalStateException.class, session::startSession, "Reject duplicate session");
        ps.setBattery(10);
        GamingSession low = new GamingSession("S2", player, ps, "Racing", 60);
        expect(IllegalStateException.class, low::startSession, "Reject insufficient battery");
        check(ps.getBattery() == 10 && player.getTotalGamingMinutes() == 60, "Rejected session preserves state");
        ps.setBattery(15);
        new GamingSession("S3", player, ps, "Racing", 60).startSession();
        check(ps.getBattery() == 0 && !ps.getIsOn(), "Automatic shutdown at zero battery");
        expect(IllegalStateException.class, ps::turnOn, "Reject power-on with empty battery");
        ps.recharge();
        check(ps.getBattery() == 100 && !ps.getIsOn(), "Recharge restores battery without changing power state");
        ControllerManager manager = new ControllerManager();
        manager.addController(ps); manager.addController(xbox); manager.addController(base);
        manager.selectController(2);
        check(manager.getSelectedController() == xbox, "Manager selects controller");
        expect(IllegalArgumentException.class, () -> manager.selectController(4), "Reject invalid selection");
        manager.getControllers().clear();
        check(manager.getControllerCount() == 3, "Defensive list copy protects collection");
        check(player.getTotalGamingMinutes() == 120 && player.getTotalGamingHours() == 2.0, "Cumulative player statistics");
        System.out.println("PASS: " + passed + " checks.");
    }
    private static void check(boolean condition, String description) {
        if (!condition) throw new AssertionError(description);
        passed++;
        System.out.println("PASS - " + description);
    }
    private static void expect(Class<? extends RuntimeException> type, Runnable task, String description) {
        try { task.run(); } catch (RuntimeException ex) {
            check(type.isInstance(ex), description);
            return;
        }
        throw new AssertionError("Expected exception: " + description);
    }
}
