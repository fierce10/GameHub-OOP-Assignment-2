/**
 * Models a registered player. Immutable profile fields prevent accidental reassignment;
 * cumulative time is modified through a validated method. Minutes are stored as integers to
 * avoid accumulating rounding errors.
 */
public class Player {
    /**
     * Immutable identifier assigned during registration.
     */
    private final String playerID;
    /**
     * Immutable display name.
     */
    private final String playerName;
    /**
     * Immutable preferred game name.
     */
    private final String favouriteGame;
    /**
     * Accumulated completed session minutes; stored as an integer to prevent rounding
     * accumulation.
     */
    private long totalGamingMinutes; // Integer minutes avoid rounding accumulation

    /**
     * Creates a player profile with zero accumulated gaming time and validates each text
     * field.
     *
     * @param playerID player identifier
     * @param playerName display name
     * @param favouriteGame preferred game
     * @throws IllegalArgumentException if any field is null or blank
     */
    public Player(String playerID, String playerName, String favouriteGame) {
        this.playerID = requireText(playerID);
        this.playerName = requireText(playerName);
        this.favouriteGame = requireText(favouriteGame);
    }
    /**
     * Reads player identifier.
     *
     * @return player identifier
     */
    public String getPlayerID() { return playerID; }
    /**
     * Reads display name.
     *
     * @return display name
     */
    public String getPlayerName() { return playerName; }
    /**
     * Reads preferred game.
     *
     * @return preferred game
     */
    public String getFavouriteGame() { return favouriteGame; }
    /**
     * Reads cumulative completed gameplay minutes.
     *
     * @return cumulative completed gameplay minutes
     */
    public long getTotalGamingMinutes() { return totalGamingMinutes; }
    /**
     * Reads cumulative gameplay hours calculated as minutes divided by 60.0.
     *
     * @return cumulative gameplay hours calculated as minutes divided by 60.0
     */
    public double getTotalGamingHours() { return totalGamingMinutes / 60.0; }
    /**
     * Adds completed gameplay time. The if statement rejects non-positive durations before
     * changing statistics.
     *
     * @param minutes completed positive gameplay minutes
     * @throws IllegalArgumentException if minutes are zero or negative
     */
    public void addGamingMinutes(int minutes) {
        if (minutes <= 0) throw new IllegalArgumentException("Minutes must be positive.");
        totalGamingMinutes += minutes;
    }
    /**
     * Prints profile details and cumulative time in both minutes and hours.
     */
    public void displayProfile() {
        System.out.printf("%s | %s | Favourite game: %s | Total: %d minutes (%.2f hours)%n",
                playerID, playerName, favouriteGame, totalGamingMinutes, getTotalGamingHours());
    }
    /**
     * Validates profile text and trims surrounding whitespace.
     *
     * @param value text to validate
     * @return trimmed, non-empty text
     * @throws IllegalArgumentException if null or blank
     */
    private static String requireText(String value) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException("Text cannot be empty.");
        return value.trim();
    }
}
